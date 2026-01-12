package org.example.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.example.model.Emprunt;
import org.example.model.Livre;
import org.example.model.Utilisateur;
import org.example.util.DbConnection;

public class EmpruntService {

    public List<Emprunt> obtenirTousLesEmprunts() {
        List<Emprunt> emprunts = new ArrayList<>();
        String query = "SELECT e.id_emprunt, CONCAT(u.nom, ' ', u.prenom) as utilisateur, u.matricule, l.titre, " +
                      "e.date_emprunt, e.date_retour_prevue, e.date_retour_effective, e.penalite, " +
                      "CASE WHEN e.date_retour_effective IS NULL THEN 'En cours' ELSE 'Retourné' END as statut " +
                      "FROM emprunts e " +
                      "JOIN utilisateurs u ON e.utilisateur = u.id_utilisateur " +
                      "JOIN livres l ON e.livre = l.id_livre " +
                      "ORDER BY e.id_emprunt DESC";

        Connection conn = DbConnection.getConnection();
        if (conn == null) {
            System.err.println("[EmpruntService] Impossible d'obtenir la connexion DB — retourne liste vide.");
            return emprunts;
        }

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Emprunt emprunt = new Emprunt(
                    rs.getInt("id_emprunt"),
                    rs.getString("utilisateur"),
                    rs.getString("matricule"),
                    rs.getString("titre"),
                    rs.getObject("date_emprunt", LocalDate.class),
                    rs.getObject("date_retour_prevue", LocalDate.class),
                    rs.getObject("date_retour_effective", LocalDate.class),
                    rs.getString("penalite") != null ? rs.getString("penalite") : "0",
                    rs.getString("statut")
                );
                emprunts.add(emprunt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }

        return emprunts;
    }

    public List<Emprunt> rechercherEmprunts(String terme) {
        List<Emprunt> emprunts = new ArrayList<>();
        String query = "SELECT e.id_emprunt, CONCAT(u.nom, ' ', u.prenom) as utilisateur, u.matricule, l.titre, " +
                      "e.date_emprunt, e.date_retour_prevue, e.date_retour_effective, e.penalite, " +
                      "CASE WHEN e.date_retour_effective IS NULL THEN 'En cours' ELSE 'Retourné' END as statut " +
                      "FROM emprunts e " +
                      "JOIN utilisateurs u ON e.utilisateur = u.id_utilisateur " +
                      "JOIN livres l ON e.livre = l.id_livre " +
                      "WHERE CONCAT(u.nom, ' ', u.prenom) LIKE ? OR u.matricule LIKE ? OR l.titre LIKE ? OR e.id_emprunt LIKE ? " +
                      "ORDER BY e.id_emprunt DESC";


        Connection conn = DbConnection.getConnection();
        if (conn == null) {
            System.err.println("[EmpruntService] Impossible d'obtenir la connexion DB — retourne liste vide.");
            return emprunts;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {

            String searchTerm = "%" + terme + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);
            pstmt.setString(3, searchTerm);
            pstmt.setString(4, searchTerm);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Emprunt emprunt = new Emprunt(
                        rs.getInt("id_emprunt"),
                        rs.getString("utilisateur"),
                        rs.getString("matricule"),
                        rs.getString("titre"),
                        rs.getObject("date_emprunt", LocalDate.class),
                        rs.getObject("date_retour_prevue", LocalDate.class),
                        rs.getObject("date_retour_effective", LocalDate.class),
                        rs.getString("penalite") != null ? rs.getString("penalite") : "0",
                        rs.getString("statut")
                    );
                    emprunts.add(emprunt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }

        return emprunts;
    }

    public boolean mettreAJourRetour(int idEmprunt, LocalDate dateRetour) {
        String query = "UPDATE emprunts SET date_retour_effective = ? WHERE id_emprunt = ?";

        Connection conn = DbConnection.getConnection();
        if (conn == null) {
            System.err.println("[EmpruntService] Impossible d'obtenir la connexion DB — mise à jour annulée.");
            return false;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setObject(1, dateRetour);
            pstmt.setInt(2, idEmprunt);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }
    }

    public boolean mettreAJourEmprunt(int idEmprunt, LocalDate dateEmprunt, LocalDate dateRetourPrevue, 
                                     LocalDate dateRetourEffective, String penalite) {
        String query = "UPDATE emprunts SET date_emprunt = ?, date_retour_prevue = ?, " +
                      "date_retour_effective = ?, penalite = ? WHERE id_emprunt = ?";

        Connection conn = DbConnection.getConnection();
        if (conn == null) {
            System.err.println("[EmpruntService] Impossible d'obtenir la connexion DB — mise à jour annulée.");
            return false;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setObject(1, dateEmprunt);
            pstmt.setObject(2, dateRetourPrevue);
            pstmt.setObject(3, dateRetourEffective);
            pstmt.setString(4, penalite);
            pstmt.setInt(5, idEmprunt);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }
    }

    public boolean creerEmprunt(int idUtilisateur, int idLivre, LocalDate dateRetourPrevu) {
        String query = "INSERT INTO emprunts (utilisateur, livre, date_emprunt, date_retour_prevue) " +
                      "VALUES (?, ?, CURDATE(), ?)";

        Connection conn = DbConnection.getConnection();
        if (conn == null) {
            System.err.println("[EmpruntService] Impossible d'obtenir la connexion DB — création annulée.");
            return false;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idUtilisateur);
            pstmt.setInt(2, idLivre);
            pstmt.setObject(3, dateRetourPrevu);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }
    }

    public Emprunt obtenirEmpruntParId(int id) {
        String query = "SELECT e.id_emprunt, CONCAT(u.nom, ' ', u.prenom) as utilisateur, u.matricule, l.titre, " +
                      "e.date_emprunt, e.date_retour_prevue, e.date_retour_effective, e.penalite, " +
                      "CASE WHEN e.date_retour_effective IS NULL THEN 'En cours' ELSE 'Retourné' END as statut " +
                      "FROM emprunts e " +
                      "JOIN utilisateurs u ON e.utilisateur = u.id_utilisateur " +
                      "JOIN livres l ON e.livre = l.id_livre " +
                      "WHERE e.id_emprunt = ?";

        Connection conn = DbConnection.getConnection();
        if (conn == null) {
            System.err.println("[EmpruntService] Impossible d'obtenir la connexion DB — obtention annulée.");
            return null;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Emprunt(
                        rs.getInt("id_emprunt"),
                        rs.getString("utilisateur"),
                        rs.getString("matricule"),
                        rs.getString("titre"),
                        rs.getObject("date_emprunt", LocalDate.class),
                        rs.getObject("date_retour_prevue", LocalDate.class),
                        rs.getObject("date_retour_effective", LocalDate.class),
                        rs.getString("penalite") != null ? rs.getString("penalite") : "0",
                        rs.getString("statut")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }

        return null;
    }

    public java.util.List<Utilisateur> obtenirUtilisateurs() {
        java.util.List<Utilisateur> utilisateurs = new java.util.ArrayList<>();
        String query = "SELECT id_utilisateur, nom, prenom, matricule, type_utilisateur FROM utilisateurs";

        java.sql.Connection conn = DbConnection.getConnection();
        if (conn == null) {
            System.err.println("[EmpruntService] BD indisponible — utilisation de données de test pour utilisateurs");
            // Fallback with test data
            utilisateurs.add(new Utilisateur(1, "Diallo", "Marie", "E12345", "Étudiant"));
            utilisateurs.add(new Utilisateur(2, "Ndiaye", "Jean", "P87654", "Professeur"));
            utilisateurs.add(new Utilisateur(3, "Fall", "Sophie", "E54321", "Étudiant"));
            return utilisateurs;
        }

        try (java.sql.Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                utilisateurs.add(new Utilisateur(
                    rs.getInt("id_utilisateur"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("matricule"),
                    rs.getString("type_utilisateur")
                ));
            }
            System.out.println("[EmpruntService] " + utilisateurs.size() + " utilisateur(s) chargé(s) depuis BD");
        } catch (java.sql.SQLException e) {
            System.err.println("[EmpruntService] Erreur lors du chargement des utilisateurs: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { conn.close(); } catch (java.sql.SQLException ignore) {}
        }

        return utilisateurs;
    }

    public java.util.List<Livre> obtenirLivresDisponibles() {
        java.util.List<Livre> livres = new java.util.ArrayList<>();
        
        java.sql.Connection conn = DbConnection.getConnection();
        if (conn == null) {
            System.err.println("[EmpruntService] BD indisponible — utilisation de données de test pour livres");
            // Fallback with test data
            livres.add(new Livre(1, "Algorithmes en Java"));
            livres.add(new Livre(2, "Design Patterns"));
            livres.add(new Livre(3, "Base de données avancées"));
            livres.add(new Livre(4, "Programmation Orientée Objet"));
            return livres;
        }

        try {
            // D'abord, récupérer TOUS les livres
            String query1 = "SELECT l.id_livre, l.titre FROM livres l";
            java.util.List<Livre> allLivres = new java.util.ArrayList<>();
            
            try (java.sql.Statement stmt = conn.createStatement();
                 java.sql.ResultSet rs = stmt.executeQuery(query1)) {
                while (rs.next()) {
                    allLivres.add(new Livre(rs.getInt("id_livre"), rs.getString("titre")));
                }
            }
            System.out.println("[EmpruntService] " + allLivres.size() + " livre(s) total(aux) trouvé(s)");
            
            // Ensuite, récupérer les livres en cours d'emprunt
            String query2 = "SELECT DISTINCT e.livre FROM emprunts e WHERE e.date_retour_effective IS NULL";
            java.util.Set<Integer> booksInLoan = new java.util.HashSet<>();
            
            try (java.sql.Statement stmt = conn.createStatement();
                 java.sql.ResultSet rs = stmt.executeQuery(query2)) {
                while (rs.next()) {
                    booksInLoan.add(rs.getInt("livre"));
                }
            }
            System.out.println("[EmpruntService] " + booksInLoan.size() + " livre(s) actuellement emprunté(s)");
            
            // Filtrer pour obtenir les livres disponibles
            for (Livre livre : allLivres) {
                if (!booksInLoan.contains(livre.getId())) {
                    livres.add(livre);
                }
            }
            
            System.out.println("[EmpruntService] " + livres.size() + " livre(s) disponible(s) pour nouvel emprunt");
        } catch (java.sql.SQLException e) {
            System.err.println("[EmpruntService] Erreur lors du chargement des livres: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { conn.close(); } catch (java.sql.SQLException ignore) {}
        }

        return livres;
    }
}
