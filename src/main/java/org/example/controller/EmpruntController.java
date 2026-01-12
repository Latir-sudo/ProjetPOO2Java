package org.example.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.example.model.Emprunt;
import org.example.model.Livre;
import org.example.model.Utilisateur;
import org.example.service.EmpruntService;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EmpruntController {

    @FXML
    private Label titleLabel;

    @FXML
    private TextField searchField;

    @FXML
    private Button newEmpruntBtn;

    @FXML
    private Button returnEmpruntBtn;

    @FXML
    private TableView<Emprunt> empruntTable;

    @FXML
    private TableColumn<Emprunt, Integer> idColumn;

    @FXML
    private TableColumn<Emprunt, String> utilisateurColumn;

    @FXML
    private TableColumn<Emprunt, String> livreColumn;

    @FXML
    private TableColumn<Emprunt, LocalDate> dateEmpruntColumn;

    @FXML
    private TableColumn<Emprunt, LocalDate> retourPrevuColumn;

    @FXML
    private TableColumn<Emprunt, LocalDate> retourEffectifColumn;

    @FXML
    private TableColumn<Emprunt, String> penaliteColumn;

    @FXML
    private TableColumn<Emprunt, String> statutColumn;

    @FXML
    private TableColumn<Emprunt, Void> actionsColumn;

    private final EmpruntService empruntService = new EmpruntService();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        try {
            setupTableColumns();
            loadEmprunts();
            setupSearchField();
            setupButtons();
        } catch (Throwable t) {
            System.err.println("[EmpruntController] Exception during initialize(): " + t.getMessage());
            t.printStackTrace();
        }
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        utilisateurColumn.setCellValueFactory(new PropertyValueFactory<>("utilisateur"));
        livreColumn.setCellValueFactory(new PropertyValueFactory<>("livre"));

        dateEmpruntColumn.setCellValueFactory(new PropertyValueFactory<>("dateEmprunt"));
        dateEmpruntColumn.setCellFactory(column -> new TableCell<Emprunt, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : dateFormatter.format(item));
            }
        });

        retourPrevuColumn.setCellValueFactory(new PropertyValueFactory<>("retourPrevu"));
        retourPrevuColumn.setCellFactory(column -> new TableCell<Emprunt, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : dateFormatter.format(item));
            }
        });

        retourEffectifColumn.setCellValueFactory(new PropertyValueFactory<>("retourEffectif"));
        retourEffectifColumn.setCellFactory(column -> new TableCell<Emprunt, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Non retourné");
                } else {
                    setText(dateFormatter.format(item));
                }
            }
        });

        penaliteColumn.setCellValueFactory(new PropertyValueFactory<>("penalite"));
        penaliteColumn.setCellFactory(column -> new TableCell<Emprunt, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.equals("0")) {
                    setText("Aucune");
                } else {
                    setText(item);
                }
            }
        });

        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        statutColumn.setCellFactory(column -> new TableCell<Emprunt, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                    setStyle("");
                } else {
                    setText(item);
                    if ("Retourné".equals(item)) {
                        setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
                    } else if ("En retard".equals(item)) {
                        setStyle("-fx-text-fill: #FF9800; -fx-font-weight: bold;");
                    } else if ("En cours".equals(item)) {
                        setStyle("-fx-text-fill: #2196F3; -fx-font-weight: bold;");
                    }
                }
            }
        });

        setupActionsColumn();
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(param -> new TableCell<Emprunt, Void>() {
            private final Button detailBtn = new Button();

            {
                try {
                    // Try to load PNG image from resources
                    java.io.InputStream imageStream = getClass().getResourceAsStream("/images/details.png");
                    if (imageStream != null) {
                        ImageView imageView = new ImageView(new Image(imageStream));
                        imageView.setFitWidth(16);
                        imageView.setFitHeight(16);
                        imageView.setPreserveRatio(true);
                        detailBtn.setGraphic(imageView);
                    } else {
                        // Fallback: use emoji if image not found
                        detailBtn.setText("👁");
                    }
                } catch (Exception e) {
                    // Fallback to emoji if image loading fails
                    detailBtn.setText("👁");
                }

                detailBtn.setStyle(
                    "-fx-background-color: black;" +
                    "-fx-text-fill: white;" +
                    "-fx-padding: 6;" +
                    "-fx-background-radius: 50%;" +
                    "-fx-cursor: hand;"
                );

                detailBtn.setOnAction(event -> {
                    int idx = getIndex();
                    if (idx < 0) return;
                    TableView<Emprunt> tv = getTableView();
                    if (tv == null || idx >= tv.getItems().size()) return;
                    Emprunt emprunt = tv.getItems().get(idx);
                    if (emprunt != null) {
                        showEmpruntDetails(emprunt);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    int idx = getIndex();
                    TableView<Emprunt> tv = getTableView();
                    if (tv != null && idx >= 0 && idx < tv.getItems().size()) {
                        setGraphic(detailBtn);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
    }

    private void setupSearchField() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                loadEmprunts();
            } else {
                searchEmprunts(newValue);
            }
        });
    }

    private void setupButtons() {
        newEmpruntBtn.setOnAction(event -> openNewEmpruntDialog());
        returnEmpruntBtn.setOnAction(event -> openReturnEmpruntDialog());
    }

    private void loadEmprunts() {
        List<Emprunt> emprunts = empruntService.obtenirTousLesEmprunts();
        ObservableList<Emprunt> observableEmprunts = FXCollections.observableArrayList(emprunts);
        empruntTable.setItems(observableEmprunts);
    }

    private void searchEmprunts(String terme) {
        List<Emprunt> emprunts = empruntService.rechercherEmprunts(terme);
        ObservableList<Emprunt> observableEmprunts = FXCollections.observableArrayList(emprunts);
        empruntTable.setItems(observableEmprunts);
    }

    private void showEmpruntDetails(Emprunt emprunt) {
        javafx.scene.control.Dialog<Boolean> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Modifier Emprunt");
        dialog.setHeaderText("Modification de l'Emprunt #" + emprunt.getId());

        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Read-only fields
        TextField utilisateurField = new TextField(emprunt.getUtilisateur());
        utilisateurField.setEditable(false);
        TextField matriculeField = new TextField(emprunt.getMatricule());
        matriculeField.setEditable(false);
        TextField livreField = new TextField(emprunt.getLivre());
        livreField.setEditable(false);

        // Editable date fields
        javafx.scene.control.DatePicker dateEmpruntPicker = new javafx.scene.control.DatePicker(emprunt.getDateEmprunt());
        javafx.scene.control.DatePicker retourPrevuPicker = new javafx.scene.control.DatePicker(emprunt.getRetourPrevu());
        javafx.scene.control.DatePicker retourEffectifPicker = new javafx.scene.control.DatePicker(emprunt.getRetourEffectif());

        // Editable penalty field
        TextField penaliteField = new TextField(emprunt.getPenalite() == null || emprunt.getPenalite().equals("0") ? "" : emprunt.getPenalite());
        
        // Status (read-only)
        TextField statutField = new TextField(emprunt.getStatut());
        statutField.setEditable(false);

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        grid.add(new Label("Utilisateur:"), 0, 0);
        grid.add(utilisateurField, 1, 0);
        grid.add(new Label("Matricule:"), 0, 1);
        grid.add(matriculeField, 1, 1);
        grid.add(new Label("Livre:"), 0, 2);
        grid.add(livreField, 1, 2);
        grid.add(new Label("Date emprunt:"), 0, 3);
        grid.add(dateEmpruntPicker, 1, 3);
        grid.add(new Label("Retour prévu:"), 0, 4);
        grid.add(retourPrevuPicker, 1, 4);
        grid.add(new Label("Retour effectif:"), 0, 5);
        grid.add(retourEffectifPicker, 1, 5);
        grid.add(new Label("Pénalité:"), 0, 6);
        grid.add(penaliteField, 1, 6);
        grid.add(new Label("Statut:"), 0, 7);
        grid.add(statutField, 1, 7);

        dialog.getDialogPane().setContent(grid);

        java.util.Optional<Boolean> result = dialog.showAndWait();
        if (result.isPresent() && result.get()) {
            // Save modifications
            String penalite = penaliteField.getText().trim().isEmpty() ? "0" : penaliteField.getText().trim();
            boolean success = empruntService.mettreAJourEmprunt(
                emprunt.getId(),
                dateEmpruntPicker.getValue(),
                retourPrevuPicker.getValue(),
                retourEffectifPicker.getValue(),
                penalite
            );
            
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText("Modifications enregistrées");
                alert.setContentText("L'emprunt a été mis à jour avec succès.");
                alert.showAndWait();
                loadEmprunts();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur lors de la sauvegarde");
                alert.setContentText("Une erreur est survenue lors de la mise à jour de l'emprunt.");
                alert.showAndWait();
            }
        }
    }

    private void openNewEmpruntDialog() {
        javafx.scene.control.Dialog<java.util.Optional<Boolean>> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Nouvel Emprunt");
        dialog.setHeaderText("Créer un nouvel emprunt");

        ButtonType okButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        javafx.scene.control.ComboBox<Utilisateur> utilisateurCombo = new javafx.scene.control.ComboBox<>();
        javafx.scene.control.ComboBox<Livre> livreCombo = new javafx.scene.control.ComboBox<>();
        javafx.scene.control.DatePicker retourPrevuPicker = new javafx.scene.control.DatePicker();

        java.util.List<Utilisateur> utilisateurs = empruntService.obtenirUtilisateurs();
        java.util.List<Livre> livres = empruntService.obtenirLivresDisponibles();
        
        System.out.println("[EmpruntController] Utilisateurs charges: " + utilisateurs.size());
        System.out.println("[EmpruntController] Livres disponibles charges: " + livres.size());
        
        utilisateurCombo.getItems().addAll(utilisateurs);
        livreCombo.getItems().addAll(livres);

        utilisateurCombo.setPromptText("Selectionner un utilisateur");
        livreCombo.setPromptText("Selectionner un livre disponible");
        retourPrevuPicker.setPromptText("Date retour prevu");
        
        utilisateurCombo.setStyle("-fx-pref-width: 300;");
        livreCombo.setStyle("-fx-pref-width: 300;");

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        grid.add(new Label("Utilisateur:"), 0, 0);
        grid.add(utilisateurCombo, 1, 0);
        grid.add(new Label("Livre:"), 0, 1);
        grid.add(livreCombo, 1, 1);
        grid.add(new Label("Retour prévu:"), 0, 2);
        grid.add(retourPrevuPicker, 1, 2);

        dialog.getDialogPane().setContent(grid);

        javafx.scene.Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);

        utilisateurCombo.valueProperty().addListener((obs, oldV, newV) -> {
            okButton.setDisable(utilisateurCombo.getValue() == null || livreCombo.getValue() == null || retourPrevuPicker.getValue() == null);
        });
        livreCombo.valueProperty().addListener((obs, oldV, newV) -> {
            okButton.setDisable(utilisateurCombo.getValue() == null || livreCombo.getValue() == null || retourPrevuPicker.getValue() == null);
        });
        retourPrevuPicker.valueProperty().addListener((obs, oldV, newV) -> {
            okButton.setDisable(utilisateurCombo.getValue() == null || livreCombo.getValue() == null || retourPrevuPicker.getValue() == null);
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return java.util.Optional.of(true);
            }
            return java.util.Optional.empty();
        });

        java.util.Optional<Boolean> res = dialog.showAndWait().orElse(java.util.Optional.empty());

        if (res.isPresent() && res.get()) {
            Utilisateur u = utilisateurCombo.getValue();
            Livre l = livreCombo.getValue();
            java.time.LocalDate retour = retourPrevuPicker.getValue();

            boolean success = empruntService.creerEmprunt(u.getId(), l.getId(), retour);
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText("Emprunt créé");
                alert.setContentText("Le nouvel emprunt a été enregistré avec succès.");
                alert.showAndWait();
                loadEmprunts();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Échec");
                alert.setContentText("Impossible d'enregistrer l'emprunt.");
                alert.showAndWait();
            }
        }
    }

    private void openReturnEmpruntDialog() {
        Emprunt selectedEmprunt = empruntTable.getSelectionModel().getSelectedItem();
        if (selectedEmprunt == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText("Sélection requise");
            alert.setContentText("Veuillez sélectionner un emprunt à retourner");
            alert.showAndWait();
            return;
        }

        if ("Retourné".equals(selectedEmprunt.getStatut())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Emprunt déjà retourné");
            alert.setContentText("Cet emprunt a déjà été retourné");
            alert.showAndWait();
            return;
        }

        javafx.scene.control.Dialog<javafx.util.Pair<String, java.time.LocalDate>> dialog = 
            new javafx.scene.control.Dialog<>();
        dialog.setTitle("Enregistrer le retour");
        dialog.setHeaderText("Retour du livre: " + selectedEmprunt.getLivre());

        ButtonType okButtonType = new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        javafx.scene.control.DatePicker dateRetourPicker = new javafx.scene.control.DatePicker();
        dateRetourPicker.setValue(java.time.LocalDate.now());
        dateRetourPicker.setPromptText("Date de retour");

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        grid.add(new Label("Livre: "), 0, 0);
        grid.add(new Label(selectedEmprunt.getLivre()), 1, 0);
        grid.add(new Label("Utilisateur: "), 0, 1);
        grid.add(new Label(selectedEmprunt.getUtilisateur()), 1, 1);
        grid.add(new Label("Date de retour: "), 0, 2);
        grid.add(dateRetourPicker, 1, 2);

        dialog.getDialogPane().setContent(grid);

        java.util.Optional<javafx.util.Pair<String, java.time.LocalDate>> result = dialog.showAndWait();

        if (result.isPresent()) {
            boolean success = empruntService.mettreAJourRetour(selectedEmprunt.getId(), dateRetourPicker.getValue());
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText("Retour enregistré");
                alert.setContentText("Le retour a été enregistré avec succès");
                alert.showAndWait();
                loadEmprunts();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur lors de l'enregistrement");
                alert.setContentText("Une erreur est survenue lors de l'enregistrement du retour");
                alert.showAndWait();
            }
        }
    }
}
