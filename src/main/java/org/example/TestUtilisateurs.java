package org.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.example.model.Utilisateur;
import org.example.service.UtlisateurService;
import org.example.util.DbConnection;

public class TestUtilisateurs {
    public static void main(String[] args) {
        // Test 1: Vérifier la connexion
        System.out.println("========== TEST 1: Connexion à la DB ==========");
        Connection con = DbConnection.getConnection();
        if (con != null) {
            System.out.println("✓ Connexion réussie");
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("✗ Connexion échouée");
            return;
        }

        // Test 2: Vérifier la structure de la table utilisateurs
        System.out.println("\n========== TEST 2: Structure de la table ==========");
        try (Connection connection = DbConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SHOW COLUMNS FROM utilisateurs")) {

            while (rs.next()) {
                System.out.println("Colonne: " + rs.getString("Field") + " - Type: " + rs.getString("Type"));
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la vérification de la structure: " + e.getMessage());
            e.printStackTrace();
        }

        // Test 3: Compter les utilisateurs
        System.out.println("\n========== TEST 3: Nombre d'utilisateurs ==========");
        try (Connection connection = DbConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM utilisateurs")) {

            if (rs.next()) {
                System.out.println("Nombre d'utilisateurs dans la DB: " + rs.getInt("count"));
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du comptage: " + e.getMessage());
            e.printStackTrace();
        }

        // Test 4: Afficher les 5 premiers utilisateurs (SQL brut)
        System.out.println("\n========== TEST 4: SQL Brut - Premiers utilisateurs ==========");
        try (Connection connection = DbConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM utilisateurs LIMIT 5")) {

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id_utilisateur") + 
                                 " | Nom: " + rs.getString("nom") + 
                                 " | Prénom: " + rs.getString("prenom") + 
                                 " | Matricule: " + rs.getString("matricule") + 
                                 " | Type: " + rs.getString("type_utilisateur"));
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture: " + e.getMessage());
            e.printStackTrace();
        }

        // Test 5: Utiliser le service
        System.out.println("\n========== TEST 5: Service getAllUsers ==========");
        UtlisateurService service = new UtlisateurService();
        List<Utilisateur> users = service.getAllUsers();
        
        System.out.println("Utilisateurs retournés par le service: " + users.size());
        for (Utilisateur u : users) {
            System.out.println("- " + u.getId() + " | " + u.getNom() + " " + u.getPrenom());
        }
    }
}
