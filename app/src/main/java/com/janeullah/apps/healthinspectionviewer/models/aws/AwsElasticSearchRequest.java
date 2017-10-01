package com.janeullah.apps.healthinspectionviewer.models.aws;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.parceler.Parcel;

/**
 * @author Jane Ullah
 * @date 9/26/2017.
 */
@Parcel(Parcel.Serialization.BEAN)
public class AwsElasticSearchRequest {
    @SerializedName("query")
    @Expose
    private ExactMatchQuery exactMatchQuery;
    @SerializedName("size")
    @Expose
    private Integer size;

    public ExactMatchQuery getExactMatchQuery() {
        return exactMatchQuery;
    }

    public void setExactMatchQuery(ExactMatchQuery exactMatchQuery) {
        this.exactMatchQuery = exactMatchQuery;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("query", exactMatchQuery).append("size", size).toString();
    }
}
