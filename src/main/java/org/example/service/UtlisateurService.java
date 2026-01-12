package org.example.service;

import org.example.model.Utilisateur;
import org.example.util.DbConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtlisateurService {

    public List<Utilisateur> getAllUsers() {
        List<Utilisateur> users = new ArrayList<Utilisateur>();
        String sql = "SELECT * FROM utilisateurs";
        int count = 0;
        try (
                Connection con = DbConnection.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
        ) {
            while (rs.next()) {
                count++;

                Utilisateur user = new Utilisateur(
                        rs.getInt("id_utilisateur"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("matricule"),
                        rs.getString("type_utilisateur")
                );

                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("le nombre d'utilisateurs :"+ count);
        return users;
    }

    // fonction permettant d'ajouter un utilisateur


    public void addUser(Utilisateur user) {

        if (!user.getNom().isEmpty() && !user.getPrenom().isEmpty() && !user.getMatricule().isEmpty() && !user.getTypeUtilisateur().isEmpty()) {
            String sql = "insert into utilisateurs(nom,prenom,matricule,type_utilisateur) values (?,?,?,?)";
            try (
                    Connection con = DbConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement(sql);
            ) {


                ps.setString(1, user.getNom());
                ps.setString(2, user.getPrenom());
                ps.setString(3, user.getMatricule());
                ps.setString(4, user.getTypeUtilisateur());

                ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    // fonction pour supprimer un utilisateur .

    public boolean deleteUser(int id) {
        int rows =0;
        String sql = "delete from utilisateurs where id_utilisateur = ?";
        try (
                Connection con = DbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setInt(1, id);
            rows = ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'utilisateur");
            e.printStackTrace();
        }
        return rows>0; // je veux vérifier qu'une ligne a bien été supprimée.
    }

// une fonction permettant de rechercher  un utilisateur par son nom ou son  matricule

    public List<Utilisateur> getUserByName(String nom) {
        String sql = "select * from utilisateurs where nom = ?";
        List<Utilisateur> users = new ArrayList<>();
        try (
                Connection con = DbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setString(1, nom);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Utilisateur user = new Utilisateur(
                        rs.getInt("id_utilisateur"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("matricule"),
                        rs.getString("type_utilisateur")
                );
                users.add(user);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // recherche par matricule

    public List<Utilisateur> getUserByMatricule(String matricule) {
        String sql = "select * from utilisateurs where matricule = ?";
        List<Utilisateur> users = new ArrayList<>();
        try (
                Connection con = DbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setString(1, matricule);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Utilisateur user = new Utilisateur(
                        rs.getInt("id_utilisateur"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("matricule"),
                        rs.getString("type_utilisateur")
                );

                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;

    }



    public Utilisateur getUserById(int id) {
        String sql = "select * from utilisateurs where id_utilisateur = ?";
        Utilisateur user = null;
        try (
                Connection con = DbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user = new Utilisateur(
                        rs.getInt("id_utilisateur"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("matricule"),
                        rs.getString("type_utilisateur")
                );


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;

    }


    // fonction update pour la mise à jour de l'utilisateur

   public boolean updateUser(Utilisateur user) {
        int row=0;
        String sql = "update utilisateurs set nom=? ,prenom =? , type_utilisateur =? where id_utilisateur =?";
        try (
                Connection con = DbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setString(1, user.getNom());
            ps.setString(2, user.getPrenom());
            ps.setString(3, user.getTypeUtilisateur());
            ps.setInt(4, user.getId());
            row = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row>0;

    }
}