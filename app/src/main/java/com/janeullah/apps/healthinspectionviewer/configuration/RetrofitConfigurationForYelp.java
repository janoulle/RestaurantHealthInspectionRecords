package com.janeullah.apps.healthinspectionviewer.configuration;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.janeullah.apps.healthinspectionviewer.constants.YelpConstants;
import com.janeullah.apps.healthinspectionviewer.services.YelpService;

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

public final class RetrofitConfigurationForYelp {
    private static final Retrofit RETROFIT;

    public static final YelpService YELP_API_SERVICE;

    private RetrofitConfigurationForYelp(){

    }

    static{
        //https://guides.codepath.com/android/Using-OkHttp#caching-network-responses
        //http://stackoverflow.com/questions/32727599/cache-post-requests-with-okhttp
        //http://stackoverflow.com/questions/23429046/can-retrofit-with-okhttp-use-cache-data-when-offline
        Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + YelpConstants.MAX_STALE)
                        .build();
            }
        };

        //https://github.com/codepath/android_guides/wiki/Debugging-with-Stetho
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(logging);

        RETROFIT = new Retrofit.Builder()
                .baseUrl(API_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        YELP_API_SERVICE = RETROFIT.create(YelpService.class);
    }
}
