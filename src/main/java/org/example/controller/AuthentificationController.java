package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.App;
import org.example.service.AuthentificationService;

public class AuthentificationController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button cancelButton;
    @FXML private Label errorLabel;

    private final AuthentificationService authentificationService = new AuthentificationService();

    @FXML
    public void initialize(){
        // initialiser les composants

        errorLabel.setVisible(false);

        // configuration des actions

        loginButton.setOnAction(event -> handleLogin());
        cancelButton.setOnAction(event -> handleCancel());

        // ceci permet de pouvoir se connecter avec le bouton enrée

        usernameField.setOnAction(e-> handleLogin());
        passwordField.setOnAction(e-> handleLogin());

         // Focus sur le premier champ

        usernameField.requestFocus();

    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if(username.isEmpty() || password.isEmpty()){
             showErreur("Le mot de passe est obligatoire");
             return ;

        }

        errorLabel.setVisible(false);
        loginButton.setDisable(true); // je desactive le bouton pendant la vérification des informations

        if (authentificationService.auth(username, password)) {

            try{
                App.loadMainInterface();
            }catch(Exception e){
                showErreur("Erreur:"+ e.getMessage());
                loginButton.setDisable(false);
            }
        }

        else {
            showErreur("email ou mot de passe incorrecte!");
            loginButton.setDisable(false);
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }




    private void showErreur(String message){
        errorLabel.setVisible(true);
        errorLabel.setText(message);
    }
}
