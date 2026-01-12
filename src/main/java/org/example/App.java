package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Stage primaryStage; 

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("Système de Gestion de Bibliothèque");

        // Charge d'abord l'interface de connexion
        loadLoginInterface();
    }

    public static void loadLoginInterface() throws Exception {
        System.out.println("Chargement de l'interface de connexion...");
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/org/example/view/graphique.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);
        scene.getStylesheets().add(App.class.getResource("/org/example/css/statistique.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    // méthode pour charger l'interface principale
    public static void loadMainInterface() throws Exception {
        System.out.println("Chargement de l'interface principale...");
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/org/example/view/graphique.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 800);
        scene.getStylesheets().add(App.class.getResource("/org/example/css/statistique.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gestion des Utilisateurs");
        primaryStage.setMaximized(true); // plein écran
        primaryStage.show();
    }

    // méthode pour charger l'interface des statistiques
    public static void loadStatistiqueInterface() throws Exception {
        System.out.println("Chargement de l'interface statistique...");
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/org/example/view/statistique.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        
        // Essayer de charger le CSS statistique, sinon utiliser Utilisateur.css
        try {
            scene.getStylesheets().add(App.class.getResource("/org/example/css/statistique.css").toExternalForm());
        } catch (Exception e) {
            scene.getStylesheets().add(App.class.getResource("/org/example/css/Utilisateur.css").toExternalForm());
        }
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tableau de Bord - Statistiques");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void loadEmpruntInterface() throws Exception {
        System.out.println("Chargement de l'interface emprunt...");
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/org/example/view/emprunt.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        scene.getStylesheets().add(App.class.getResource("/org/example/css/Utilisateur.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gestion des Emprunts");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}