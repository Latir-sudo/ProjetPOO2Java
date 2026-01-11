package org.example.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import org.example.util.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatistiqueService {

    // Livres les plus empruntés
    public ObservableList<XYChart.Data<String, Number>> getLivresPlusEmpruntes(int limit) {
        ObservableList<XYChart.Data<String, Number>> data = FXCollections.observableArrayList();

        // CORRECTION : Requête SQL
        String sql = """
            SELECT l.titre, COUNT(e.id_emprunts) as nb_emprunts
            FROM livres l
            LEFT JOIN emprunts e ON l.id_livre = e.livre
            GROUP BY l.id_livre, l.titre
            ORDER BY nb_emprunts DESC
            LIMIT ?
            """;

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String titre = rs.getString("titre");
                int nbEmprunts = rs.getInt("nb_emprunts");
                data.add(new XYChart.Data<>(titre, nbEmprunts));
            }

        } catch (SQLException e) {
            System.err.println("Erreur getLivresPlusEmpruntes: " + e.getMessage());
            e.printStackTrace();
        }

        return data;
    }

    // Répartition étudiants / enseignants
    public ObservableList<PieChart.Data> repartitionUtilisateurs() {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

        String sql = """
            SELECT type_utilisateur, COUNT(*) as nb_utilisateur 
            FROM utilisateurs 
            GROUP BY type_utilisateur
            """;

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String type = rs.getString("type_utilisateur");
                int count = rs.getInt("nb_utilisateur");

                // CORRECTION : Ajouter les données au pieData
                String label = type.equals("ETUDIANT") ? "Étudiants" : "Enseignants";
                pieData.add(new PieChart.Data(label + " (" + count + ")", count));
            }

        } catch (SQLException e) {
            System.err.println("Erreur repartitionUtilisateurs: " + e.getMessage());
            e.printStackTrace();
        }

        return pieData;
    }

    // Top des utilisateurs les plus actifs
    public ObservableList<XYChart.Data<String, Number>> topUtilisateurs(int limit) {
        ObservableList<XYChart.Data<String, Number>> data = FXCollections.observableArrayList();

        // CORRECTION : Requête SQL
        String query = """
            SELECT 
                CONCAT(u.nom, ' ', u.prenom) as nom_complet, 
                COUNT(e.id_emprunts) as nb_emprunts
            FROM utilisateurs u 
            LEFT JOIN emprunts e ON u.id_utilisateur = e.utilisateur
            GROUP BY u.id_utilisateur, u.nom, u.prenom 
            ORDER BY nb_emprunts DESC 
            LIMIT ?
            """;

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String nomComplet = rs.getString("nom_complet");
                int nbEmprunts = rs.getInt("nb_emprunts");
                data.add(new XYChart.Data<>(nomComplet, nbEmprunts));
            }

        } catch (SQLException e) {
            System.err.println(" Erreur topUtilisateurs: " + e.getMessage());
            e.printStackTrace();
        }

        return data;
    }

    // Statistiques générales (Bonus)
    public int getTotalLivres() {
        String sql = "SELECT COUNT(*) as total FROM livres";
        return getCount(sql);
    }

    public int getTotalUtilisateurs() {
        String sql = "SELECT COUNT(*) as total FROM utilisateurs";
        return getCount(sql);
    }

    public int getTotalEmprunts() {
        String sql = "SELECT COUNT(*) as total FROM emprunts";
        return getCount(sql);
    }

    public int getEmpruntsEnRetard() {
        String sql = """
            SELECT COUNT(*) as total 
            FROM emprunts 
            WHERE date_retour_effective IS NULL 
            AND date_retour_prevue < CURDATE()
            """;
        return getCount(sql);
    }

    private int getCount(String sql) {
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println(" Erreur getCount: " + e.getMessage());
        }

        return 0;
    }




    // test de connexion pour les tables crées


    // Méthode de test pour vérifier les noms de tables/colonnes
    public void testConnexionEtTables() {
        System.out.println("=== TEST DE CONNEXION ET TABLES ===");

        try (Connection con = DbConnection.getConnection()) {
            System.out.println("✓ Connexion à la base réussie");

            // Vérifier les tables
            String[] tables = {"livres", "utilisateurs", "emprunts"};

            for (String table : tables) {
                try {
                    String sql = "SELECT COUNT(*) FROM " + table;
                    try (PreparedStatement ps = con.prepareStatement(sql);
                         ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            System.out.println("✓ Table '" + table + "' existe (" + rs.getInt(1) + " lignes)");
                        }
                    }
                } catch (SQLException e) {
                    System.err.println("✗ Table '" + table + "' n'existe pas ou erreur: " + e.getMessage());
                }
            }

        } catch (SQLException e) {
            System.err.println("✗ Impossible de se connecter à la base: " + e.getMessage());
        }

        System.out.println("=== FIN TEST ===");
    }
}