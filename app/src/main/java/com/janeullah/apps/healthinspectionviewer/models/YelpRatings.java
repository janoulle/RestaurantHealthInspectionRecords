package com.janeullah.apps.healthinspectionviewer.models;

/**
 * @author Jane Ullah
 * @date 5/6/2017.
 */
public enum YelpRatings {
    YELP_ZERO_STARS(0);

    int imageResourceId;

    YelpRatings(int resourceId) {
        imageResourceId = resourceId;
    }
}
