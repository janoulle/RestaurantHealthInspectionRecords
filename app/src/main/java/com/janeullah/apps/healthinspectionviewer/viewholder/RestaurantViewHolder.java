package com.janeullah.apps.healthinspectionviewer.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedRestaurant;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jane Ullah
 * @date 4/28/2017.
 */
public class RestaurantViewHolder extends RecyclerView.ViewHolder  {
    @BindView(R.id.restaurantName)
    public TextView restaurantName;

    @BindView(R.id.restaurantAddress)
    public TextView restaurantAddress;

    @BindView(R.id.restaurantNameKey)
    public TextView restaurantNameKey;

    @BindView(R.id.restaurantId)
    public TextView restaurantId;

    @BindView(R.id.restaurantCheckMark)
    public ImageView restaurantCheckMark;

    public RestaurantViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(final FlattenedRestaurant restaurant) {
        restaurantName.setText(restaurant.name);
        restaurantAddress.setText(restaurant.address);
        restaurantNameKey.setText(restaurant.getNameKey());
        restaurantId.setText(String.valueOf(restaurant.id));
        computeAndSetResourceId(restaurant);
    }

    private void computeAndSetResourceId(final FlattenedRestaurant restaurant){
        if (restaurant.criticalViolations == 0 && restaurant.score >= 90){
            restaurant.restaurantCheckMarkResourceId = R.drawable.ic_greencheckmark;
        }else if (restaurant.criticalViolations >= 1){
            restaurant.restaurantCheckMarkResourceId = R.drawable.ic_redx;
        }else{
            restaurant.restaurantCheckMarkResourceId = R.drawable.ic_yellowcheckmark;
        }
        restaurantCheckMark.setImageResource(restaurant.restaurantCheckMarkResourceId);
    }
}
