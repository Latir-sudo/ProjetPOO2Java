package org.bibliotheque.model;

import java.time.LocalDate;

public class Utilisateur {
    private int id;
    private String code;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String type;
    private LocalDate dateInscription;
    private String statut;
    
    public Utilisateur() {}
    
    public Utilisateur(String code, String nom, String prenom, String email, String type) {
        this.code = code;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.type = type;
    }
    
    // Getters
    public int getId() { return id; }
    public String getCode() { return code; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getTelephone() { return telephone; }
    public String getType() { return type; }
    public LocalDate getDateInscription() { return dateInscription; }
    public String getStatut() { return statut; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setCode(String code) { this.code = code; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setType(String type) { this.type = type; }
    public void setDateInscription(LocalDate dateInscription) { this.dateInscription = dateInscription; }
    public void setStatut(String statut) { this.statut = statut; }
    
    // Méthodes utilitaires
    public String getNomComplet() {
        return prenom + " " + nom;
    }
    
    public String getCodeAvecNom() {
        return getNomComplet() + " (" + code + ")";
    }
    
    @Override
    public String toString() {
        return getCodeAvecNom();
    }
}