package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;
import com.example.android.bakingapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeListFragment.OnRecipeSelectedListener, RecipeDetailFragment.OnStepSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Bundle bundle = new Bundle();
        RecipeListFragment listFragment = new RecipeListFragment();
//        listFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, listFragment)
                .commit();

        getSupportFragmentManager()
                // Listen for changes in the back stack (className::methodName - double colon operator)
                .addOnBackStackChangedListener(this::onBackStackChanged);
    }

    //  Implement abstract method onRecipeSelected() of interface OnRecipeSelectedListener
    @Override
    public void onRecipeSelected(Recipe recipe) {
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
    }

    //  Implement abstract method onStepSelected() of interface OnStepSelectedListener
    @Override
    public void onStepSelected(List<Step> steps, int position) {
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