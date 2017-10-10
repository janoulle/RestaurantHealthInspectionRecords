package com.janeullah.apps.healthinspectionviewer;


import com.google.maps.model.GeocodingResult;
import com.janeullah.apps.healthinspectionviewer.dtos.GeocodedAddressComponent;
import com.janeullah.apps.healthinspectionviewer.utils.GeocodingUtils;
import com.janeullah.apps.healthinspectionviewer.utils.StringUtilities;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * @author Jane Ullah
 * @date 10/9/2017.
 */

public class GeocodingResultTest {
    private static String SAMPLE_GEOCODING_RESULT = "";

    @Before
    public void setup() throws IOException{
        //SAMPLE_GEOCODING_RESULT = Files.toString(new File("/geocoding/sample_geocoding_results.json"), Charsets.UTF_8);
        SAMPLE_GEOCODING_RESULT = TestingUtils.asString("geocoding/sample_geocoding_results.json");
    }

    @Test
    public void convertGeocodingResultsToGeocodedAddressComponent_isSuccessful() throws Exception{
        //GeocodingResult[] geocodingResultsJackson = new ObjectMapper().readValue(SAMPLE_GEOCODING_RESULT, GeocodingResult[].class);
        GeocodingResult[] geocodingResults = StringUtilities.serialize(SAMPLE_GEOCODING_RESULT,GeocodingResult[].class);
        GeocodedAddressComponent result = GeocodingUtils.convertGeocodingResultsToGeocodedAddressComponent(geocodingResults[0]);
        assertTrue(result != null);
        assertEquals("GA",result.getState());
        assertEquals("Barrow County", result.getAdministrativeArea());
    }
}
