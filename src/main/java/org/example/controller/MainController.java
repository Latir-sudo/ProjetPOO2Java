package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane rootPane;

    // Méthode pour changer le centre
    public void setCenter(String fxml) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/org/example/view/" + fxml));
            rootPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {

        // charger le sidebar et récupérer son controller

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/sidebar.fxml"));
            Parent sidebar = loader.load();
            rootPane.setLeft(sidebar);
            SidebarController sidebarController = loader.getController();
            sidebarController.setMainController(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
