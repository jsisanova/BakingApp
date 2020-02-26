package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.utils.Constants;

public class RecipeDetailFragment extends Fragment {

    private Recipe recipe;

    // Mandatory empty constructor for the fragment manager to instantiate the fragment
    public RecipeDetailFragment() { }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the object in onCreate();
        if (getArguments() != null) {
            recipe = getArguments().getParcelable(Constants.RECIPE_KEY);
        }
        Log.e("Recipe Detail: ", String.valueOf(recipe.getRecipeName()));

        if(((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Constants.DETAILS_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate view
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        TextView recipeTitleTV = view.findViewById(R.id.recipeTitle);
        recipeTitleTV.setText(recipe.getRecipeName());

        // Return view
        return view;
    }
}