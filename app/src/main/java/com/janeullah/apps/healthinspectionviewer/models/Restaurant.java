package com.janeullah.apps.healthinspectionviewer.models;

/**
 * @author Jane Ullah
 * @date 4/28/2017.
 */

public class Restaurant {
    private String restaurantName;
    private String restaurantNameKey;
    private String restaurantAddress;
    private String restaurantId;

    public Restaurant(){}

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantNameKey() {
        return restaurantNameKey;
    }

    public void setRestaurantNameKey(String restaurantNameKey) {
        this.restaurantNameKey = restaurantNameKey;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
