package org.bibliotheque.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bibliotheque.model.Emprunt;

public class EmpruntDAO {
    
    private Connection connection;

    public EmpruntDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Emprunt> getEmpruntsRecents(int limite) {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM v_emprunts_details LIMIT ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, limite);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Emprunt e = new Emprunt();
                e.setId(rs.getInt("id_emprunt"));
                e.setUtilisateur(rs.getString("utilisateur"));
                e.setLivre(rs.getString("livre"));
                e.setDateEmprunt(rs.getDate("date_emprunt").toLocalDate());
                e.setDateRetourPrevue(rs.getDate("date_retour_prevue").toLocalDate());
                e.setStatut(rs.getString("statut"));
                emprunts.add(e);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return emprunts;
    }
}