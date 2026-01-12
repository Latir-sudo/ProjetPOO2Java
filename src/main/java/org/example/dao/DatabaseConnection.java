package org.example.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    
    private static DatabaseConnection instance;
    private Connection connection;
    private String url;
    private String username;
    private String password;
    
    private DatabaseConnection() {
        loadProperties();
    }
    
    private void loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("db.properties")) {
            
            if (input == null) {
                System.out.println("⚠️  db.properties introuvable, config par défaut");
                setDefaultConfig();
                return;
            }
            
            props.load(input);
            
            String host = props.getProperty("db.host", "localhost");
            String port = props.getProperty("db.port", "3306");
            String dbName = props.getProperty("db.name", "bibliotheque");
            
            this.url = String.format(
                "jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=UTC",
                host, port, dbName
            );
            
            this.username = props.getProperty("db.user", "root");
            this.password = props.getProperty("db.password", "francois12");
            
            System.out.println("✅ Configuration chargée depuis db.properties");
            
        } catch (IOException e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            setDefaultConfig();
        }
    }
    
    private void setDefaultConfig() {
        this.url = "jdbc:mysql://localhost:3306/bibliotheque?useSSL=false&serverTimezone=UTC";
        this.username = "root";
        this.password = "francois12";
        System.out.println("✅ Configuration par défaut");
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }
    
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("✅ Connexion MySQL établie");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL non trouvé", e);
            }
        }
        return connection;
    }
    
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("✅ Connexion fermée");
            } catch (SQLException e) {
                System.err.println("❌ Erreur fermeture: " + e.getMessage());
            }
        }
    }
    
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("❌ Test échoué: " + e.getMessage());
            return false;
        }
    }
}