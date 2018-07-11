package com.janeullah.apps.healthinspectionviewer.listeners;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.janeullah.apps.healthinspectionviewer.activities.BaseActivity;
import com.janeullah.apps.healthinspectionviewer.activities.RestaurantDataActivity;
import com.janeullah.apps.healthinspectionviewer.constants.AppConstants;
import com.janeullah.apps.healthinspectionviewer.constants.IntentNames;
import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedRestaurant;
import com.janeullah.apps.healthinspectionviewer.utils.EventLoggingUtils;
import com.janeullah.apps.healthinspectionviewer.viewholder.RestaurantViewHolder;

import org.parceler.Parcels;

import java.util.Locale;

/**
 * @author Jane Ullah
 * @date 10/1/2017.
 */
public class RestaurantRowClickListener implements View.OnClickListener {
    private static final String TAG = "RestaurantRowListener";
    private RestaurantViewHolder restaurantViewHolder;
    private FlattenedRestaurant sourceModel;
    private BaseActivity activity;

    public RestaurantRowClickListener(
            RestaurantViewHolder holder, FlattenedRestaurant sourceModel, BaseActivity activity) {
        this.restaurantViewHolder = holder;
        this.sourceModel = sourceModel;
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        final Intent intent =
                new Intent(
                        restaurantViewHolder.itemView.getContext(), RestaurantDataActivity.class);
        Log.i(TAG, String.format(Locale.getDefault(), "%s selected", sourceModel.name));
        intent.putExtra(IntentNames.RESTAURANT_KEY_SELECTED, sourceModel.getNameKey());
        intent.putExtra(IntentNames.COUNTY_SELECTED, sourceModel.county);
        intent.putExtra(IntentNames.RESTAURANT_SELECTED, Parcels.wrap(sourceModel));
        intent.putExtra(IntentNames.RESTAURANT_ADDRESS_SELECTED, sourceModel.address);

        FirebaseAnalytics mFirebaseAnalytics =
                FirebaseAnalytics.getInstance(restaurantViewHolder.itemView.getContext());
        EventLoggingUtils.logSelectionEvent(
                AppConstants.RESTAURANT_SELECTION,
                sourceModel.getNameKey(),
                TAG,
                mFirebaseAnalytics,
                activity);
        restaurantViewHolder.itemView.getContext().startActivity(intent);
    }
}
