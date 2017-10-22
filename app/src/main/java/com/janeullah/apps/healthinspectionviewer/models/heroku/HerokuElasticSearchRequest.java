package com.janeullah.apps.healthinspectionviewer.models.heroku;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.janeullah.apps.healthinspectionviewer.constants.HerokuConstants;
import com.janeullah.apps.healthinspectionviewer.models.elasticsearch.BaseElasticSearchRequest;

/**
 * @author Jane Ullah
 * @date 10/21/2017.
 */

public class HerokuElasticSearchRequest extends BaseElasticSearchRequest {
    private static final String TAG = "HerokuEsSearchRequest";

    public HerokuElasticSearchRequest(){}

    public HerokuElasticSearchRequest(String searchValue) {
        populateSearchRequestObject(searchValue);
        generateAndSetAuthorizationHeader();
    }

    private void generateAndSetAuthorizationHeader() {
        try {
            Uri bonsaiUrl = Uri.parse(HerokuConstants.ES_HOST_URL);
            String encodedString = new String(Base64.encodeToString(bonsaiUrl.getUserInfo().getBytes("UTF-8"),Base64.DEFAULT));
            headers.put("Authorization", "Basic " + encodedString);
        } catch (Exception e) {
            Log.e(TAG, "Unspecified error while generating authorization headers", e);
        }
    }
}

