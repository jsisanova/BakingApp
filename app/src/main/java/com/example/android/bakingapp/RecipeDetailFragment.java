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
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.adapter.IngredientAdapter;
import com.example.android.bakingapp.adapter.StepAdapter;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;
import com.example.android.bakingapp.utils.Constants;
import com.example.android.bakingapp.widget.UpdateIngredientsService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeDetailFragment extends Fragment {

    private Recipe recipe;
    // To see ingredients after rotation when opened
    Boolean isShowingDialog = false;

    @BindView(R.id.recipe_title) TextView recipeTitleTV;
    @BindView(R.id.servings) TextView recipeServingsTV;
    @BindView(R.id.update_recipe_ingredients_widget_button) TextView addToWidgetButton;
    @BindView(R.id.ingredients_CardView) CardView ingredientsCV;

    @BindView(R.id.ingredients_recyclerView) RecyclerView mIngredientsRecyclerView;
    private Adapter mIngredientsAdapter;
    private LayoutManager mIngredientsLayoutManager;

    @BindView(R.id.steps_recyclerView) RecyclerView mStepsRecyclerView;
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
        // Inflate the view
        View rootview = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, rootview);


        recipeTitleTV.setText(recipe.getRecipeName());
        recipeServingsTV.setText(Integer.toString(recipe.getServings()));

        // Set up ingredients RecyclerView
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
        mStepsLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mStepsRecyclerView.setLayoutManager(mStepsLayoutManager);

        // Specify adapter
        mStepsAdapter = new StepAdapter(recipe.getSteps(), getContext(), mCallback);
        mStepsRecyclerView.setAdapter(mStepsAdapter);
        mStepsRecyclerView.setNestedScrollingEnabled(false);


        // Add to widget button
        addToWidgetButton.setOnClickListener(view1 -> {
            // Launch a service
            UpdateIngredientsService.startActionUpdateIngredients(getContext(), recipe);
            Toast.makeText(getActivity(), "Recipe:  " + recipe.getRecipeName() + " added to Widget.", Toast.LENGTH_SHORT).show();
        });

        // Return the rootview
        return rootview;
    }


    // Source: https://stackoverflow.com/questions/5592866/how-do-i-handle-screen-orientation-changes-when-a-dialog-is-open
    // To see list of ingredients when opened, also after rotation
    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putBoolean(Constants.IS_SHOWING_DIALOG_KEY, isShowingDialog);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        if (savedInstanceState != null) {
            isShowingDialog = savedInstanceState.getBoolean(Constants.IS_SHOWING_DIALOG_KEY, false);
            if(isShowingDialog){
                mIngredientsRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }
}

