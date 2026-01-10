package org.bibliotheque.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bibliotheque.model.Livre;

 public class LivreDAO {
    
    private Connection connection;
    
    public LivreDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Livre> getAllLivres() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT l.*, c.nom_categorie " +
                    "FROM livres l " +
                    "LEFT JOIN categories c ON l.id_categorie = c.id_categorie " +
                    "ORDER BY l.titre";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                livres.add(mapResultSetToLivre(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return livres;
    }
    
    public Livre getLivreById(int id) {
        String sql = "SELECT l.*, c.nom_categorie " +
                    "FROM livres l " +
                    "LEFT JOIN categories c ON l.id_categorie = c.id_categorie " +
                    "WHERE l.id_livre = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToLivre(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    private Livre mapResultSetToLivre(ResultSet rs) throws SQLException {
        Livre l = new Livre();
        l.setId(rs.getInt("id_livre"));
        l.setIsbn(rs.getString("isbn"));
        l.setTitre(rs.getString("titre"));
        l.setAuteur(rs.getString("auteur"));
        l.setEditeur(rs.getString("editeur"));
        l.setAnneePublication(rs.getInt("annee_publication"));
        l.setIdCategorie(rs.getInt("id_categorie"));
        l.setNomCategorie(rs.getString("nom_categorie"));
        l.setNombreExemplaires(rs.getInt("nombre_exemplaires"));
        l.setExemplairesDisponibles(rs.getInt("exemplaires_disponibles"));
        l.setEmplacement(rs.getString("emplacement"));
        return l;
    }
}