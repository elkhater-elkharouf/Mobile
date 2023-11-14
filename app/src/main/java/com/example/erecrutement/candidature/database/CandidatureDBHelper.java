package com.example.erecrutement.candidature.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.erecrutement.candidature.model.Candidature;

import java.util.ArrayList;
import java.util.List;

public class CandidatureDBHelper {

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public CandidatureDBHelper(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Get all candidatures
    public List<Candidature> getAll() {
        List<Candidature> candidatures = new ArrayList<>();

        Cursor cursor = database.query(
                Constants.CANDIDATURE_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Candidature candidature = cursorToCandidature(cursor);
                candidatures.add(candidature);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return candidatures;
    }

    // Get a candidature by ID
    public Candidature getById(long candidatureId) {
        Cursor cursor = database.query(
                Constants.CANDIDATURE_TABLE_NAME,
                null,
                Constants.CANDIDATURE_COLUMN_ID + " = ?",
                new String[]{String.valueOf(candidatureId)},
                null,
                null,
                null
        );

        Candidature candidature = null;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                candidature = cursorToCandidature(cursor);
            }
            cursor.close();
        }

        return candidature;
    }

    // Insert a new candidature
    public long add(Candidature candidature) {
        ContentValues values = new ContentValues();
        values.put(Constants.CANDIDATURE_COLUMN_NOM_OFFRE, candidature.getNomOffre());
        values.put(Constants.CANDIDATURE_COLUMN_NOM, candidature.getNom());
        values.put(Constants.CANDIDATURE_COLUMN_PRENOM, candidature.getPrenom());
        values.put(Constants.CANDIDATURE_COLUMN_NUMERO, candidature.getNumero());
        values.put(Constants.CANDIDATURE_COLUMN_ETAT, candidature.getEtat());
        values.put(Constants.CANDIDATURE_COLUMN_CV_FILE_PATH, candidature.getCvFilePath());

        return database.insert(Constants.CANDIDATURE_TABLE_NAME, null, values);
    }

    // Update a candidature
    public int update(Candidature candidature) {
        ContentValues values = new ContentValues();
        values.put(Constants.CANDIDATURE_COLUMN_NOM_OFFRE, candidature.getNomOffre());
        values.put(Constants.CANDIDATURE_COLUMN_NOM, candidature.getNom());
        values.put(Constants.CANDIDATURE_COLUMN_PRENOM, candidature.getPrenom());
        values.put(Constants.CANDIDATURE_COLUMN_NUMERO, candidature.getNumero());
        values.put(Constants.CANDIDATURE_COLUMN_ETAT, candidature.getEtat());
        values.put(Constants.CANDIDATURE_COLUMN_CV_FILE_PATH, candidature.getCvFilePath());

        return database.update(
                Constants.CANDIDATURE_TABLE_NAME,
                values,
                Constants.CANDIDATURE_COLUMN_ID + " = ?",
                new String[]{String.valueOf(candidature.getId())}
        );
    }

    // Delete a candidature
    public int delete(long candidatureId) {
        return database.delete(
                Constants.CANDIDATURE_TABLE_NAME,
                Constants.CANDIDATURE_COLUMN_ID + " = ?",
                new String[]{String.valueOf(candidatureId)}
        );
    }

    // Helper method to convert a cursor to a Candidature object
    private Candidature cursorToCandidature(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(Constants.CANDIDATURE_COLUMN_ID));
        String nomOffre = cursor.getString(cursor.getColumnIndexOrThrow(Constants.CANDIDATURE_COLUMN_NOM_OFFRE));
        String nom = cursor.getString(cursor.getColumnIndexOrThrow(Constants.CANDIDATURE_COLUMN_NOM));
        String prenom = cursor.getString(cursor.getColumnIndexOrThrow(Constants.CANDIDATURE_COLUMN_PRENOM));
        String numero = cursor.getString(cursor.getColumnIndexOrThrow(Constants.CANDIDATURE_COLUMN_NUMERO));
        long etat = cursor.getLong(cursor.getColumnIndexOrThrow(Constants.CANDIDATURE_COLUMN_ETAT));
        String cvFilePath = cursor.getString(cursor.getColumnIndexOrThrow(Constants.CANDIDATURE_COLUMN_CV_FILE_PATH));

        return new Candidature(id, nomOffre, nom, prenom, numero, etat, cvFilePath);
    }
}
