package org.example.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import org.example.service.StatistiqueService;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.control.Label;

public class StatistiqueController {

    @FXML
    private VBox livresChartContainer;
    @FXML
    private VBox pieChartContainer;
    @FXML
    private VBox evolutionChartContainer;

    @FXML
    private Button btnRefresh;
    @FXML
    private Label lblInfoMessage;

    private BarChart<String, Number> livresChart;
    private PieChart usersPieChart;
    private BarChart<String, Number> evolutionChart;

    private final StatistiqueService statsService = new StatistiqueService();

    @FXML
    public void initialize() {
        // test de connexion pour les tables créées
        statsService.testConnexionEtTables();

        // création et ajout des graphiques dynamiquement
        createLivreChart();
        createPieChart();
        createEvolutionChart();

        // charger les données dans les graphiques respectifs
        loadChartData();

        // Initialiser les boutons
        setupButtons();
    }

    private void setupButtons() {
        // Configuration des actions des boutons
        btnRefresh.setOnAction(event -> handleRefresh());
        // Message d'information initial
        lblInfoMessage.setText("Données chargées - " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
    }

    @FXML
    private void handleRefresh() {
        try {
            // Désactiver le bouton pendant le rafraîchissement
            btnRefresh.setDisable(true);
            btnRefresh.setText("🔄 Chargement...");

            // Rafraîchir les données
            loadChartData();

            // Mettre à jour le message
            lblInfoMessage.setText("Données rafraîchies - " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

            // Réactiver le bouton après un délai
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(1), e -> {
                        btnRefresh.setText("🔄 Actualiser");
                        btnRefresh.setDisable(false);

                        // Afficher un message de succès
                        showAlert("Succès", "Données rafraîchies avec succès", Alert.AlertType.INFORMATION);
                    })
            );
            timeline.play();

        } catch (Exception e) {
            System.err.println("Erreur lors du rafraîchissement: " + e.getMessage());
            showAlert("Erreur", "Impossible de rafraîchir les données: " + e.getMessage(), Alert.AlertType.ERROR);
            btnRefresh.setText("🔄 Actualiser");
            btnRefresh.setDisable(false);
        }
    }
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void createLivreChart() {
        // créer les axes d'abord
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Livre");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Nombre d'emprunts");

        // créer le barchart
        livresChart = new BarChart<>(xAxis, yAxis);
        livresChart.setLegendVisible(false);
        livresChart.setPrefHeight(300);
        livresChart.setTitle("Livres les plus empruntés");
        livresChartContainer.getChildren().add(livresChart);
    }

    private void createPieChart() {
        // créer le PieChart
        usersPieChart = new PieChart();
        usersPieChart.setTitle("Répartition des utilisateurs");
        usersPieChart.setPrefHeight(300);
        usersPieChart.setLabelsVisible(true);
        usersPieChart.setLegendVisible(true);

        // ajouter le graphe au conteneur correspondant
        pieChartContainer.getChildren().add(usersPieChart);
    }

    private void createEvolutionChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Utilisateurs");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Nombre d'emprunts");

        evolutionChart = new BarChart<>(xAxis, yAxis);
        evolutionChart.setTitle("Top 10 des utilisateurs les plus actifs");
        evolutionChart.setLegendVisible(false);
        evolutionChart.setPrefHeight(300);
        evolutionChartContainer.getChildren().add(evolutionChart);
    }

    private void loadChartData() {
        // chargement des données dans les graphiques
        loadLivresData();
        loadUsersData();
        loadEvolutionData();
    }

    private void loadLivresData() {
        try {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Livres");

            ObservableList<XYChart.Data<String, Number>> livresData =
                    statsService.getLivresPlusEmpruntes(10);

            series.getData().addAll(livresData);

            livresChart.getData().clear();
            livresChart.getData().add(series);

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des données livres: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadUsersData() {
        try {
            usersPieChart.getData().clear();

            ObservableList<PieChart.Data> pieData = statsService.repartitionUtilisateurs();
            usersPieChart.getData().addAll(pieData);

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des données utilisateurs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadEvolutionData() {
        try {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Top 10 des utilisateurs les plus actifs");

            ObservableList<XYChart.Data<String, Number>> topData =
                    statsService.topUtilisateurs(10);

            series.getData().addAll(topData);

            evolutionChart.getData().clear();
            evolutionChart.getData().add(series);

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des données évolution: " + e.getMessage());
            e.printStackTrace();
        }
    }
}