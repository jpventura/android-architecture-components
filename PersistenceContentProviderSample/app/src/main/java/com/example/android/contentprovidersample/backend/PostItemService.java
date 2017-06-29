package com.example.android.contentprovidersample.backend;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostItemService extends IntentService implements DatabaseReference.CompletionListener {

    private static final String TAG = PostItemService.class.getSimpleName();

    public PostItemService() {
        super(PostItemService.class.getSimpleName());
    }

    @Override
    public void onComplete(DatabaseError error, DatabaseReference reference) {
        if (null == error) {
            Log.d(TAG, "success");
        } else {
            Log.e(TAG, error.getMessage(), error.toException());
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Record record = intent.getParcelableExtra(Record.class.getCanonicalName());

        Log.d(TAG, "onHandleIntent " + record.toString());

        FirebaseDatabase.getInstance()
                .getReference()
                .child("cheeses")
                .child(record._id.toString())
                .setValue(record, this);
    }

}