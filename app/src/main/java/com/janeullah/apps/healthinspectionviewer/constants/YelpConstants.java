package com.janeullah.apps.healthinspectionviewer.constants;

/**
 * @author Jane Ullah
 * @date 5/3/2017.
 */

public class YelpConstants {
    private YelpConstants(){}
    // API constants, you shouldn't have to change these.
    public static final String API_HOST = "https://api.yelp.com";
    public static final String VERSION = "v3";
    public static final String SEARCH_PATH = "businesses/search";
    public static final String BUSINESS_PATH = "businesses/";  // Business ID will come after slash.
    public static final String TOKEN_PATH = "oauth2/token";
    public static final String DEFAULT_GRANT_TYPE = "client_credentials";

    public static final String GRANT_TYPE = "grant_type";
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET = "client_secret";


    public static final String LOCALE = "locale";
    public static final String LATITUDE = "latitude";
    public static final String SORT_BY = "sort_by";
    public static final String LONGITUDE = "longitude";
    public static final String TERM = "term";

    public static final int MAX_AGE = 60 * 60 * 24;
    public static final int MAX_STALE = 60 * 60 * 24;
    public static final String YELP_PREFERENCES = "yelp_preferences";
    public static final String SAVED_YELP_AUTH_TOKEN = "saved_yelp_auth_token";
    public static final String SAVED_YELP_TOKEN_EXPIRATION = "saved_yelp_token_expiration";
    public static final String SAVED_YELP_TOKEN_TYPE = "saved_yelp_token_type";

}
