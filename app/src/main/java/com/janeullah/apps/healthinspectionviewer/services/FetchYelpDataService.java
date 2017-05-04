package com.janeullah.apps.healthinspectionviewer.services;

import com.janeullah.apps.healthinspectionviewer.interfaces.YelpService;

import okhttp3.OkHttpClient;
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
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        RETROFIT = new Retrofit.Builder()
                .baseUrl(API_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        YELP_API_SERVICE = RETROFIT.create(YelpService.class);
    }
}
