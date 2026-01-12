package org.example.service;

import org.example.util.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthentificationService {

    public boolean auth(String username, String password) {
        String sql = "select password  from administrateur where email=?";

        try(
                Connection con = DbConnection.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql);

                ) {

            stmt.setString(1, username);


            try (ResultSet rs = stmt.executeQuery()) {

                // vérifie si l'utilisateur existe
                if(rs.next()) {
                    String pass=rs.getString("password");

                    // test pour éviter les null pointer exception.
                    if(pass==null)
                         return false;

                    // on compare avec le mot de passe donné en argument .
                    return pass.equals(password);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur connexion base de données:");
            System.err.println("Email tenté:"+ username);
            System.err.println("Message SQL" + e.getMessage());
            System.err.println("Code erreur "+ e.getErrorCode());
            e.printStackTrace();
        }


        return false;
    }
    
}
