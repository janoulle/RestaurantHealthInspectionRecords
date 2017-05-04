package com.janeullah.apps.healthinspectionviewer.models.yelp;

import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedRestaurant;
import com.janeullah.apps.healthinspectionviewer.dtos.GeocodedAddressComponent;

/**
 * @author Jane Ullah
 * @date 5/4/2017.
 */

public class YelpSearchRequest {
    public static YelpAuthTokenResponse bearerToken;
    public static GeocodedAddressComponent restaurantMetadata;
    public static FlattenedRestaurant restaurant;
}
