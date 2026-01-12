package org.example.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnection {

    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        try (InputStream input = DbConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties props = new Properties();
            if (input != null) {
                props.load(input);
                URL = props.getProperty("db.url", "jdbc:mysql://localhost:3306/bibliotheque?useSSL=false&serverTimezone=UTC");
                USER = props.getProperty("db.user", "root");
                PASSWORD = props.getProperty("db.password", "francois12");
                System.out.println("[DbConnection] Configuration chargée depuis db.properties");
            } else {
                System.err.println("[DbConnection] db.properties non trouvé, utilisation des valeurs par défaut");
                URL = "jdbc:mysql://localhost:3306/bibliotheque?useSSL=false&serverTimezone=UTC";
                USER = "root";
                PASSWORD = "fatou";
            }
        } catch (Exception e) {
            System.err.println("[DbConnection] Erreur lors du chargement de db.properties: " + e.getMessage());
            URL = "jdbc:mysql://localhost:3306/bibliotheque?useSSL=false&serverTimezone=UTC";
            USER = "root";
            PASSWORD = "francois12";
        }
    }

    // methode pour obtenir une connexion
    public static Connection getConnection(){
       try{
           return DriverManager.getConnection(URL, USER, PASSWORD);
       } catch (SQLException e) {
          System.err.println("[DbConnection] Erreur de connexion: " + e.getMessage());
          e.printStackTrace();
          return null;
       }
    }

}
