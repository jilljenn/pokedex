package net.androidsensei.pokedex;

import android.os.Parcel;
import android.os.Parcelable;

public class Pokemon implements Parcelable {

    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.description);
    }

    public Pokemon() {
    }

    private Pokemon(Parcel in) {
        this.name = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<Pokemon> CREATOR = new Parcelable.Creator<Pokemon>() {
        public Pokemon createFromParcel(Parcel source) {
            return new Pokemon(source);
        }

        public Pokemon[] newArray(int size) {
            return new Pokemon[size];
        }
    };
}
