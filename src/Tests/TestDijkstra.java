package Tests;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import Autre.CalculPCC;
import Autre.Chemin;
import Donnees.DonneesSimulation;
import Donnees.Incendie;
import Donnees.LecteurDonnees;
import Donnees.Robot.Robot;
import Evenements.Simulateur;
import Exceptions.IllegalPathException;
import Exceptions.PasDeCheminException;
import gui.GUISimulator;

public class TestDijkstra {
    public static void initialize(String fichier, GUISimulator gui) {
        try {
            LecteurDonnees lecteur = new LecteurDonnees();
            DonneesSimulation donnees = lecteur.creerSimulation(fichier);
            Simulateur simulateur = new Simulateur();
            Simulation simulation = new Simulation(gui, donnees, simulateur, Test.TestDijkstra, fichier);
            CalculPCC calculateur = new CalculPCC(donnees);

            Robot robot = donnees.getRobots()[2];
            Incendie incendie = donnees.getIncendies().getFirst();
            Chemin chemin = new Chemin();

            chemin = calculateur.dijkstra(robot.getPosition(), incendie.getPosition(), robot, robot.getLastDate());
            System.out.println(chemin.toString());
            chemin.creerEvenements(simulateur, robot);

        } catch (IllegalPathException e) {
            System.out.println(e);
        } catch (FileNotFoundException e) {
            System.out.println("fichier " + fichier + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + fichier + " invalide: " + e.getMessage());
        } catch (PasDeCheminException e) {
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

        initialize(args[0], gui);
    }
}