package com.janeullah.apps.healthinspectionviewer.models.aws;

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
public class ElasticSearchRequest {
    @SerializedName("query")
    @Expose
    private ContainsMatchQuery containsMatchQuery;

    @SerializedName("sort")
    @Expose
    private List<Sort> sort = null;

    @SerializedName("size")
    @Expose
    private Integer size;

    public ContainsMatchQuery getContainsMatchQuery() {
        return containsMatchQuery;
    }

    public void setContainsMatchQuery(ContainsMatchQuery containsMatchQuery) {
        this.containsMatchQuery = containsMatchQuery;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<Sort> getSort(){
        return sort;
    }

    public void setSort(List<Sort> sort){
        this.sort = sort;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("query", containsMatchQuery).append("sort", sort).append("size", size).toString();
    }
}
