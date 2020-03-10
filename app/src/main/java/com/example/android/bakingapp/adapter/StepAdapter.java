package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeDetailFragment;
import com.example.android.bakingapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private Context context;
    private List<Step> steps;

    // Define a new interface OnStepSelectedListener
    RecipeDetailFragment.OnStepSelectedListener listener;

    // Pass data into the constructor
    public StepAdapter(List<Step> steps, Context context, RecipeDetailFragment.OnStepSelectedListener listener) {
        this.context = context;
        this.steps = steps;
        this.listener = listener;

    }

    // Store and recycle views as they are scrolled off screen
    // Provide a reference to the item views in viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.stepsShortDescriptionTextView) TextView stepShortDescriptionTV;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    // Inflate the cell layout from xml when needed (invoked by Layout Manager)
    @Override
    public StepAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_list_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Bind the data to the view in each item (invoked by Layout Manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get element from the dataset at this position
        String shortDescription = steps.get(position).getStepShortDescription();

        // Replace the contents of the view with that element
        holder.stepShortDescriptionTV.setText(shortDescription);

        holder.itemView.setOnClickListener(v -> {
            if(listener != null) {
                listener.onStepSelected (steps, position);
            }
        });
    }

    // Total number of items
    // Return the size of your dataset (invoked by the Layout Manager)
    @Override
    public int getItemCount() {
        if (steps == null || steps.size() == 0) {
            return -1;
        }
        return steps.size();
    }
}