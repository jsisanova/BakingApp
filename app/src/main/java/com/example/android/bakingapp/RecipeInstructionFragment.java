package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.model.Step;
import com.example.android.bakingapp.utils.Constants;

public class RecipeInstructionFragment extends Fragment {

    private Step step;

    // Mandatory empty constructor for the fragment manager to instantiate the fragment
    public RecipeInstructionFragment() { }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Get the object in onCreate();
        step = getArguments().getParcelable(Constants.STEPS_KEY);

        if(((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(step.getStepShortDescription());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate view
        View rootview = inflater.inflate(R.layout.fragment_recipe_instruction, container, false);

        TextView stepDescriptionTV = rootview.findViewById(R.id.stepsDescriptionTextView);
//        Button previousButton = view.findViewById (R.id.previousButton);
//        Button nextButton = view.findViewById (R.id.nextButton);

        stepDescriptionTV.setText(step.getStepDescription());

//        String stepUrl = step.getStepVideoUrl();
//        if (stepUrl.isEmpty ()) {
//            // set visibility to gone
//        } else {
//            // video
//        }

        // Return rootview
        return rootview;
    }
}