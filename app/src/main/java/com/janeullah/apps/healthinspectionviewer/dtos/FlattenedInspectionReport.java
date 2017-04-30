package com.janeullah.apps.healthinspectionviewer.dtos;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import org.parceler.Parcel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Jane Ullah
 * Date:  4/26/2017
 */
@IgnoreExtraProperties
@Parcel
public class FlattenedInspectionReport {
    public String name;
    public Long id;
    public int score;
    public String dateReported;
    public List<FlattenedViolation> violations;

    public FlattenedInspectionReport() {
    }

    public FlattenedInspectionReport(String name, Long id, int score, String dateReported, List<FlattenedViolation> violations) {
        this.name = name;
        this.id = id;
        this.score = score;
        this.dateReported = dateReported;
        this.violations = violations;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("id", id);
        result.put("score", score);
        result.put("dateReported", dateReported);
        result.put("violations", violations);
        return result;
    }
}
