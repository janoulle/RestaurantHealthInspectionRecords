package com.janeullah.apps.healthinspectionviewer;

import android.app.Application;

import com.janeullah.apps.healthinspectionviewer.di.components.DaggerNetComponent;
import com.janeullah.apps.healthinspectionviewer.di.components.DaggerYelpComponent;
import com.janeullah.apps.healthinspectionviewer.di.components.YelpComponent;
import com.janeullah.apps.healthinspectionviewer.di.modules.AppModule;
import com.janeullah.apps.healthinspectionviewer.di.modules.NetModule;
import com.janeullah.apps.healthinspectionviewer.di.modules.YelpModule;

import static com.janeullah.apps.healthinspectionviewer.constants.YelpConstants.YELP_API_HOST;

/**
 * https://github.com/codepath/android_guides/wiki/Dependency-Injection-with-Dagger-2
 * http://www.vogella.com/tutorials/Dagger/article.html#introduction-to-the-concept-of-dependency-injection
 * @author Jane Ullah
 * @date 9/17/2017.
 */

public class RestaurantScoresApplication extends Application{

    private YelpComponent yelpComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        // If a Dagger 2 component does not have any constructor arguments for any of its modules,
        // then we can use .create() as a shortcut instead:
        //  myComponent = com.codepath.dagger.components.DaggerNetComponent.create();
        // specify the full namespace of the component
        // Dagger_xxxx (where xxxx = component name)

        yelpComponent = DaggerYelpComponent.builder()
                .netComponent(DaggerNetComponent.builder()
                        .appModule(new AppModule(this))
                        .netModule(new NetModule(YELP_API_HOST))
                        .build())
                .yelpModule(new YelpModule())
                .build();
    }

    public YelpComponent getYelpComponent() {
        return yelpComponent;
    }
}
