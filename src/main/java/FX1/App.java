package FX1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("=== DÉMARRAGE APPLICATION BIBLIOTHÈQUE ===");
            
            // Test connexion BD
            System.out.println("Test de connexion à la base de données...");
            try {
                FX1.utilitaire.ConnexionBD.getConnexion();
                System.out.println("Connexion MySQL réussie");
            } catch (Exception e) {
                System.out.println("Connexion MySQL échouée: " + e.getMessage());
            }
            
            // Charger le FXML
            System.out.println("Chargement de l'interface...");
            Parent root;
            
            try {
                System.out.println(getClass().getResource("view/sample.fxml"));
                
                root = FXMLLoader.load(getClass().getResource("view/sample.fxml"));
                System.out.println("FXML chargé avec succès");
            } catch (Exception e) {
                System.out.println("Impossible de charger le FXML: " + e.getMessage());
                System.out.println("Création d'une interface de secours...");
                root = createFallbackUI();
            }
            
           
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("Biblio Univ - Gestion des Livres");
            primaryStage.setScene(scene);
            

            primaryStage.setMinWidth(635);
            primaryStage.setMinHeight(520);
            
            primaryStage.show();
            System.out.println("Application démarrée avec succès!");
            
        } catch (Exception e) {
            System.err.println(" ERREUR CRITIQUE: " + e.getMessage());
            e.printStackTrace();
            showErrorAlert(e);
        }
    }
    
    private Parent createFallbackUI() {
        javafx.scene.layout.VBox vbox = new javafx.scene.layout.VBox(20);
        vbox.setStyle("-fx-padding: 50; -fx-alignment: center; -fx-background-color: #f0f0f0;");
        
        javafx.scene.control.Label title = new javafx.scene.control.Label("Biblio Univ");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        javafx.scene.control.Label subtitle = new javafx.scene.control.Label("Gestion de Bibliothèque");
        subtitle.setStyle("-fx-font-size: 18px; -fx-text-fill: #7f8c8d;");
        
        javafx.scene.control.Label info = new javafx.scene.control.Label("Interface FXML non trouvée");
        info.setStyle("-fx-text-fill: #e74c3c;");
        
        javafx.scene.control.Button testBtn = new javafx.scene.control.Button("Tester la connexion BD");
        testBtn.setOnAction(e -> {
            try {
                FX1.utilitaire.ConnexionBD.getConnexion();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Connexion BD réussie!");
                alert.show();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erreur BD: " + ex.getMessage());
                alert.show();
            }
        });
        
        vbox.getChildren().addAll(title, subtitle, info, testBtn);
        return vbox;
    }
    
    private void showErrorAlert(Exception e) {
        try {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur d'application");
            alert.setHeaderText("Impossible de démarrer l'application");
            alert.setContentText("Erreur: " + e.getMessage() + 
                               "\n\nVérifiez que:\n" +
                               "1. sample.fxml est dans src/main/resources/FX1/view/\n" +
                               "2. Le contrôleur est correctement spécifié");
            alert.showAndWait();
        } catch (Exception ex) {
           
            System.err.println("Impossible d'afficher l'alerte: " + ex.getMessage());
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== LANCEMENT DE L'APPLICATION ===");
        launch(args);
    }
}