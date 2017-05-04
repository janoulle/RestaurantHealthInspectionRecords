package com.janeullah.apps.healthinspectionviewer.services;

import com.janeullah.apps.healthinspectionviewer.interfaces.YelpService;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.janeullah.apps.healthinspectionviewer.constants.YelpConstants.API_HOST;

/**
 * https://futurestud.io/tutorials/retrofit-2-log-requests-and-responses
 * @author Jane Ullah
 * @date 5/4/2017.
 */

public final class FetchYelpDataService {
    public static final Retrofit RETROFIT;

    public static final YelpService YELP_API_SERVICE;

    private FetchYelpDataService(){

    }

    static{
        //http://stackoverflow.com/questions/23429046/can-retrofit-with-okhttp-use-cache-data-when-offline
        Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                String cacheControl = originalResponse.header("Cache-Control");
                if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                        cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24)
                            .build();
                } else {
                    return originalResponse;
                }
            }
        };

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);
        httpClient.addInterceptor(logging);

        RETROFIT = new Retrofit.Builder()
                .baseUrl(API_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        YELP_API_SERVICE = RETROFIT.create(YelpService.class);
    }
}
