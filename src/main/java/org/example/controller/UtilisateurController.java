package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.example.model.Utilisateur;
import org.example.service.UtlisateurService;
import org.kordamp.ikonli.javafx.FontIcon;

import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class UtilisateurController {

    @FXML
    private TableView<Utilisateur> tableau;

    @FXML
    private TableColumn<Utilisateur, Integer> id;
    @FXML
    private TableColumn<Utilisateur, String> nom;
    @FXML
    private TableColumn<Utilisateur, String> prenom;
    @FXML
    private TableColumn<Utilisateur, String> matricule;
    @FXML
    private TableColumn<Utilisateur, String> typeUtilisateur;
    
    @FXML
    private TableColumn<Utilisateur, Void> action;

    private final UtlisateurService utilisateurService = new UtlisateurService();

    @FXML
    public void initialize() {
        // Liaison colonnes → propriétés du modèle
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        matricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        typeUtilisateur.setCellValueFactory(new PropertyValueFactory<>("typeUtilisateur"));

        // Ajouter les boutons par ligne
        addButtonsToTable();

        // Charger les utilisateurs depuis le service
        refreshTable();
    }

    private void refreshTable() {
        List<Utilisateur> utilisateurs = utilisateurService.getAllUsers();
        ObservableList<Utilisateur> obsList = FXCollections.observableArrayList(utilisateurs);
        tableau.setItems(obsList);
    }

    private void addButtonsToTable() {
        Callback<TableColumn<Utilisateur, Void>, TableCell<Utilisateur, Void>> cellFactory = col -> new TableCell<Utilisateur, Void>() {

            private final Button btnModifier = new Button();
            private final Button btnSupprimer = new Button();
            private final HBox pane = new HBox(5, btnModifier, btnSupprimer);

            {
                // Bouton Modifier
                
                

                btnModifier.getStyleClass().add("btn-edit");
                btnModifier.setOnAction(e -> {
                    Utilisateur user = getTableView().getItems().get(getIndex());
                    if (user != null) {
                        System.out.println("Modifier : " + user.getNom());
                        // Appeler ta méthode de modification ici
                    }
                });

                // Bouton Supprimer
              
               
               
                btnSupprimer.getStyleClass().add("btn-delete");
                btnSupprimer.setOnAction(e -> {
                    Utilisateur user = getTableView().getItems().get(getIndex());
                    if (user != null) {
                        utilisateurService.deleteUser(user.getId());
                        getTableView().getItems().remove(user);
                        System.out.println("Supprimer : " + user.getNom());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        };

        action.setCellFactory(cellFactory);
    }
}
