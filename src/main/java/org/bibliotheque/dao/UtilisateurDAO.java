package org.bibliotheque.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bibliotheque.model.Utilisateur;

public class UtilisateurDAO {
    
    private Connection connection;
    
    public UtilisateurDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Utilisateur> getAllUtilisateurs() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs WHERE statut = 'ACTIF' ORDER BY nom";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                utilisateurs.add(mapResultSetToUtilisateur(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return utilisateurs;
    }
    
    public Utilisateur getUtilisateurByCode(String code) {
        String sql = "SELECT * FROM utilisateurs WHERE code_utilisateur = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUtilisateur(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<Utilisateur> rechercherUtilisateurs(String motCle) {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs WHERE " +
                    "(nom LIKE ? OR prenom LIKE ? OR code_utilisateur LIKE ?) " +
                    "AND statut = 'ACTIF' ORDER BY nom";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String pattern = "%" + motCle + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            pstmt.setString(3, pattern);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                utilisateurs.add(mapResultSetToUtilisateur(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return utilisateurs;
    }
    
    private Utilisateur mapResultSetToUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur u = new Utilisateur();
        u.setId(rs.getInt("id_utilisateur"));
        u.setCode(rs.getString("code_utilisateur"));
        u.setNom(rs.getString("nom"));
        u.setPrenom(rs.getString("prenom"));
        u.setEmail(rs.getString("email"));
        u.setTelephone(rs.getString("telephone"));
        u.setType(rs.getString("type_utilisateur"));
        u.setStatut(rs.getString("statut"));
        
        Date dateInscription = rs.getDate("date_inscription");
        if (dateInscription != null) {
            u.setDateInscription(dateInscription.toLocalDate());
        }
        
        return u;
    }
}