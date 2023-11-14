package com.example.erecrutement.offres.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.erecrutement.offres.entity.Offre;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface OffreDAO {

    @Insert
    public void addOffre(Offre offre);

   @Query("Select * from Offre")
    public List<Offre> getAllOffre();

    @Delete
    void deleteOffre(Offre offre);

    @Update
    void updateOffre(Offre offre);
}
