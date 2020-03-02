package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.adapter.IngredientAdapter;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.utils.Constants;


public class RecipeDetailFragment extends Fragment {

    private Recipe recipe;

    private RecyclerView mIngredientsRecyclerView;
    private Adapter mIngredientsAdapter;
    private LayoutManager mLayoutManager;

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
        // Inflate the view
        View rootview = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        TextView recipeTitleTV = rootview.findViewById(R.id.recipeTitle);
        TextView recipeServingsTV = rootview.findViewById(R.id.servings);
        CardView ingredientsCV = rootview.findViewById(R.id.ingredientsCardView);

        recipeTitleTV.setText(recipe.getRecipeName());
        recipeServingsTV.setText(Integer.toString(recipe.getServings()));

        mIngredientsRecyclerView = rootview.findViewById (R.id.ingredientsRecyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mIngredientsRecyclerView.setLayoutManager(mLayoutManager);
        mIngredientsRecyclerView.setVisibility(View.GONE);

        //specify adapter
        mIngredientsAdapter = new IngredientAdapter(recipe.getIngredients(), getContext());
        mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);
        mIngredientsRecyclerView.setNestedScrollingEnabled(false);

        ingredientsCV.setOnClickListener(v -> {
            if (mIngredientsRecyclerView.getVisibility() == View.VISIBLE) {
                mIngredientsRecyclerView.setVisibility(View.GONE);
            } else {
                mIngredientsRecyclerView.setVisibility(View.VISIBLE);
            }
        });

        // Return the rootview
        return rootview;
    }
}