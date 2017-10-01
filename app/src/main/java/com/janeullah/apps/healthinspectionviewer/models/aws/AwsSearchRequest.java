package com.janeullah.apps.healthinspectionviewer.models.aws;

import android.util.Log;

import com.google.gson.Gson;
import com.janeullah.apps.healthinspectionviewer.auth.aws.AWS4SignerBase;
import com.janeullah.apps.healthinspectionviewer.auth.aws.AWS4SignerForAuthorizationHeader;
import com.janeullah.apps.healthinspectionviewer.utils.BinaryUtils;
import com.janeullah.apps.healthinspectionviewer.utils.StringUtilities;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.janeullah.apps.healthinspectionviewer.constants.AwsElasticSearchConstants.AWS_ES_READONLY_ACCESS_KEY;
import static com.janeullah.apps.healthinspectionviewer.constants.AwsElasticSearchConstants.AWS_ES_READONLY_SECRET;
import static com.janeullah.apps.healthinspectionviewer.constants.AwsElasticSearchConstants.AWS_ES_SERVICE;
import static com.janeullah.apps.healthinspectionviewer.constants.AwsElasticSearchConstants.AWS_REGION;
import static com.janeullah.apps.healthinspectionviewer.constants.AwsElasticSearchConstants.AWS_SEARCH_URL;

/**
 * ttps://github.com/square/retrofit/issues/1153
 * http://pokusak.blogspot.com/2015/10/aws-elasticsearch-request-signing.html
 * http://www.codeography.com/2016/03/20/signing-aws-api-requests-in-swift.html
 *
 * @author Jane Ullah
 * @date 9/29/2017.
 */

public class AwsSearchRequest {
    private static final Gson gson = new Gson();
    private Map<String, String> headers = new HashMap<>();
    private final AwsElasticSearchRequest searchRequest = new AwsElasticSearchRequest();
    private static final String TAG = "AwsSearchRequest";

    public AwsElasticSearchRequest getPayload() {
        return searchRequest;
    }

    public String getJsonPayload() {
        return gson.toJson(searchRequest);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public AwsSearchRequest() {
    }

    public AwsSearchRequest(String searchValue) {
        populateSearchRequestObject(searchValue);
        setup();
    }

    private void setup() {
        try {
            // precompute hash of the body content
            String payload = StringUtilities.getString(searchRequest);
            byte[] contentHash = AWS4SignerBase.hash(payload);
            String contentHashString = BinaryUtils.toHex(contentHash);

            //instantiate class doing the heavy lifting
            headers.put("content-length", Integer.toString(payload.length()));
            AWS4SignerForAuthorizationHeader signer = new AWS4SignerForAuthorizationHeader(new URL(AWS_SEARCH_URL), "POST", AWS_ES_SERVICE, AWS_REGION);

            String authorization = signer.computeSignature(headers,
                    null, // no query parameters
                    contentHashString,
                    AWS_ES_READONLY_ACCESS_KEY,
                    AWS_ES_READONLY_SECRET);

            // express authorization for this as a header
            headers.put("Authorization", authorization);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Malformed url in use for AWS query", e);
        } catch (Exception e) {
            Log.e(TAG, "Unspecified error while generating signature", e);
        }
    }

    private void populateSearchRequestObject(String searchValue) {
        Match matchTerm = new Match();
        matchTerm.setName(searchValue);
        Query query = new Query();
        query.setMatch(matchTerm);
        searchRequest.setQuery(query);
        searchRequest.setSize(100);
    }

        @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("headers", headers)
                .append("payload", searchRequest)
                .toString();
    }
}
