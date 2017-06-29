package com.example.android.contentprovidersample.backend;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.android.contentprovidersample.provider.SampleContentProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GetCollectionService extends IntentService implements ValueEventListener {

    public GetCollectionService() {
        super(GetCollectionService.class.getSimpleName());
    }

    @Override
    public void onCancelled(DatabaseError error) {
    }

    @Override
    public void onDataChange(DataSnapshot snapshot) {
        List<ContentValues> batch = new ArrayList<>();

        for (DataSnapshot child : snapshot.getChildren()) {
            Record record = child.getValue(Record.class);
            ContentValues value = new ContentValues();
            value.put("_id", record._id);
            value.put("name", record.name);
            batch.add(value);
        }

        final ContentValues[] bulk = new ContentValues[batch.size()];
        batch.toArray(bulk);

        new Thread(new Runnable(){
            @Override
            public void run(){
                getContentResolver().bulkInsert(SampleContentProvider.URI_CHEESE, bulk);
            }
        }).start();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        FirebaseDatabase.getInstance().getReference().child("cheeses").addListenerForSingleValueEvent(this);
    }

}
