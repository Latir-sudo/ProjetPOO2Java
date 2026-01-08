package org.example;

import org.example.model.Utilisateur;
import org.example.service.UtlisateurService;

public class TestService {

    public static void main(String[] args) {

        UtlisateurService service = new UtlisateurService();

         Utilisateur user = new Utilisateur(
                2,
                "Ndiaye",
                "Fatou",
                "ETU002",
                "ETUDIANT"
        );

        service.addUser(user);
        System.out.println("Utilisateur ajouté !");

        service.deleteUser(2);

    }
}
