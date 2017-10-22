package com.janeullah.apps.healthinspectionviewer.activities;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import com.janeullah.apps.healthinspectionviewer.R;

import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AboutDisclaimerTextValidations extends BaseTest{
    private IdlingResource mIdlingResource;


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }


    @Test
    public void aboutDisclaimerTextValidations() {

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        sleepForNMilliseconds(TEN_SECONDS);

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("About"), isDisplayed()));
        appCompatTextView.perform(click());

        sleepForNMilliseconds(TEN_SECONDS);

        ViewInteraction textView = onView(
                allOf(withId(R.id.aboutTextView), withText(" Made with duct tape, super glue, and love by Jane Ullah\n\n Some details:\n\n 1. Data was scraped from the websites below using a Spring application that makes use of Jsoup. This data was then transformed into the format needed for consumption by this application and for use with Firebase\n\n 2. Direct links to the County restaurant reports: Barrow, Elbert, Clarke, Greene, Jackson, Madison, Morgan, Oconee, Oglethorpe, and Walton "),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        1),
                                0),
                        isDisplayed()));
        textView.check(matches(withText(" Made with duct tape, super glue, and love by Jane Ullah   Some details:   1. Data was scraped from the websites below using a Spring application that makes use of Jsoup. This data was then transformed into the format needed for consumption by this application and for use with Firebase   2. Direct links to the County restaurant reports: Barrow, Elbert, Clarke, Greene, Jackson, Madison, Morgan, Oconee, Oglethorpe, and Walton ")));

        ViewInteraction textView2 = onView(
                allOf(withText("About"),
                        childAtPosition(
                                allOf(withId(R.id.app_toolbar),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("About")));


        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        withParent(withId(R.id.app_toolbar)),
                        isDisplayed()));
        appCompatImageButton.perform(click());
        sleepForNMilliseconds(TEN_SECONDS);

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.title), withText("Disclaimer"), isDisplayed()));
        appCompatTextView2.perform(click());
        sleepForNMilliseconds(TEN_SECONDS);

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.legalTextView), withText(" Please note that PublicHealthAthens.com is the canonical and most-up-to-date source of information on the restaurant health inspection data. By using this app, you agree to indemnify and hold me, relatives, descendants, my neighbors, friends, and my dearly departed pet harmless from any events that result from using the application.\n\n The green, red, and yellow icons are indicators of the restaurant's score. The information on how these restaurants are scored is available on PublicHealthAthens.com. Captured here as of 09/16/2017, green means no critical violations and a score of >= 90. Red means >= 1 or more critical violations. Yellow means a score below 90 but not in the red.\n\n This app also consumes data from Yelp. Specifically, the Yelp stars shown when viewing some restaurants comes from yelp.com and Yelp.com is the canonical source for rating information. The ratings data shown are not guaranteed to match the actual restaurant being viewed.\n\n Internet connectivity is required to use this application. The restaurant information data is fetched from Firebase (TOS) and is temporarily cached on your device. You agree to bear the cost of any charges associated with the requirement for internet connectivity on your device.\n\n Other third party services in use include: Google Maps API for displaying the restaurant location (TOS), Firebase Crash Reporting (which does not collect any personally identifiable information), Fabric (TOS) for monitoring the app's stability and AWS ElasticSearch service powers the restaurant name search. "),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        1),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText(" Please note that PublicHealthAthens.com is the canonical and most-up-to-date source of information on the restaurant health inspection data. By using this app, you agree to indemnify and hold me, relatives, descendants, my neighbors, friends, and my dearly departed pet harmless from any events that result from using the application.   The green, red, and yellow icons are indicators of the restaurant's score. The information on how these restaurants are scored is available on PublicHealthAthens.com. Captured here as of 09/16/2017, green means no critical violations and a score of >= 90. Red means >= 1 or more critical violations. Yellow means a score below 90 but not in the red.   This app also consumes data from Yelp. Specifically, the Yelp stars shown when viewing some restaurants comes from yelp.com and Yelp.com is the canonical source for rating information. The ratings data shown are not guaranteed to match the actual restaurant being viewed.   Internet connectivity is required to use this application. The restaurant information data is fetched from Firebase (TOS) and is temporarily cached on your device. You agree to bear the cost of any charges associated with the requirement for internet connectivity on your device.   Other third party services in use include: Google Maps API for displaying the restaurant location (TOS), Firebase Crash Reporting (which does not collect any personally identifiable information), Fabric (TOS) for monitoring the app's stability and AWS ElasticSearch service powers the restaurant name search. ")));

        ViewInteraction textView4 = onView(
                allOf(withText("Disclaimer"),
                        childAtPosition(
                                allOf(withId(R.id.app_toolbar),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                1),
                        isDisplayed()));
        textView4.check(matches(withText("Disclaimer")));

    }

}
