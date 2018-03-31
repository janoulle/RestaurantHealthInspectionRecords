package com.janeullah.apps.healthinspectionviewer.models.elasticsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.parceler.Parcel;

@Parcel(Parcel.Serialization.BEAN)
public class Sort {

    @SerializedName("name.keyword")
    @Expose
    private NameKeyword nameKeyword;

    public NameKeyword getNameKeyword() {
        return nameKeyword;
    }

    public void setNameKeyword(NameKeyword nameKeyword) {
        this.nameKeyword = nameKeyword;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("nameKeyword", nameKeyword).toString();
    }
}
