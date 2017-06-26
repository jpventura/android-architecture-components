package com.example.android.contentprovidersample;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.contentprovidersample.data.Cheese;

class CheeseAdapter extends RecyclerView.Adapter<CheeseAdapter.ViewHolder> {

    private Cursor mCursor;

    @Override
    public CheeseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mCursor.moveToPosition(position)) {
            holder.mText.setText(mCursor.getString(
                    mCursor.getColumnIndexOrThrow(Cheese.COLUMN_NAME)));
        }
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    void setCheeses(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mText;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(
                    android.R.layout.simple_list_item_1, parent, false));
            mText = (TextView) itemView.findViewById(android.R.id.text1);
        }

    }

}
