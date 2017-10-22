package com.janeullah.apps.healthinspectionviewer.utils;


import com.google.maps.model.GeocodingResult;
import com.janeullah.apps.healthinspectionviewer.TestingUtils;
import com.janeullah.apps.healthinspectionviewer.dtos.GeocodedAddressComponent;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author Jane Ullah
 * @date 10/9/2017.
 */

public class GeocodingUtilsTest {
    private static String SAMPLE_GEOCODING_RESULT = "";

    @Before
    public void setup() throws IOException{
        SAMPLE_GEOCODING_RESULT = TestingUtils.asString("geocoding/sample_geocoding_results.json");
    }

    @Test
    public void convertGeocodingResultsToGeocodedAddressComponent_isSuccessful() throws Exception{
        GeocodingResult[] geocodingResults = StringUtilities.serialize(SAMPLE_GEOCODING_RESULT,GeocodingResult[].class);
        assertFalse(geocodingResults.length == 0);
        GeocodedAddressComponent result = GeocodingUtils.convertGeocodingResultsToGeocodedAddressComponent(geocodingResults[0]);
        assertTrue(result != null);
        assertEquals("GA",result.getState());
        assertEquals("Barrow County", result.getAdministrativeArea());
    }
}
