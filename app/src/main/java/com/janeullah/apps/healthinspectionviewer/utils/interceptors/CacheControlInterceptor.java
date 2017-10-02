package com.janeullah.apps.healthinspectionviewer.utils.interceptors;


import com.janeullah.apps.healthinspectionviewer.constants.YelpConstants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * https://guides.codepath.com/android/Using-OkHttp#caching-network-responses
 * http://stackoverflow.com/questions/32727599/cache-post-requests-with-okhttp
 * http://stackoverflow.com/questions/23429046/can-retrofit-with-okhttp-use-cache-data-when-offline
 */

public class CacheControlInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=" + YelpConstants.MAX_STALE)
                .build();
    }
}
