package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;
import com.example.android.bakingapp.utils.Constants;

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
                //Listen for changes in the back stack (className::methodName - double colon operator)
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
    public void onStepSelected(Step step) {
        // Pass an object that implements Parcelable btw. fragments
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.STEPS_KEY, step);
        RecipeInstructionFragment fragment = new RecipeInstructionFragment();
        // Parse the object to the fragment as a bundle;
        fragment.setArguments(bundle);

        //Listen for changes in the back stack (className::methodName - double colon operator)
        getSupportFragmentManager()
                .addOnBackStackChangedListener(this::onBackStackChanged);
        // Commit the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, fragment)
                .addToBackStack(null)
                .commit();
    }


    /**
     * Source: https://stackoverflow.com/questions/13086840/actionbar-up-navigation-with-fragments
     * In RecipeDetailFragment, when I press actionbar 'up button', I return to the main layout
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