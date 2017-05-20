package com.janeullah.apps.healthinspectionviewer.utils;

import retrofit2.Response;

/**
 * @author Jane Ullah
 * @date 5/3/2017.
 */

public class YelpUtils {
    //http://stackoverflow.com/questions/41727750/detect-if-okhttp-response-comes-from-cache-with-retrofit
    public static <T> boolean isFromCache(Response<T> response) {
        return response.raw().cacheResponse() != null;
    }

    public static <T> boolean isFromNetwork(Response<T> response) {
        return response.raw().networkResponse() != null;
    }

}
