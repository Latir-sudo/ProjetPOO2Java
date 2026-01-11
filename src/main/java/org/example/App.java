package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.stage.Stage;

public class App extends Application {

    private static Stage primaryStage; // la fenêtre principale de l'application

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        loadLoginInterface(); // on affiche d'abords l'interface de connexion

    }

    public static void loadLoginInterface() throws Exception {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/org/example/view/authentification.fxml"));
        Scene scene = new Scene(loader.load(),800,600);
        scene.getStylesheets().add(App.class.getResource("/org/example/css/Utilisateur.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // méthode pour charger l'interface principale

    public static void loadMainInterface() throws Exception {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/org/example/view/user.fxml"));
        Scene scene = new Scene(loader.load(),1000,800);
        scene.getStylesheets().add(App.class.getResource("/org/example/css/Utilisateur.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gestion des Utilisateurs");
        primaryStage.setMaximized(true); // plein écran .
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
