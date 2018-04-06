package com.janeullah.apps.healthinspectionviewer.models.heroku;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.google.common.base.Charsets;
import com.janeullah.apps.healthinspectionviewer.constants.HerokuConstants;
import com.janeullah.apps.healthinspectionviewer.models.elasticsearch.BaseElasticSearchRequest;

/**
 * @author Jane Ullah
 * @date 10/21/2017.
 */
public class HerokuElasticSearchRequest extends BaseElasticSearchRequest {
    private static final String TAG = "HerokuEsSearchRequest";

    public HerokuElasticSearchRequest() {}

    public HerokuElasticSearchRequest(String searchValue) {
        populateSearchRequestObject(searchValue);
        generateAndSetAuthorizationHeader();
    }

    private void generateAndSetAuthorizationHeader() {
        try {
            Uri bonsaiUrl = Uri.parse(HerokuConstants.ES_HOST_URL);
            // https://developer.android.com/reference/android/util/Base64.html. Default adds the
            // new
            // lines
            String encodedString =
                    Base64.encodeToString(
                            bonsaiUrl.getUserInfo().getBytes(Charsets.UTF_8), Base64.NO_WRAP);
            headers.put("Authorization", "Basic " + encodedString);
        } catch (Exception e) {
            Log.e(TAG, "Unspecified error while generating authorization headers", e);
        }
    }
}
