package org.example.controller;

import org.example.model.Livre;
import org.example.service.LivreService;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FormulaireLivreController {
    
    @FXML private TextField titreField;
    @FXML private TextField auteurField;
    @FXML private TextField isbnField;
    @FXML private TextField quantiteField;
    @FXML private TextField disponiblesField;
    @FXML private ComboBox<String> statutCombo;
    
    private Livre livre;
    private LivreService livreService = new LivreService();
    private LivreController parentController;
    
    @FXML
    public void initialize() {

  
        statutCombo.getItems().addAll("Disponible", "Indisponible", "Réservé", "En réparation");
        statutCombo.setValue("Disponible");
        quantiteField.setText("1");
        disponiblesField.setText("1");
    }
    
    public void setLivre(Livre livre) {
        this.livre = livre;
        if (livre != null) {
            titreField.setText(livre.getTitre());
            auteurField.setText(livre.getAuteur());
            isbnField.setText(livre.getIsbn());
            quantiteField.setText(String.valueOf(livre.getQuantiteTotale()));
            disponiblesField.setText(String.valueOf(livre.getDisponibles()));
            statutCombo.setValue(livre.getStatut());
        }
    }
    
    public void setParentController(LivreController parentController) {
        this.parentController = parentController;
    }
    
    @FXML
    private void enregistrer() {
        try {
            
            if (titreField.getText().trim().isEmpty()) {
                showAlert("Erreur", "Le titre est obligatoire", AlertType.ERROR);
                return;
            }
            
            if (auteurField.getText().trim().isEmpty()) {
                showAlert("Erreur", "L'auteur est obligatoire", AlertType.ERROR);
                return;
            }
            
            String titre = titreField.getText().trim();
            String auteur = auteurField.getText().trim();
            String isbn = isbnField.getText().trim();
            int quantite = Integer.parseInt(quantiteField.getText().trim());
            int disponibles = Integer.parseInt(disponiblesField.getText().trim());
            String statut = statutCombo.getValue();
            
            
            if (quantite < 0) {
                showAlert("Erreur", "La quantité totale doit être positive", AlertType.ERROR);
                return;
            }
            
            if (disponibles < 0 || disponibles > quantite) {
                showAlert("Erreur", 
                    "Le nombre de disponibles doit être entre 0 et " + quantite, 
                    AlertType.ERROR);
                return;
            }
            
            boolean success;
            
            if (livre == null) {
               
                Livre nouveauLivre = new Livre(titre, auteur, isbn, quantite, disponibles, statut);
                success = livreService.addLivre(nouveauLivre);
            } else {
             
                livre.setTitre(titre);
                livre.setAuteur(auteur);
                livre.setIsbn(isbn);
                livre.setQuantiteTotale(quantite);
                livre.setDisponibles(disponibles);
                livre.setStatut(statut);
                success = livreService.updateLivre(livre);
            }
            
            if (success) {
                showAlert("Succès", 
                    livre == null ? "Livre ajouté avec succès" : "Livre modifié avec succès", 
                    AlertType.INFORMATION);
                
                
                fermerFenetre();
                
             
                if (parentController != null) {
                    parentController.rafraichirTableau();
                }
            } else {
                showAlert("Erreur", 
                    livre == null ? "Échec de l'ajout du livre" : "Échec de la modification", 
                    AlertType.ERROR);
            }
            
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des nombres valides pour les quantités", AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue: " + e.getMessage(), AlertType.ERROR);
        }
    }
    
    @FXML
    private void annuler() {
        fermerFenetre();
    }
    
    private void fermerFenetre() {
        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.close();
    }
    
    private void showAlert(String title, String content, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}