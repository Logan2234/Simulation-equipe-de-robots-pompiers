package Tests;

import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;
import Exceptions.NoFireException;
import Exceptions.IllegalCheminRobotException;

import Autre.*;
import Donnees.*;
import Donnees.Robot.*;
import Evenements.*;

import java.awt.Color;

import gui.GUISimulator;
import gui.Simulable;
import io.Dessin;

public class TestDijkstra {
    public static void main(String[] args){
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestLecteurDonnees <nomDeFichier>");
            System.exit(1);
        }

        DonneesSimulation donnees;

        try {
            LecteurDonnees lecteur = new LecteurDonnees();
            donnees = lecteur.creerSimulation(args[0]);

            // crée la fenêtre graphique dans laquelle dessiner
            GUISimulator gui = new GUISimulator(800, 600, Color.BLACK);

            // crée l'invader, en l'associant à la fenêtre graphique précédente
            Simulateur simulateur = new Simulateur();
            Simulation simulation = new Simulation(gui, donnees, simulateur);
            CalculPCC calculateur = new CalculPCC(donnees, simulateur);

            Robot robot = donnees.getRobots()[0];
            Incendie incendie = donnees.getIncendies().getFirst();
            Chemin chemin = new Chemin();
            //initialise chemin avec dijkstra
            chemin = calculateur.dijkstra(robot.getPosition(), incendie.getPosition(), robot);
            //crée les événements pour le chemin choisi
            chemin.creerEvenements(simulateur, robot);
            
            System.out.println(chemin);

        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        } catch (IllegalCheminRobotException e) {
            System.out.println(e.getMessage());
        }

    }


}

class Simulation implements Simulable {
    /** L'interface graphique associée */
    private GUISimulator gui;
    private DonneesSimulation donnees;
    private Simulateur simulateur;
    private Dessin fonctionDessin;

    public Simulation(GUISimulator gui, DonneesSimulation donnees, Simulateur simulateur) {
        this.gui = gui;
        this.donnees = donnees;
        this.simulateur = simulateur;
        this.fonctionDessin = new Dessin(this.donnees, this.gui);

        gui.setSimulable(this);

        draw();
    }

    private void draw() {
        gui.reset();
        fonctionDessin.dessin();
    }

    @Override
    public void next() {
        try {
            System.out.println(donnees.getRobots()[0].getReservoir());
            simulateur.execute();
        } catch (NoFireException e) {
            System.out.println(e);
        }
        simulateur.incrementeDate();
        draw();
    }

    @Override
    public void restart() {
    }
}