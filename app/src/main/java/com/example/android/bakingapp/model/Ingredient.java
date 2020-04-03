package com.example.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Ingredient implements Parcelable {

    @SerializedName("quantity")
    private double ingredientsQuantity;
    @SerializedName("measure")
    private String ingredientsMeasure;
    @SerializedName("ingredient")
    private String ingredientsName;

    public Ingredient() { }


    // Setter methods
    public void setIngredientsQuantity(double ingredientsQuantity) {
        this.ingredientsQuantity = ingredientsQuantity;
    }
    public void setIngredientsMeasure(String ingredientsMeasure) {
        this.ingredientsMeasure = ingredientsMeasure;
    }
    public void setIngredientsName(String ingredientsName) {
        this.ingredientsName = ingredientsName;
    }

    // Getter methods
    public double getIngredientsQuantity() {
        return ingredientsQuantity;
    }
    public String getIngredientsMeasure() {
        return ingredientsMeasure;
    }
    public String getIngredientsName() {
        return ingredientsName;
    }


    // Use Android Parcelable interface to transfer object and its data between activities
    // (by deconstructing the object in one activity and reconstructing it in another)

    // Add object values to Parcel in preparation for transfer
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(ingredientsQuantity);
        dest.writeString(ingredientsMeasure);
        dest.writeString(ingredientsName);
    }

    // This method is the constructor, called on the receiving activity, where you will be collecting values.
    // When the secondary activity calls the getParcelableExtra method of the intent object to start the process,
    // this constructor is where you collect the values and set up the properties of the object:
    // At this point you’ve populated the object with data.

    // Constructor used for parcel
    protected Ingredient(Parcel parcel) {
        ingredientsQuantity = parcel.readDouble();
        ingredientsMeasure = parcel.readString();
        ingredientsName = parcel.readString();
    }

    // This method binds everything together. There’s little needed to do here as the createFromParcel method will return newly populated object.
    // Creator - used when un-parceling our parcel (creating the object)
    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }
        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    // Return hashcode of object - this method does not do much.
    @Override
    public int describeContents() {
        return 0;
    }
}