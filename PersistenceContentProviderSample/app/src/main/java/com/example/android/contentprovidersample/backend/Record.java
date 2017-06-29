package com.example.android.contentprovidersample.backend;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

public class Record implements Parcelable {

    public static final Creator<Record> CREATOR = new Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel source) {
            return new Record(source);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };

    public final Long _id;
    public final String name;

    public Record() {
        _id = null;
        name = null;
    }

    public Record(ContentValues values) {
        _id = values.containsKey("_id") ? values.getAsLong("_id") : null;
        name = values.containsKey("name") ? values.getAsString("name") : null;
    }

    public Record(@NonNull Long id, @NonNull final String name) {
        this._id = id;
        this.name = name;
    }

    private Record(Parcel source) {
        _id = source.readLong();
        name = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeString(name);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
