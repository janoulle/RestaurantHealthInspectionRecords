package com.janeullah.apps.healthinspectionviewer.models;

import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedRestaurant;

/**
 * @author Jane Ullah
 * @date 4/29/2017.
 */

public class InspectionReport {
    private int score;
    private String mostRecentInspectionDate;
    private int criticalViolationCount;
    private int nonCriticalViolationCount;
    private FlattenedRestaurant restaurant;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getMostRecentInspectionDate() {
        return mostRecentInspectionDate;
    }

    public void setMostRecentInspectionDate(String mostRecentInspectionDate) {
        this.mostRecentInspectionDate = mostRecentInspectionDate;
    }

    public int getCriticalViolationCount() {
        return criticalViolationCount;
    }

    public void setCriticalViolationCount(int criticalViolationCount) {
        this.criticalViolationCount = criticalViolationCount;
    }

    public int getNonCriticalViolationCount() {
        return nonCriticalViolationCount;
    }

    public void setNonCriticalViolationCount(int nonCriticalViolationCount) {
        this.nonCriticalViolationCount = nonCriticalViolationCount;
    }

    public FlattenedRestaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(FlattenedRestaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getName() {
        return restaurant.name;
    }
}
