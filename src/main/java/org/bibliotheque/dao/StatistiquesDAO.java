package org.bibliotheque.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class StatistiquesDAO {
    
    private Connection connection;
    
    public StatistiquesDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Map<String, Integer> getStatistiquesGlobales() {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT * FROM v_statistiques";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                stats.put("utilisateurs", rs.getInt("total_utilisateurs"));
                stats.put("enseignants", rs.getInt("total_enseignants"));
                stats.put("etudiants", rs.getInt("total_etudiants"));
                stats.put("livres", rs.getInt("total_livres"));
                stats.put("livres_disponibles", rs.getInt("livres_disponibles"));
                stats.put("emprunts_en_cours", rs.getInt("emprunts_en_cours"));
                stats.put("emprunts_en_retard", rs.getInt("emprunts_en_retard"));
                stats.put("penalites", rs.getInt("total_penalites"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return stats;
    }
}