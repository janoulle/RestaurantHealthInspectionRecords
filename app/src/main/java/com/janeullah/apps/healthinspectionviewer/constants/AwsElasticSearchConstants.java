package com.janeullah.apps.healthinspectionviewer.constants;

import com.amazonaws.regions.Regions;
import com.janeullah.apps.healthinspectionviewer.BuildConfig;

/**
 * @author Jane Ullah
 * @date 9/26/2017.
 */
public class AwsElasticSearchConstants {
    private AwsElasticSearchConstants() {}

    public static final String AWS_ES_SEARCH_PATH = "restaurants/_search";

    public static final String AWS_ES_READONLY_SECRET =
            ""; // Build Config key: AWS_ES_READONLY_SECRET;
    public static final String AWS_ES_READONLY_ACCESS_KEY =
            ""; // Build Config key: AWS_ES_READONLY_ACCESS_KEY;
    public static final String AWS_REGION = Regions.US_EAST_1.getName();
    public static final String AWS_COGNITO_POOL_ID =
            "us-east-1:6dc9b0c7-fa37-4a72-b709-907d22b0635f";
    public static final String AWS_ES_SERVICE = "es";
    public static final String AWS_ALGORITHM = "AWS4-HMAC-SHA256";
    public static final String HMAC_ALGORITHM = "HmacSHA256";
    public static final String AWS_ES_HOST = BuildConfig.AWS_ES_HOST;
    public static final String AWS_ES_HOST_URL = "https://" + AWS_ES_HOST;
    public static final String AWS_SEARCH_URL = "https://" + AWS_ES_HOST + "/" + AWS_ES_SEARCH_PATH;
}
