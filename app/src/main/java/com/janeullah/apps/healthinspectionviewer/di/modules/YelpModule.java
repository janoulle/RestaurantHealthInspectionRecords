package com.janeullah.apps.healthinspectionviewer.di.modules;

import com.janeullah.apps.healthinspectionviewer.di.scopes.AppScope;
import com.janeullah.apps.healthinspectionviewer.network.interfaces.YelpApiInterface;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author Jane Ullah
 * @date 9/17/2017.
 */
@Module
public class YelpModule {

    @Provides
    @AppScope
    public YelpApiInterface providesYelpApiService(Retrofit retrofit) {
        return retrofit.create(YelpApiInterface.class);
    }
}
