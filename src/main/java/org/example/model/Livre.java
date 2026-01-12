package org.example.model;

public class Livre {
    private int id;
    private String titre;

    public Livre(int id, String titre) {
        this.id = id;
        this.titre = titre;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    @Override
    public String toString() {
        return id + " - " + titre;
    }
}
