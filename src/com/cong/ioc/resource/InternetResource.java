package com.cong.ioc.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class InternetResource implements Resource {

    private String url;

    public InternetResource(String url) {
        this.url = url;
    }

    @Override
    public InputStream getInputStream() throws IOException {

        try {
            HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();

            return connection.getInputStream();
        } catch(IOException ex) {
            throw ex;
        }
    }

}
