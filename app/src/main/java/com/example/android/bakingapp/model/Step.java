package com.example.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Step implements Parcelable {

    @SerializedName("id")
    private int stepId;
    @SerializedName("shortDescription")
    private String stepShortDescription;
    @SerializedName("description")
    private String stepDescription;
    @SerializedName("videoURL")
    private String stepVideoUrl;

    public Step() { }


    // Setter methods
    public void setStepId(int stepId) {
        this.stepId = stepId;
    }
    public void setStepDescription(String stepDescription) {
        this.stepDescription = stepDescription;
    }
    public void setStepShortDescription(String stepShortDescription) {
        this.stepShortDescription = stepShortDescription;
    }
    public void setStepVideoUrl(String stepVideoUrl) {
        this.stepVideoUrl = stepVideoUrl;
    }

    // Getter methods
    public int getStepId() {
        return stepId;
    }
    public String getStepDescription() {
        return stepDescription;
    }
    public String getStepShortDescription() {
        return stepShortDescription;
    }
    public String getStepVideoUrl() {
        return stepVideoUrl;
    }


    // Use Android Parcelable interface to transfer object and its data between activities
    // (by deconstructing the object in one activity and reconstructing it in another)

    // Add object values to Parcel in preparation for transfer
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(stepId);
        dest.writeString(stepShortDescription);
        dest.writeString(stepDescription);
        dest.writeString(stepVideoUrl);
    }


    // This method is the constructor, called on the receiving activity, where you will be collecting values.
    // When the secondary activity calls the getParcelableExtra method of the intent object to start the process,
    // this constructor is where you collect the values and set up the properties of the object:
    // At this point you’ve populated the object with data.

    // Constructor used for parcel
    protected Step(Parcel parcel) {
        // Read and set saved values from parcel
        stepId = parcel.readInt();
        stepShortDescription = parcel.readString();
        stepDescription = parcel.readString();
        stepVideoUrl = parcel.readString();
    }

    // This method binds everything together. There’s little needed to do here as the createFromParcel method will return newly populated object.
    // Creator - used when un-parceling our parcel (creating the object)
    public static final Creator<Step> CREATOR = new Creator<Step> () {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }
        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };


    // Return hashcode of object - this method does not do much.
    @Override
    public int describeContents() {
        return 0;
    }
}