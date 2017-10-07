package com.janeullah.apps.healthinspectionviewer.activities;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.janeullah.apps.healthinspectionviewer.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchActivityAndViolationViewingTest extends BaseTest{

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void searchActivityAndViolationViewingTest() {
        sleepForNMilliseconds(TEN_SECONDS);

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.menu_search), withContentDescription("Search restaurant name"), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction searchAutoComplete2 = onView(
                allOf(withId(R.id.search_src_text), withText("ca"),
                        withParent(allOf(withId(R.id.search_plate),
                                withParent(withId(R.id.search_edit_frame)))),
                        isDisplayed()));
        searchAutoComplete2.perform(replaceText("cafe"), closeSoftKeyboard());

        sleepForNMilliseconds(TEN_SECONDS);


        ViewInteraction searchAutoComplete3 = onView(
                allOf(withId(R.id.search_src_text), withText("cafe"),
                        withParent(allOf(withId(R.id.search_plate),
                                withParent(withId(R.id.search_edit_frame)))),
                        isDisplayed()));
        searchAutoComplete3.perform(pressImeActionButton());

        sleepForNMilliseconds(TEN_SECONDS);

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.restaurants_search_listing_recyclerview), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(9, click()));


        sleepForNMilliseconds(TEN_SECONDS);
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.viewViolationsButton), withText("HealthCode Violations")));
        appCompatButton.perform(scrollTo(), click());

        sleepForNMilliseconds(TEN_SECONDS);

        ViewInteraction textView = onView(
                allOf(withText("Health Code Violations"),
                        childAtPosition(
                                allOf(withId(R.id.app_toolbar),
                                        childAtPosition(
                                                withId(R.id.appbar),
                                                0)),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("Health Code Violations")));

    }

}
