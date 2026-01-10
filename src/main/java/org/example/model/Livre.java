package org.example.model;


public class Livre {
    private int id;
    private String titre;
    private String auteur;
    private String isbn;
    private int quantiteTotale;
    private int disponibles;
    private String statut;
    
    
    public Livre() {}
    
    public Livre(String titre, String auteur, String isbn, int quantiteTotale, int disponibles, String statut) {
        this.titre = titre;
        this.auteur = auteur;
        this.isbn = isbn;
        this.quantiteTotale = quantiteTotale;
        this.disponibles = disponibles;
        this.statut = statut;
    }
    
    public Livre(int id, String titre, String auteur, String isbn, int quantiteTotale, int disponibles, String statut) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.isbn = isbn;
        this.quantiteTotale = quantiteTotale;
        this.disponibles = disponibles;
        this.statut = statut;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    
    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }
    
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public int getQuantiteTotale() { return quantiteTotale; }
    public void setQuantiteTotale(int quantiteTotale) { this.quantiteTotale = quantiteTotale; }
    
    public int getDisponibles() { return disponibles; }
    public void setDisponibles(int disponibles) { this.disponibles = disponibles; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    @Override
    public String toString() {
        return String.format("%s - %s (ISBN: %s) [%d/%d] - %s", 
            titre, auteur, isbn, disponibles, quantiteTotale, statut);
    }
    
    // Méthodes utilitaires
    public boolean estDisponible() {
        return "Disponible".equalsIgnoreCase(statut) && disponibles > 0;
    }
    
    public boolean peutEtreEmprunte() {
        return estDisponible() && disponibles > 0;
    }
    
    public void emprunter() {
        if (peutEtreEmprunte()) {
            disponibles--;
            if (disponibles == 0) {
                statut = "Indisponible";
            }
        }
    }
    
    public void retourner() {
        disponibles++;
        if (disponibles > 0) {
            statut = "Disponible";
        }
    }
}