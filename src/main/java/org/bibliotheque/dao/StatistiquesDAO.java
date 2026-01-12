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

        try {
            // Total utilisateurs
            String sqlUtilisateurs = "SELECT COUNT(*) FROM utilisateurs";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlUtilisateurs)) {
                if (rs.next()) stats.put("utilisateurs", rs.getInt(1));
            }

            // Enseignants
            String sqlEnseignants = "SELECT COUNT(*) FROM utilisateurs WHERE type_utilisateur = 'ENSEIGNANT'";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlEnseignants)) {
                if (rs.next()) stats.put("enseignants", rs.getInt(1));
            }

            // Étudiants
            String sqlEtudiants = "SELECT COUNT(*) FROM utilisateurs WHERE type_utilisateur = 'ETUDIANT'";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlEtudiants)) {
                if (rs.next()) stats.put("etudiants", rs.getInt(1));
            }

            // Total livres
            String sqlLivres = "SELECT COUNT(*) FROM livres";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlLivres)) {
                if (rs.next()) stats.put("livres", rs.getInt(1));
            }

            // Livres disponibles
            String sqlDisponibles = "SELECT SUM(quantite_disponible) FROM livres";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlDisponibles)) {
                if (rs.next()) stats.put("livres_disponibles", rs.getInt(1));
            }

            // Emprunts en cours
            String sqlEmpruntsCours = "SELECT COUNT(*) FROM emprunts WHERE date_retour_effective IS NULL";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlEmpruntsCours)) {
                if (rs.next()) stats.put("emprunts_en_cours", rs.getInt(1));
            }

            // Emprunts en retard
            String sqlRetards = "SELECT COUNT(*) FROM emprunts WHERE date_retour_effective IS NULL "
                    + "AND date_retour_prevue < CURDATE()";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlRetards)) {
                if (rs.next()) stats.put("emprunts_en_retard", rs.getInt(1));
            }

            // Total pénalités
            String sqlPenalites = "SELECT SUM(penalite) FROM emprunts";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlPenalites)) {
                if (rs.next()) stats.put("penalites", rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stats;
    }
}