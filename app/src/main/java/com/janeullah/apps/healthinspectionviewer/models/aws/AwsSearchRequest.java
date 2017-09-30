package com.janeullah.apps.healthinspectionviewer.models.aws;

import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import static com.janeullah.apps.healthinspectionviewer.constants.AwsElasticSearchConstants.AWS_ALGORITHM;
import static com.janeullah.apps.healthinspectionviewer.constants.AwsElasticSearchConstants.AWS_ES_HOST;
import static com.janeullah.apps.healthinspectionviewer.constants.AwsElasticSearchConstants.AWS_ES_READONLY_ACCESS_KEY;
import static com.janeullah.apps.healthinspectionviewer.constants.AwsElasticSearchConstants.AWS_ES_READONLY_SECRET;
import static com.janeullah.apps.healthinspectionviewer.constants.AwsElasticSearchConstants.AWS_ES_SERVICE;
import static com.janeullah.apps.healthinspectionviewer.constants.AwsElasticSearchConstants.AWS_REGION;
import static com.janeullah.apps.healthinspectionviewer.constants.AwsElasticSearchConstants.AWS_SEARCH_URL;
import static com.janeullah.apps.healthinspectionviewer.utils.AwsUtilities.convertByteArrayToHexadecimalAsLowercase;
import static com.janeullah.apps.healthinspectionviewer.utils.AwsUtilities.getSignatureKey;
import static com.janeullah.apps.healthinspectionviewer.utils.AwsUtilities.getString;
import static com.janeullah.apps.healthinspectionviewer.utils.AwsUtilities.hashPayload;
import static com.janeullah.apps.healthinspectionviewer.utils.AwsUtilities.hmacSHA256;
import static com.janeullah.apps.healthinspectionviewer.utils.StringUtilities.COLON;
import static com.janeullah.apps.healthinspectionviewer.utils.StringUtilities.FORWARD_SLASH;
import static com.janeullah.apps.healthinspectionviewer.utils.StringUtilities.NEW_LINE;
import static com.janeullah.apps.healthinspectionviewer.utils.StringUtilities.SEMICOLON;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * http://pokusak.blogspot.com/2015/10/aws-elasticsearch-request-signing.html
 * http://www.codeography.com/2016/03/20/signing-aws-api-requests-in-swift.html
 * @author Jane Ullah
 * @date 9/29/2017.
 */

public class AwsSearchRequest {
    private static final Gson gson = new Gson();
    private SortedMap<String, String> headersSorted = new TreeMap<>();
    private final AwsElasticSearchRequest searchRequest = new AwsElasticSearchRequest();
    private static final String TAG = "AwsSearchRequest";
    private String timeStampedDate = "";
    private String date = "";
    private String signedHeaders = "";

    private String canonicalRequest = "";
    private String stringToSign = "";
    private String signature = "";
    private String authorization = "";

    public AwsElasticSearchRequest getPayload(){
        return searchRequest;
    }

    public String getTimestampedDate(){
        return timeStampedDate;
    }

    public String getJsonPayload(){
        return gson.toJson(searchRequest);
    }

    public Map<String,String> getHeaders(){
        return headersSorted;
    }

    //https://github.com/square/retrofit/issues/1153
    public String getSignedHeaders(){
        return signedHeaders.trim();
    }

    public String getAuthorizationHeader(){
        return headersSorted.get("authorization");
    }

    public AwsSearchRequest(){}

    public AwsSearchRequest(String searchValue){
        populateSearchRequestObject(searchValue);
        generateDateInformation();
        headersSorted = generateHttpHeaders(timeStampedDate);
        generateSignatureAndAddAuthorizationHeader();
    }

    private SortedMap<String,String> generateHttpHeaders(String timeStampedDate){
        headersSorted.put("cache-control","no-cache");
        headersSorted.put("content-type","application/json");
        headersSorted.put("host", AWS_ES_HOST);
        headersSorted.put("x-amz-date", timeStampedDate);
        return headersSorted;
    }

    private void populateSearchRequestObject(String searchValue){
        Match matchTerm = new Match();
        matchTerm.setName(searchValue);
        Query query = new Query();
        query.setMatch(matchTerm);
        searchRequest.setQuery(query);
        searchRequest.setSize(100);
    }

    private void generateDateInformation() {
        Calendar cal = Calendar.getInstance();
        DateFormat dfmT = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.US);
        dfmT.setTimeZone(TimeZone.getTimeZone("GMT"));
        timeStampedDate = dfmT.format(cal.getTime());
        DateFormat dfmD = new SimpleDateFormat("yyyyMMdd", Locale.US);
        dfmD.setTimeZone(TimeZone.getTimeZone("GMT"));
        date = dfmD.format(cal.getTime());
    }

    private void generateSignatureAndAddAuthorizationHeader(){
        String canonicalRequest = createCanonicalRequest();
        String stringToSign = createStringToSign(canonicalRequest);
        String signature = createSignature(stringToSign);
        String authorizationHeader = createAuthorizationHeader(signature);
        this.canonicalRequest = canonicalRequest;
        this.stringToSign = stringToSign;
        this.signature = signature;
        this.authorization = authorizationHeader;
        Log.v(TAG,"canonicalRequest="+canonicalRequest);
        Log.v(TAG,"stringToSign="+stringToSign);
        Log.v(TAG,"signature="+signature);
        Log.v(TAG,"authorizationHeader="+authorizationHeader);
        headersSorted.put("authorization",authorizationHeader);
    }

    private String createAuthorizationHeader(String signature){
        StringBuilder authHeader = new StringBuilder();
        authHeader.append(AWS_ALGORITHM);
        authHeader.append(" Credential=");
        authHeader.append(AWS_ES_READONLY_ACCESS_KEY + FORWARD_SLASH);
        authHeader.append(date);
        authHeader.append(FORWARD_SLASH);
        authHeader.append(getCredentialScope());
        authHeader.append(", SignedHeaders=");
        authHeader.append(getSignedHeaders());
        authHeader.append(", Signature=");
        authHeader.append(signature);
        return authHeader.toString();
    }

    /**
     * http://docs.aws.amazon.com/general/latest/gr/sigv4-create-canonical-request.html
     */
    private String createCanonicalRequest(){
        StringBuilder canonicalRequestBuilder = new StringBuilder();
        canonicalRequestBuilder.append("POST");
        canonicalRequestBuilder.append(NEW_LINE);
        canonicalRequestBuilder.append(AWS_SEARCH_URL);
        canonicalRequestBuilder.append(NEW_LINE);
        canonicalRequestBuilder.append("");//no query string so use empty string
        canonicalRequestBuilder.append(NEW_LINE);

        StringBuilder signedHeadersBuilder = new StringBuilder();
        for(Map.Entry<String,String> entry : headersSorted.entrySet()){
            canonicalRequestBuilder.append(trim(entry.getKey()) + COLON + trim(entry.getValue()));
            canonicalRequestBuilder.append(NEW_LINE);
            signedHeadersBuilder.append(entry.getKey() + SEMICOLON);
        }
        signedHeaders = trimTrailingSemiColonAndAppendNewLine(signedHeadersBuilder);
        canonicalRequestBuilder.append(signedHeadersBuilder);
        canonicalRequestBuilder.append(NEW_LINE);
        canonicalRequestBuilder.append(createHashedPayload());
        return canonicalRequestBuilder.toString();
    }

    /**
     * http://docs.aws.amazon.com/general/latest/gr/sigv4-create-string-to-sign.html
     * @param canonicalRequest Generated 'canonical request'
     * @return return the created string
     */
    private String createStringToSign(String canonicalRequest){
        StringBuilder signedString = new StringBuilder();
        signedString.append(AWS_ALGORITHM);
        signedString.append(NEW_LINE);
        signedString.append(getCredentialScope());
        signedString.append(NEW_LINE);
        signedString.append(createHashedCanonicalRequest(canonicalRequest));
        return signedString.toString();
    }

    /**
     * http://docs.aws.amazon.com/general/latest/gr/sigv4-calculate-signature.html
     * @param stringToBeSigned hashed payload
     * @return return calculated signature
     */
    private String createSignature(String stringToBeSigned) {
        try {
            byte[] signatureInformation = getSignatureKey(AWS_ES_READONLY_SECRET, date, AWS_REGION, AWS_ES_SERVICE);
            byte[] hashedPayload = hmacSHA256(stringToBeSigned,signatureInformation);
            return convertByteArrayToHexadecimalAsLowercase(hashedPayload);
        }catch(Exception e){
            FirebaseCrash.report(e);
            Log.e(TAG, "Failed to generate signature of payload = " + stringToBeSigned);
        }
        return "";
    }

    private String trimTrailingSemiColonAndAppendNewLine(StringBuilder signedHeaders){
        if (signedHeaders.length() >= 1 && signedHeaders.charAt(signedHeaders.length()-1) == ';'){
            return signedHeaders.substring(0,signedHeaders.length()-1) + NEW_LINE;
        }
        return signedHeaders + NEW_LINE;
    }

    //YYYYMMDD'T'HHMMSS'Z'
    //https://stackoverflow.com/questions/7401841/java-date-formatting
    /*private String generateDateForHeader(){
        //DateTime now = DateTime.now( DateTimeZone.UTC );
        //return now.toString();
        final SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.US);
        f.setTimeZone(TimeZone.getTimeZone("UTC"));
        return f.format(new Date());
    }*/

    private String getCredentialScope() {
        return AWS_REGION + '/' + AWS_ES_SERVICE + '/' + "aws4_request";
    }

    private String createHashedPayload(){
        return createHexEncodingOfHashedData(getString(searchRequest));
    }

    private String createHashedCanonicalRequest(String request){
        return createHexEncodingOfHashedData(request);
    }

    private String createHexEncodingOfHashedData(String data){
        return convertByteArrayToHexadecimalAsLowercase(hashPayload(getString(data)));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("headers", headersSorted)
                .append("payload", searchRequest)
                .append("canonical_request", canonicalRequest)
                .append("string_to_sign", stringToSign)
                .append("signature", signature)
                .append("authorization", authorization)
                .toString();
    }
}
