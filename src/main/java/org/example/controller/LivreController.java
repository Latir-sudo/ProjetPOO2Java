package org.example.controller;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.example.model.Livre;
import org.example.service.LivreService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LivreController implements Initializable {

    @FXML private TableView<Livre> livresTable;
    @FXML private TableColumn<Livre, Integer> idColumn;
    @FXML private TableColumn<Livre, String> titreColumn;
    @FXML private TableColumn<Livre, String> auteurColumn;
    @FXML private TableColumn<Livre, String> isbnColumn;
    @FXML private TableColumn<Livre, Integer> quantiteColumn;
    @FXML private TableColumn<Livre, Integer> disponiblesColumn;
    @FXML private TableColumn<Livre, String> statutColumn;
    @FXML private TableColumn<Livre, Void> actionColumn;
    @FXML private TextField txtRecherche;


    private ObservableList<Livre> livresData = FXCollections.observableArrayList();
    private LivreService livreService = new LivreService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurerTableau();
        configurerColonneActions();
        chargerLivres();
    }

    private void configurerTableau() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        auteurColumn.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantiteTotale"));
        disponiblesColumn.setCellValueFactory(new PropertyValueFactory<>("disponibles"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        livresTable.setItems(livresData);
    }

    private void chargerLivres() {
        livresData.setAll(livreService.getAllLivres());
    }


    @FXML
    private void Rechercher() {
        String keyword = txtRecherche.getText().trim();
        System.out.println("Recherche : " + keyword);
        if (keyword.isEmpty()) {
            chargerLivres();
        } else {
        livresData.setAll(livreService.rechercherLivres(keyword));
        }
    }


    @FXML
    private void Ajouter() {
        ouvrirFormulaire(null);
    }

    private void ouvrirFormulaire(Livre livre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FX1/view/formulaireLivre.fxml"));
            Parent root = loader.load();

            FormulaireLivreController controller = loader.getController();
            controller.setLivre(livre);
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(livre == null ? "Ajouter un livre" : "Modifier un livre");
            stage.showAndWait();

        } catch (IOException e) {
            showAlert("Erreur", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void supprimerLivre(Livre livre) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Supprimer ce livre ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            livreService.deleteLivre(livre.getId());
            chargerLivres();
        }
    }

    public void rafraichirTableau() {
        chargerLivres();
    }

    private void showAlert(String t, String c, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setTitle(t);
        a.setContentText(c);
        a.showAndWait();
    }

    private void configurerColonneActions() {
        actionColumn.setCellFactory(col -> new TableCell<Livre, Void>() {

            private final Button btnEdit = new Button();
            private final Button btnDelete = new Button();
            private final HBox box = new HBox(5);

            {
                ImageView editIcon = new ImageView(
                        new Image(getClass().getResourceAsStream("/FX1/images/edit.png")));
                editIcon.setFitWidth(42);
                editIcon.setFitHeight(42);
                editIcon.setPreserveRatio(true);

                ImageView deleteIcon = new ImageView(
                        new Image(getClass().getResourceAsStream("/FX1/images/delete.png")));
                deleteIcon.setFitWidth(42);
                deleteIcon.setFitHeight(42);
                deleteIcon.setPreserveRatio(true);

                btnEdit.setGraphic(editIcon);
                btnDelete.setGraphic(deleteIcon);

                btnEdit.getStyleClass().add("btn-action");
                btnDelete.getStyleClass().add("btn-action");

                box.getChildren().addAll(btnEdit, btnDelete);
                box.setStyle("-fx-alignment: center;");

                btnEdit.setOnAction(e -> {
                    Livre livre = getTableView().getItems().get(getIndex());
                    if (livre != null) ouvrirFormulaire(livre);
                });

                btnDelete.setOnAction(e -> {
                    Livre livre = getTableView().getItems().get(getIndex());
                    if (livre != null) supprimerLivre(livre);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }
}
