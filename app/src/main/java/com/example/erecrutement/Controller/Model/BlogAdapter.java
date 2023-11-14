package com.example.erecrutement.Controller.Model;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.erecrutement.R;
import com.example.erecrutement.Controller.Data.DatabaseHelper;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {
    private Cursor cursor;

    public BlogAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View itemView = inflater.inflate(R.layout.item_blog, parent, false);

        // Return a new holder instance
        return new BlogViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BlogViewHolder holder, int position) {
        // Move the cursor to the correct position
        if (!cursor.moveToPosition(position)) {
            return; // bail if returned null
        }

        // Extract data from the cursor
        String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE));
        String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION));

        // Populate the views with data
        holder.textViewTitle.setText(title);
        holder.textViewDescription.setText(description);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        // Swap in the new cursor and update the UI
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        notifyDataSetChanged();
    }

    // Inner ViewHolder class
    class BlogViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;

        BlogViewHolder(View itemView) {
            super(itemView);

            // Get references to the views
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
        }
    }
}

