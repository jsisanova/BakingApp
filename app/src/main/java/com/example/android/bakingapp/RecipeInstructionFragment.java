package com.example.android.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bakingapp.model.Step;
import com.example.android.bakingapp.utils.Constants;

import java.util.ArrayList;

public class RecipeInstructionFragment extends Fragment {

    private ArrayList<Step> steps;
    private Step step;

    private TextView stepDescriptionTV;
    private Button previousButton;
    private Button nextButton;

    // Define a new interface OnStepSelectedListener that triggers a callback in the host activity
    RecipeDetailFragment.OnStepSelectedListener mCallback;

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (RecipeDetailFragment.OnStepSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnStepSelectedListener");
        }
    }

    // Mandatory empty constructor for the fragment manager to instantiate the fragment
    public RecipeInstructionFragment() { }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the object in onCreate();
        steps = getArguments().getParcelableArrayList(Constants.STEPS_KEY);
        step = getArguments().getParcelable(Constants.STEP_KEY);

        if(((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(step.getStepShortDescription());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate view
        View rootview = inflater.inflate(R.layout.fragment_recipe_instruction, container, false);



//        String stepUrl = step.getStepVideoUrl();
//        if (stepUrl.isEmpty ()) {
//            // set visibility to gone
//        } else {
//            // video
//        }



        setupButtonsAndViews(rootview, step);

        previousButton.setOnClickListener(v -> {
            if (step.getStepId() == 0) {
                setupButtonsAndViews(rootview, step);
            } else {
                mCallback.onStepSelected(steps, (step.getStepId()) - 1);
            }
        });

        nextButton.setOnClickListener(v -> {
            if (step.getStepId() == (steps.size() - 1)) {
                setupButtonsAndViews(rootview, step);
            } else {
                mCallback.onStepSelected(steps, (step.getStepId()) + 1);
            }
        });




        // Return rootview
        return rootview;
    }

    // basic setup of buttons and views
    public void setupButtonsAndViews(View view, Step step) {
        stepDescriptionTV = view.findViewById(R.id.stepsDescriptionTextView);
        previousButton = view.findViewById (R.id.previousButton);
        nextButton = view.findViewById (R.id.nextButton);

        stepDescriptionTV.setText(step.getStepDescription());

        if(step.getStepId() == 0) {
            previousButton.setVisibility(View.GONE);
        } else if(step.getStepId() == steps.size() - 1) {
            previousButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.GONE);
        } else {
            previousButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
        }
    }
}