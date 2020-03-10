package com.example.android.bakingapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.android.bakingapp.adapter.RecipeNameAdapter;
import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;
import com.example.android.bakingapp.utils.Constants;
import com.example.android.bakingapp.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

// This fragment displays all of the recipe names in one list (as RecyclerView)
public class RecipeListFragment extends Fragment {

    private Recipe[] recipes;
    // Get a reference to the RecyclerView
    @BindView(R.id.recipeNameRecyclerView) RecyclerView mRecipeRecyclerView;
    private Adapter mRecipeAdapter;
    private LayoutManager mLayoutManager;

    private Ingredient[] ingredients;
    private Step[] steps;

    // To handle rotation
    private Bundle mBundleRecyclerViewState;

    // Define a new interface OnRecipeSelectedListener that triggers a callback in the host activity (MainActivity)
    OnRecipeSelectedListener mCallback;

    // OnRecipeSelectedListener interface, calls a method in the host activity named onRecipeSelected
    public interface OnRecipeSelectedListener {
        void onRecipeSelected(Recipe recipe);
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnRecipeSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnRecipeSelectedListener");
        }
    }

    // Mandatory empty constructor for the fragment manager to instantiate the fragment
    public RecipeListFragment() {
    }


    // Inflates the RecycleView of all recipe names
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate view
        View rootview = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, rootview);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Constants.MAIN_TITLE);

        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {
            rootview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
            mRecipeRecyclerView.setLayoutManager(mLayoutManager);
        } else {
            mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            mRecipeRecyclerView.setLayoutManager(mLayoutManager);
        }


        new FetchRecipeDataAsyncTask().execute();

        // Return the rootview
        return rootview;
    }


    // Change string of json recipes data to an ARRAY OF RECIPE OBJECTS
    public Recipe[] changeRecipesDataToArray(String recipesJsonResults) throws JSONException {

        // Get results as an array
        JSONArray resultsArray = new JSONArray(recipesJsonResults);

        // Create array of Recipe objects that stores data from the JSON string
        recipes = new Recipe[resultsArray.length()];

        // Iterate through recipes and get data
        for (int i = 0; i < resultsArray.length(); i++) {
            // Initialize each object before it can be used
            recipes[i] = new Recipe();

            // Object contains all tags we're looking for
            JSONObject recipeInfo = resultsArray.getJSONObject(i);

            // Store data in recipe object
            recipes[i].setRecipeName(recipeInfo.getString(Constants.NAME_KEY));
            recipes[i].setRecipeId(recipeInfo.getInt(Constants.ID_KEY));
            recipes[i].setServings(recipeInfo.getInt(Constants.SERVINGS_KEY));


            // Get ingredients as an array
            JSONArray ingredientsArray = recipeInfo.getJSONArray(Constants.INGREDIENTS_KEY);
            // Create array of Ingredient objects that stores data from the JSON string
            ingredients = new Ingredient[ingredientsArray.length()];

            // Iterate through ingredients and get data
            for (int j = 0; j < ingredientsArray.length(); j++) {
                // Initialize each object before it can be used
                ingredients[j] = new Ingredient();

                // Object contains all tags we're looking for
                JSONObject ingredientInfo = ingredientsArray.getJSONObject(j);

                // Store data in ingredient object
                ingredients[j].setIngredientsQuantity(ingredientInfo.getDouble(Constants.INGREDIENTS_QUANTITY_KEY));
                ingredients[j].setIngredientsMeasure(ingredientInfo.getString(Constants.INGREDIENTS_MEASURE_KEY).toLowerCase());
                ingredients[j].setIngredientsName(ingredientInfo.getString (Constants.INGREDIENTS_NAME_KEY));
            }


            // Get steps as an array
            JSONArray stepsArray = recipeInfo.getJSONArray(Constants.STEPS_KEY);
            // Create array of Steps objects that stores data from the JSON string
            steps = new Step[stepsArray.length()];

            // Iterate through steps and get data
            for (int k = 0; k < stepsArray.length(); k++) {
                // Initialize each object before it can be used
                steps[k] = new Step();

                // Object contains all tags we're looking for
                JSONObject stepInfo = stepsArray.getJSONObject(k);

                // Store data in step object
                steps[k].setStepId(stepInfo.getInt(Constants.ID_KEY));
                steps[k].setStepShortDescription(stepInfo.getString(Constants.STEPS_SHORT_DESCRIPTION_KEY));
                steps[k].setStepDescription(stepInfo.getString(Constants.STEPS_LONG_DESCRIPTION_KEY));

                if (stepInfo.getString(Constants.STEPS_VIDEO_URL_KEY).equals("")) {
                    steps[k].setStepVideoUrl(null);
                } else {
                    steps[k].setStepVideoUrl(stepInfo.getString(Constants.STEPS_VIDEO_URL_KEY));
                }
            }

            recipes[i].setIngredients(Arrays.asList(ingredients));
            recipes[i].setSteps(Arrays.asList(steps));
        }
        return recipes;
    }


    // Use AsyncTask to fetch recipes data
    private class FetchRecipeDataAsyncTask extends AsyncTask<String, Void, Recipe[]> {

        public FetchRecipeDataAsyncTask() {
            super();
        }

        @Override
        protected Recipe[] doInBackground(String... params) {

            try {
                URL url = JsonUtils.buildBakingAppUrl();
                // Holds data returned from the json
                String bakingResults = JsonUtils.getResponseFromHttpUrl(url);

                if(bakingResults == null) {
                    return null;
                }
                // Call method to change string of recipes data to an ARRAY OF RECIPE OBJECTS
                return changeRecipesDataToArray(bakingResults);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Recipe[] recipes) {
            // Create the adapter and Set the adapter on the RecyclerView
            mRecipeAdapter = new RecipeNameAdapter(recipes, getContext(), mCallback);
            mRecipeRecyclerView.setAdapter(mRecipeAdapter);
        }
    }


    // Source (2nd solution): https://stackoverflow.com/questions/28236390/recyclerview-store-restore-state-between-activities
    // Save state of RecyclerView during rotation
    @Override
    public void onPause() {
        super.onPause();

        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = mRecipeRecyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(Constants.RECIPE_LIST_STATE_KEY, listState);
    }

    @Override
    public void onResume() {
        super.onResume();

        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(Constants.RECIPE_LIST_STATE_KEY);
            mRecipeRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }
}