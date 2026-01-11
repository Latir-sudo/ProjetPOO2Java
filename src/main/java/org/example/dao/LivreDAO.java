package org.example.dao;

import org.example.model.LivreBis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


 public class LivreDAO {
    
    private Connection connection;
    
    public LivreDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
     public List<LivreBis> getAllLivres() {
         List<LivreBis> livres = new ArrayList<>();
         String sql = "SELECT * FROM livres ORDER BY titre";

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

     // Récupérer un livre par son ID
     public LivreBis getLivreById(int id) {
         String sql = "SELECT * FROM livres WHERE id_livre = ?";

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

     // Récupérer un livre par ISBN
     public LivreBis getLivreByIsbn(String isbn) {
         String sql = "SELECT * FROM livres WHERE isbn = ?";

         try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
             pstmt.setString(1, isbn);
             ResultSet rs = pstmt.executeQuery();

             if (rs.next()) {
                 return mapResultSetToLivre(rs);
             }

         } catch (SQLException e) {
             e.printStackTrace();
         }

         return null;
     }


     private LivreBis mapResultSetToLivre(ResultSet rs) throws SQLException {
         LivreBis l = new LivreBis();
         l.setIdLivre(rs.getInt("id_livre"));           // id_livre
         l.setTitre(rs.getString("titre"));            // titre
         l.setAuteur(rs.getString("auteur"));          // auteur
         l.setIsbn(rs.getString("isbn"));              // isbn
         l.setQuantiteTotale(rs.getInt("quantite_totale"));        // quantite_totale
         l.setQuantiteDisponible(rs.getInt("quantite_disponible")); // quantite_disponible
         return l;
     }
}