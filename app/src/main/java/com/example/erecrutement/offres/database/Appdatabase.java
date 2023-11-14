package com.example.erecrutement.offres.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.erecrutement.offres.DAO.OffreDAO;
import com.example.erecrutement.offres.entity.Offre;

@Database(entities = {Offre.class}  , version =2 ,exportSchema = false)
public abstract class Appdatabase extends RoomDatabase {

private static Appdatabase instance;
public  OffreDAO offreDAO;

public  static  Appdatabase getInstance(Context context){
    if (instance == null){
                                        //context d'execution             //l'appel class de config
        instance = Room.databaseBuilder(context.getApplicationContext(), Appdatabase.class, "offreDB")
                .addMigrations(MIGRATION_1_2)
                .allowMainThreadQueries() //mm thread de l'application
                .build();
        // Initialize offreDAO within getInstance()
        instance.offreDAO = instance.createOffreDAO();
    }
    return  instance;

}
    // Initialize offreDAO here
    protected abstract OffreDAO createOffreDAO();
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Migration logic goes here
            // Add the new column 'newColumn' to the 'Offre' table
            database.execSQL("ALTER TABLE Offre ADD COLUMN newColumn TEXT");
        }
    };
}
