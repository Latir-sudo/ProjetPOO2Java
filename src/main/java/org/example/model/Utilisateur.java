package org.example.model;

public class Utilisateur {
    private int id;
    private String nom;
    private String prenom;
    private String matricule;
    private String typeUtilisateur;


    public Utilisateur(int id, String nom, String prenom, String matricule, String typeUtilisateur) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;
        this.typeUtilisateur = typeUtilisateur;
    }

    // définition des getteurs et setteurs

    public int getId() {return id;}
    public void setId(int id) {  this.id = id;}
    public String getNom() { return nom;}
    public void setNom(String nom) { this.nom = nom;}
    public String getPrenom() { return prenom;}
    public void setPrenom(String prenom) { this.prenom = prenom;}
    public String getMatricule() {   return matricule;}
    public void setMatricule(String matricule) { this.matricule = matricule;}
    public String getTypeUtilisateur() {return typeUtilisateur;}
    public void setTypeUtilisateur(String typeUtilisateur){this.typeUtilisateur=typeUtilisateur;}

    @Override
    public String toString() {
        return id + " - " + nom + " " + prenom + " (" + matricule + ")";
    }
}
