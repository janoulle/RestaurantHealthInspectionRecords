package com.janeullah.apps.healthinspectionviewer.di.components;

import com.janeullah.apps.healthinspectionviewer.activities.MainActivity;
import com.janeullah.apps.healthinspectionviewer.activities.RestaurantDataActivity;
import com.janeullah.apps.healthinspectionviewer.activities.RestaurantViolations;
import com.janeullah.apps.healthinspectionviewer.activities.RestaurantsInCountyActivity;
import com.janeullah.apps.healthinspectionviewer.di.modules.YelpModule;
import com.janeullah.apps.healthinspectionviewer.di.scopes.AppScope;

import dagger.Component;

/**
 * @author Jane Ullah
 * @date 9/17/2017.
 */
@AppScope
@Component(dependencies = NetComponent.class, modules = YelpModule.class)
public interface YelpComponent {
    void inject(MainActivity mainActivity);
    void inject(RestaurantsInCountyActivity restaurantsInCountyActivity);
    void inject(RestaurantDataActivity restaurantDataActivity);
    void inject(RestaurantViolations restaurantViolationsActivity);
}