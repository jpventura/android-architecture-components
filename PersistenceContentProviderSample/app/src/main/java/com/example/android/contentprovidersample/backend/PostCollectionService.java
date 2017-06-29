package com.example.android.contentprovidersample.backend;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.example.android.contentprovidersample.provider.SampleContentProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PostCollectionService extends IntentService implements DatabaseReference.CompletionListener {

    public PostCollectionService() {
        super(PostCollectionService.class.getSimpleName());
    }

    @Override
    public void onComplete(DatabaseError error, DatabaseReference reference) {
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Cursor cursor = getContentResolver().query(SampleContentProvider.URI_CHEESE, new String[]{ "_id", "name" }, null, null, null);

        Map<String, Record> map = new HashMap<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Long id = cursor.getLong(cursor.getColumnIndex("_id"));
            map.put(id.toString(), new Record(id, cursor.getString(cursor.getColumnIndex("name"))));
            cursor.moveToNext();
        }

        FirebaseDatabase.getInstance().getReference().child("cheeses").setValue(map);
    }

}
