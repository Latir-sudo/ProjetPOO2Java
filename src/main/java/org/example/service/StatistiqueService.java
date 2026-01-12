package org.example.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.example.model.EmpruntEnRetard;
import org.example.util.ConnexionBD;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

public class StatistiqueService {

    public ObservableList<XYChart.Data<String, Number>> getLivresPlusEmpruntes(int limit) {
        ObservableList<XYChart.Data<String, Number>> data = FXCollections.observableArrayList();

        String sql = """
            SELECT l.titre, COUNT(emprunts.id_emprunt) as nb_emprunts
            FROM livres l
            LEFT JOIN emprunts ON l.id_livre = emprunts.livre
            GROUP BY l.id_livre, l.titre
            ORDER BY nb_emprunts DESC
            LIMIT ?
            """;

        try (Connection con = ConnexionBD.getConnexion();
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

    public ObservableList<PieChart.Data> repartitionUtilisateurs() {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

        String sql = """
            SELECT type_utilisateur, COUNT(*) as nb_utilisateur 
            FROM utilisateurs 
            GROUP BY type_utilisateur
            """;

        try (Connection con =  ConnexionBD.getConnexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String type = rs.getString("type_utilisateur");
                int count = rs.getInt("nb_utilisateur");

                String label = type.equals("ETUDIANT") ? "Etudiants" : "Enseignats";
                pieData.add(new PieChart.Data(label + " (" + count + ")", count));
            }

        } catch (SQLException e) {
            System.err.println("Erreur repartitionUtilisateurs: " + e.getMessage());
            e.printStackTrace();
        }

        return pieData;
    }

    public ObservableList<XYChart.Data<String, Number>> topUtilisateurs(int limit) {
        ObservableList<XYChart.Data<String, Number>> data = FXCollections.observableArrayList();

        String query = """
            SELECT 
                CONCAT(u.nom, ' ', u.prenom, ' (', u.matricule, ')') as nom_complet, 
                COUNT(emprunts.id_emprunt) as nb_emprunts
            FROM utilisateurs u 
            LEFT JOIN emprunts ON u.id_utilisateur = emprunts.utilisateur
            GROUP BY u.id_utilisateur, u.nom, u.prenom, u.matricule
            ORDER BY nb_emprunts DESC 
            LIMIT ?
            """;

        try (Connection con =  ConnexionBD.getConnexion();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String nomComplet = rs.getString("nom_complet");
                int nbEmprunts = rs.getInt("nb_emprunts");
                data.add(new XYChart.Data<>(nomComplet, nbEmprunts));
            }

        } catch (SQLException e) {
            System.err.println("Erreur topUtilisateurs: " + e.getMessage());
            e.printStackTrace();
        }

        return data;
    }

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

    public ObservableList<EmpruntEnRetard> getEmpruntsEnRetardDetails() {
        ObservableList<EmpruntEnRetard> data = FXCollections.observableArrayList();

        System.out.println("\n=== DEBUT RÉCUPÉRATION EMPRUNTS EN RETARD ===");
        
        String sql = """
            SELECT 
                emprunts.id_emprunt as id,
                CONCAT(utilisateurs.nom, ' ', utilisateurs.prenom, ' (', utilisateurs.matricule, ')') as utilisateur,
                livres.titre as livre,
                DATE_FORMAT(emprunts.date_emprunt, '%d/%m/%Y') as dateEmprunt,
                DATE_FORMAT(emprunts.date_retour_prevue, '%d/%m/%Y') as dateRetourPrevue,
                DATEDIFF(CURDATE(), emprunts.date_retour_prevue) as joursRetard,
                DATEDIFF(CURDATE(), emprunts.date_retour_prevue) * 0.5 as penalite
            FROM emprunts
            INNER JOIN utilisateurs ON emprunts.utilisateur = utilisateurs.id_utilisateur
            INNER JOIN livres ON emprunts.livre = livres.id_livre
            WHERE emprunts.date_retour_effective IS NULL 
            AND emprunts.date_retour_prevue < CURDATE()
            ORDER BY emprunts.date_retour_prevue ASC
            """;

        try (Connection con =  ConnexionBD.getConnexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            int count = 0;
            while (rs.next()) {
                count++;
                int id = rs.getInt("id");
                String utilisateur = rs.getString("utilisateur");
                String livre = rs.getString("livre");
                String dateEmprunt = rs.getString("dateEmprunt");
                String dateRetourPrevue = rs.getString("dateRetourPrevue");
                int joursRetard = rs.getInt("joursRetard");
                double penalite = rs.getDouble("penalite");

                System.out.printf("  %d. ID: %d, Utilisateur: %s, Livre: %s, Jours: %d%n", 
                    count, id, utilisateur, livre, joursRetard);

                EmpruntEnRetard emprunt = new EmpruntEnRetard(id, utilisateur, livre, 
                                                              dateEmprunt, dateRetourPrevue, 
                                                              joursRetard, penalite);
                data.add(emprunt);
            }
            
            System.out.println("Total d'emprunts en retard trouvés: " + count);

        } catch (SQLException e) {
            System.err.println("ERREUR getEmpruntsEnRetardDetails: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== FIN RÉCUPÉRATION ===");

        return data;
    }

    private int getCount(String sql) {
        try (Connection con = ConnexionBD.getConnexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Erreur getCount: " + e.getMessage());
        }

        return 0;
    }

    public void testConnexionEtTables() {
        System.out.println("=== TEST DE CONNEXION ET TABLES ===");

        try (Connection con =  ConnexionBD.getConnexion()) {
            System.out.println("✓ Connexion à la base réussie");

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