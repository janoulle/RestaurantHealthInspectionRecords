package com.janeullah.apps.healthinspectionviewer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedRestaurant;
import com.janeullah.apps.healthinspectionviewer.viewholder.RestaurantViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jane Ullah
 * @date 9/30/2017.
 */

public class RestaurantsSearchListAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {
    private static final String TAG = "SearchAdapterRS";
    private List<FlattenedRestaurant> flattenedRestaurants = new ArrayList<>();
    private Context context;

    public RestaurantsSearchListAdapter(Context context, List<FlattenedRestaurant> flattenedRestaurants){
        this.flattenedRestaurants = flattenedRestaurants;
        this.context = context;
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_flattenedrestaurant, parent, false);

        // Return a new holder instance
        RestaurantViewHolder viewHolder = new RestaurantViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {
        // Get the data model based on position
        FlattenedRestaurant restaurant = flattenedRestaurants.get(position);
        holder.bindData(restaurant);
    }

    @Override
    public int getItemCount() {
        return flattenedRestaurants.size();
    }
}
