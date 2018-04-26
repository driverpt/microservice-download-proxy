package org.nuxeo.downloadproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class DownloadProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(DownloadProxyApplication.class, args);
    }
}
