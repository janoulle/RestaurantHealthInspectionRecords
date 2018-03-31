package com.janeullah.apps.healthinspectionviewer.services.yelp;

import com.janeullah.apps.healthinspectionviewer.models.yelp.YelpAuthTokenResponse;
import com.janeullah.apps.healthinspectionviewer.models.yelp.YelpResults;
import com.janeullah.apps.healthinspectionviewer.utils.StringUtilities;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

import static com.janeullah.apps.healthinspectionviewer.constants.YelpConstants.SEARCH_PATH;
import static com.janeullah.apps.healthinspectionviewer.constants.YelpConstants.TOKEN_PATH;
import static com.janeullah.apps.healthinspectionviewer.constants.YelpConstants.VERSION;

/**
 * https://futurestud.io/tutorials/android-basic-authentication-with-retrofit
 * http://www.vogella.com/tutorials/Retrofit/article.html#retrofit
 * https://guides.codepath.com/android/Consuming-APIs-with-Retrofit
 * https://guides.codepath.com/android/RxJava http://www.jsonschema2pojo.org/
 * https://github.com/joelittlejohn/jsonschema2pojo/tree/master/jsonschema2pojo-gradle-plugin
 * https://guides.codepath.com/android/Leveraging-the-Gson-Library
 * https://futurestud.io/tutorials/retrofit-2-activate-response-caching-etag-last-modified
 * https://futurestud.io/tutorials/retrofit-2-upgrade-guide-from-1-9
 *
 * @author Jane Ullah
 * @date 5/4/2017.
 */
public interface YelpService {

    @FormUrlEncoded
    @Headers({"Cache-Control: max-age=86400"})
    @POST(TOKEN_PATH)
    Call<YelpAuthTokenResponse> getAuthToken(@FieldMap Map<String, String> requestData);

    @GET(VERSION + StringUtilities.FORWARD_SLASH + SEARCH_PATH)
    Call<YelpResults> searchBusinesses(
            @Header("Authorization") String token, @QueryMap Map<String, Object> params);
}
