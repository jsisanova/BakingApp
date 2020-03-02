package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Step;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private Context context;
    private List<Step> steps;

    // Pass data into the constructor
    public StepAdapter(List<Step> steps, Context context) {
        this.context = context;
        this.steps = steps;
    }

    // Store and recycle views as they are scrolled off screen
    // Provide a reference to the item views in viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView stepShortDescriptionTV;
        private TextView stepDescriptionTV;

        public ViewHolder(View itemView) {
            super(itemView);

            stepShortDescriptionTV = itemView.findViewById(R.id.stepsShortDescriptionTextView);
            stepDescriptionTV = itemView.findViewById(R.id.stepsDescriptionTextView);
        }
    }

    // Inflate the cell layout from xml when needed (invoked by Layout Manager)
    @NonNull
    @Override
    public StepAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_list_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Bind the data to the view in each item (invoked by Layout Manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get element from the dataset at this position
        String shortDescription = steps.get(position).getStepShortDescription();
        String description = steps.get(position).getStepDescription ();

        // Replace the contents of the view with that element
        holder.stepShortDescriptionTV.setText(shortDescription);
        holder.stepDescriptionTV.setText(description);
    }

    // Total number of items
    // Return the size of your dataset (invoked by the Layout Manager)@Override
    public int getItemCount() {
        if (steps == null || steps.size() == 0) {
            return -1;
        }
        return steps.size();
    }
}