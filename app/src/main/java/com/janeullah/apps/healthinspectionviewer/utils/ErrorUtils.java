package com.janeullah.apps.healthinspectionviewer.utils;


import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.janeullah.apps.healthinspectionviewer.constants.HerokuConstants;
import com.janeullah.apps.healthinspectionviewer.models.heroku.HerokuAppSleepingResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.janeullah.apps.healthinspectionviewer.configuration.RetrofitConfiguration.getRetrofitBuilder;

/**
 * https://futurestud.io/tutorials/retrofit-2-simple-error-handling
 * https://futurestud.io/tutorials/retrofit-2-error-handling-for-synchronous-requests
 */

public class ErrorUtils {
    private static final String TAG = "AwsSearchTask";
    private static final Retrofit HEROKU_RETROFIT = getRetrofitBuilder(HerokuConstants.ES_HOST_URL).build();

    private ErrorUtils() {
    }

    public static HerokuAppSleepingResponse parseError(Response<?> response) {
        try {
            Converter<ResponseBody, HerokuAppSleepingResponse> converter =
                    HEROKU_RETROFIT.responseBodyConverter(HerokuAppSleepingResponse.class, new Annotation[0]);
            return converter.convert(response.errorBody());
        } catch (IOException e) {
            FirebaseCrash.report(e);
            Log.e(TAG,"Error while converting retrofit response to Heroku Error");
            return new HerokuAppSleepingResponse(e);
        }
    }

}