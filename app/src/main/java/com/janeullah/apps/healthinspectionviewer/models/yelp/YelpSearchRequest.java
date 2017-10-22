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
    private YelpAuthTokenResponse bearerToken;
    private GeocodedAddressComponent restaurantMetadata;
    private FlattenedRestaurant restaurant;

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

    public String getBearerToken(){
        return "Bearer " + bearerToken.getAccessToken();
    }

    public double getLatitude(){
        if (restaurantMetadata != null && restaurantMetadata.getCoordinates() != null){
            return restaurantMetadata.getCoordinates().latitude;
        }
        return 0d;
    }

    public double getLongitude(){
        if (restaurantMetadata != null && restaurantMetadata.getCoordinates() != null){
            return restaurantMetadata.getCoordinates().longitude;
        }
        return 0d;
    }

    public YelpMatch scoreMatch(Business yelpBusinessObject){
        double addressMatchScore = 0d, nameMatchScore = 0d;
        if (matchesCityStateZip(yelpBusinessObject)){
            Location yelpLocation = yelpBusinessObject.getLocation();
            int longestStringLength = Math.max(getStringLength(restaurantMetadata.getAddressLine1()),getStringLength(yelpLocation.getAddress1()));
            int levenshteinDistance = StringUtils.getLevenshteinDistance(trimString(yelpLocation.getAddress1()),trimString(restaurantMetadata.getAddressLine1()));
            addressMatchScore = 1.0 - ((levenshteinDistance * 1.0) / longestStringLength);

            longestStringLength = Math.max(getStringLength(restaurant.name),getStringLength(yelpBusinessObject.getName()));
            levenshteinDistance = StringUtils.getLevenshteinDistance(trimString(yelpBusinessObject.getName()),trimString(restaurant.name));
            nameMatchScore = 1.0 - ((levenshteinDistance * 1.0) / longestStringLength);
        }
        return new YelpMatch(yelpBusinessObject,addressMatchScore, nameMatchScore);
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
            return (TextUtils.equals(yelpLocation.getZipCode(),restaurantMetadata.getPostalCode()) &&
                    TextUtils.equals(yelpLocation.getCity(),restaurantMetadata.getLocality()) &&
                    TextUtils.equals(yelpLocation.getState(),restaurantMetadata.getState()));
        }
        return false;
    }

}
