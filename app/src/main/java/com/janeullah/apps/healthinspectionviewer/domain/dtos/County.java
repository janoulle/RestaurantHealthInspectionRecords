package com.janeullah.apps.healthinspectionviewer.domain.dtos;

import java.util.Map;

/**
 * @author Jane Ullah
 * @date 4/22/2017.
 */

public class County {
    private String name;
    private Map<String,FlattenedRestaurant> restaurants;

    public County(){}

    public County(String name, Map<String,FlattenedRestaurant> restaurants) {
        setName(name);
        setRestaurants(restaurants);
    }

    public Map<String, FlattenedRestaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(Map<String, FlattenedRestaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
