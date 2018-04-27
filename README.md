# Gatling Download Proxy

This project's aim is to proxy Download of big files due to a Gatling limitation

### How to run

```
java -DAPP_PORT=8080 -Ddownloader.files.prefix=tmp -Ddownloader.files.downloadDirectory=/tmp -Ddownloader.files.noStoreFiles=false -jar download-proxy-0.0.1-SNAPSHOT.jar
```

Please bear in mind that the example above has the default values
