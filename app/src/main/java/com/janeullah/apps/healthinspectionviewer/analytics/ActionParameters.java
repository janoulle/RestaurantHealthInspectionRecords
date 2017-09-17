package com.janeullah.apps.healthinspectionviewer.analytics;

/**
 * @author Jane Ullah
 * @date 9/17/2017.
 */

public enum ActionParameters {
    UUID("uuid"),
    START_DATE("start_date"),
    END_DATE("end_date"),
    LOCATION("location"),
    CONTENT_TYPE("content_type"),
    CONTENT_NAME("content_name"),
    ITEM_NAME("item_name"),
    ITEM_VALUE("item_value");

    private String text;

    ActionParameters(String value){
        this.text = value;
    }

    public String text(){
        return text;
    }
}
