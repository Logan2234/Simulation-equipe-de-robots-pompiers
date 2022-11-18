package Tests;

import java.awt.Color;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import Autre.CalculPCC;
import Autre.Chemin;
import Donnees.DonneesSimulation;
import Donnees.Incendie;
import Donnees.Robot.Robot;
import Evenements.Simulateur;
import Exceptions.NoPathAvailableException;
import gui.GUISimulator;
import io.LecteurDonnees;


/**
 * Test qui a pour but de voir si la méthode CalculPCC.dijkstra fonctionne.
 * Si la map carteSujet est choisie, on souhaite que le robot à pattes aille sur l'incendie en bas
 * à gauche en évitant l'eau mais en traversant les rochers
 */
public class TestDijkstra {
    
    /** 
     * Va initialiser et lancer la simulation
     * 
     * @param fichier : fichier avec les données à tester
     * @param gui : gui
     */
    public static void initialize(String fichier, GUISimulator gui) {
        try {
            // Initialisation de la simulation
            DonneesSimulation donnees = LecteurDonnees.creerSimulation(fichier);
            Simulateur simulateur = new Simulateur();
            new Simulation(gui, donnees, simulateur, Test.TEST_DIJKSTRA, fichier);

            // On va sélectionner un robot aléatoire (sur la carteSujet, il s'agira du robot à pattes).
            Robot robot = donnees.getRobots().get(2);
            // On veut récupérer dans ce test le 1er incendie (sur la carteSujet, il s'agira de celui en bas à gauche)
            Incendie incendie = donnees.getIncendies().iterator().next();

            // On calcule le chemin et on lance le déplacement
            Chemin chemin = CalculPCC.dijkstra(donnees.getCarte(), robot.getPosition(), incendie.getPosition(), robot,
                    robot.getLastDate());
            chemin.creerEvenements(simulateur, robot);

        } catch (FileNotFoundException e) {
            System.out.println("fichier " + fichier + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + fichier + " invalide: " + e.getMessage());
        } catch (NoPathAvailableException e) {
            System.out.println(e);
        }
    }

    

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestDijkstra <nomDeFichier>");
            System.exit(1);
        }
        // crée la fenêtre graphique dans laquelle dessiner
        GUISimulator gui = new GUISimulator(800, 600, Color.BLACK);
        gui.setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 200,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 150);

        initialize(args[0], gui);
    }
}