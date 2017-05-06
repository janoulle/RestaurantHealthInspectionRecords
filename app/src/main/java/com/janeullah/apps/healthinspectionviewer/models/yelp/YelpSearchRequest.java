package com.janeullah.apps.healthinspectionviewer.models.yelp;

import android.text.TextUtils;

import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedRestaurant;
import com.janeullah.apps.healthinspectionviewer.dtos.GeocodedAddressComponent;
import com.janeullah.apps.healthinspectionviewer.dtos.YelpMatch;

import org.apache.commons.lang3.StringUtils;

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

    public YelpMatch scoreMatch(Business yelpBusinessObject){
        double score = 0d;
        if (matchesCityStateZip(yelpBusinessObject)){
            Location yelpLocation = yelpBusinessObject.getLocation();
            int longestStringLength = Math.max(getStringLength(restaurantMetadata.getAddressLine1()),getStringLength(yelpLocation.getAddress1()));
            int levenshteinDistance = StringUtils.getLevenshteinDistance(trimString(yelpLocation.getAddress1()),trimString(restaurantMetadata.getAddressLine1()));
            score = 1.0 - ((levenshteinDistance * 1.0) / longestStringLength);
        }
        return new YelpMatch(yelpBusinessObject,score);
    }

    private String trimString(String s){
        if (StringUtils.isNotBlank(s)){
            return s.trim();
        }
        return StringUtils.EMPTY;
    }

    private int getStringLength(String s){
        if (StringUtils.isNotBlank(s)){
            return s.length();
        }
        return 0;
    }

    public boolean matchesCityStateZip(Business yelpBusinessObject){
        if (yelpBusinessObject != null && yelpBusinessObject.getLocation() != null){
            Location yelpLocation = yelpBusinessObject.getLocation();
            return (TextUtils.equals(yelpLocation.getZipCode(),restaurantMetadata.postalCode) &&
                    TextUtils.equals(yelpLocation.getCity(),restaurantMetadata.locality) &&
                    TextUtils.equals(yelpLocation.getState(),restaurantMetadata.state));
        }
        return false;
    }

}
