package Tests;

import java.awt.Color;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import Chefs.ChefAvance;
import Donnees.DonneesSimulation;
import Evenements.Simulateur;
import gui.GUISimulator;
import io.LecteurDonnees;

/**
 * Test qui a pour but de tester le chef pompier avancé
 */
public class TestAvance {
    
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
            ChefAvance chef = new ChefAvance(donnees, simulateur);
            new Simulation(gui, donnees, simulateur, EnumTest.TEST_BASIQUE, fichier, chef);

        } catch (FileNotFoundException e) {
            System.out.println("fichier " + fichier + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + fichier + " invalide: " + e.getMessage());
        }
    }

    
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestSimulation <nomDeFichier>");
            System.exit(1);
        }
        // crée la fenêtre graphique dans laquelle dessiner
        GUISimulator gui = new GUISimulator(800, 600, Color.BLACK);
        gui.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 100, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 50);

        initialize(args[0], gui);
    }

}
