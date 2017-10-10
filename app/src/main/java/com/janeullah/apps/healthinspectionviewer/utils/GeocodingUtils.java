package com.janeullah.apps.healthinspectionviewer.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.janeullah.apps.healthinspectionviewer.dtos.GeocodedAddressComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * https://developers.google.com/maps/documentation/geocoding/start
 * @author Jane Ullah
 * @date 5/3/2017.
 */

public class GeocodingUtils {

    private GeocodingUtils(){}

    public static GeocodedAddressComponent convertGeocodingResultsToGeocodedAddressComponent(GeocodingResult geocodingResult){
        GeocodedAddressComponent geocodedAddressComponent = new GeocodedAddressComponent();
        geocodedAddressComponent.setFormattedAddress(geocodingResult.formattedAddress);
        geocodedAddressComponent.setCoordinates(new LatLng(
                geocodingResult.geometry.location.lat,
                geocodingResult.geometry.location.lng));
        AddressComponent[] addressComponents = geocodingResult.addressComponents;
        for(AddressComponent component : addressComponents){
            Map<String,Boolean> types = mapTypes(component.types);
            if (types.containsKey("street_number")){
                geocodedAddressComponent.setStreetNumber(component.shortName);
            }else if (types.containsKey("route")){
                geocodedAddressComponent.setRoute(component.shortName);
            }else if (types.containsKey("locality")){
                geocodedAddressComponent.setLocality(component.shortName);
            }else if (types.containsKey("administrative_area_level_2")){
                geocodedAddressComponent.setAdministrativeArea(component.shortName);
            }else if (types.containsKey("administrative_area_level_1")){
                geocodedAddressComponent.setState(component.shortName);
            }else if (types.containsKey("country")){
                geocodedAddressComponent.setCountry(component.shortName);
            }else if (types.containsKey("postal_code")){
                geocodedAddressComponent.setPostalCode(component.shortName);
            }
        }
        return geocodedAddressComponent;
    }

    private static Map<String,Boolean> mapTypes(AddressComponentType[] types) {
        Map<String,Boolean> result = new HashMap<>();
        for(AddressComponentType type: types){
            result.put(type.toCanonicalLiteral(),true);
        }
        return result;
    }
}
