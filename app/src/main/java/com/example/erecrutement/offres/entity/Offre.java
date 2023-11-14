package com.example.erecrutement.offres.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Offre implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo
    private String titleJob, typeJob, nameCompagny, locationCompagny, jobDescription, companyUrl;
    @ColumnInfo
    private int imgJob;
    @ColumnInfo
    private String imagePath;
    @ColumnInfo
    private String imageName;

    @ColumnInfo
    private double latitude;

    @ColumnInfo
    private double longitude;

    @ColumnInfo
    private boolean isClicked;

    public Offre() {
    }

    public Offre(String titleJob, String typeJob, String nameCompagny,
                 String locationCompagny, String imagePath,
                 int imgJob, String imageName,
                 String jobDescription, String companyUrl ,
                 double latitude , double longitude , boolean isClicked) {
        this.titleJob = titleJob;
        this.typeJob = typeJob;
        this.nameCompagny = nameCompagny;
        this.locationCompagny = locationCompagny;
        this.imgJob = imgJob;
        this.companyUrl = companyUrl;
        this.jobDescription = jobDescription;
        this.imageName = imageName;
        this.imagePath = imagePath;

        this.latitude = latitude;
        this.longitude = longitude;
        this.isClicked = isClicked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitleJob() {
        return titleJob;
    }

    public void setTitleJob(String titleJob) {
        this.titleJob = titleJob;
    }

    public String getTypeJob() {
        return typeJob;
    }

    public void setTypeJob(String typeJob) {
        this.typeJob = typeJob;
    }

    public String getNameCompagny() {
        return nameCompagny;
    }

    public void setNameCompagny(String nameCompagny) {
        this.nameCompagny = nameCompagny;
    }

    public String getLocationCompagny() {
        return locationCompagny;
    }

    public void setLocationCompagny(String locationCompagny) {
        this.locationCompagny = locationCompagny;
    }

    public int getImgJob() {
        return imgJob;
    }

    public void setImgJob(int imgJob) {
        this.imgJob = imgJob;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getCompanyUrl() {
        return companyUrl;
    }

    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    // Ajoutez les getters et setters pour imagePath
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }
}