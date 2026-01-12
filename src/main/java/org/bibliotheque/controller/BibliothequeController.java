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
import javafx.scene.control.cell.PropertyValueFactory;

public class BibliothequeController implements Initializable {

    // Composants FXML
    @FXML private Label lblNbUtilisateurs;
    @FXML private Label lblNbLivres;
    @FXML private Label lblNbEmprunts;
    @FXML private Label lblNbRetards;
    @FXML private Label lbNbEnseignants;
    @FXML private Label lbEtudiants;
    @FXML private Label lbLivresDisponibles;
    @FXML private Label lbNbLivresARetouner;
    @FXML private Label lbPenalite;
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

        // Configuration des colonnes AVEC LES BONS NOMS D'ATTRIBUTS
        // IMPORTANT: Ces noms doivent correspondre aux getters de la classe Emprunt

        // Pour afficher "Nom Prénom" (nomUtilisateur dans EmpruntDAO)
        colUtilisateur.setCellValueFactory(new PropertyValueFactory<>("nomUtilisateur"));

        // Pour afficher le titre du livre
        colLivre.setCellValueFactory(new PropertyValueFactory<>("titreLivre"));

        // Pour afficher la date d'emprunt (format LocalDate)
        colDateEmprunt.setCellValueFactory(new PropertyValueFactory<>("dateEmprunt"));

        // Pour afficher la date de retour prévue (format LocalDate)
        colRetourPrevu.setCellValueFactory(new PropertyValueFactory<>("dateRetourPrevue"));

        // Pour afficher le statut (calculé dans EmpruntDAO)
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

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

            if (lblNbUtilisateurs != null && stats.containsKey("utilisateurs")) {
                lblNbUtilisateurs.setText(String.valueOf(stats.get("utilisateurs")));
            }
            if (lblNbLivres != null && stats.containsKey("livres")) {
                lblNbLivres.setText(String.valueOf(stats.get("livres")));
            }
            if (lbLivresDisponibles != null && stats.containsKey("livres_disponibles")) {
                lbLivresDisponibles.setText(String.valueOf(stats.get("livres_disponibles")));
            }

            if (lbNbLivresARetouner != null && stats.containsKey("emprunts_en_cours")) {
                lbNbLivresARetouner.setText(String.valueOf(stats.get("emprunts_en_cours")));
            }

            if (lbPenalite!= null && stats.containsKey("penalite")) {
                lbPenalite.setText(String.valueOf(stats.get("penalite")));
            }


            if (lbNbEnseignants != null && stats.containsKey("enseignants")) {
                lbNbEnseignants.setText(String.valueOf(stats.get("enseignants")+" enseignants"));
            }

            if (lbEtudiants != null && stats.containsKey("etudiants")) {
                lbEtudiants.setText(String.valueOf(stats.get("etudiants")+" enseignants"));
            }

            if (lblNbEmprunts != null && stats.containsKey("emprunts_en_cours")) {
                lblNbEmprunts.setText(String.valueOf(stats.get("emprunts_en_cours")));
            }
            if (lblNbRetards != null && stats.containsKey("emprunts_en_retard")) {
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

            if (emprunts != null) {
                listeEmprunts.addAll(emprunts);
                System.out.println("✅ " + emprunts.size() + " emprunts chargés");
            } else {
                System.out.println("⚠️  Aucun emprunt trouvé");
            }

        } catch (Exception e) {
            System.err.println("Erreur chargement emprunts: " + e.getMessage());
            e.printStackTrace();
        }
    }
}