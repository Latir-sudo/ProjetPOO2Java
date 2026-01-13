package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Stage primaryStage; // la fenêtre principale de l'application




    public void start(Stage stage) throws Exception {
        primaryStage = stage;



        loadLoginInterface(); // on affiche d'abords l'interface de connexion
    }

    public static void loadLoginInterface() throws Exception {
        // Charge un fichier différent pour le login
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/org/example/view/authentification.fxml")); // <-- Changé ici
        Scene scene = new Scene(loader.load(), 800, 600);
        scene.getStylesheets().add(App.class.getResource("/org/example/css/Utilisateur.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Connexion - Bibliothèque");
        primaryStage.show();
    }

    // méthode pour charger l'interface principale
    public static void loadMainInterface() throws Exception {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/org/example/view/main.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 800);
        scene.getStylesheets().add(App.class.getResource("/org/example/css/Utilisateur.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gestion de Bibliothèque");
        primaryStage.setMaximized(true); // plein écran
        primaryStage.show();
    }

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> {
            System.err.println("[Uncaught] Exception in thread " + thread.getName() + ": " + ex.getMessage());
            ex.printStackTrace();
        });
        launch();
    }


    // Méthode utilitaire pour accéder à la stage primaire si besoin
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}



