package com.janeullah.apps.healthinspectionviewer.utils;

import android.util.Log;

import com.google.common.base.Charsets;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * http://docs.aws.amazon.com/AmazonS3/latest/API/sig-v4-examples-using-sdks.html Various Http
 * helper routines
 */
public class HttpUtils {
    private static final String TAG = "HttpUtils";

    private HttpUtils() {}

    /** Makes a http request to the specified endpoint */
    public static String invokeHttpRequest(
            URL endpointUrl, String httpMethod, Map<String, String> headers, String requestBody) {
        HttpURLConnection connection = createHttpConnection(endpointUrl, httpMethod, headers);
        try {
            if (requestBody != null) {
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(requestBody);
                wr.flush();
                wr.close();
            }
        } catch (Exception e) {
            EventLoggingUtils.logException(e);
            throw new RuntimeException("Request failed. " + e.getMessage(), e);
        }
        return executeHttpRequest(connection);
    }

    public static String executeHttpRequest(HttpURLConnection connection) {
        try {
            // Get Response
            InputStream is;
            try {
                is = connection.getInputStream();
            } catch (IOException e) {
                EventLoggingUtils.logException(e);
                is = connection.getErrorStream();
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charsets.UTF_8));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            EventLoggingUtils.logException(e);
            throw new RuntimeException("Request failed. " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static HttpURLConnection createHttpConnection(
            URL endpointUrl, String httpMethod, Map<String, String> headers) {
        try {
            HttpURLConnection connection = (HttpURLConnection) endpointUrl.openConnection();
            connection.setRequestMethod(httpMethod);

            if (headers != null) {
                Log.d(TAG, "--------- Request headers ---------");
                for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
                    Log.d(TAG, headerEntry.getKey() + ": " + headerEntry.getValue());
                    connection.setRequestProperty(headerEntry.getKey(), headerEntry.getValue());
                }
            }

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            return connection;
        } catch (Exception e) {
            EventLoggingUtils.logException(e);
            throw new RuntimeException("Cannot create connection. " + e.getMessage(), e);
        }
    }

    public static String urlEncode(String url, boolean keepPathSlash) {
        String encoded;
        try {
            encoded = URLEncoder.encode(url, Charsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            EventLoggingUtils.logException(e);
            throw new RuntimeException("UTF-8 encoding is not supported.", e);
        }
        if (keepPathSlash) {
            encoded = encoded.replace("%2F", "/");
        }
        return encoded;
    }
}
