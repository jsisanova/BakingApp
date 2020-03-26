package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        @BindView(R.id.ingredient_quantity_tv) TextView ingredientQuantityTV;
        @BindView (R.id.ingredient_name_tv) TextView ingredientNameTV;
        @BindView (R.id.ingredient_measure_tv) TextView ingredientMeasureTV;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    // Inflate the cell layout from xml when needed (invoked by Layout Manager)
    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_list_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Bind the data to the view in each item (invoked by Layout Manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
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