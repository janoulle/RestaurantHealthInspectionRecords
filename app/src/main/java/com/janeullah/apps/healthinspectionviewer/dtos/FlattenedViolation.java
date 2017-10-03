package com.janeullah.apps.healthinspectionviewer.dtos;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import org.parceler.Parcel;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Jane Ullah
 * Date:  4/26/2017
 */
@IgnoreExtraProperties
@Parcel(Parcel.Serialization.BEAN)
public class FlattenedViolation {

    @Exclude
    private String section;
    private Long violationId;
    private String severity;
    private String category;
    private String inspectionType;
    private String summary;
    private String notes;

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Long getViolationId() {
        return violationId;
    }

    public void setViolationId(Long violationId) {
        this.violationId = violationId;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInspectionType() {
        return inspectionType;
    }

    public void setInspectionType(String inspectionType) {
        this.inspectionType = inspectionType;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public FlattenedViolation() {
        /**
         * Default constructor
         */
    }

    public FlattenedViolation(Long violationId, String severity, String category, String section, String inspectionType, String summary, String notes) {
        this.violationId = violationId;
        this.severity = severity;
        this.category = category;
        this.inspectionType = inspectionType;
        this.section = section;
        this.summary = summary;
        this.notes = notes;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", violationId);
        result.put("severity", severity);
        result.put("category", category);
        result.put("inspectionType", inspectionType);
        result.put("section", section);
        result.put("summary", summary);
        result.put("notes", notes);
        return result;
    }
}
