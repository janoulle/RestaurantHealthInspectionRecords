package com.janeullah.apps.healthinspectionviewer.configuration;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.janeullah.apps.healthinspectionviewer.services.aws.AwsElasticSearchService;
import com.janeullah.apps.healthinspectionviewer.services.yelp.YelpService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.janeullah.apps.healthinspectionviewer.constants.AwsElasticSearchConstants.AWS_ES_HOST_URL;
import static com.janeullah.apps.healthinspectionviewer.constants.YelpConstants.YELP_API_HOST;

/**
 * https://stackoverflow.com/questions/36498131/set-dynamic-base-url-using-retrofit-2-0-and-dagger-2
 * https://github.com/square/okhttp/wiki/Interceptors
 * https://futurestud.io/tutorials/retrofit-2-log-requests-and-responses
 * @author Jane Ullah
 * @date 5/4/2017.
 */

public final class RetrofitConfiguration {
    public static final String TAG = "RetrofitConfig";

    private static OkHttpClient.Builder httpClient;

    public static final YelpService YELP_SERVICE;
    public static final AwsElasticSearchService ELASTIC_SEARCH_SERVICE;

    private RetrofitConfiguration(){}

    static{
        setOkHttpClient();

        YELP_SERVICE = getRetrofitBuilder(YELP_API_HOST).build().create(YelpService.class);
        ELASTIC_SEARCH_SERVICE = getRetrofitBuilder(AWS_ES_HOST_URL).build().create(AwsElasticSearchService.class);
    }

    private static void setOkHttpClient() {
        //https://github.com/codepath/android_guides/wiki/Debugging-with-Stetho
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(logging);
    }

    private static Retrofit.Builder getRetrofitBuilder(String apiBaseUrl){
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .baseUrl(apiBaseUrl);
    }
}
