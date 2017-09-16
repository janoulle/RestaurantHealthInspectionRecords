package com.janeullah.apps.healthinspectionviewer.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedViolation;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jane Ullah
 * @date 5/20/2017.
 */

public class ViolationViewHolder extends RecyclerView.ViewHolder  {
    private static final String TAG = "ViolationViewHolder";

    @BindView(R.id.violationCategory)
    public TextView violationCategory;

    @BindView(R.id.violationSummary)
    public TextView violationSummary;

    @BindView(R.id.violationLongDesc)
    public TextView violationLongDesc;


    public ViolationViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(final FlattenedViolation violation) {
        violationCategory.setText(violation.category);
        violationSummary.setText(violation.summary);
        violationLongDesc.setText(violation.notes);
    }
}
