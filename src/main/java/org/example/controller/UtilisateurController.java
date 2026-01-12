package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.example.model.Utilisateur;
import org.example.service.UtlisateurService;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @FXML
    private TextField searchField;

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
        // ma fonction pour la recherche d'utilisateur

        setupSearch();
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

                Image image = new Image(
                        getClass().getResourceAsStream("/org/example/image/delete.png")
                );

                ImageView icon = new ImageView(image);
                icon.setFitWidth(11);
                icon.setFitHeight(11);
                icon.setPreserveRatio(true);
                btnModifier.setGraphic(icon);
                btnModifier.getStyleClass().add("btn-edit");
                btnModifier.setOnAction(e -> {
                    Utilisateur user = getTableView().getItems().get(getIndex());
                    if (user != null) {
                        System.out.println("Modifier : " + user.getNom());
                        // Appeler ta méthode de modification ici
                        updateButton(user.getIdUtilisateur());
                    }
                });


                Image imageDelete = new Image(
                        getClass().getResourceAsStream("/org/example/image/modifier.png")
                );

                ImageView iconDelete = new ImageView(imageDelete);
                iconDelete.setFitWidth(11);
                iconDelete.setFitHeight(11);
                iconDelete.setPreserveRatio(true);
                // Bouton Supprimer
                btnSupprimer.setGraphic(iconDelete);
                btnSupprimer.getStyleClass().add("btn-delete");
                btnSupprimer.setOnAction(e -> {
                    Utilisateur user = getTableView().getItems().get(getIndex());
                    if (user != null) {
                        utilisateurService.deleteUser(user.getIdUtilisateur());
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

    private void setupSearch() {
        // pour ecouter les changements dans le champ de recherche

        searchField.textProperty().addListener((observable, oldvalue, newvalue) -> {
            filterTable(newvalue);
        });
    }

    private void filterTable(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            tableau.setItems(FXCollections.observableArrayList(utilisateurService.getAllUsers()));
            return;
        }

        String lowerCaseFilter = searchText.toLowerCase();
        List<Utilisateur> filteredList = utilisateurService.getAllUsers().stream().filter(
                user ->
                        user.getNom().toLowerCase().contains(lowerCaseFilter) ||
                                user.getPrenom().toLowerCase().contains(lowerCaseFilter) ||
                                user.getMatricule().toLowerCase().contains(lowerCaseFilter)
        ).collect(Collectors.toList());

        tableau.setItems(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    private void handleAddButton() {
        showAddUserDialog();
    }

    private void showAddUserDialog() {
        // création d'une fenetre de dialoue simple

        Dialog<Utilisateur> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un utilisateur");

        // boutons
        ButtonType addButton = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // formulaire

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nomField = new TextField();
        nomField.setPromptText("Nom");
        TextField prenomField = new TextField();
        prenomField.setPromptText("Prenom");
        TextField matriculeField = new TextField();
        matriculeField.setPromptText("Matricule");

        // pour le type d'utilisateur , il doit choisir entre ETUDIANT ET PROFESSEUR

        ComboBox<String> typeUtilisateur = new ComboBox<>();
        typeUtilisateur.getItems().addAll("ETUDIANT", "PROFESSEUR");
        typeUtilisateur.setPromptText("TypeUtilisateur");

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Prenom:"), 0, 1);
        grid.add(prenomField, 1, 1);
        grid.add(new Label("Matricule:"), 0, 2);
        grid.add(matriculeField, 1, 2);
        grid.add(new Label("TypeUtilisateur:"), 0, 3);
        grid.add(typeUtilisateur, 1, 3);
        dialog.getDialogPane().setContent(grid);

        Node addButtonNode = dialog.getDialogPane().lookupButton(addButton);
        addButtonNode.setDisable(true);

        Button ajouter = (Button) addButtonNode;
        ajouter.getStyleClass().add("btn-ajout");

        // Activer le bouton quand tous les champs sont remplis

        Runnable checkFields = () -> {
            boolean allField = !nomField.getText().isEmpty() && !prenomField.getText().isEmpty()
                    && !matriculeField.getText().isEmpty() && !typeUtilisateur.getItems().isEmpty();
            addButtonNode.setDisable(!allField);
        };

        nomField.textProperty().addListener((observable, oldValue, newValue) -> checkFields.run());
        prenomField.textProperty().addListener((observable, oldValue, newValue) -> checkFields.run());
        matriculeField.textProperty().addListener((observable, oldValue, newValue) -> checkFields.run());

        dialog.setResultConverter(buttonType -> {
            if (buttonType == addButton) {
                return new Utilisateur(
                        0,
                        nomField.getText(),
                        prenomField.getText(), matriculeField.getText(),
                        typeUtilisateur.getValue()
                );
            }
            return null;
        });

        // Ajouter l'utilisateur

        Optional<Utilisateur> result = dialog.showAndWait();
        result.ifPresent(user -> {
            utilisateurService.addUser(user);
            refreshTable(); // mettre à jour la tableview
        });
    }

    @FXML
    private void updateButton(int id) {
        showUpdateUserDialog(id);
    }

    private void showUpdateUserDialog(int id) {
        // RÉCUPÉRER L'UTILISATEUR ACTUEL D'ABORD
        Utilisateur utilisateurActuel = utilisateurService.getUserById(id);
        if (utilisateurActuel == null) {
            showAlert("Erreur", "Utilisateur non trouvé", Alert.AlertType.ERROR);
            return;
        }

        // Création du Dialog
        Dialog<Utilisateur> dialog = new Dialog<>();
        dialog.setTitle("Modifier l'utilisateur");

        // Charger le CSS
        dialog.getDialogPane().getStylesheets().add(
                getClass().getResource("/org/example/css/Utilisateur.css").toExternalForm()
        );

        // Boutons
        ButtonType updateButtonType = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        // Formulaire PRÉ-REMPLI avec les données actuelles
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nomField = new TextField(utilisateurActuel.getNom());
        nomField.setPromptText("Nom");

        TextField prenomField = new TextField(utilisateurActuel.getPrenom());
        prenomField.setPromptText("Prénom");

        TextField matriculeField = new TextField(utilisateurActuel.getMatricule());
        matriculeField.setPromptText("Matricule");

        ComboBox<String> typeUtilisateur = new ComboBox<>();
        typeUtilisateur.getItems().addAll("ETUDIANT", "ENSEIGNANT");
        typeUtilisateur.setValue(utilisateurActuel.getTypeUtilisateur()); // Pré-sélectionner

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Prénom:"), 0, 1);
        grid.add(prenomField, 1, 1);
        grid.add(new Label("Matricule:"), 0, 2);
        grid.add(matriculeField, 1, 2);
        grid.add(new Label("Type:"), 0, 3);
        grid.add(typeUtilisateur, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Récupérer le bouton Modifier APRÈS l'ajout
        Node updateButtonNode = dialog.getDialogPane().lookupButton(updateButtonType);

        // Appliquer le style CSS
        if (updateButtonNode instanceof Button) {
            ((Button) updateButtonNode).getStyleClass().add("update");
        }

        // Désactiver le bouton si champs vides (mais normalement déjà remplis)
        updateButtonNode.setDisable(false); // Désactivé par défaut

        // Validation en temps réel
        Runnable checkFields = () -> {
            boolean allFieldsValid = !nomField.getText().trim().isEmpty() &&
                    !prenomField.getText().trim().isEmpty() &&
                    !matriculeField.getText().trim().isEmpty() &&
                    typeUtilisateur.getValue() != null;
            updateButtonNode.setDisable(!allFieldsValid);
        };

        // Ajouter les écouteurs
        nomField.textProperty().addListener((obs, old, newVal) -> checkFields.run());
        prenomField.textProperty().addListener((obs, old, newVal) -> checkFields.run());
        matriculeField.textProperty().addListener((obs, old, newVal) -> checkFields.run());
        typeUtilisateur.valueProperty().addListener((obs, old, newVal) -> checkFields.run());

        // Vérifier immédiatement (car champs pré-remplis)
        checkFields.run();

        // Convertir le résultat
        dialog.setResultConverter(buttonType -> {
            if (buttonType == updateButtonType) {
                // Validation finale
                if (nomField.getText().trim().isEmpty() ||
                        prenomField.getText().trim().isEmpty() ||
                        matriculeField.getText().trim().isEmpty() ||
                        typeUtilisateur.getValue() == null) {
                    return null;
                }

                return new Utilisateur(
                        id, // Garder le même ID
                        nomField.getText(),
                        prenomField.getText(),
                        matriculeField.getText(),
                        typeUtilisateur.getValue()
                );
            }
            return null;
        });

        // Traiter le résultat
        Optional<Utilisateur> result = dialog.showAndWait();
        result.ifPresent(updatedUser -> {
            boolean success = utilisateurService.updateUser(updatedUser);
            if (success) {
                refreshTable();
                showAlert("Succès", "Utilisateur modifié avec succès", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erreur", "Échec de la modification", Alert.AlertType.ERROR);
            }
        });
    }

    // Méthode helper pour les alertes
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}