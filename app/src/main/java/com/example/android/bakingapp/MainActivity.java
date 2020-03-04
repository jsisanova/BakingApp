package com.example.android.bakingapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;
import com.example.android.bakingapp.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeListFragment.OnRecipeSelectedListener, RecipeDetailFragment.OnStepSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if online
        if (isOnline ()) {
            RecipeListFragment listFragment = new RecipeListFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, listFragment)
                    .commit();

            getSupportFragmentManager()
                    // Listen for changes in the back stack (className::methodName - double colon operator)
                    .addOnBackStackChangedListener(this::onBackStackChanged);
        } else {
            isNoConnection();
        }
    }

    //  Implement abstract method onRecipeSelected() of interface OnRecipeSelectedListener
    @Override
    public void onRecipeSelected(Recipe recipe) {

        // Check if online
        if (isOnline ()) {
            // Pass an object that implements Parcelable btw. fragments
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.RECIPE_KEY, recipe);
            RecipeDetailFragment fragment = new RecipeDetailFragment();
            // Parse the object to the fragment as a bundle;
            fragment.setArguments(bundle);

            // Commit the fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            isNoConnection();
        }
    }

    //  Implement abstract method onStepSelected() of interface OnStepSelectedListener
    @Override
    public void onStepSelected(List<Step> steps, int position) {

        // Check if online
        if (isOnline ()) {
            // Pass an object that implements Parcelable btw. fragments
            Bundle bundle = new Bundle();
            // Make ArrayList from List to avoid ClassCastException
            ArrayList<Step> stepsArrayList = new ArrayList<>(steps);
            bundle.putParcelableArrayList(Constants.STEPS_KEY, stepsArrayList);
            bundle.putParcelable(Constants.STEP_KEY, steps.get(position));

            RecipeInstructionFragment fragment = new RecipeInstructionFragment();
            // Parse the object to the fragment as a bundle;
            fragment.setArguments(bundle);

            // Source: https://medium.com/@bherbst/managing-the-fragment-back-stack-373e87e4ff62
            // Clean back stack - when back button & up button is presed after going through detail steps (subscreen), I return to RecipeDetailFragment and not to previous step
            // Pop off everything up to and including the current tab
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .popBackStack("BACK_STACK_ROOT_TAG", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            // Commit the fragment
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .addToBackStack("BACK_STACK_ROOT_TAG")
                    .commit();
        } else {
            isNoConnection();
        }
    }


    /***
     * Make sure the app does not crash when there is no network connection (ping a server)
     * source: https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
     ***/
    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Snackbar when no internet connection
    public void isNoConnection() {
        View coordinator_layout;

        coordinator_layout = findViewById(R.id.coordinator_layout);
        Snackbar snackbar = Snackbar
                .make(coordinator_layout, R.string.snackbar_currently_no_connection, Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", view -> {
                    Snackbar snackbar1 = Snackbar.make(coordinator_layout, R.string.snackbar_message_restored, Snackbar.LENGTH_SHORT);
                    snackbar1.show();
                });
        // Changing message text color (= "RETRY")
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color (= "Currently there is no internet connection.")
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);

        snackbar.show();
    }

    /**
     * Source: https://stackoverflow.com/questions/13086840/actionbar-up-navigation-with-fragments
     * From RecipeDetailFragment, when I press actionbar 'up button', I return to the main layout
     */
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }
    public void shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canGoback = getSupportFragmentManager().getBackStackEntryCount()>0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canGoback);
    }
    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getSupportFragmentManager().popBackStack();
        return true;
    }
}