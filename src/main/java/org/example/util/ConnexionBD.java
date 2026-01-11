package org.example.util;



import java.sql.Connection;
import java.sql.DriverManager;  
import java.sql.SQLException;


public class ConnexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/bibliotheque?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "francois12";

    public static Connection getConnexion() {
        try{
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion reussie avec succès !");
            return conn;
        }
        catch (SQLException ex) {
           System.out.println("Erreur de connexion ");
           ex.printStackTrace();
           return null;
    }  
}
}