package com.minhld.httpd;

import android.content.Context;
import android.util.Log;

import com.minhld.utils.Utils;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.protocols.http.response.Status;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by minhld on 2/20/2016.
 */
public class EnhancedHTTPD extends NanoHTTPD {
    public static final String SERVER_DEF_HOST = "localhost";
    public static final int SERVER_DEF_PORT = 3883;

    final String TAG = "EnhancedHTTPD";

    static final String MIME_DEFAULT_BINARY = "application/octet-stream";

    static final Map<String,String> textMimes = new HashMap<String, String>() {{
        put("", "text/html");
        put("css", "text/css");
        put("htm", "text/html");
        put("html", "text/html");
        put("xhtml", "application/xhtml+xml");
        put("xml", "text/xml");
        put("json", "application/json");
        put("java", "text/x-java-source, text/java");
        put("md", "text/plain");
        put("txt", "text/plain");
        put("asc", "text/plain");
    }};

    static final Map<String,String> imageMimes = new HashMap<String, String>() {{
        put("gif", "image/gif");
        put("jpg", "image/jpeg");
        put("jpeg", "image/jpeg");
        put("png", "image/png");
    }};

    static final Map<String,String> binaryMimes = new HashMap<String, String>() {{
        put("mp3", "audio/mpeg");
        put("m3u", "audio/mpeg-url");
        put("mp4", "video/mp4");
        put("ogv", "video/ogg");
        put("flv", "video/x-flv");
        put("mov", "video/quicktime");
        put("swf", "application/x-shockwave-flash");
        put("js", "application/javascript");
        put("pdf", "application/pdf");
        put("doc", "application/msword");
        put("ogg", "application/x-ogg");
        put("zip", "application/octet-stream");
        put("exe", "application/octet-stream");
        put("class", "application/octet-stream");
    }};

    EnhancedHTTPD enhanceServer;
    Context context;

    public EnhancedHTTPD(Context context) {
        super(EnhancedHTTPD.SERVER_DEF_HOST, EnhancedHTTPD.SERVER_DEF_PORT);
        this.context = context;
    }

    public EnhancedHTTPD(Context context, String hostName, int port){
        super(hostName, port);
        this.context = context;
    }

    @Override
    public Response serve(IHTTPSession session) {
//        String msg = "<html><body><h1>Hello server</h1>\n";
//        Map<String, String> parms = session.getParms();
//        if (parms.get("username") == null) {
//            msg += "<form action='?' method='get'>\n  <p>Your name: <input type='text' name='username'></p>\n" + "</form>\n";
//        } else {
//            msg += "<p>Hello, " + parms.get("username") + "!</p>";
//        }
//        return Response.newFixedLengthResponse(msg + "</body></html>\n");

        String uri = session.getUri();

        if (uri.length() > 0 && uri.startsWith("/")) {
            uri = uri.substring(1);
        }

        // if URI contains a default icon
        if (uri.contains("favicon")){
            uri = "";
        }

        if (uri.equals("")) return null;

        // get mime-type
        String mime = MIME_DEFAULT_BINARY;
        int dot = uri.lastIndexOf('.');
        String ext = "";
        if (dot >= 0) {
            ext = uri.substring(dot + 1).toLowerCase();
        }

        // if the URI is text
        mime = textMimes.get(ext);
        if (mime != null) {
            return solveText(mime, uri);
        }

        // if the URI is image
        mime = imageMimes.get(ext);
        if (mime != null) {
            return solveImage(mime, uri);
        }

        // if the URI is image
        mime = binaryMimes.get(ext);
        if (mime != null) {

        }

        // other types, will be considered as text
        return solveText(mime, uri);
    }

    private Response solveText(String mimeType, String uri) {
        try{
            InputStream is = Utils.getInputStream(uri);
            Response res = Response.newFixedLengthResponse(Status.OK, mimeType, is, is.available());
//            if (res != null){
//                String eTag = Integer.toHexString(new Random().nextInt());
//                res.addHeader("ETag", eTag);
//                return res;
//            }
            return res;
        }catch(IOException e){
            Log.v(TAG, e.getMessage());
            return null;
        }
    }

    private Response solveImage(String mimeType, String uri) {
        Response res = null;
        try{
            String eTag = "";
            InputStream is = new FileInputStream(uri);
            res = Response.newFixedLengthResponse(Status.OK, mimeType, is, is.available());
            if (res != null){
                eTag = Integer.toHexString(new Random().nextInt());
                res.addHeader("ETag", eTag);
                return res;
            }
        }catch(IOException e){
            Log.v(TAG, e.getMessage());
        }
        return null;
    }
}
