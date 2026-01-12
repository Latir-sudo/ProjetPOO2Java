package org.bibliotheque.model;

import java.time.LocalDate;

public class Emprunt {
    private int idEmprunt;
    private int utilisateur; // id_utilisateur
    private int livre; // id_livre
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;
    private double penalite;

    // ===== NOUVEAUX ATTRIBUTS POUR AFFICHAGE =====
    private String nomUtilisateur;
    private String prenomUtilisateur;
    private String matriculeUtilisateur;
    private String titreLivre;
    private String auteurLivre;
    private String statut;
    // ============================================

    // Constructeurs
    public Emprunt() {}

    public Emprunt(int idEmprunt, int utilisateur, int livre,
                   LocalDate dateEmprunt, LocalDate dateRetourPrevue,
                   LocalDate dateRetourEffective, double penalite) {
        this.idEmprunt = idEmprunt;
        this.utilisateur = utilisateur;
        this.livre = livre;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourEffective = dateRetourEffective;
        this.penalite = penalite;
    }

    // Getters et Setters originaux
    public int getIdEmprunt() { return idEmprunt; }
    public void setIdEmprunt(int idEmprunt) { this.idEmprunt = idEmprunt; }

    public int getUtilisateur() { return utilisateur; }
    public void setUtilisateur(int utilisateur) { this.utilisateur = utilisateur; }

    public int getLivre() { return livre; }
    public void setLivre(int livre) { this.livre = livre; }

    public LocalDate getDateEmprunt() { return dateEmprunt; }
    public void setDateEmprunt(LocalDate dateEmprunt) { this.dateEmprunt = dateEmprunt; }

    public LocalDate getDateRetourPrevue() { return dateRetourPrevue; }
    public void setDateRetourPrevue(LocalDate dateRetourPrevue) { this.dateRetourPrevue = dateRetourPrevue; }

    public LocalDate getDateRetourEffective() { return dateRetourEffective; }
    public void setDateRetourEffective(LocalDate dateRetourEffective) { this.dateRetourEffective = dateRetourEffective; }

    public double getPenalite() { return penalite; }
    public void setPenalite(double penalite) { this.penalite = penalite; }

    // ===== NOUVEAUX GETTERS/SETTERS =====
    public String getNomUtilisateur() { return nomUtilisateur; }
    public void setNomUtilisateur(String nomUtilisateur) { this.nomUtilisateur = nomUtilisateur; }

    public String getPrenomUtilisateur() { return prenomUtilisateur; }
    public void setPrenomUtilisateur(String prenomUtilisateur) { this.prenomUtilisateur = prenomUtilisateur; }

    public String getMatriculeUtilisateur() { return matriculeUtilisateur; }
    public void setMatriculeUtilisateur(String matriculeUtilisateur) { this.matriculeUtilisateur = matriculeUtilisateur; }

    public String getTitreLivre() { return titreLivre; }
    public void setTitreLivre(String titreLivre) { this.titreLivre = titreLivre; }

    public String getAuteurLivre() { return auteurLivre; }
    public void setAuteurLivre(String auteurLivre) { this.auteurLivre = auteurLivre; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    // Méthode utilitaire pour afficher le nom complet
    public String getNomCompletUtilisateur() {
        return (nomUtilisateur != null && prenomUtilisateur != null)
                ? nomUtilisateur + " " + prenomUtilisateur
                : "Utilisateur inconnu";
    }

    // Méthodes utilitaires
    public boolean estEnRetard() {
        if (dateRetourEffective != null) {
            return dateRetourEffective.isAfter(dateRetourPrevue);
        }
        return LocalDate.now().isAfter(dateRetourPrevue);
    }

    public boolean estEnCours() {
        return dateRetourEffective == null;
    }
}