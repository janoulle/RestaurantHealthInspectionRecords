package com.janeullah.apps.healthinspectionviewer.utils;

/**
 * TODO: this feels off. Refactor
 * @author Jane Ullah
 * @date 5/3/2017.
 */

public enum  YelpQueryParams{
    DEFAULT_SEARCH_LOCALE("locale","en_US"),
    DEFAULT_RADIUS("radius",16094),
    DEFAULT_SORT("sort_by","best_match"),
    DEFAULT_LIMIT("limit",3);

    private String key;
    private Object value;

    YelpQueryParams(String key, Object value){
        this.key = key;
        this.value  = value;
    }


    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
