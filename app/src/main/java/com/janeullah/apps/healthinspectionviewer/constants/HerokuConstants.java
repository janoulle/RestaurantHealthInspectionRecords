package com.janeullah.apps.healthinspectionviewer.constants;

import com.janeullah.apps.healthinspectionviewer.BuildConfig;

/**
 * @author Jane Ullah
 * @date 10/21/2017.
 */

public class HerokuConstants {
    private HerokuConstants(){}
    public static final String ES_SEARCH_PATH = "restaurants/_search";
    public static final String ES_HOST_URL = "https://" + BuildConfig.HEROKU_NEGA_ES_HOST;
    public static final String ES_SEARCH_URL = ES_HOST_URL + "/estaurants/_search";
}
