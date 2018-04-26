package org.nuxeo.downloadproxy.rest.api.model;

import lombok.Data;

@Data
public class Info {
    public String fileDownloadLocation;
    public String filePrefix;
    public String fileSuffix;
}
