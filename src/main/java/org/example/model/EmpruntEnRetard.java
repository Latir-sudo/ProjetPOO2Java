package org.example.model;

import javafx.beans.property.*;

public class EmpruntEnRetard {
    private final IntegerProperty id;
    private final StringProperty utilisateur;
    private final StringProperty livre;
    private final StringProperty dateEmprunt;
    private final StringProperty dateRetourPrevue;
    private final IntegerProperty joursRetard;
    private final DoubleProperty penalite;

    // Constructeur
    public EmpruntEnRetard(int id, String utilisateur, String livre, 
                          String dateEmprunt, String dateRetourPrevue, 
                          int joursRetard, double penalite) {
        this.id = new SimpleIntegerProperty(id);
        this.utilisateur = new SimpleStringProperty(utilisateur);
        this.livre = new SimpleStringProperty(livre);
        this.dateEmprunt = new SimpleStringProperty(dateEmprunt);
        this.dateRetourPrevue = new SimpleStringProperty(dateRetourPrevue);
        this.joursRetard = new SimpleIntegerProperty(joursRetard);
        this.penalite = new SimpleDoubleProperty(penalite);
    }

    // Property getters
    public IntegerProperty idProperty() { return id; }
    public StringProperty utilisateurProperty() { return utilisateur; }
    public StringProperty livreProperty() { return livre; }
    public StringProperty dateEmpruntProperty() { return dateEmprunt; }
    public StringProperty dateRetourPrevueProperty() { return dateRetourPrevue; }
    public IntegerProperty joursRetardProperty() { return joursRetard; }
    public DoubleProperty penaliteProperty() { return penalite; }

    // Getters normaux
    public int getId() { return id.get(); }
    public String getUtilisateur() { return utilisateur.get(); }
    public String getLivre() { return livre.get(); }
    public String getDateEmprunt() { return dateEmprunt.get(); }
    public String getDateRetourPrevue() { return dateRetourPrevue.get(); }
    public int getJoursRetard() { return joursRetard.get(); }
    public double getPenalite() { return penalite.get(); }

    // Setters
    public void setId(int id) { this.id.set(id); }
    public void setUtilisateur(String utilisateur) { this.utilisateur.set(utilisateur); }
    public void setLivre(String livre) { this.livre.set(livre); }
    public void setDateEmprunt(String dateEmprunt) { this.dateEmprunt.set(dateEmprunt); }
    public void setDateRetourPrevue(String dateRetourPrevue) { this.dateRetourPrevue.set(dateRetourPrevue); }
    public void setJoursRetard(int joursRetard) { this.joursRetard.set(joursRetard); }
    public void setPenalite(double penalite) { this.penalite.set(penalite); }
}