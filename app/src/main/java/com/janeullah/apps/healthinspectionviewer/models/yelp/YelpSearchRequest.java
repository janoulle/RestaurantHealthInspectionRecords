package com.janeullah.apps.healthinspectionviewer.models.yelp;

import android.text.TextUtils;

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

    public boolean matches(Business yelpBusinessObject){
        if (yelpBusinessObject != null && yelpBusinessObject.getLocation() != null){
            Location yelpLocation = yelpBusinessObject.getLocation();
            boolean hasMatchedBasicData = (TextUtils.equals(yelpLocation.getZipCode(),restaurantMetadata.postalCode) &&
                    TextUtils.equals(yelpLocation.getCity(),restaurantMetadata.locality) &&
                    TextUtils.equals(yelpLocation.getState(),restaurantMetadata.state));
            if (hasMatchedBasicData){
                return yelpLocation.getAddress1().contains(restaurantMetadata.streetNumber);
                //TODO: figure out how to reuse some library/algorithm that can give a certainty-of-match rank
                // yelpLocation.getAddress1().contains(restaurantMetadata.route);
            }
        }
        return false;
    }

}
