package com.minhld.utils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by minhld on 11/14/2016.
 */

public class Utils {
    /**
     * read a file in UTF-8 format
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static InputStream getInputStream(String url) throws IOException {
        URL website = new URL(url);
        URLConnection conn = website.openConnection();
        return conn.getInputStream();
    }

}
