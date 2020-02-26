package com.example.android.bakingapp;

import android.content.Context;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.adapter.RecipeListNameAdapter;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.utils.Constants;
import com.example.android.bakingapp.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

// This fragment displays all of the recipe names in one list (as RecyclerView)
public class RecipeListFragment extends Fragment {

    private Recipe[] recipes;
    private static RecyclerView mRecipeRecyclerView;
    private static RecyclerView.Adapter mRecipeAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Constants.MAIN_TITLE);

        // Get a reference to the RecyclerView in the fragment_recipe_list.xml layout file
        mRecipeRecyclerView = rootview.findViewById(R.id.recipeNameRecyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecipeRecyclerView.setLayoutManager(mLayoutManager);

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

            // Store data in movie object
            recipes[i].setRecipeName(recipeInfo.getString(Constants.NAME_KEY));
            recipes[i].setRecipeId(recipeInfo.getInt(Constants.ID_KEY));
            recipes[i].setServings(recipeInfo.getInt(Constants.SERVINGS_KEY));
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
            mRecipeAdapter = new RecipeListNameAdapter(recipes, getContext(), mCallback);
            mRecipeRecyclerView.setAdapter(mRecipeAdapter);
        }
    }
}