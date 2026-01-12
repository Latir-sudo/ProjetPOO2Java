package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class TestUtilisateursFX extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("[TestUtilisateursFX] Lancement de l'application JavaFX");
        
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/view/user.fxml")));
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Test - Gestion des Utilisateurs");
            primaryStage.show();
            
            System.out.println("[TestUtilisateursFX] Interface chargée avec succès");
        } catch (Exception e) {
            System.err.println("[TestUtilisateursFX] Erreur lors du chargement du FXML");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
