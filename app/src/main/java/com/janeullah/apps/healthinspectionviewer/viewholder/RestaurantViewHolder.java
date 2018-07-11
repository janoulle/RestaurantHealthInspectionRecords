package com.janeullah.apps.healthinspectionviewer.viewholder;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.activities.RestaurantDataActivity;
import com.janeullah.apps.healthinspectionviewer.constants.IntentNames;
import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedRestaurant;

import org.parceler.Parcels;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.janeullah.apps.healthinspectionviewer.utils.EventLoggingUtils.logException;

/**
 * http://stackoverflow.com/questions/16611759/how-set-alpha-opacity-value-to-color-on-xml-drawable
 *
 * @author Jane Ullah
 * @date 4/28/2017.
 */
public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

    private String county;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private FlattenedRestaurant model;

    public RestaurantViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(final FlattenedRestaurant restaurant) {
        restaurantName.setText(restaurant.name);
        restaurantAddress.setText(restaurant.address);
        restaurantNameKey.setText(restaurant.getNameKey());
        restaurantId.setText(String.valueOf(restaurant.id));
        county = restaurant.county;
        computeAndSetResourceId(restaurant);
        model = restaurant;
        itemView.setOnClickListener(this);
    }

    private void computeAndSetResourceId(final FlattenedRestaurant restaurant) {
        try {
            Context context = itemView.getContext();
            if (restaurant.criticalViolations == 0 && restaurant.score >= 90) {
                restaurant.restaurantCheckMarkResourceId = R.drawable.ic_greencheck;
                restaurant.restaurantCheckMarkDescriptor =
                        context.getString(R.string.greencheckmark_contentdescriptor);
            } else if (restaurant.criticalViolations >= 1) {
                restaurant.restaurantCheckMarkResourceId = R.drawable.ic_redx;
                restaurant.restaurantCheckMarkDescriptor =
                        context.getString(R.string.redx_contentdescriptor);
            } else {
                restaurant.restaurantCheckMarkResourceId = R.drawable.ic_yellowcheck;
                restaurant.restaurantCheckMarkDescriptor =
                        context.getString(R.string.yellowcheckmark_contentdescriptor);
            }

            Resources resources = itemView.getContext().getResources();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                restaurantCheckMark.setImageDrawable(
                        resources.getDrawable(
                                restaurant.restaurantCheckMarkResourceId, context.getTheme()));
            } else {
                final Bitmap picture =
                        BitmapFactory.decodeResource(
                                resources, restaurant.restaurantCheckMarkResourceId);
                restaurantCheckMark.setImageBitmap(picture);
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception setting restaurant check mark resource in viewholder.", e);
            logException(e);
        }
    }

    public SparseBooleanArray getSelected() {
        return selectedItems;
    }

    public void setSelected(SparseBooleanArray selectedItems) {
        this.selectedItems = selectedItems;
    }

    // https://stackoverflow.com/questions/33524574/recyclerviewgetting-item-on-recyclerview/33524897#33524897
    @Override
    public void onClick(View v) {
        final Intent intent = new Intent(this.itemView.getContext(), RestaurantDataActivity.class);
        Log.i(TAG, String.format(Locale.getDefault(), "%s selected", model.name));
        intent.putExtra(IntentNames.RESTAURANT_KEY_SELECTED, model.getNameKey());
        intent.putExtra(IntentNames.COUNTY_SELECTED, model.county);
        intent.putExtra(IntentNames.RESTAURANT_SELECTED, Parcels.wrap(model));
        intent.putExtra(IntentNames.RESTAURANT_ADDRESS_SELECTED, model.address);

        // FirebaseAnalytics mFirebaseAnalytics =
        // FirebaseAnalytics.getInstance(this.itemView.getContext());
        // EventLoggingUtils.logSelectionEvent(AppConstants.RESTAURANT_SELECTION,model.getNameKey(),TAG,mFirebaseAnalytics,this.itemView.getContext());
        this.itemView.getContext().startActivity(intent);
    }
}
