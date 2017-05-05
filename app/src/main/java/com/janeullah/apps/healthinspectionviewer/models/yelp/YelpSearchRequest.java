package com.janeullah.apps.healthinspectionviewer.models.yelp;

import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedRestaurant;
import com.janeullah.apps.healthinspectionviewer.dtos.GeocodedAddressComponent;

/**
 * @author Jane Ullah
 * @date 5/4/2017.
 */

public class YelpSearchRequest {
    public YelpAuthTokenResponse bearerToken;
    public GeocodedAddressComponent restaurantMetadata;
    public FlattenedRestaurant restaurant;

    public String getBearerToken(){
        return "Bearer " + bearerToken.getAccessToken();
    }

    public double getLatitude(){
        if (restaurantMetadata != null && restaurantMetadata.coordinates != null){
            return restaurantMetadata.coordinates.latitude;
        }
        return 0d;
    }

    public double getLongitude(){
        if (restaurantMetadata != null && restaurantMetadata.coordinates != null){
            return restaurantMetadata.coordinates.longitude;
        }
        return 0d;
    }

    public YelpSearchRequest(){
        /**
         * Default constructor
         */
    }

    public YelpSearchRequest(YelpAuthTokenResponse bearerToken, GeocodedAddressComponent component, FlattenedRestaurant restaurant){
        this.bearerToken = bearerToken;
        this.restaurantMetadata = component;
        this.restaurant = restaurant;
    }


}
