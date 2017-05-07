package com.janeullah.apps.healthinspectionviewer.viewholder;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;
import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedRestaurant;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jane Ullah
 * @date 4/28/2017.
 */
public class RestaurantViewHolder extends RecyclerView.ViewHolder  {
    private static final String TAG = "RestaurantViewHolder";

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

    public void bindData(final Context context, final FlattenedRestaurant restaurant) {
        restaurantName.setText(restaurant.name);
        restaurantAddress.setText(restaurant.address);
        restaurantNameKey.setText(restaurant.getNameKey());
        restaurantId.setText(String.valueOf(restaurant.id));
        computeAndSetResourceId(context,restaurant);
    }

    private void computeAndSetResourceId(final Context context, final FlattenedRestaurant restaurant){
        try {
            if (restaurant.criticalViolations == 0 && restaurant.score >= 90) {
                restaurant.restaurantCheckMarkResourceId = R.drawable.ic_greencheck;
            } else if (restaurant.criticalViolations >= 1) {
                restaurant.restaurantCheckMarkResourceId = R.drawable.ic_redx;
            } else {
                restaurant.restaurantCheckMarkResourceId = R.drawable.ic_yellowcheck;
            }

            Resources resources = context.getResources();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
                restaurantCheckMark.setImageDrawable(resources.getDrawable(restaurant.restaurantCheckMarkResourceId,context.getTheme()));
            }else{
                final Bitmap picture = BitmapFactory.decodeResource(resources, restaurant.restaurantCheckMarkResourceId);
                restaurantCheckMark.setImageBitmap(picture);
            }

            //int resId = resources.getIdentifier(drawableName,"drawable",context.getPackageName());
            //restaurantCheckMark.setImageResource(restaurant.restaurantCheckMarkResourceId);
        }catch( Exception e){
            Log.e(TAG,"Exception setting restaurant check mark resource in viewholder.",e);
            FirebaseCrash.report(e);
        }
    }
}
