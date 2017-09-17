package com.janeullah.apps.healthinspectionviewer.di.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.janeullah.apps.healthinspectionviewer.constants.YelpConstants;

import java.io.IOException;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * https://github.com/codepath/android_guides/wiki/Dependency-Injection-with-Dagger-2
 * @author Jane Ullah
 * @date 9/17/2017.
 */
@Module
public class NetModule {
    String mBaseUrl;
    public NetModule(String baseUrl){
        mBaseUrl = baseUrl;
    }

    @Provides
    @Named(YelpConstants.YELP_PREFERENCES)
    @Singleton
    SharedPreferences providesYelpSharedPreferences(Application application){
        return application.getApplicationContext().getSharedPreferences(YelpConstants.YELP_PREFERENCES, Context.MODE_PRIVATE);
    }

    // Dagger will only look for methods annotated with @Provides
    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        // Application reference must come from AppModule.class
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache) {
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
                .addInterceptor(logging)
                .cache(cache);
        return httpClient.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
        return retrofit;
    }
}
