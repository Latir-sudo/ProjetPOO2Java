package org.example.model;

public class Utilisateur {
    private int idUtilisateur;
    private String nom;
    private String prenom;
    private String matricule;
    private String typeUtilisateur; // "ENSEIGNANT", "ETUDIANT", etc.

    // Constructeurs
    public Utilisateur() {}

    public Utilisateur(int idUtilisateur, String nom, String prenom,
                       String matricule, String typeUtilisateur) {
        this.idUtilisateur = idUtilisateur;
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;
        this.typeUtilisateur = typeUtilisateur;
    }

    // Getters et Setters
    public int getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(int idUtilisateur) { this.idUtilisateur = idUtilisateur; }

    public int getId() {return idUtilisateur;}
    public void setId(int id) {  this.idUtilisateur = id;}
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
        return idUtilisateur + " - " + nom + " " + prenom + " (" + matricule + ")";
    }
}

