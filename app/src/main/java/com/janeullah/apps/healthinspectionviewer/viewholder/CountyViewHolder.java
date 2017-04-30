package com.janeullah.apps.healthinspectionviewer.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.dtos.County;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jane Ullah
 * @date 4/27/2017.
 */

public class CountyViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.county_name_view)
    public TextView countyNameView;

    public CountyViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindToCounty(County county, View.OnClickListener countyClickListener){
        countyNameView.setText(county.name);
    }
}
