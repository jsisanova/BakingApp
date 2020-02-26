package com.example.android.bakingapp.model;

import android.os.Parcelable;
import android.os.Parcel;


public class Recipe implements Parcelable {
    private int recipeId;
    private String recipeName;
    private double ingredientsQuantity;
    private String ingredientsMeasure;
    private String ingredientsName;
    private String steps;
    private int servings;
    private String imagePath;

    public Recipe() { }


    // Setter methods
    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }
    public void setIngredientsQuantity(double ingredientsQuantity) {
        this.ingredientsQuantity = ingredientsQuantity;
    }
    public void setIngredientsMeasureType(String ingredientsMeasureType) {
        this.ingredientsMeasure = ingredientsMeasureType;
    }
    public void setIngredientsName(String ingredientsName) {
        this.ingredientsName = ingredientsName;
    }
    public void setSteps(String steps) {
        this.steps = steps;
    }
    public void setServings(int servings) {
        this.servings = servings;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    // Getter methods
    public int getRecipeId() {
        return recipeId;
    }
    public String getRecipeName() {
        return recipeName;
    }
    public double getIngredientsQuantity() {
        return ingredientsQuantity;
    }
    public String getIngredientsMeasureType() {
        return ingredientsMeasure;
    }
    public String getIngredientsName() {
        return ingredientsName;
    }
    public String getSteps() {
        return steps;
    }
    public int getServings() {
        return servings;
    }
    public String getImagePath() {
        return imagePath;
    }



    // Use Android Parcelable interface to transfer object and its data between activities
    // (by deconstructing the object in one activity and reconstructing it in another)

    // Add object values to Parcel in preparation for transfer
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt (recipeId);
        dest.writeString(recipeName);
        dest.writeDouble(ingredientsQuantity);
        dest.writeString(ingredientsMeasure);
        dest.writeString(ingredientsName);
        dest.writeString(steps);
        dest.writeInt(servings);
        dest.writeString(imagePath);
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
        ingredientsQuantity = parcel.readDouble();
        ingredientsMeasure = parcel.readString();
        ingredientsName = parcel.readString();
        steps = parcel.readString();
        servings = parcel.readInt();
        imagePath = parcel.readString();
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