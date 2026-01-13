CREATE TABLE utilisateurs (
    id_utilisateur INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    matricule VARCHAR(20) NOT NULL UNIQUE,
    type_utilisateur VARCHAR(20) NOT NULL,
    CHECK (type_utilisateur IN ('ETUDIANT', 'ENSEIGNANT'))
);




INSERT INTO utilisateurs (nom, prenom, matricule, type_utilisateur) VALUES
('Diop', 'Aliou', 'MAT001', 'ETUDIANT'),
('Ndiaye', 'Fatou', 'MAT002', 'ETUDIANT'),
('Fall', 'Moussa', 'MAT003', 'ETUDIANT'),
('Ba', 'Aminata', 'MAT004', 'ETUDIANT'),
('Sarr', 'Cheikh', 'MAT005', 'ETUDIANT'),
('Kane', 'Abdou', 'MAT006', 'ENSEIGNANT'),
('Sy', 'Mariam', 'MAT007', 'ENSEIGNANT'),
('Diallo', 'Ousmane', 'MAT008', 'ENSEIGNANT'),
('Faye', 'Awa', 'MAT009', 'ENSEIGNANT'),
('Ndoye', 'Ibrahima', 'MAT010', 'ENSEIGNANT');




CREATE TABLE livres (
    id_livre INT AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(100) NOT NULL,
    auteur VARCHAR(100) NOT NULL,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    quantite_totale INT NOT NULL CHECK (quantite_totale >= 0),
    quantite_disponible INT NOT NULL CHECK (quantite_disponible >= 0)
);




INSERT INTO livres (titre, auteur, isbn, quantite_totale, quantite_disponible) VALUES
('Programmation Java', 'Deitel', 'ISBN001', 10, 7),
('Algorithmique', 'Cormen', 'ISBN002', 8, 4),
('Base de données', 'Elmasri', 'ISBN003', 6, 2),
('Réseaux', 'Tanenbaum', 'ISBN004', 5, 3),
('Systèmes d’exploitation', 'Silberschatz', 'ISBN005', 7, 5),
('UML et conception', 'Booch', 'ISBN006', 4, 1),
('Python avancé', 'Mark Lutz', 'ISBN007', 9, 6),
('C++ moderne', 'Stroustrup', 'ISBN008', 5, 2),
('JavaFX', 'Oracle', 'ISBN009', 6, 4),
('Sécurité informatique', 'Stallings', 'ISBN010', 3, 1);



CREATE TABLE emprunts (
    id_emprunt INT AUTO_INCREMENT PRIMARY KEY,
    id_utilisateur INT NOT NULL,
    id_livre INT NOT NULL,
    date_emprunt DATE NOT NULL,
    date_retour_prevue DATE NOT NULL,
    date_retour_effective DATE,
    penalite INT DEFAULT 0,

    FOREIGN KEY (id_utilisateur) REFERENCES utilisateurs(id_utilisateur),
    FOREIGN KEY (id_livre) REFERENCES livres(id_livre)
);

INSERT INTO emprunts 
(utilisateur, livre, date_emprunt, date_retour_prevue, date_retour_effective, penalite) 
VALUES
(2, 1, '2026-01-01', '2026-01-15', NULL, 5),
(3, 2, '2026-01-02', '2026-01-16', NULL, 3),
(4, 3, '2026-01-03', '2026-01-17', '2026-01-18', 1),
(5, 4, '2026-01-04', '2026-01-18', NULL, 2),
(6, 5, '2026-01-05', '2026-01-19', '2026-01-19', 0),
(7, 6, '2026-01-06', '2026-01-20', NULL, 4),
(2, 7, '2026-01-07', '2026-01-21', '2026-01-22', 1),
(3, 8, '2026-01-08', '2026-01-22', NULL, 6),
(4, 9, '2026-01-09', '2026-01-23', '2026-01-23', 0),
(5, 10,'2026-01-10', '2026-01-24', NULL, 7);