package com.example.erecrutement.Controller.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.erecrutement.Controller.Data.DatabaseHelper;

public class BlogDataSource {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public BlogDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Open the database
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    // Close the database
    public void close() {
        dbHelper.close();
    }

    // Insert a new blog
    public long insertBlog(String title, String description) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);

        return database.insert(DatabaseHelper.TABLE_BLOG, null, values);
    }

    // Retrieve all blogs
    public Cursor getAllBlogs() {
        String[] allColumns = {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_TITLE, DatabaseHelper.COLUMN_DESCRIPTION};
        return database.query(DatabaseHelper.TABLE_BLOG, allColumns, null, null, null, null, null);
    }

    // Update a blog
    public int updateBlog(long id, String title, String description) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);

        return database.update(DatabaseHelper.TABLE_BLOG, values, DatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    // Delete a blog
    public void deleteBlog(long id) {
        database.delete(DatabaseHelper.TABLE_BLOG, DatabaseHelper.COLUMN_ID + " = " + id, null);
    }
}
