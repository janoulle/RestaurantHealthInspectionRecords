package com.janeullah.apps.healthinspectionviewer.constants;

import com.janeullah.apps.healthinspectionviewer.BuildConfig;

/**
 * @author Jane Ullah
 * @date 9/26/2017.
 */

public class AwsElasticSearchConstants {
    private AwsElasticSearchConstants(){}
    public static final String AWS_ES_SEARCH_PATH = "restaurants/_search";

    public static final String AWS_ES_READONLY_SECRET = BuildConfig.AWS_ES_READONLY_SECRET;
    public static final String AWS_ES_READONLY_ACCESS_KEY = BuildConfig.AWS_ES_READONLY_ACCESS_KEY;
    public static final String AWS_REGION = "us-east-1";
    public static final String AWS_ES_SERVICE = "es";
    public static final String AWS_ALGORITHM = "AWS4-HMAC-SHA256";
    public static final String HMAC_ALGORITHM = "HmacSHA256";
    public static final String AWS_ES_HOST = BuildConfig.AWS_ES_HOST;
    public static final String AWS_ES_HOST_URL = "https://" + AWS_ES_HOST;
    public static final String AWS_SEARCH_URL = "https://" + AWS_ES_HOST + "/" + AWS_ES_SEARCH_PATH;
}

