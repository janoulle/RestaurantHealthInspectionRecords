package com.janeullah.apps.healthinspectionviewer.callbacks;

import android.view.View;

import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedRestaurant;

/**
 * @author Jane Ullah
 * @date 5/21/2017.
 */
public interface ViolationActivityCallBack{
    void onClick(View view, FlattenedRestaurant restaurant);
}