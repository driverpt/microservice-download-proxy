package org.nuxeo.downloadproxy.rest.api.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okio.BufferedSink;
import okio.Okio;

@Component
@Path("/download")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Slf4j
public class DownloadController {

    // 20 Megabytes
    public static final long CHUNK_SIZE = 10_000_000L;

    @Value("${downloader.files.prefix:tmp}")
    private String filePrefix;

    @Value("${downloader.files.downloadDirectory:/tmp}")
    private File fileDownloadDirectory;

    @Value("${downloader.files.noStoreFiles:false}")
    private boolean noStoreFiles;

    @GET
    public void downloadFile(@Context HttpHeaders httpHeaders, @Suspended final AsyncResponse asyncResponse, @QueryParam("url") String url) throws Exception {
        OkHttpClient client = new OkHttpClient.Builder().build();

        Request.Builder requestBuilder = new Request.Builder().url(url);


        Map<String, String> headers = httpHeaders.getRequestHeaders()
                                                 .entrySet()
                                                 .stream()
                                                 .collect(
                                                         Collectors.toMap(Map.Entry::getKey, entry -> String.join("", entry
                                                                 .getValue()))
                                                 );
        requestBuilder.headers(Headers.of(headers));

        Request request = requestBuilder.build();

        if (!fileDownloadDirectory.exists() && !noStoreFiles) {
            asyncResponse.resume(new FileNotFoundException());
            return;
        }

        File tempFile = null;
        if (noStoreFiles) {
            tempFile = Paths.get("/dev/null").toFile();
        } else {
            try {
                tempFile = Files.createFile(
                        Paths.get(fileDownloadDirectory.getPath(), MessageFormat.format("{0}_{1}", filePrefix, UUID.randomUUID()
                                                                                                                   .toString()))
                ).toFile();
            } catch (Exception e) {
                log.error("Error while creating temp file", e);
                asyncResponse.resume(e);
            }
        }

        okhttp3.Response response = client.newCall(request).execute();
        if (response.code() > 400) {
            asyncResponse.resume(Response.status(response.code()).build());
            return;
        }
        try (BufferedSink sink = Okio.buffer(Okio.sink(tempFile)) ){
            sink.writeAll(response.body().source());
        } catch (IOException e) {
            log.error("Exception while downloading file", e);
            asyncResponse.resume(e);
            return;
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("file", tempFile.getAbsolutePath());

        asyncResponse.resume(Response.ok(result).build());
    }
}
