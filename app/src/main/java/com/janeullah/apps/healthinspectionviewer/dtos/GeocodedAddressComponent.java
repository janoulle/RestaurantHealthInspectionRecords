package com.janeullah.apps.healthinspectionviewer.dtos;

import com.google.android.gms.maps.model.LatLng;

import org.parceler.Parcel;

/**
 * @author Jane Ullah
 * @date 5/3/2017.
 */
@Parcel(Parcel.Serialization.BEAN)
public class GeocodedAddressComponent {
    private String streetNumber;
    private String route;
    private String locality;
    private String administrativeArea;
    private String state;
    private String country;
    private String postalCode;
    private String formattedAddress;
    private LatLng coordinates;

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getAdministrativeArea() {
        return administrativeArea;
    }

    public void setAdministrativeArea(String administrativeArea) {
        this.administrativeArea = administrativeArea;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    public String getAddressLine1(){
        return streetNumber + " " + route;
    }
}
