package com.janeullah.apps.healthinspectionviewer.models.elasticsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.parceler.Parcel;

/**
 * @author Jane Ullah
 * @date 9/26/2017.
 */
@Parcel(Parcel.Serialization.BEAN)
public class Source {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("score")
    @Expose
    private Integer score;

    @SerializedName("nonCriticalViolations")
    @Expose
    private Integer nonCriticalViolations;

    @SerializedName("criticalViolations")
    @Expose
    private Integer criticalViolations;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("dateReported")
    @Expose
    private String dateReported;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("county")
    @Expose
    private String county;

    @SerializedName("nameKey")
    @Expose
    private String nameKey;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getNameKey() {
        return nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getNonCriticalViolations() {
        return nonCriticalViolations;
    }

    public void setNonCriticalViolations(Integer nonCriticalViolations) {
        this.nonCriticalViolations = nonCriticalViolations;
    }

    public Integer getCriticalViolations() {
        return criticalViolations;
    }

    public void setCriticalViolations(Integer criticalViolations) {
        this.criticalViolations = criticalViolations;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("score", score)
                .append("nonCriticalViolations", nonCriticalViolations)
                .append("criticalViolations", criticalViolations)
                .append("dateReported", dateReported)
                .append("address", address)
                .append("county", county)
                .append("nameKey", nameKey)
                .toString();
    }
}
