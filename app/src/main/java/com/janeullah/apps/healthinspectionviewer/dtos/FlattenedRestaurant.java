package com.janeullah.apps.healthinspectionviewer.dtos;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import org.parceler.Parcel;

import java.util.HashMap;
import java.util.Map;

import static com.janeullah.apps.healthinspectionviewer.utils.FirebaseUtils.replaceInvalidCharsInKey;

/**
 * @author Jane Ullah
 * @date 4/22/2017.
 */
@IgnoreExtraProperties
@Parcel
public class FlattenedRestaurant {
    public Long id;
    public int score;
    public int criticalViolations;
    public int nonCriticalViolations;
    public String name;
    public String dateReported;
    public String address;
    public String county;

    @Exclude
    public int restaurantCheckMarkResourceId;

    @Exclude
    public LatLng coordinates;

    public FlattenedRestaurant() {
        /**
         * Default constructor
         */
    }

    public FlattenedRestaurant(Long id, int score, int criticalViolations, int nonCriticalViolations, String name, String dateReported, String address, String county) {
        this.id = id;
        this.score = score;
        this.criticalViolations = criticalViolations;
        this.nonCriticalViolations = nonCriticalViolations;
        this.name = name;
        this.dateReported = dateReported;
        this.address = address;
        this.county = county;
    }

    @Exclude
    public String getNameKey() {
        return replaceInvalidCharsInKey(name + "-" + id);
    }

    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("score", score);
        result.put("criticalViolations", criticalViolations);
        result.put("nonCriticalViolations", nonCriticalViolations);
        result.put("dateReported", dateReported);
        result.put("address", address);
        result.put("county", county);
        return result;
    }
}
