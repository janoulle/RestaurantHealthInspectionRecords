package com.janeullah.apps.healthinspectionviewer.services.heroku;

import com.janeullah.apps.healthinspectionviewer.models.elasticsearch.ElasticSearchRequest;
import com.janeullah.apps.healthinspectionviewer.models.elasticsearch.ElasticSearchResponse;
import com.janeullah.apps.healthinspectionviewer.utils.StringUtilities;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

import static com.janeullah.apps.healthinspectionviewer.constants.HerokuConstants.ES_SEARCH_PATH;

/**
 * @author Jane Ullah
 * @date 10/21/2017.
 */

public interface HerokuElasticSearchService {
    @POST(StringUtilities.FORWARD_SLASH + ES_SEARCH_PATH)
    Call<ElasticSearchResponse> findRestaurantsByName(@HeaderMap Map<String,String> headerParameters,
                                                      @Body ElasticSearchRequest searchRequest);

}
