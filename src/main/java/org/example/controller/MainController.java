package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Objects;

public class MainController {

    @FXML
    private StackPane contentPane;

    @FXML
    private Button btnUtilisateurs;

    @FXML
    private Button btnEmprunts;

    @FXML
    private Button btnLivres;

    @FXML
    public void initialize() {
        System.out.println("[MainController] Initialisation du contrôleur principal");

        // Configuration des boutons de navigation
        btnUtilisateurs.setOnAction(event -> loadPage("utilisateurs"));
        btnEmprunts.setOnAction(event -> loadPage("emprunts"));
        btnLivres.setOnAction(event -> loadPage("livres"));

        // Charger la page utilisateurs par défaut
        loadPage("utilisateurs");
    }

    private void loadPage(String page) {
        try {
            System.out.println("[MainController] Chargement de la page: " + page);

            String fxmlFile = switch (page) {
                case "utilisateurs" -> "/org/example/view/user.fxml";
                case "emprunts" -> "/org/example/view/empruntFXML.fxml";
                case "livres" -> "/org/example/view/livres.fxml";
                default -> "/org/example/view/user.fxml";
            };

            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlFile)));
            Node node = loader.load();

            contentPane.getChildren().clear();
            contentPane.getChildren().add(node);

            // Mettre à jour le style des boutons
            updateButtonStyles(page);

            System.out.println("[MainController] ✓ Page " + page + " chargée avec succès");
        } catch (IOException e) {
            System.err.println("[MainController] Erreur lors du chargement de la page " + page);
            e.printStackTrace();
        }
    }

    private void updateButtonStyles(String activePage) {
        String activeStyle = "-fx-padding: 10 20; -fx-font-size: 12; -fx-background-color: #C85A17; -fx-text-fill: white; -fx-cursor: hand; -fx-border-radius: 5; -fx-background-radius: 5;";
        String inactiveStyle = "-fx-padding: 10 20; -fx-font-size: 12; -fx-background-color: #666; -fx-text-fill: white; -fx-cursor: hand; -fx-border-radius: 5; -fx-background-radius: 5;";

        btnUtilisateurs.setStyle(activePage.equals("utilisateurs") ? activeStyle : inactiveStyle);
        btnEmprunts.setStyle(activePage.equals("emprunts") ? activeStyle : inactiveStyle);
        btnLivres.setStyle(activePage.equals("livres") ? activeStyle : inactiveStyle);
    }
}
