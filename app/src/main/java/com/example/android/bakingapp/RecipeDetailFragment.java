package com.example.android.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.example.android.bakingapp.adapter.StepAdapter;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;
import com.example.android.bakingapp.utils.Constants;

import java.util.List;


public class RecipeDetailFragment extends Fragment {

    private Recipe recipe;

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

            recipeTitleTV.setText(recipe.getRecipeName());
            recipeServingsTV.setText(Integer.toString(recipe.getServings()));

            // Set up ingredients RecyclerView
            mIngredientsRecyclerView = rootview.findViewById(R.id.ingredientsRecyclerView);
            mIngredientsLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            mIngredientsRecyclerView.setLayoutManager(mIngredientsLayoutManager);
//            mIngredientsRecyclerView.setVisibility(View.GONE);

            //specify adapter
            mIngredientsAdapter = new IngredientAdapter(recipe.getIngredients(), getContext());
            mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);
            mIngredientsRecyclerView.setNestedScrollingEnabled(false);

//        ingredientsCV.setOnClickListener(v -> {
//            if (mIngredientsRecyclerView.getVisibility() == View.VISIBLE) {
//                mIngredientsRecyclerView.setVisibility(View.GONE);
//            } else {
//                mIngredientsRecyclerView.setVisibility(View.VISIBLE);
//            }
//        });


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


    // https://stackoverflow.com/questions/28236390/recyclerview-store-restore-state-between-activities
    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        // Save list state for Steps
        Parcelable mListState = mStepsLayoutManager.onSaveInstanceState();
        Parcelable mListState2 = mIngredientsLayoutManager.onSaveInstanceState();
        state.putParcelable(Constants.DETAIL_LIST_STATE_KEY, mListState);
        state.putParcelable(Constants.DETAIL_LIST_STATE_KEY2, mListState2);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        if (savedInstanceState != null) {
            Parcelable mListState = savedInstanceState.getParcelable(Constants.DETAIL_LIST_STATE_KEY);
            Parcelable mListState2 = savedInstanceState.getParcelable(Constants.DETAIL_LIST_STATE_KEY2);
            mStepsLayoutManager.onRestoreInstanceState(mListState);
            mIngredientsLayoutManager.onRestoreInstanceState(mListState2);
        }
    }



    // Source (2nd solution): https://stackoverflow.com/questions/28236390/recyclerview-store-restore-state-between-activities
    // Save state of RecyclerView during rotation
    private Bundle mBundleRecyclerViewState;

//    @Override
//    public void onPause() {
//        super.onPause();
//
//        // save RecyclerView state
//        mBundleRecyclerViewState = new Bundle();
//        Parcelable listState = mIngredientsRecyclerView.getLayoutManager().onSaveInstanceState();
//        Parcelable listState2 = mStepsRecyclerView.getLayoutManager().onSaveInstanceState();
//        mBundleRecyclerViewState.putParcelable(Constants.DETAIL_LIST_STATE_KEY, listState);
//        mBundleRecyclerViewState.putParcelable(Constants.DETAIL_LIST_STATE_KEY2, listState2);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        // restore RecyclerView state
//        if (mBundleRecyclerViewState != null) {
//            Parcelable listState = mBundleRecyclerViewState.getParcelable(Constants.DETAIL_LIST_STATE_KEY);
//            Parcelable listState2 = mBundleRecyclerViewState.getParcelable(Constants.DETAIL_LIST_STATE_KEY2);
//            mIngredientsRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
//            mStepsRecyclerView.getLayoutManager().onRestoreInstanceState(listState2);
//        }
//    }
}