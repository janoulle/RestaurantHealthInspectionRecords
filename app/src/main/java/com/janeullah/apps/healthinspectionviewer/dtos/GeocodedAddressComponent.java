package com.janeullah.apps.healthinspectionviewer.dtos;

import com.google.android.gms.maps.model.LatLng;

import org.parceler.Parcel;

/**
 * @author Jane Ullah
 * @date 5/3/2017.
 */
@Parcel
public class GeocodedAddressComponent {
    public String streetNumber;
    public String route;
    public String locality;
    public String administrativeArea;
    public String state;
    public String country;
    public String postalCode;
    public String formattedAddress;
    public LatLng coordinates;

    public String getAddressLine1(){
        return streetNumber + " " + route;
    }
}
