package org.nuxeo.downloadproxy.rest.api.controllers;

import java.util.Collections;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

@Component
@Path("/")
public class RootController {

    @GET
    @Path("/info")
    public Map<String, Object> info() {
        return Collections.emptyMap();
    }
}
