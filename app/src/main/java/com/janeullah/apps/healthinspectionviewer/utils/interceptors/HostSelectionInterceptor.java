package com.janeullah.apps.healthinspectionviewer.utils.interceptors;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * https://github.com/square/retrofit/pull/1652
 * https://gist.github.com/swankjesse/8571a8207a5815cca1fb#file-hostselectioninterceptor-java
 */
public class HostSelectionInterceptor implements Interceptor {
    private volatile String host;

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String host = this.host;
        if (host != null) {
            HttpUrl newUrl = request.url().newBuilder().host(host).build();
            request = request.newBuilder().url(newUrl).build();
        }
        return chain.proceed(request);
    }
}
