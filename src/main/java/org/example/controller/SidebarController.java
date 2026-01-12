package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SidebarController {
    private MainController mainController;


    // injection du maincontroller

    public void setMainController(MainController mainController) {
    this.mainController = mainController;
    }


    @FXML private Button btnDashboard;
    @FXML private Button btnGestionUsers;
    @FXML private Button btnGestionLivres;
    @FXML private Button btnGestionEmprunts;
    @FXML private Button btnStatistique;

    public void initialize(){
        btnDashboard.setOnAction(event -> mainController.setCenter("accueil.fxml"));
        btnGestionUsers.setOnAction(event -> mainController.setCenter("user.fxml"));
        btnGestionLivres.setOnAction(event -> mainController.setCenter("sample.fxml"));
        btnGestionEmprunts.setOnAction(event -> mainController.setCenter("emprunts.fxml"));

    }
}