package com.example.haneenalawneh.extrafit.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by haneenalawneh on 11/12/17.
 */

public class Exercise implements Parcelable {
    int duration;
    String image;
    String name;

    public Exercise(int duration, String imageURL, String name) {
        this.duration = duration;
        this.image = imageURL;
        this.name = name;

    }

    public int getDuration() {
        return duration;
    }

    public String getImageURL() {
        return image;
    }

    public String getName() {
        return name;
    }

    public int getSeconds() {
        return duration % 60;


    }

    public int getMinutes() {


        return duration / 60;
    }

    public String getDurationDescription() {
        String description = getMinutes() + ":" + getSeconds() + " Mins";

        if (getSeconds() == 0) {
            description = getMinutes() + " Mins";

        }
        return description;

    }

    private Exercise(Parcel in) {
        duration = in.readInt();
        image = in.readString();
        name = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(duration);
        parcel.writeString(image);
        parcel.writeString(name);


    }

    public static final Parcelable.Creator<Exercise> CREATOR = new Parcelable.Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };
}
