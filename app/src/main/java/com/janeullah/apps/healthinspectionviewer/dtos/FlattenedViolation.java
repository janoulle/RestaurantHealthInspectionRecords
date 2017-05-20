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
@Parcel
public class FlattenedViolation {
    public Long violationId;
    public String severity;
    public String category;
    public String section;
    public String inspectionType;
    public String summary;
    public String notes;

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
