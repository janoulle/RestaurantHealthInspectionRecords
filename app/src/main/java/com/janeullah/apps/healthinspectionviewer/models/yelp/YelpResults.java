package com.janeullah.apps.healthinspectionviewer.models.yelp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * @author Jane Ullah
 * @date 5/4/2017.
 */
@Parcel(Parcel.Serialization.BEAN)
public class YelpResults {

    @SerializedName("businesses")
    @Expose
    private List<Business> businesses = null;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("region")
    @Expose
    private Region region;

    /*TODO: investigate solution here http://stackoverflow.com/questions/4802887/gson-how-to-exclude-specific-fields-from-serialization-without-annotations
        Marking as 'transient' to prevent Gson serializaton issues
        Previously marke as @Transient to prevent Parceling
    */
    private transient Business matchedBusiness;

    public List<Business> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Business getMatchedBusiness() {
        return matchedBusiness;
    }

    public void setMatchedBusiness(Business matchedBusiness) {
        this.matchedBusiness = matchedBusiness;
    }
}
