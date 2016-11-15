package com.minhld.httpd;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import java.util.List;

public class EnhancedWebServer {
	private static EnhancedHTTPD enhancedHTTPD;
	private static String innerHostName;

	public static void startServer(Context context) {
		WifiManager WiMan = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
		WifiInfo wifiInfo = WiMan.getConnectionInfo();
		int address = wifiInfo.getIpAddress();
		String ipAddress = Formatter.formatIpAddress(address);
		innerHostName = ipAddress;
		try {
			enhancedHTTPD = new EnhancedHTTPD(context, ipAddress, EnhancedHTTPD.SERVER_DEF_PORT);
			enhancedHTTPD.start();
		} catch (Exception e) {
			// if port is occupied by other third parties
			// port will be updated to a random number around the default value
			int newPort = EnhancedHTTPD.SERVER_DEF_PORT + (int) (Math.random() * 200 + 1);
			try {
				enhancedHTTPD = new EnhancedHTTPD(context, EnhancedHTTPD.SERVER_DEF_HOST, newPort);
				enhancedHTTPD.start();
			} catch (Exception ex) {
				// no further catch, let it go
			}
		}
	}

	public static void stopServer() {
		if (enhancedHTTPD != null) {
			enhancedHTTPD.stop();
		}
	}
	
	/**
	 * return the URL that contents are decrypted
	 * 
	 * @param url
	 */
	public static String getUrl(String url) {
		return "http://" + innerHostName + ":" + EnhancedHTTPD.SERVER_DEF_PORT + "/" + url;
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
