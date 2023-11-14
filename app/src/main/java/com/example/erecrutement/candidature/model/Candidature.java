package com.example.erecrutement.candidature.model;

public class Candidature {
    private long id;
    private String nomOffre;
    private String nom;
    private String prenom;
    private String numero;
    private long etat;
    private String cvFilePath;

    public Candidature() {
    }

    public Candidature(long id, String nomOffre, String nom, String prenom, String numero, long etat, String cvFilePath) {
        this.id = id;
        this.nomOffre = nomOffre;
        this.nom = nom;
        this.prenom = prenom;
        this.numero = numero;
        this.etat = etat;
        this.cvFilePath = cvFilePath;
    }

    public Candidature(String nomOffre, String nom, String prenom, String numero, long etat, String cvFilePath) {
        this.nomOffre = nomOffre;
        this.nom = nom;
        this.prenom = prenom;
        this.numero = numero;
        this.etat = etat;
        this.cvFilePath = cvFilePath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomOffre() {
        return nomOffre;
    }

    public void setNomOffre(String nomOffre) {
        this.nomOffre = nomOffre;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public long getEtat() {
        return etat;
    }

    public void setEtat(long etat) {
        this.etat = etat;
    }

    public String getCvFilePath() {
        return cvFilePath;
    }

    public void setCvFilePath(String cvFilePath) {
        this.cvFilePath = cvFilePath;
    }
}