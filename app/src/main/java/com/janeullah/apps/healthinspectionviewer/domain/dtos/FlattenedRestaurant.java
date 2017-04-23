package com.janeullah.apps.healthinspectionviewer.domain.dtos;

/**
 * @author Jane Ullah
 * @date 4/22/2017.
 */

public class FlattenedRestaurant {
    private Long id;
    private int score;
    private int criticalViolations;
    private int nonCriticalViolations;
    private String name;
    private String dateReported;
    private String address;
    private String county;

    public FlattenedRestaurant(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCriticalViolations() {
        return criticalViolations;
    }

    public void setCriticalViolations(int criticalViolations) {
        this.criticalViolations = criticalViolations;
    }

    public int getNonCriticalViolations() {
        return nonCriticalViolations;
    }

    public void setNonCriticalViolations(int nonCriticalViolations) {
        this.nonCriticalViolations = nonCriticalViolations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateReported() {
        return dateReported;
    }

    public void setDateReported(String dateReported) {
        this.dateReported = dateReported;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
}
