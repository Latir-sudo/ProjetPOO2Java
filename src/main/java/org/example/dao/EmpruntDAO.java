package org.example.dao;

import org.example.model.Emprunt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class EmpruntDAO {

    private Connection connection;

    public EmpruntDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour récupérer les emprunts récents avec détails
    public List<Emprunt> getEmpruntsRecents(int limite) {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT e.*, u.nom, u.prenom, u.matricule, l.titre, l.auteur, l.isbn " +
                "FROM emprunts e " +
                "JOIN utilisateurs u ON e.utilisateur = u.id_utilisateur " +
                "JOIN livres l ON e.livre = l.id_livre " +
                "ORDER BY e.date_emprunt DESC LIMIT ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, limite);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Emprunt e = new Emprunt();
                e.setIdEmprunt(rs.getInt("id_emprunt"));
                e.setUtilisateur(rs.getInt("utilisateur"));
                e.setLivre(rs.getInt("livre"));
                e.setDateEmprunt(rs.getDate("date_emprunt").toLocalDate());
                e.setDateRetourPrevue(rs.getDate("date_retour_prevue").toLocalDate());

                // Gestion du champ nullable
                java.sql.Date dateRetourEffective = rs.getDate("date_retour_effective");
                if (dateRetourEffective != null) {
                    e.setDateRetourEffective(dateRetourEffective.toLocalDate());
                }

                e.setPenalite(rs.getDouble("penalite"));

                // Ajout des informations détaillées
                e.setNomUtilisateur(rs.getString("nom") + " " + rs.getString("prenom"));
                e.setMatriculeUtilisateur(rs.getString("matricule"));
                e.setTitreLivre(rs.getString("titre"));
                e.setAuteurLivre(rs.getString("auteur"));


                // Déterminer le statut
                if (e.getDateRetourEffective() != null) {
                    e.setStatut("Retourné");
                } else if (LocalDate.now().isAfter(e.getDateRetourPrevue())) {
                    e.setStatut("En retard");
                } else {
                    e.setStatut("En cours");
                }

                emprunts.add(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return emprunts;
    }

}