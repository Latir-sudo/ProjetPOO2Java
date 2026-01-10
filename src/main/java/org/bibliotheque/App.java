package org.bibliotheque;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Charger le FXML
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/accueil.fxml")
            );
            
            Parent root = loader.load();
            
            // Créer la scène
            Scene scene = new Scene(root, 1400, 800);
            
            // Configurer la fenêtre
            primaryStage.setTitle("Bibliothèque UGB - Gestion");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1200);
            primaryStage.setMinHeight(700);
            
            // Afficher
            primaryStage.show();
            
            System.out.println("✅ Application démarrée avec succès");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur démarrage: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}