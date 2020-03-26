package com.example.android.bakingapp;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;



// Test with Espresso
// This source helped me: https://androidresearch.wordpress.com/2015/04/04/an-introduction-to-espresso/
//
// Add annotation to specify AndroidJUnitRunner class as the default test runner
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    public static final String INGREDIENT_QUANTITY_TV = "8";
    public static final String NUTELLA_PIE_TV = "Nutella Pie";
    public static final String BROWNIES_TV = "Brownies";

    // Add the rule that provides functional testing of a single activity
    /**
     * The ActivityTestRule is a rule provided by Android used for functional testing of a single, specific activity.
     * The activity that will be tested will be launched before each test that's annotated
     * with @Test and before methods annotated with @before. The activity will be terminated after
     * the test and methods annotated with @After are complete.
     * This rule allows you to directly access the activity during the test.
     */
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void clickRecipeNameListItem_OpensDetailFragment() {
        // Find the view
        // Perform action on view
        ViewInteraction recyclerView = onView(withId(R.id.recipe_name_recycler_view));
        recyclerView.perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Check if the view does what you expected
        onView(withId(R.id.ingredients_CardView)).check(matches(isDisplayed()));
        onView(withText(INGREDIENT_QUANTITY_TV)).check(matches(isDisplayed()));
    }

    @Test
    public void clickRV() {
        onView(withId(R.id.recipe_name_recycler_view)).perform(click());
    }

    @Test
    public void scrollRV() {
        onView(withId(R.id.recipe_name_recycler_view)).perform(RecyclerViewActions.scrollToPosition(1))
                .perform(click());
        onView(withText(BROWNIES_TV)).check(matches(isDisplayed()));
    }

    @Test
    public void scrollToFirstPositionOfRecyclerView_DisplaysNutellaPie() {
        onView(withId(R.id.recipe_name_recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_name_recycler_view)).perform(RecyclerViewActions.scrollToPosition(0));
        onView(withText(NUTELLA_PIE_TV)).check(matches(isDisplayed()));
    }


    @Test
    public void clickFirstPositionRecyclerView_DisplaysNutellaPie() {
        onView(ViewMatchers.withId(R.id.recipe_name_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, scrollTo()));
        onView(withText(NUTELLA_PIE_TV)).perform(click());
        onView(withId(R.id.recipe_title)).check(matches(withText(NUTELLA_PIE_TV)));
    }
}
