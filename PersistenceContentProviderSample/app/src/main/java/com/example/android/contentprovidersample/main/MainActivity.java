/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.contentprovidersample.main;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.android.contentprovidersample.R;
import com.example.android.contentprovidersample.backend.GetCollectionService;
import com.example.android.contentprovidersample.data.Cheese;
import com.example.android.contentprovidersample.provider.SampleContentProvider;


/**
 * Not very relevant to Room. This just shows data from {@link SampleContentProvider}.
 *
 * <p>Since the data is exposed through the ContentProvider, other apps can read and write the
 * content in a similar manner to this.</p>
 */
public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final int LOADER_CHEESES = 1;

    private SwipeRefreshLayout mSwipeContainer;

    private CheeseAdapter mCheeseAdapter;

    private int count = 0;

    @Override
    public void onClick(View view) {
        fakeUpdate();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_CHEESES:
                return new CursorLoader(getApplicationContext(),
                        SampleContentProvider.URI_CHEESE,
                        new String[]{Cheese.COLUMN_NAME},
                        null, null, null);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_CHEESES:
                mCheeseAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case LOADER_CHEESES:
                mCheeseAdapter.swapCursor(null);
                break;
        }
    }

    @Override
    public void onRefresh() {
        // Your code to refresh the list here.
        // Make sure you call swipeContainer.setRefreshing(false)
        // once the network request has completed successfully.
        fetchTimelineAsync();
    }

    private void fetchTimelineAsync() {
        mSwipeContainer.setRefreshing(false);
        Log.d(MainActivity.class.getSimpleName(), "fetchTimelineAsync");
        startService(new Intent(this, GetCollectionService.class));
        // startService(new Intent(this, PostCollectionService.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        final RecyclerView list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(list.getContext()));
        mCheeseAdapter = new CheeseAdapter(this, null);
        list.setAdapter(mCheeseAdapter);

        mSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        mSwipeContainer.setOnRefreshListener(this);


        findViewById(R.id.fab).setOnClickListener(this);

        getSupportLoaderManager().initLoader(LOADER_CHEESES, null, this);
    }

    private void fakeUpdate() {
        final Uri uri = SampleContentProvider.URI_CHEESE.buildUpon().appendPath("1").build();

        new Thread(new Runnable(){
            @Override
            public void run(){
                ContentValues values = new ContentValues();
                values.put("_id", 1);
                values.put("name", "Teste " + Integer.toString(count++));
                getContentResolver().update(uri, values, null, null);
            }
        }).start();
    }

}
