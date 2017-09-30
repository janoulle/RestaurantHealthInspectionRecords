package com.janeullah.apps.healthinspectionviewer.interfaces;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.constants.IntentNames;
import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedYelpData;
import com.janeullah.apps.healthinspectionviewer.models.yelp.YelpResults;

import org.parceler.Parcels;

import static io.fabric.sdk.android.Fabric.TAG;

/**
 * @author Jane Ullah
 * @date 9/30/2017.
 */

public class YelpSearchTaskListener implements TaskListener<Void,YelpResults> {
    private View view;
    private Intent intent;

    public void setIntent(Intent intent){
        this.intent = intent;
    }

    public void setView(View view){
        this.view = view;
    }

    @Override
    public Void onSuccess(YelpResults yelpResults) {
        intent.putExtra(IntentNames.YELP_RESULTS, Parcels.wrap(yelpResults));
        Log.v(TAG,"Received yelp results for " + yelpResults.getSearchQuery() + ": " + yelpResults);
        if (yelpResults.getMatchedBusiness() != null){
            Log.v(TAG,"Found business match from Yelp listings: " + gson.toJson(yelpResults.getMatchedBusiness()));
            FlattenedYelpData flattenedYelpData = new FlattenedYelpData(yelpResults.getMatchedBusiness());
            RelativeLayout mYelpLayout = view.findViewById(R.id.yelpDataLayout);
            ImageView yelpStars = view.findViewById(R.id.yelpStarsDisplay);
            if (mYelpLayout != null) {
                yelpStars.setImageResource(flattenedYelpData.yelpStarsResourceId);
                mYelpLayout.setVisibility(View.VISIBLE);
            }
        }
        return null;
    }
}
