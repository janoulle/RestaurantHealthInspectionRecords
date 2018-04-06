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
public class ElasticSearchResponse {

    @SerializedName("took")
    @Expose
    private Integer took;

    @SerializedName("timed_out")
    @Expose
    private Boolean timedOut;

    @SerializedName("_shards")
    @Expose
    private Shards shards;

    @SerializedName("hits")
    @Expose
    private Hits hits;

    public Integer getTook() {
        return took;
    }

    public void setTook(Integer took) {
        this.took = took;
    }

    public Boolean getTimedOut() {
        return timedOut;
    }

    public void setTimedOut(Boolean timedOut) {
        this.timedOut = timedOut;
    }

    public Shards getShards() {
        return shards;
    }

    public void setShards(Shards shards) {
        this.shards = shards;
    }

    public Hits getHits() {
        return hits;
    }

    public void setHits(Hits hits) {
        this.hits = hits;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("took", took)
                .append("timedOut", timedOut)
                .append("shards", shards)
                .append("hits", hits)
                .toString();
    }
}
