package Tests;

import java.awt.Color;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import Chefs.ChefBasique;
import Donnees.DonneesSimulation;
import Evenements.Simulateur;
import gui.GUISimulator;
import io.LecteurDonnees;

/**
 * Test qui a pour but de tester le chef pompier basique.
 */
public class TestBasique {

    /** 
     * Va initialiser et lancer la simulation
     * 
     * @param fichier : fichier avec les données à tester
     * @param gui : gui
     */
    public static void initialize(String fichier, GUISimulator gui) {
        try {
            // Initialisation de simulation
            DonneesSimulation donnees = LecteurDonnees.creerSimulation(fichier);
            Simulateur simulateur = new Simulateur();
            ChefBasique chef = new ChefBasique(donnees, simulateur);
            // Lancement de la simulation avec le chef basique
            new Simulation(gui, donnees, simulateur, Test.TEST_BASIQUE, fichier, chef);
        } catch (FileNotFoundException e) {
            System.out.println("fichier " + fichier + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + fichier + " invalide: " + e.getMessage());
        }
    }

    
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestBasique <nomDeFichier>");
            System.exit(1);
        }
        // crée la fenêtre graphique dans laquelle dessiner
        GUISimulator gui = new GUISimulator(800, 600, Color.BLACK);
        gui.setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 200,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 150);

        initialize(args[0], gui);
    }

}
