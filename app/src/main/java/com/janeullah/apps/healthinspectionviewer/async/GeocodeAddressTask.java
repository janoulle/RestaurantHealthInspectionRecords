package com.janeullah.apps.healthinspectionviewer.async;

import android.os.AsyncTask;

import com.google.maps.model.GeocodingResult;

/**
 * @author Jane Ullah
 * @date 4/30/2017.
 */

public class GeocodeAddressTask extends AsyncTask<String,Long,GeocodingResult[]> {
    @Override
    protected GeocodingResult[] doInBackground(String... params) {
        return new GeocodingResult[0];
    }
}
