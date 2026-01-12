package org.example.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.model.EmpruntEnRetard;
import org.example.service.StatistiqueService;

import java.net.URL;
import java.util.ResourceBundle;

public class StatistiqueController implements Initializable {

    @FXML private VBox livresChartContainer;
    @FXML private VBox pieChartContainer;
    @FXML private VBox evolutionChartContainer;
    @FXML private TableView<EmpruntEnRetard> tableEmpruntsRetard;
    @FXML private Label lblInfoMessage;
    @FXML private Button btnRefresh;
    @FXML private Label lblCount;

    private StatistiqueService statistiqueService = new StatistiqueService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("=== INITIALISATION STATISTIQUES ===");
        
        try {
            // Initialisation de la table
            initialiserTableView();
            
            // Chargement de toutes les données
            chargerToutesDonnees();
            
            System.out.println("=== INITIALISATION TERMINÉE ===");
            
        } catch (Exception e) {
            System.err.println("ERREUR lors de l'initialisation: " + e.getMessage());
            e.printStackTrace();
            lblInfoMessage.setText("Erreur lors du chargement des données!");
            lblInfoMessage.setStyle("-fx-text-fill: red;");
        }
    }

    private void initialiserTableView() {
        System.out.println("Initialisation de la TableView...");
        
        if (tableEmpruntsRetard == null) {
            System.err.println("ERREUR: TableView est null!");
            return;
        }
        
        // Vider les colonnes existantes
        tableEmpruntsRetard.getColumns().clear();
        
        // Créer les colonnes avec PropertyValueFactory
        TableColumn<EmpruntEnRetard, Integer> idCol = new TableColumn<>("ID");
        idCol.setPrefWidth(60);
        idCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));
        idCol.setStyle("-fx-alignment: CENTER;");
        
        TableColumn<EmpruntEnRetard, String> utilisateurCol = new TableColumn<>("Utilisateur");
        utilisateurCol.setPrefWidth(200);
        utilisateurCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("utilisateur"));
        
        TableColumn<EmpruntEnRetard, String> livreCol = new TableColumn<>("Livre");
        livreCol.setPrefWidth(180);
        livreCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("livre"));
        
        TableColumn<EmpruntEnRetard, String> dateEmpruntCol = new TableColumn<>("Date Emprunt");
        dateEmpruntCol.setPrefWidth(120);
        dateEmpruntCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateEmprunt"));
        dateEmpruntCol.setStyle("-fx-alignment: CENTER;");
        
        TableColumn<EmpruntEnRetard, String> dateRetourCol = new TableColumn<>("Retour Prévu");
        dateRetourCol.setPrefWidth(120);
        dateRetourCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateRetourPrevue"));
        dateRetourCol.setStyle("-fx-alignment: CENTER;");
        
        TableColumn<EmpruntEnRetard, Integer> joursRetardCol = new TableColumn<>("Jours Retard");
        joursRetardCol.setPrefWidth(100);
        joursRetardCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("joursRetard"));
        joursRetardCol.setStyle("-fx-alignment: CENTER;");
        
        TableColumn<EmpruntEnRetard, Double> penaliteCol = new TableColumn<>("Pénalité (€)");
        penaliteCol.setPrefWidth(120);
        penaliteCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("penalite"));
        
        // Formater la colonne Pénalité
        penaliteCol.setCellFactory(column -> new TableCell<EmpruntEnRetard, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.format("%.2f €", item));
                    setStyle("-fx-alignment: CENTER_RIGHT; -fx-font-weight: bold;");
                }
            }
        });
        
        // Ajouter toutes les colonnes
        tableEmpruntsRetard.getColumns().addAll(
            idCol, utilisateurCol, livreCol, dateEmpruntCol,
            dateRetourCol, joursRetardCol, penaliteCol
        );
        
        System.out.println("TableView initialisée avec " + tableEmpruntsRetard.getColumns().size() + " colonnes");
    }

    private void chargerToutesDonnees() {
        System.out.println("Chargement des données...");
        
        // Charger les emprunts en retard
        chargerEmpruntsRetard();
        
        // Charger les graphiques
        chargerGraphiqueLivres();
        chargerGraphiqueUtilisateurs();
        chargerGraphiqueTopUtilisateurs();
        
        // Mettre à jour les statistiques générales
        mettreAJourMessageInfo();
    }

    private void chargerEmpruntsRetard() {
        System.out.println("Chargement des emprunts en retard...");
        
        try {
            ObservableList<EmpruntEnRetard> emprunts = statistiqueService.getEmpruntsEnRetardDetails();
            System.out.println("Nombre d'emprunts trouvés: " + emprunts.size());
            
            // Mettre à jour le compteur
            if (lblCount != null) {
                lblCount.setText("(" + emprunts.size() + " emprunts)");
            }
            
            // Définir les données
            tableEmpruntsRetard.setItems(emprunts);
            
            // Rafraîchir
            tableEmpruntsRetard.refresh();
            
            if (emprunts.isEmpty()) {
                System.out.println("Aucun emprunt en retard trouvé.");
            } else {
                // Afficher les 2 premiers pour vérifier
                for (int i = 0; i < Math.min(emprunts.size(), 2); i++) {
                    EmpruntEnRetard e = emprunts.get(i);
                    System.out.printf("  Exemple %d: ID=%d, User=%s%n", i+1, e.getId(), e.getUtilisateur());
                }
            }
            
        } catch (Exception e) {
            System.err.println("ERREUR lors du chargement des emprunts: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void chargerGraphiqueLivres() {
        try {
            livresChartContainer.getChildren().clear();
            
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Livres");
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Nombre d'emprunts");
            
            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            barChart.setTitle("Top 5 des livres les plus empruntés");
            barChart.setLegendVisible(false);
            
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Emprunts");
            
            ObservableList<XYChart.Data<String, Number>> data = statistiqueService.getLivresPlusEmpruntes(5);
            series.getData().addAll(data);
            
            barChart.getData().add(series);
            barChart.setPrefHeight(300);
            
            livresChartContainer.getChildren().add(barChart);
            
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du graphique livres: " + e.getMessage());
        }
    }

    private void chargerGraphiqueUtilisateurs() {
        try {
            pieChartContainer.getChildren().clear();
            
            PieChart pieChart = new PieChart();
            pieChart.setTitle("Répartition des utilisateurs");
            pieChart.setData(statistiqueService.repartitionUtilisateurs());
            pieChart.setLabelsVisible(true);
            pieChart.setLegendVisible(true);
            pieChart.setPrefHeight(300);
            
            pieChartContainer.getChildren().add(pieChart);
            
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du graphique utilisateurs: " + e.getMessage());
        }
    }

    private void chargerGraphiqueTopUtilisateurs() {
        try {
            evolutionChartContainer.getChildren().clear();
            
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Utilisateurs");
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Nombre d'emprunts");
            
            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            barChart.setTitle("Top 10 des utilisateurs les plus actifs");
            barChart.setLegendVisible(false);
            
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Emprunts");
            
            ObservableList<XYChart.Data<String, Number>> data = statistiqueService.topUtilisateurs(10);
            series.getData().addAll(data);
            
            barChart.getData().add(series);
            barChart.setPrefHeight(350);
            
            evolutionChartContainer.getChildren().add(barChart);
            
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du graphique top utilisateurs: " + e.getMessage());
        }
    }

    private void mettreAJourMessageInfo() {
        try {
            int totalLivres = statistiqueService.getTotalLivres();
            int totalUtilisateurs = statistiqueService.getTotalUtilisateurs();
            int totalEmprunts = statistiqueService.getTotalEmprunts();
            int empruntsRetard = statistiqueService.getEmpruntsEnRetard();
            
            String message = String.format(
                "Statistiques : %d livres • %d utilisateurs • %d emprunts • %d en retard",
                totalLivres, totalUtilisateurs, totalEmprunts, empruntsRetard
            );
            
            lblInfoMessage.setText(message);
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour des statistiques: " + e.getMessage());
            lblInfoMessage.setText("Erreur lors du chargement des statistiques");
        }
    }

    @FXML
    private void handleRefresh() {
        System.out.println("=== RAFRAICHISSEMENT DES DONNÉES ===");
        
        // Recharger toutes les données
        chargerToutesDonnees();
        
        lblInfoMessage.setText("Données actualisées avec succès !");
        lblInfoMessage.setStyle("-fx-text-fill: #2ecc71;");
        
        System.out.println("=== RAFRAICHISSEMENT TERMINÉ ===");
    }
}