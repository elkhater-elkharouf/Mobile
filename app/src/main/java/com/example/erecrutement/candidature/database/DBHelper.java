package com.example.erecrutement.candidature.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "offreDB.db";
    private static final int DATABASE_VERSION = 10;  // Incremented version due to changes in the database schema

    // SQL statement to create the candidature table
    private static final String SQL_CREATE_CANDIDATURE_TABLE =
            "CREATE TABLE " + Constants.CANDIDATURE_TABLE_NAME + " (" +
                    Constants.CANDIDATURE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Constants.CANDIDATURE_COLUMN_NOM_OFFRE + " TEXT NOT NULL," +
                    Constants.CANDIDATURE_COLUMN_NOM + " TEXT NOT NULL," +
                    Constants.CANDIDATURE_COLUMN_PRENOM + " TEXT NOT NULL," +
                    Constants.CANDIDATURE_COLUMN_NUMERO + " TEXT NOT NULL," +
                    Constants.CANDIDATURE_COLUMN_ETAT + " INTEGER NOT NULL," +
                    Constants.CANDIDATURE_COLUMN_CV_FILE_PATH + " TEXT NOT NULL)";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CANDIDATURE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.CANDIDATURE_TABLE_NAME);
        onCreate(db);
    }
}
