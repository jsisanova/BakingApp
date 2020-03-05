package com.example.android.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.android.bakingapp.adapter.IngredientAdapter;
import com.example.android.bakingapp.adapter.StepAdapter;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;
import com.example.android.bakingapp.utils.Constants;

import java.util.List;


public class RecipeDetailFragment extends Fragment {

    private Recipe recipe;
    // To see ingredients after rotation when opened
    Boolean isShowingDialog = false;

    private RecyclerView mIngredientsRecyclerView;
    private Adapter mIngredientsAdapter;
    private LayoutManager mIngredientsLayoutManager;

    private RecyclerView mStepsRecyclerView;
    private Adapter mStepsAdapter;
    private LayoutManager mStepsLayoutManager;

    // Define a new interface OnStepSelectedListener that triggers a callback in the host activity (MainActivity)
    RecipeDetailFragment.OnStepSelectedListener mCallback;

    // OnStepelectedListener interface, calls a method in the host activity named onStepSelected
    public interface OnStepSelectedListener {
        void onStepSelected(List<Step> steps, int position);
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnStepSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnStepSelectedListener");
        }
    }

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
        View rootview = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        // Inflate the view

        TextView recipeTitleTV = rootview.findViewById(R.id.recipeTitle);
        TextView recipeServingsTV = rootview.findViewById(R.id.servings);
        CardView ingredientsCV = rootview.findViewById(R.id.ingredientsCardView);

        recipeTitleTV.setText(recipe.getRecipeName());
        recipeServingsTV.setText(Integer.toString(recipe.getServings()));

        // Set up ingredients RecyclerView
        mIngredientsRecyclerView = rootview.findViewById(R.id.ingredientsRecyclerView);
        mIngredientsLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mIngredientsRecyclerView.setLayoutManager(mIngredientsLayoutManager);
        // for smooth scrolling
        mIngredientsRecyclerView.setNestedScrollingEnabled(false);
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
                isShowingDialog = true;
            }
        });


        // Set up steps RecyclerView
        mStepsRecyclerView = rootview.findViewById(R.id.stepsRecyclerView);
        mStepsLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mStepsRecyclerView.setLayoutManager(mStepsLayoutManager);

        //specify adapter
        mStepsAdapter = new StepAdapter(recipe.getSteps(), getContext(), mCallback);
        mStepsRecyclerView.setAdapter(mStepsAdapter);
        mStepsRecyclerView.setNestedScrollingEnabled(false);

        // Return the rootview
        return rootview;
    }


    // Source: https://stackoverflow.com/questions/5592866/how-do-i-handle-screen-orientation-changes-when-a-dialog-is-open
    // To see list of ingredients when opened, also after rotation
    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        state.putBoolean("IS_SHOWING_DIALOG", isShowingDialog);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        if (savedInstanceState != null) {
            isShowingDialog = savedInstanceState.getBoolean("IS_SHOWING_DIALOG", false);
            if(isShowingDialog){
                mIngredientsRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }
}

