package com.janeullah.apps.healthinspectionviewer.models.aws;

/**
 * @author Jane Ullah
 * @date 10/1/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.parceler.Parcel;


@Parcel(Parcel.Serialization.BEAN)
public class NameKeyword {

    @SerializedName("order")
    @Expose
    private String order;

    public NameKeyword(){}

    public NameKeyword(String order){
        this.order = order;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("order", order).toString();
    }
}