package org.bibliotheque.model;

import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Emprunt {
    private int id;
    private int idUtilisateur;
    private int idLivre;
    private String utilisateur;
    private String livre;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;
    private String statut;
    
    public Emprunt() {}
    
    // Getters
    public int getId() { return id; }
    public int getIdUtilisateur() { return idUtilisateur; }
    public int getIdLivre() { return idLivre; }
    public String getUtilisateur() { return utilisateur; }
    public String getLivre() { return livre; }
    public LocalDate getDateEmprunt() { return dateEmprunt; }
    public LocalDate getDateRetourPrevue() { return dateRetourPrevue; }
    public LocalDate getDateRetourEffective() { return dateRetourEffective; }
    public String getStatut() { return statut; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setIdUtilisateur(int idUtilisateur) { this.idUtilisateur = idUtilisateur; }
    public void setIdLivre(int idLivre) { this.idLivre = idLivre; }
    public void setUtilisateur(String utilisateur) { this.utilisateur = utilisateur; }
    public void setLivre(String livre) { this.livre = livre; }
    public void setDateEmprunt(LocalDate dateEmprunt) { this.dateEmprunt = dateEmprunt; }
    public void setDateRetourPrevue(LocalDate dateRetourPrevue) { this.dateRetourPrevue = dateRetourPrevue; }
    public void setDateRetourEffective(LocalDate dateRetourEffective) { 
        this.dateRetourEffective = dateRetourEffective; 
    }
    public void setStatut(String statut) { this.statut = statut; }
    
    // Pour JavaFX TableView - retourner des String
    public String getDateEmpruntString() {
        return dateEmprunt != null ? dateEmprunt.toString() : "";
    }
    
    public String getDateRetourPrevueString() {
        return dateRetourPrevue != null ? dateRetourPrevue.toString() : "";
    }
    
    // Pour JavaFX TableView - Property
    public StringProperty utilisateurProperty() {
        return new SimpleStringProperty(utilisateur);
    }
    
    public StringProperty livreProperty() {
        return new SimpleStringProperty(livre);
    }
    
    public StringProperty statutProperty() {
        return new SimpleStringProperty(statut);
    }
}