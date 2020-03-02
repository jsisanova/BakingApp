package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredient;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private Context context;
    private List<Ingredient> ingredients;

    // Pass data into the constructor
    public IngredientAdapter(List<Ingredient> ingredients, Context context) {
        this.context = context;
        this.ingredients = ingredients;
    }

    // Store and recycle views as they are scrolled off screen
    // Provide a reference to the item views in viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ingredientQuantityTV;
        private TextView ingredientNameTV;
        private TextView ingredientMeasureTV;

        public ViewHolder(ConstraintLayout itemView) {
            super(itemView);

            ingredientQuantityTV = itemView.findViewById(R.id.ingredientQuantityTextView);
            ingredientNameTV = itemView.findViewById(R.id.ingredientNameTextView);
            ingredientMeasureTV = itemView.findViewById(R.id.ingredientMeasureTextView);
        }
    }

    // Inflate the cell layout from xml when needed (invoked by Layout Manager)
    @NonNull
    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        ConstraintLayout view = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_list_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Bind the data to the view in each item (invoked by Layout Manager)
    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.ViewHolder holder, int position) {
        // Get element from the dataset at this position
        double quantity = ingredients.get(position).getIngredientsQuantity();
        String measureType = ingredients.get(position).getIngredientsMeasure();
        String ingredientName = ingredients.get(position).getIngredientsName();

        // Replace the contents of the view with that element
        holder.ingredientQuantityTV.setText(String.valueOf(quantity));
        holder.ingredientMeasureTV.setText(measureType);
        holder.ingredientNameTV.setText(ingredientName);
    }

    // Total number of items
    // Return the size of your dataset (invoked by the Layout Manager)
    @Override
    public int getItemCount() {
        if (ingredients == null || ingredients.size() == 0) {
            return -1;
        }
        return ingredients.size();
    }
}