package org.example.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.example.model.Livre;
import org.example.util.ConnexionBD;
public class LivreService {

    // Récupérer tous les livres
    public List<Livre> getAllLivres() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres ORDER BY titre";
        
        try (
            Connection con = ConnexionBD.getConnexion();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
        ) {
            while (rs.next()) {
                Livre livre = new Livre(
                    rs.getInt("id_livre"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getString("isbn"),
                    rs.getInt("quantite_totale"),
                    rs.getInt("quantite_disponible")

                );
                livres.add(livre);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des livres: " + e.getMessage());
            e.printStackTrace();
        }

        return livres;
    }

    // Ajouter un livre
    public boolean addLivre(Livre livre) {
        if (!livre.getTitre().isEmpty() && !livre.getAuteur().isEmpty()) {
            String sql = "INSERT INTO livres (titre, auteur, isbn, quantite_totale, quantite_disponible) VALUES (?, ?, ?, ?, ?)";
            
            try (
                Connection con = ConnexionBD.getConnexion();
                PreparedStatement ps = con.prepareStatement(sql);
            ) {
                ps.setString(1, livre.getTitre());
                ps.setString(2, livre.getAuteur());
                ps.setString(3, livre.getIsbn());
                ps.setInt(4, livre.getQuantiteTotale());
                ps.setInt(5, livre.getDisponibles());


                int rows = ps.executeUpdate();
                return rows > 0;
                
            } catch (Exception e) {
                System.err.println("Erreur lors de l'ajout du livre: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return false;
    }

    // Supprimer un livre
    public boolean deleteLivre(int id) {
        int rows = 0;
        String sql = "DELETE FROM livres WHERE id_livre = ?";
        
        try (
            Connection con = ConnexionBD.getConnexion();
            PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setInt(1, id);
            rows = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du livre: " + e.getMessage());
            e.printStackTrace();
        }
        return rows > 0;
    }

    // Rechercher un livre par titre
    public List<Livre> getLivreByTitre(String titre) {
        String sql = "SELECT * FROM livres WHERE titre LIKE ?";
        List<Livre> livres = new ArrayList<>();
        
        try (
            Connection con = ConnexionBD.getConnexion();
            PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setString(1, "%" + titre + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Livre livre = new Livre(
                    rs.getInt("id_livre"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getString("isbn"),
                    rs.getInt("quantite_totale"),
                    rs.getInt("quantite_disponible")

                );
                livres.add(livre);
            }

        } catch (SQLException e) {
            System.err.println("Erreur recherche par titre: " + e.getMessage());
            e.printStackTrace();
        }
        return livres;
    }

    // Rechercher un livre par auteur
    public List<Livre> getLivreByAuteur(String auteur) {
        String sql = "SELECT * FROM livres WHERE auteur LIKE ?";
        List<Livre> livres = new ArrayList<>();
        
        try (
            Connection con = ConnexionBD.getConnexion();
            PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setString(1, "%" + auteur + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Livre livre = new Livre(
                    rs.getInt("id_livre"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getString("isbn"),
                    rs.getInt("quantite_totale"),
                    rs.getInt("quantite_disponible")

                );
                livres.add(livre);
            }

        } catch (SQLException e) {
            System.err.println("Erreur recherche par auteur: " + e.getMessage());
            e.printStackTrace();
        }
        return livres;
    }

    // Rechercher un livre par ISBN
    public List<Livre> getLivreByISBN(String isbn) {
        String sql = "SELECT * FROM livres WHERE isbn LIKE ?";
        List<Livre> livres = new ArrayList<>();
        
        try (
            Connection con = ConnexionBD.getConnexion();
            PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setString(1, "%" + isbn + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Livre livre = new Livre(
                    rs.getInt("id_livre"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getString("isbn"),
                    rs.getInt("quantite_totale"),
                    rs.getInt("quantite_disponible")

                );
                livres.add(livre);
            }

        } catch (SQLException e) {
            System.err.println("Erreur recherche par ISBN: " + e.getMessage());
            e.printStackTrace();
        }
        return livres;
    }

    // Recherche  (titre, auteur ou ISBN)
    public List<Livre> rechercherLivres(String keyword) {
        String sql = "SELECT * FROM livres WHERE titre LIKE ? OR auteur LIKE ? OR isbn LIKE ?";
        List<Livre> livres = new ArrayList<>();
        
        try (
            Connection con = ConnexionBD.getConnexion();
            PreparedStatement ps = con.prepareStatement(sql);
        ) {
            String searchTerm = "%" + keyword + "%";
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);
            ps.setString(3, searchTerm);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Livre livre = new Livre(
                    rs.getInt("id_livre"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getString("isbn"),
                    rs.getInt("quantite_totale"),
                    rs.getInt("quantite_disponible")

                );
                livres.add(livre);
            }

        } catch (SQLException e) {
            System.err.println("Erreur recherche générale: " + e.getMessage());
            e.printStackTrace();
        }
        return livres;
    }

    // Mettre à jour un livre
    public boolean updateLivre(Livre livre) {
        int row = 0;
        String sql = "UPDATE livres SET titre = ?, auteur = ?, isbn = ?, quantite_totale = ?, quantite_disponible = ? WHERE id_livre = ?";
        
        try (
            Connection con = ConnexionBD.getConnexion();
            PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setString(1, livre.getTitre());
            ps.setString(2, livre.getAuteur());
            ps.setString(3, livre.getIsbn());
            ps.setInt(4, livre.getQuantiteTotale());
            ps.setInt(5, livre.getDisponibles());
            ps.setInt(6, livre.getId());
            
            row = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du livre: " + e.getMessage());
            e.printStackTrace();
        }
        return row > 0;
    }

    // Récupérer un livre par son ID
    public Livre getLivreById(int id) {
        String sql = "SELECT * FROM livres WHERE id_livre = ?";
        Livre livre = null;
        
        try (
            Connection con = ConnexionBD.getConnexion();
            PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                livre = new Livre(
                    rs.getInt("id_livre"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getString("isbn"),
                    rs.getInt("quantite_totale"),
                    rs.getInt("quantite_disponible")

                );
            }

        } catch (SQLException e) {
            System.err.println("Erreur récupération par ID: " + e.getMessage());
            e.printStackTrace();
        }
        return livre;
    }

    // Compter le nombre total de livres
    public int getTotalLivres() {
        String sql = "SELECT COUNT(*) as total FROM livres";
        int total = 0;
        
        try (
            Connection con = ConnexionBD.getConnexion();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
        ) {
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Erreur comptage livres: " + e.getMessage());
            e.printStackTrace();
        }
        return total;
    }

    // Compter le nombre de livres disponibles
    public int getLivresDisponibles() {
        String sql = "SELECT SUM(quantite_disponible) as total_disponibles FROM livres";
        int total = 0;
        
        try (
            Connection con = ConnexionBD.getConnexion();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
        ) {
            if (rs.next()) {
                total = rs.getInt("total_disponibles");
            }
        } catch (SQLException e) {
            System.err.println("Erreur comptage livres disponibles: " + e.getMessage());
            e.printStackTrace();
        }
        return total;
    }

    // Mettre à jour la quantité disponible après emprunt/retour
    public boolean updateDisponibles(int id, int nouveauxDisponibles) {
        String sql = "UPDATE livres SET quantite_disponible = ? WHERE id_livre = ?";
        
        try (
            Connection con = ConnexionBD.getConnexion();
            PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setInt(1, nouveauxDisponibles);
            ps.setInt(2, id);
            
            int rows = ps.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour disponibles: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}