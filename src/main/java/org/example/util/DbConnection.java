package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/bibliotheque?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "francois12";

    // methode pour obtenir une connexion
    public static Connection getConnection(){
       try{
           return DriverManager.getConnection(URL,USER,PASSWORD);
       } catch (SQLException e) {
          e.printStackTrace();
          return null;
       }
    }

}
