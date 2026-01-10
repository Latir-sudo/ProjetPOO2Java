package org.example.model;

public class Livre {
    private int id_livre;
    private String titre;
    private String auteur;
    private String isbn;
    private int quantiteTotale;
    private int quantite_disponible;
    private String statut;

    
    public Livre() {}
    
    public Livre(String titre, String auteur, String isbn, int quantiteTotale, int disponibles, String statut) {
        this.titre = titre;
        this.auteur = auteur;
        this.isbn = isbn;
        this.quantiteTotale = quantiteTotale;
        this.quantite_disponible = disponibles;
        this.statut = statut;
    }
    
    public Livre(int id, String titre, String auteur, String isbn, int quantiteTotale, int disponibles, String statut) {
        this.id_livre = id;
        this.titre = titre;
        this.auteur = auteur;
        this.isbn = isbn;
        this.quantiteTotale = quantiteTotale;
        this.quantite_disponible = disponibles;
        this.statut = statut;
    }
    
    public int getId() { return id_livre; }
    public void setId(int id) { this.id_livre = id; }
    
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    
    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }
    
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public int getQuantiteTotale() { return quantiteTotale; }
    public void setQuantiteTotale(int quantiteTotale) { this.quantiteTotale = quantiteTotale; }
    
    public int getQuantiteDisponible() { return quantite_disponible; }
    public void setQuantiteDisponible(int disponibles) { this.quantite_disponible = disponibles; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    @Override
    public String toString() {
        return String.format("%s - %s (ISBN: %s) [%d/%d] - %s", 
            titre, auteur, isbn, quantite_disponible, quantiteTotale, statut);
    }
    
    // Méthodes utilitaires
    public boolean estDisponible() {
        return "Disponible".equalsIgnoreCase(statut) && quantite_disponible > 0;
    }
    
    public boolean peutEtreEmprunte() {
        return estDisponible() && quantite_disponible > 0;
    }
    
    public void emprunter() {
        if (peutEtreEmprunte()) {
            quantite_disponible--;
            if (quantite_disponible == 0) {
                statut = "Indisponible";
            }
        }
    }
    
    public void retourner() {
        quantite_disponible++;
        if (quantite_disponible > 0) {
            statut = "Disponible";
        }
    }
}