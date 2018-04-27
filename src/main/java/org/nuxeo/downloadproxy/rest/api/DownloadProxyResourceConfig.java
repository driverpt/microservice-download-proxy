package org.nuxeo.downloadproxy.rest.api;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.nuxeo.downloadproxy.rest.api.controllers.DownloadController;
import org.nuxeo.downloadproxy.rest.api.controllers.RootController;
import org.springframework.stereotype.Component;

@Component
@ApplicationPath("/api")
public class DownloadProxyResourceConfig extends ResourceConfig {
    public DownloadProxyResourceConfig() {
        register(JacksonFeature.class);
        register(DownloadController.class);
        register(RootController.class);
    }
}
