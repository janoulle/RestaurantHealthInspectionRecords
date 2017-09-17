package com.janeullah.apps.healthinspectionviewer.di.components;

import android.content.SharedPreferences;

import com.janeullah.apps.healthinspectionviewer.di.modules.AppModule;
import com.janeullah.apps.healthinspectionviewer.di.modules.NetModule;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author Jane Ullah
 * @date 9/17/2017.
 */

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {
    // downstream components need these exposed
    Retrofit retrofit();
    OkHttpClient okHttpClient();
    SharedPreferences sharedPreferences();
}
