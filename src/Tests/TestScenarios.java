package Tests;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import Donnees.Direction;
import Donnees.DonneesSimulation;
import Donnees.LecteurDonnees;
import Donnees.Robot.Robot;
import Evenements.EventIntervenir;
import Evenements.EventMouvement;
import Evenements.EventRemplir;
import Evenements.Simulateur;
import gui.GUISimulator;

public class TestScenarios {
    public static void initialize(String fichier, GUISimulator gui) {
        try {
            LecteurDonnees lecteur = new LecteurDonnees();
            DonneesSimulation donnees = lecteur.creerSimulation(fichier);
            Simulateur simulateur = new Simulateur();
            Simulation simulation = new Simulation(gui, donnees, simulateur, Test.TestScenarios, fichier);
            Robot robot1 = donnees.getRobots()[0];
            Robot robot2 = donnees.getRobots()[1];

            simulateur.ajouteEvenement(new EventMouvement(0, robot1, Direction.NORD));
            simulateur.ajouteEvenement(new EventMouvement(1, robot1, Direction.NORD));
            simulateur.ajouteEvenement(new EventMouvement(2, robot1, Direction.NORD));
            simulateur.ajouteEvenement(new EventMouvement(3, robot1, Direction.NORD));

            simulateur.ajouteEvenement(new EventMouvement(6, robot2, Direction.NORD));
            simulateur.ajouteEvenement(new EventIntervenir(7, robot2, donnees.getIncendies()));
            simulateur.ajouteEvenement(new EventMouvement(8, robot2, Direction.OUEST));
            simulateur.ajouteEvenement(new EventMouvement(8, robot2, Direction.OUEST));
            simulateur.ajouteEvenement(new EventRemplir(9, robot2));
            simulateur.ajouteEvenement(new EventMouvement(10, robot2, Direction.EST));
            simulateur.ajouteEvenement(new EventMouvement(10, robot2, Direction.EST));
            simulateur.ajouteEvenement(new EventIntervenir(11, robot2, donnees.getIncendies()));
            simulateur.ajouteEvenement(new EventMouvement(12, robot2, Direction.OUEST));
        } catch (FileNotFoundException e) {
            System.out.println("fichier " + fichier + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + fichier + " invalide: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestScenarios <nomDeFichier>");
            System.exit(1);
        }
        // crée la fenêtre graphique dans laquelle dessiner
        GUISimulator gui = new GUISimulator(800, 600, Color.BLACK);

        initialize(args[0], gui);
    }

}