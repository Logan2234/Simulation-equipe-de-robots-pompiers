package Tests;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import Donnees.ChefBasique;
import Donnees.ChefBasiqueV2;
import Donnees.DonneesSimulation;
import Donnees.LecteurDonnees;
import Evenements.Simulateur;
import Exceptions.EmptyRobotsException;
import gui.GUISimulator;

public class TestBasique {

    public static void initialize(String fichier, GUISimulator gui) {
        try {
            LecteurDonnees lecteur = new LecteurDonnees();
            DonneesSimulation donnees = lecteur.creerSimulation(fichier);
            Simulateur simulateur = new Simulateur();
            Simulation simulation = new Simulation(gui, donnees, simulateur, Test.TestBasique, fichier);

            // ChefBasique chef = new ChefBasique(donnees, simulateur);
            // chef.strategie();
            ChefBasiqueV2 chef = new ChefBasiqueV2(donnees, simulateur);
            chef.strategie();

        } catch (FileNotFoundException e) {
            System.out.println("fichier " + fichier + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + fichier + " invalide: " + e.getMessage());
        } 
        // catch (EmptyRobotsException e) {
        //     System.out.println(e);
        //}

    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestBasique <nomDeFichier>");
            System.exit(1);
        }
        // crée la fenêtre graphique dans laquelle dessiner
        GUISimulator gui = new GUISimulator(800, 600, Color.BLACK);

        initialize(args[0], gui);
    }

}
