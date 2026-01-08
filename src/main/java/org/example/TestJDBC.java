package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestJDBC {


    public static void main(String[] args) {

        String URL="jdbc:mysql://localhost:3306/bibliotheque?useSSL=false&serverTimezone=UTC";
        String USER="root";
        String PASSWORD="francois12";

        try {
            Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);
            String sql ="select * from utilisateurs";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                int id = rs.getInt("id_utilisateur");
                String nom = rs.getString("nom");
                String prenom= rs.getString ("prenom");
                String matricule = rs.getString("matricule");
                String type_utilisateur = rs.getString ("type_utilisateur");

                System.out.println(id + " "+ nom + " "+ prenom + " "+ matricule + " "+ type_utilisateur);
            }

            // tout fermer

            rs.close();
            stmt.close();
            conn.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
