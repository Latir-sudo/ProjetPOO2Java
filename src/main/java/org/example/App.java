package org.example;

import java.io.IOException;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/view/empruntFXML.fxml")));
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion des Emprunts");
            stage.show();
        } catch (Throwable t) {
            System.err.println("[App] Exception during start(): " + t.getMessage());
            t.printStackTrace();
            throw t;
        }
    }

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> {
            System.err.println("[Uncaught] Exception in thread " + thread.getName() + ": " + ex.getMessage());
            ex.printStackTrace();
        });
        launch();
    }
}
