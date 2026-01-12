package org.example;

import org.example.model.Livre;
import org.example.model.Utilisateur;
import org.example.service.EmpruntService;
import org.example.util.DbConnection;

import java.sql.*;
import java.util.List;

public class TestEmpruntService {
    public static void main(String[] args) {
        System.out.println("========== TEST DES SERVICES EMPRUNT ==========\n");

        // Test 1: Connexion à la base de données
        System.out.println("TEST 1: Connexion à la BD");
        Connection conn = DbConnection.getConnection();
        if (conn != null) {
            System.out.println("✓ Connexion réussie\n");
            try { conn.close(); } catch (SQLException ignore) {}
        } else {
            System.out.println("✗ Connexion échouée\n");
            return;
        }

        // Test 2: Vérifier les tables et leurs données
        System.out.println("TEST 2: Contenu des tables\n");
        testTableContent("livres");
        testTableContent("utilisateurs");
        testTableContent("emprunts");

        // Test 3: Vérifier les livres disponibles
        System.out.println("\nTEST 3: Livres disponibles");
        System.out.println("SQL: SELECT l.id_livre, l.titre FROM livres l WHERE l.id_livre NOT IN (SELECT e.livre FROM emprunts e WHERE e.statut = 'En cours')");
        try (Connection c = DbConnection.getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT l.id_livre, l.titre FROM livres l WHERE l.id_livre NOT IN (SELECT e.livre FROM emprunts e WHERE e.statut = 'En cours')")) {
            
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println("  - ID: " + rs.getInt("id_livre") + ", Titre: " + rs.getString("titre"));
            }
            System.out.println("Total: " + count + " livre(s) disponible(s)\n");
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
            e.printStackTrace();
        }

        // Test 4: Service obtenirUtilisateurs
        System.out.println("TEST 4: Service obtenirUtilisateurs()");
        EmpruntService service = new EmpruntService();
        List<Utilisateur> users = service.obtenirUtilisateurs();
        System.out.println("Utilisateurs retournés: " + users.size());
        for (Utilisateur u : users) {
            System.out.println("  - " + u);
        }

        // Test 5: Service obtenirLivresDisponibles
        System.out.println("\nTEST 5: Service obtenirLivresDisponibles()");
        List<Livre> books = service.obtenirLivresDisponibles();
        System.out.println("Livres disponibles retournés: " + books.size());
        for (Livre b : books) {
            System.out.println("  - " + b.getId() + " - " + b.getTitre());
        }

        // Test 6: Vérifier les emprunts
        System.out.println("\nTEST 6: Emprunts actuels");
        try (Connection c = DbConnection.getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id_emprunt, utilisateur, livre, statut FROM emprunts")) {
            
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println("  - ID: " + rs.getInt("id_emprunt") + ", User: " + rs.getInt("utilisateur") + ", Livre: " + rs.getInt("livre") + ", Statut: " + rs.getString("statut"));
            }
            System.out.println("Total: " + count + " emprunt(s)\n");
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testTableContent(String tableName) {
        System.out.println("Table: " + tableName);
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM " + tableName)) {
            
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("  Nombre d'enregistrements: " + count);
                
                if (count > 0) {
                    System.out.println("  Premiers enregistrements:");
                    try (ResultSet rs2 = stmt.executeQuery("SELECT * FROM " + tableName + " LIMIT 3")) {
                        int colCount = rs2.getMetaData().getColumnCount();
                        while (rs2.next()) {
                            System.out.print("    ");
                            for (int i = 1; i <= colCount; i++) {
                                if (i > 1) System.out.print(" | ");
                                System.out.print(rs2.getMetaData().getColumnName(i) + "=" + rs2.getObject(i));
                            }
                            System.out.println();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("  Erreur: " + e.getMessage());
        }
        System.out.println();
    }
}
