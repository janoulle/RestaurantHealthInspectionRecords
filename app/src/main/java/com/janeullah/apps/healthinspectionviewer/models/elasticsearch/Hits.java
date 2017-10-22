package com.janeullah.apps.healthinspectionviewer.models.elasticsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.parceler.Parcel;

import java.util.List;

/**
 * @author Jane Ullah
 * @date 9/26/2017.
 */
@Parcel(Parcel.Serialization.BEAN)
public class Hits {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("max_score")
    @Expose
    private Double maxScore;
    @SerializedName("hits")
    @Expose
    private List<Hit> hits = null;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Double maxScore) {
        this.maxScore = maxScore;
    }

    public List<Hit> getHits() {
        return hits;
    }

    public void setHits(List<Hit> hits) {
        this.hits = hits;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("total", total).append("maxScore", maxScore).append("hits", hits).toString();
    }
}
