package org.bibliotheque.model;

public class Livre {
    private int id;
    private String isbn;
    private String titre;
    private String auteur;
    private String editeur;
    private int anneePublication;
    private int idCategorie;
    private String nomCategorie;
    private int nombreExemplaires;
    private int exemplairesDisponibles;
    private String emplacement;
    
    public Livre() {}
    
    public Livre(String titre, String auteur, String isbn) {
        this.titre = titre;
        this.auteur = auteur;
        this.isbn = isbn;
    }
    
    // Getters
    public int getId() { return id; }
    public String getIsbn() { return isbn; }
    public String getTitre() { return titre; }
    public String getAuteur() { return auteur; }
    public String getEditeur() { return editeur; }
    public int getAnneePublication() { return anneePublication; }
    public int getIdCategorie() { return idCategorie; }
    public String getNomCategorie() { return nomCategorie; }
    public int getNombreExemplaires() { return nombreExemplaires; }
    public int getExemplairesDisponibles() { return exemplairesDisponibles; }
    public String getEmplacement() { return emplacement; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setTitre(String titre) { this.titre = titre; }
    public void setAuteur(String auteur) { this.auteur = auteur; }
    public void setEditeur(String editeur) { this.editeur = editeur; }
    public void setAnneePublication(int anneePublication) { this.anneePublication = anneePublication; }
    public void setIdCategorie(int idCategorie) { this.idCategorie = idCategorie; }
    public void setNomCategorie(String nomCategorie) { this.nomCategorie = nomCategorie; }
    public void setNombreExemplaires(int nombreExemplaires) { this.nombreExemplaires = nombreExemplaires; }
    public void setExemplairesDisponibles(int exemplairesDisponibles) { 
        this.exemplairesDisponibles = exemplairesDisponibles; 
    }
    public void setEmplacement(String emplacement) { this.emplacement = emplacement; }
    
    // Méthodes utilitaires
    public boolean estDisponible() {
        return exemplairesDisponibles > 0;
    }
    
    @Override
    public String toString() {
        return titre + " - " + auteur;
    }
}