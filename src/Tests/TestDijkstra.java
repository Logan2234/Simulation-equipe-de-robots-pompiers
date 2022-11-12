package Tests;

import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;
import Exceptions.IllegalCheminRobotException;
import Exceptions.CaseOutOfMapException;

import Autre.*;
import Donnees.*;
import Donnees.Robot.*;
import Evenements.*;

import java.awt.Color;

import gui.GUISimulator;
import gui.Simulable;
import io.Dessin;

public class TestDijkstra {
    public static void initialize(String fichier, GUISimulator gui) {
        try {
            LecteurDonnees lecteur = new LecteurDonnees();
            DonneesSimulation donnees = lecteur.creerSimulation(fichier);
            Simulateur simulateur = new Simulateur();
            Simulation simulation = new Simulation(gui, donnees, simulateur, Test.TestSimulation, fichier);
            CalculPCC calculateur = new CalculPCC(donnees, simulateur);

            Carte carte = donnees.getCarte();
            Robot robot = donnees.getRobots()[0];
            Incendie incendie = donnees.getIncendies().getFirst();
            Chemin chemin = new Chemin();
            Case pos = robot.getPosition();
            long date = 0;

            chemin = calculateur.dijkstra(robot.getPosition(), incendie.getPosition(), robot);
            chemin.creerEvenements(simulateur, robot);

            simulateur.ajouteEvenement(new EventRemplir(date, robot));

            date += (long) (robot.getTmpRemplissage() * 1 - robot.getReservoir() / robot.getCapacite());

        } catch (IllegalCheminRobotException e) {
            System.out.println(e);
        } catch (FileNotFoundException e) {
            System.out.println("fichier " + fichier + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + fichier + " invalide: " + e.getMessage());
        } catch (CaseOutOfMapException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestSimulation <nomDeFichier>");
            System.exit(1);
        }
        // crée la fenêtre graphique dans laquelle dessiner
        GUISimulator gui = new GUISimulator(800, 600, Color.BLACK);

        initialize(args[0], gui);
    }
}