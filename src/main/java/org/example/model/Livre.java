package org.example.model;

public class Livre {
    private int idLivre;
    private String titre;
    private String auteur;
    private String isbn;
    private int quantiteTotale;
    private int quantiteDisponible;

    // Constructeurs
    public Livre() {}

    public Livre(int idLivre, String titre, String auteur, String isbn,
                 int quantiteTotale, int quantiteDisponible) {
        this.idLivre = idLivre;
        this.titre = titre;
        this.auteur = auteur;
        this.isbn = isbn;
        this.quantiteTotale = quantiteTotale;
        this.quantiteDisponible = quantiteDisponible;
    }

    // Getters et Setters
    public int getIdLivre() { return idLivre; }
    public void setIdLivre(int idLivre) { this.idLivre = idLivre; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public int getQuantiteTotale() { return quantiteTotale; }
    public void setQuantiteTotale(int quantiteTotale) { this.quantiteTotale = quantiteTotale; }

    public int getQuantiteDisponible() { return quantiteDisponible; }
    public void setQuantiteDisponible(int quantiteDisponible) { this.quantiteDisponible = quantiteDisponible; }

    @Override
    public String toString() {
        return titre + " - " + auteur;
    }
}