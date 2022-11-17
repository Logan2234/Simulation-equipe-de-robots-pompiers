package Tests;

import java.awt.Color;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import Chefs.ChefBasique;
import Donnees.DonneesSimulation;
import Evenements.Simulateur;
import Exceptions.NoMoreFireException;
import gui.GUISimulator;
import io.LecteurDonnees;

public class TestBasique {

    public static void initialize(String fichier, GUISimulator gui) {
        try {
            LecteurDonnees lecteur = new LecteurDonnees();
            DonneesSimulation donnees = lecteur.creerSimulation(fichier);
            Simulateur simulateur = new Simulateur();
            ChefBasique chef = new ChefBasique(donnees, simulateur);
            Simulation simulation = new Simulation(gui, donnees, simulateur, Test.TestBasique, fichier);
            
            chef.strategie();

        } catch (FileNotFoundException e) {
            System.out.println("fichier " + fichier + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + fichier + " invalide: " + e.getMessage());
        } catch (NoMoreFireException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        if (args.length < 0) {
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
