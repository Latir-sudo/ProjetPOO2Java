package org.example.service;

<<<<<<< HEAD
=======
package org.example.service;
>>>>>>> f3fd123f6ae0c72c1816b8ae390ec4ea80478b29

import org.example.model.Livre;


public class TestLivreService {
    public static void main(String[] args) {
        LivreService service = new LivreService();
        
        System.out.println("=== TEST LIVRE SERVICE ===");
        
        //1 Teste de l'ajout
        System.out.println("\n1. Test ajout livre:");
        Livre livre = new Livre("Test Java", "Auteur Test", "978-1234567890", 10, 8, "Disponible");
        boolean ajoutReussi = service.addLivre(livre);
        System.out.println("Ajout réussi: " + ajoutReussi);
        
        //2 teste de la récupération
        System.out.println("\n2. Liste des livres:");
        service.getAllLivres().forEach(System.out::println);
        
        //3 teste de la recherche
        System.out.println("\n3. Recherche 'Java':");
        service.rechercherLivres("Java").forEach(System.out::println);
        
        //4 teste des statistiques
        System.out.println("\n4. Statistiques:");
        System.out.println("Total livres: " + service.getTotalLivres());
        System.out.println("Livres disponibles: " + service.getLivresDisponibles());
        
        //5 teste de la mise à jour
        System.out.println("\n5. Test modification:");
        Livre livreAModifier = service.getLivreByTitre("Test Java").get(0);
        if (livreAModifier != null) {
            livreAModifier.setTitre("Test Java Modifié");
            livreAModifier.setDisponibles(5);
            boolean modificationReussie = service.updateLivre(livreAModifier);
            System.out.println("Modification réussi: " + modificationReussie);
        }
        
        //6 teste de la suppression
        System.out.println("\n6. Test suppression:");
        Livre livreASupprimer = service.getLivreByTitre("Test Java Modifié").get(0);
        if (livreASupprimer != null) {
            boolean suppressionReussie = service.deleteLivre(livreASupprimer.getId());
            System.out.println("Suppression réussi: " + suppressionReussie);
        }
        
        System.out.println("\n Tests terminés!");
    }
}