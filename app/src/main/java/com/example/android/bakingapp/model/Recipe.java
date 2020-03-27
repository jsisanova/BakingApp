package com.example.android.bakingapp.model;

import android.os.Parcelable;
import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;


public class Recipe implements Parcelable {

    private int recipeId;
    private String recipeName;
    private List<Ingredient> ingredients = null;
    private List<Step> steps = null;
    private int servings;

    public Recipe() { }


    // Setter methods
    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
    public void setServings(int servings) {
        this.servings = servings;
    }

    // Getter methods
    public int getRecipeId() {
        return recipeId;
    }
    public String getRecipeName() {
        return recipeName;
    }
    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    public List<Step> getSteps() {
        return steps;
    }
    public int getServings() {
        return servings;
    }



    // Use Android Parcelable interface to transfer object and its data between activities
    // (by deconstructing the object in one activity and reconstructing it in another)

    // Add object values to Parcel in preparation for transfer
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(recipeId);
        dest.writeString(recipeName);
        dest.writeList(ingredients);
        dest.writeList(steps);
        dest.writeInt(servings);
    }

    // This method is the constructor, called on the receiving activity, where you will be collecting values.
    // When the secondary activity calls the getParcelableExtra method of the intent object to start the process,
    // this constructor is where you collect the values and set up the properties of the object:
    // At this point you’ve populated the object with data.

    // Constructor used for parcel
    protected Recipe(Parcel parcel) {
        // Read and set saved values from parcel
        recipeId = parcel.readInt();
        recipeName = parcel.readString();

        ingredients = new ArrayList<>();
        parcel.readList(ingredients, Recipe.class.getClassLoader());

        steps = new ArrayList<> ();
        parcel.readList(steps, Step.class.getClassLoader());

        servings = parcel.readInt();
    }

    // This method binds everything together. There’s little needed to do here as the createFromParcel method will return newly populated object.
    // Creator - used when un-parceling our parcel (creating the object)
    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel parcel) {
            return new Recipe (parcel);
        }
        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    // Return hashcode of object - this method does not do much.
    @Override
    public int describeContents() {
        return hashCode();
    }
}