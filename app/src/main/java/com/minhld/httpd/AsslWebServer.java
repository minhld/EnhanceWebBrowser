package com.minhld.httpd;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class AsslWebServer {
	private static AsslHTTPD asslServer;
	private static String innerHostName;

	public static void startServer(Context context) {
		WifiManager WiMan = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
		WifiInfo wifiInfo = WiMan.getConnectionInfo();
		int address = wifiInfo.getIpAddress();
		String ipAddress = Formatter.formatIpAddress(address);
		innerHostName = ipAddress;
		try {
			asslServer = new AsslHTTPD(context, ipAddress, AsslHTTPD.SERVER_DEF_PORT);
			asslServer.start();
		} catch (Exception e) {
			// if port is occupied by other third parties
			// port will be updated to a random number around the default value
			int newPort = AsslHTTPD.SERVER_DEF_PORT + (int) (Math.random() * 200 + 1);
			try {
				asslServer = new AsslHTTPD(context, AsslHTTPD.SERVER_DEF_HOST, newPort);
				asslServer.start();
			} catch (Exception ex) {
				// no further catch, let it go
			}
		}
	}

	public static void stopServer() {
		if (asslServer != null) {
			asslServer.stop();
		}
	}
	
	/**
	 * return the URL that contents are decrypted
	 * 
	 * @param url
	 */
	public static String getUrl(String url) {
		return "http://" + innerHostName + ":" + AsslHTTPD.SERVER_DEF_PORT + "/" + url;
	}
	
	public static String concatenateStrings(List<String> items)
    {
        if (items == null)
            return null;
        if (items.size() == 0)
            return "";
        int expectedSize = 0;
        for (String item: items)
            expectedSize += item.length();
        StringBuffer result = new StringBuffer(expectedSize);
        for (String item: items)
            result.append(item);
        return result.toString();
    }
}
