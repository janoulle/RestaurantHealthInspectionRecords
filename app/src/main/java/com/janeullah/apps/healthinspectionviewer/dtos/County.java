package com.janeullah.apps.healthinspectionviewer.dtos;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import org.parceler.Parcel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jane Ullah
 * @date 4/22/2017.
 */
@IgnoreExtraProperties
@Parcel
public class County {
    public String name;
    public Map<String, FlattenedRestaurant> restaurants;

    public County() {
    }

    public County(String name, Map<String, FlattenedRestaurant> restaurants) {
        this.name = name;
        this.restaurants = restaurants;
    }

    @Exclude
    public Collection<FlattenedRestaurant> getRestaurantsAsCollection() {
        return restaurants.values();
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("restaurants", getRestaurantsAsCollection());
        return result;
    }
}
