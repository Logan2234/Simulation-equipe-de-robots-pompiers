import java.io.*;
import java.util.*;
import java.util.zip.DataFormatException;

public class LecteurDonnees {
    private static Scanner scanner;

    public DonneesSimulation creerSimulation(String fichierDonnees)
            throws FileNotFoundException, DataFormatException {
        scanner = new Scanner(new File(fichierDonnees));
        scanner.useLocale(Locale.US);

        // Création de la carte
        int[] tailles = lireCarte();

        Carte carte = new Carte(tailles[0], tailles[1]);
        carte.setTailleCases(tailles[2]);

        Case[][] tab_cases = new Case[tailles[0]][tailles[1]];

        for (int lig = 0; lig < tailles[0]; lig++) {
            for (int col = 0; col < tailles[1]; col++) {
                tab_cases[lig][col] = lireCase(lig, col);
            }
        }

        carte.setTab_cases(tab_cases);

        // Création du tableau des incendies
        int nbIncendies = lireIncendies();
        Incendie[] incendies = new Incendie[nbIncendies];

        for (int i = 0; i < nbIncendies; i++) {
            incendies[i] = lireIncendie(i);
        }

        // Création du tableau des robots
        int nbRobots = lireRobots();
        Robot[] robots = new Robot[nbRobots];

        for (int i = 0; i < nbRobots; i++) {
            robots[i] = lireRobot(i);
        }

        return new DonneesSimulation(carte, incendies, robots);
    }

    /**
     * Lit et affiche les donnees de la carte.
     * 
     * @throws ExceptionFormatDonnees
     */
    private int[] lireCarte() throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbLignes = scanner.nextInt();
            int nbColonnes = scanner.nextInt();
            int tailleCases = scanner.nextInt(); // en m

            int[] tailles = { nbLignes, nbColonnes, tailleCases };
            return tailles;

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbLignes nbColonnes tailleCases");
        }
        // une ExceptionFormat levee depuis lireCase est remontee telle quelle
    }

    /**
     * Lit et affiche les donnees d'une case.
     */
    private Case lireCase(int lig, int col) throws DataFormatException {
        ignorerCommentaires();

        try {
            NatureTerrain nature = NatureTerrain.valueOf(scanner.next());

            verifieLigneTerminee();

            return new Case(nature, lig, col);

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format de case invalide. "
                    + "Attendu: nature altitude [valeur_specifique]");
        }
    }

    /**
     * Lit et affiche les donnees des incendies.
     */
    private int lireIncendies() throws DataFormatException {
        ignorerCommentaires();
        try {
            return scanner.nextInt();

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbIncendies");
        }
    }

    /**
     * Lit et affiche les donnees du i-eme incendie.
     * 
     * @param i
     */
    private Incendie lireIncendie(int i) throws DataFormatException {
        ignorerCommentaires();

        try {
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            int intensite = scanner.nextInt();
            if (intensite <= 0) {
                throw new DataFormatException("incendie " + i
                        + "nb litres pour eteindre doit etre > 0");
            }

            verifieLigneTerminee();

            return new Incendie(new Case(NatureTerrain.FORET, lig, col), intensite);

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format d'incendie invalide. "
                    + "Attendu: ligne colonne intensite");
        }
    }

    /**
     * Lit et affiche les donnees des robots.
     */
    private int lireRobots() throws DataFormatException {
        ignorerCommentaires();
        try {
            return scanner.nextInt();

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbRobots");
        }
    }

    /**
     * Lit et affiche les donnees du i-eme robot.
     * 
     * @param i
     */
    private Robot lireRobot(int i) throws DataFormatException {
        ignorerCommentaires();

        try {
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            String type = scanner.next();

            // lecture eventuelle d'une vitesse du robot (entier)
            String s = scanner.findInLine("(\\d+)"); // 1 or more digit(s) ?
            // pour lire un flottant: ("(\\d+(\\.\\d+)?)");
            
            int vitesse = 0;
            if (s != null) {
                vitesse = Integer.parseInt(s);
            }

            verifieLigneTerminee();

            switch (type) {
                case "DRONES":
                    if (vitesse != 0):
                        return new RobotDrone(lig, col, vitesse);
                    return new RobotDrone(lig, col);
                    break;
                case "ROUES":
                    return new RobotRoues(lig, col);
                    break;
                case "PATTES":
                    return new RobotPattes(lig, col);
                    break;
                case "CHENILLES":
                    return new RobotChenilles(lig, col);
                    break;
                default:
                    throw new DataFormatException("Un robot ne peut avoir qu'un des types suivant: DRONE, ROUES, PATTES, CHENILLES");
                    break;
            }

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format de robot invalide. "
                    + "Attendu: ligne colonne type [valeur_specifique]");
        }
    }

    /** Ignore toute (fin de) ligne commencant par '#' */
    private void ignorerCommentaires() {
        while (scanner.hasNext("#.*")) {
            scanner.nextLine();
        }
    }

    /**
     * Verifie qu'il n'y a plus rien a lire sur cette ligne (int ou float).
     * 
     * @throws ExceptionFormatDonnees
     */
    private void verifieLigneTerminee() throws DataFormatException {
        if (scanner.findInLine("(\\d+)") != null) {
            throw new DataFormatException("format invalide, donnees en trop.");
        }
    }
}
