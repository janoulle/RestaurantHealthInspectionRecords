package com.janeullah.apps.healthinspectionviewer.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.janeullah.apps.healthinspectionviewer.dtos.GeocodedAddressComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jane Ullah
 * @date 5/3/2017.
 */

public class GeocodingUtils {

    private GeocodingUtils(){}

    public static GeocodedAddressComponent convertGeocodingResultsToGeocodedAddressComponent(GeocodingResult geocodingResult){
        GeocodedAddressComponent geocodedAddressComponent = new GeocodedAddressComponent();
        geocodedAddressComponent.coordinates = new LatLng(
                geocodingResult.geometry.location.lat,
                geocodingResult.geometry.location.lng);
        AddressComponent[] addressComponents = geocodingResult.addressComponents;
        for(AddressComponent component : addressComponents){
            Map<String,Boolean> types = mapTypes(component.types);
            if (types.containsKey("street_number")){
                geocodedAddressComponent.streetNumber = component.shortName;
            }else if (types.containsKey("route")){
                geocodedAddressComponent.route = component.shortName;
            }else if (types.containsKey("locality")){
                geocodedAddressComponent.locality = component.shortName;
            }else if (types.containsKey("administrative_area_level_2")){
                geocodedAddressComponent.administrativeArea = component.shortName;
            }else if (types.containsKey("administrative_area_level_1")){
                geocodedAddressComponent.state = component.shortName;
            }else if (types.containsKey("country")){
                geocodedAddressComponent.country = component.shortName;
            }else if (types.containsKey("postal_code")){
                geocodedAddressComponent.postalCode = component.shortName;
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
