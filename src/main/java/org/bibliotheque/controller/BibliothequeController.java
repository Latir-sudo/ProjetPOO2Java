package org.bibliotheque.controller;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.bibliotheque.dao.DatabaseConnection;
import org.bibliotheque.dao.EmpruntDAO;
import org.bibliotheque.dao.LivreDAO;
import org.bibliotheque.dao.StatistiquesDAO;
import org.bibliotheque.dao.UtilisateurDAO;
import org.bibliotheque.model.Emprunt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class BibliothequeController implements Initializable {
    
    // Composants FXML
    @FXML private Label lblNbUtilisateurs;
    @FXML private Label lblNbLivres;
    @FXML private Label lblNbEmprunts;
    @FXML private Label lblNbRetards;
    
    @FXML private TableView<Emprunt> tableEmprunts;
    @FXML private TableColumn<Emprunt, String> colUtilisateur;
    @FXML private TableColumn<Emprunt, String> colLivre;
    @FXML private TableColumn<Emprunt, String> colDateEmprunt;
    @FXML private TableColumn<Emprunt, String> colRetourPrevu;
    @FXML private TableColumn<Emprunt, String> colStatut;
    
    // DAO
    private UtilisateurDAO utilisateurDAO;
    private LivreDAO livreDAO;
    private EmpruntDAO empruntDAO;
    private StatistiquesDAO statistiquesDAO;
    
    // Données
    private ObservableList<Emprunt> listeEmprunts = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("========================================");
        System.out.println("   🎓 BIBLIOTHÈQUE - DÉMARRAGE");
        System.out.println("========================================\n");
        
        testConnexion();
        initDAOs();
        initTableau();
        chargerDonnees();
        
        System.out.println("\n========================================");
        System.out.println("   ✅ APPLICATION PRÊTE");
        System.out.println("========================================\n");
    }
    
    private void testConnexion() {
        System.out.println("🔌 Test de connexion MySQL...");
        DatabaseConnection dbConn = DatabaseConnection.getInstance();
        
        if (dbConn.testConnection()) {
            System.out.println("✅ Connexion MySQL réussie\n");
        } else {
            System.err.println("❌ ERREUR: Connexion MySQL échouée\n");
        }
    }
    
    private void initDAOs() {
        System.out.println("🔧 Initialisation des DAO...");
        try {
            utilisateurDAO = new UtilisateurDAO();
            livreDAO = new LivreDAO();
            empruntDAO = new EmpruntDAO();
            statistiquesDAO = new StatistiquesDAO();
            System.out.println("✅ Tous les DAO initialisés\n");
        } catch (Exception e) {
            System.err.println("❌ Erreur initialisation DAO: " + e.getMessage() + "\n");
        }
    }
    
    private void initTableau() {
        System.out.println("📊 Configuration du tableau...");
        
        if (tableEmprunts == null) {
            System.err.println("⚠️  TableView non injectée\n");
            return;
        }
        
        colUtilisateur.setCellValueFactory(cellData -> 
            cellData.getValue().utilisateurProperty());
        
        colLivre.setCellValueFactory(cellData -> 
            cellData.getValue().livreProperty());
        
        colDateEmprunt.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getDateEmpruntString()));
        
        colRetourPrevu.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getDateRetourPrevueString()));
        
        colStatut.setCellValueFactory(cellData -> 
            cellData.getValue().statutProperty());
        
        tableEmprunts.setItems(listeEmprunts);
        
        System.out.println("✅ Tableau configuré\n");
    }
    
    private void chargerDonnees() {
        System.out.println("📥 Chargement des données...");
        chargerStatistiques();
        chargerEmprunts();
        System.out.println("✅ Données chargées\n");
    }
    
    private void chargerStatistiques() {
        try {
            Map<String, Integer> stats = statistiquesDAO.getStatistiquesGlobales();
            
            if (lblNbUtilisateurs != null) {
                lblNbUtilisateurs.setText(String.valueOf(stats.get("utilisateurs")));
            }
            if (lblNbLivres != null) {
                lblNbLivres.setText(String.valueOf(stats.get("livres")));
            }
            if (lblNbEmprunts != null) {
                lblNbEmprunts.setText(String.valueOf(stats.get("emprunts_en_cours")));
            }
            if (lblNbRetards != null) {
                lblNbRetards.setText(String.valueOf(stats.get("emprunts_en_retard")));
            }
            
        } catch (Exception e) {
            System.err.println("Erreur chargement statistiques: " + e.getMessage());
        }
    }
    
    private void chargerEmprunts() {
        try {
            listeEmprunts.clear();
            List<Emprunt> emprunts = empruntDAO.getEmpruntsRecents(10);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}