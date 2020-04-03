package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeListFragment;
import com.example.android.bakingapp.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeNameAdapter extends RecyclerView.Adapter<RecipeNameAdapter.ViewHolder> {

    private List<Recipe> recipes;
    private Context context;
    RecipeListFragment.OnRecipeSelectedListener listener;

    // Pass data into the constructor
    public RecipeNameAdapter(List<Recipe> recipes, Context context, RecipeListFragment.OnRecipeSelectedListener listener) {
        this.recipes = recipes;
        this.context = context;
        this.listener = listener;
    }

    // Store and recycle views as they are scrolled off screen
    // Provide a reference to the item views in viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_name) TextView recipeNameTV;
            // needed to set visibility to GONE by last item
        @BindView(R.id.recipe_divider) View divider;

        public ViewHolder(ConstraintLayout itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    // Inflate the cell layout from xml when needed (invoked by Layout Manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        ConstraintLayout view = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_name_list_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Bind the data to the view in each item (invoked by Layout Manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get element from the dataset at this position
        // Replace the contents of the view with that element
        String recipeName = recipes.get(position).getRecipeName();
        holder.recipeNameTV.setText(recipeName);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRecipeSelected(recipes.get(position));
            }
        });

        // Set visibility of divider to GONE by last item (if not tablet)
        boolean isTablet = context.getResources().getBoolean(R.bool.isTablet);
        if (!isTablet) {
            if (position == recipes.size() - 1) {
                holder.divider.setVisibility(View.GONE);
            }
        }
    }

    // Total number of items
    // Return the size of your dataset (invoked by the Layout Manager)
    @Override
    public int getItemCount() {
        if (recipes == null || recipes.size() == 0) {
            return -1;
        }
        return recipes.size();
    }
}