package org.nuxeo.downloadproxy.rest.api;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
@ApplicationPath("/api")
public class DownloadProxyResourceConfig extends ResourceConfig {
    public DownloadProxyResourceConfig() {
        register(JacksonFeature.class);
        packages("org.nuxeo.downloadproxy.rest.api");
    }
}
