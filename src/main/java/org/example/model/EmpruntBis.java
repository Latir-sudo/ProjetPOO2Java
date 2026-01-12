package org.example.model;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EmpruntBis {
    private final IntegerProperty id;
    private final StringProperty utilisateur;
    private final StringProperty matricule;
    private final StringProperty livre;
    private final ObjectProperty<LocalDate> dateEmprunt;
    private final ObjectProperty<LocalDate> retourPrevu;
    private final ObjectProperty<LocalDate> retourEffectif;
    private final StringProperty penalite;
    private final StringProperty statut;

    public EmpruntBis(int id, String utilisateur, String matricule, String livre,
                      LocalDate dateEmprunt, LocalDate retourPrevu, LocalDate retourEffectif,
                      String penalite, String statut) {
        this.id = new SimpleIntegerProperty(id);
        this.utilisateur = new SimpleStringProperty(utilisateur);
        this.matricule = new SimpleStringProperty(matricule);
        this.livre = new SimpleStringProperty(livre);
        this.dateEmprunt = new SimpleObjectProperty<>(dateEmprunt);
        this.retourPrevu = new SimpleObjectProperty<>(retourPrevu);
        this.retourEffectif = new SimpleObjectProperty<>(retourEffectif);
        this.penalite = new SimpleStringProperty(penalite);
        this.statut = new SimpleStringProperty(statut);
    }

    // Getters et Setters

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getUtilisateur() {
        return utilisateur.get();
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur.set(utilisateur);
    }

    public StringProperty utilisateurProperty() {
        return utilisateur;
    }

    public String getMatricule() {
        return matricule.get();
    }

    public void setMatricule(String matricule) {
        this.matricule.set(matricule);
    }

    public StringProperty matriculeProperty() {
        return matricule;
    }

    public String getLivre() {
        return livre.get();
    }

    public void setLivre(String livre) {
        this.livre.set(livre);
    }

    public StringProperty livreProperty() {
        return livre;
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt.get();
    }

    public void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt.set(dateEmprunt);
    }

    public ObjectProperty<LocalDate> dateEmpruntProperty() {
        return dateEmprunt;
    }

    public LocalDate getRetourPrevu() {
        return retourPrevu.get();
    }

    public void setRetourPrevu(LocalDate retourPrevu) {
        this.retourPrevu.set(retourPrevu);
    }

    public ObjectProperty<LocalDate> retourPrevuProperty() {
        return retourPrevu;
    }

    public LocalDate getRetourEffectif() {
        return retourEffectif.get();
    }

    public void setRetourEffectif(LocalDate retourEffectif) {
        this.retourEffectif.set(retourEffectif);
    }

    public ObjectProperty<LocalDate> retourEffectifProperty() {
        return retourEffectif;
    }

    public String getPenalite() {
        return penalite.get();
    }

    public void setPenalite(String penalite) {
        this.penalite.set(penalite);
    }

    public StringProperty penaliteProperty() {
        return penalite;
    }

    public String getStatut() {
        return statut.get();
    }

    public void setStatut(String statut) {
        this.statut.set(statut);
    }

    public StringProperty statutProperty() {
        return statut;
    }
}
