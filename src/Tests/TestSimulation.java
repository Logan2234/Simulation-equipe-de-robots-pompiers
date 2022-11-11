package Tests;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import Autre.CalculPCC;
import Autre.Chemin;
import Donnees.Carte;
import Donnees.Case;
import Donnees.Direction;
import Donnees.DonneesSimulation;
import Donnees.LecteurDonnees;
import Donnees.Robot.Robot;
import Evenements.EventIntervenir;
import Evenements.EventRemplir;
import Evenements.Simulateur;
import Exceptions.IllegalCheminRobotException;
import Exceptions.NoFireException;

import gui.GUISimulator;
import gui.Simulable;
import io.Dessin;

public class TestSimulation {
    public static void main(String[] args) {
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

            Carte carte = donnees.getCarte();
            Robot robot = donnees.getRobots()[0];
            Chemin chemin = new Chemin();
            Case pos = robot.getPosition();
            Case nextCase;
            int date = 0;

            chemin.addElement(pos, 0);

            Direction[] moves = { Direction.SUD, Direction.SUD, Direction.EST, Direction.EST };

            for (Direction dir : moves) {
                nextCase = carte.getVoisin(pos, dir);
                date += calculateur.tpsDpltCaseACase(pos, nextCase, robot);
                chemin.addElement(nextCase, date);
                pos = nextCase;
            }

            simulateur.ajouteEvenement(new EventIntervenir(date + 1, robot, donnees.getIncendies()));
            
            for (Direction dir : moves) {
                nextCase = carte.getVoisin(pos, dir);
                date += calculateur.tpsDpltCaseACase(pos, nextCase, robot);
                chemin.addElement(nextCase, date);
                pos = nextCase;
            }
            
            simulateur.ajouteEvenement(new EventIntervenir(date + 1, robot, donnees.getIncendies()));
            
            Direction[] moves2 = {Direction.OUEST, Direction.OUEST, Direction.OUEST, Direction.OUEST};
            
            for (Direction dir : moves2) {
                nextCase = carte.getVoisin(pos, dir);
                date += calculateur.tpsDpltCaseACase(pos, nextCase, robot);
                chemin.addElement(nextCase, date);
                pos = nextCase;
            }
            
            chemin.creerEvenements(simulateur, robot);

            simulateur.ajouteEvenement(new EventRemplir(date, robot));

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