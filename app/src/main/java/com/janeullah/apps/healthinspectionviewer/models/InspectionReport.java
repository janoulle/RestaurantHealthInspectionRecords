package com.janeullah.apps.healthinspectionviewer.models;

import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedRestaurant;
import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedViolation;

import org.parceler.Parcel;

import java.util.List;

/**
 * @author Jane Ullah
 * @date 4/29/2017.
 */
@Parcel(Parcel.Serialization.BEAN)
public class InspectionReport {

    private FlattenedRestaurant restaurant;
    private List<FlattenedViolation> violations;

    public InspectionReport(){
        /**
         * Declaring default constructor due to overloaded one below
         */
    }

    public InspectionReport(FlattenedRestaurant restaurant){
        this.restaurant = restaurant;
    }

    public FlattenedRestaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(FlattenedRestaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<FlattenedViolation> getViolations() {
        return violations;
    }

    public void setViolations(List<FlattenedViolation> violations) {
        this.violations = violations;
    }
}
