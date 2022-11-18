package Tests;

import java.awt.Color;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import Autre.CalculPCC;
import Autre.Chemin;
import Donnees.Carte;
import Donnees.Case;
import Donnees.Direction;
import Donnees.DonneesSimulation;
import Donnees.Robot.Robot;
import Evenements.EventIntervenir;
import Evenements.EventRemplir;
import Evenements.Simulateur;
import Exceptions.CellOutOfMapException;
import gui.GUISimulator;
import io.LecteurDonnees;

/**
 * Test du déplacement des bots et du remplissage (en particulier du non remplissage de celui à pattes)
 */
public class TestSimulation {
    
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
            new Simulation(gui, donnees, simulateur, EnumTest.TEST_SIMULATION, fichier);
            // On facilite les notations pour la suite 
            Carte carte = donnees.getCarte();
            Robot robot = donnees.getRobots().get(0);
            Chemin chemin = new Chemin();
            Case pos = robot.getPosition();
            chemin.addElement(pos, robot.getLastDate());
            Case nextCase;

            // Avec le robot 0
            // On se déplace deux fois vers le Sud puis deux fois vers l'Est
            Direction[] moves = { Direction.SUD, Direction.SUD, Direction.EST, Direction.EST };
            for (Direction dir : moves) {
                nextCase = carte.getVoisin(pos, dir);
                chemin.addElement(nextCase, chemin.getLastDate() + CalculPCC.tpsDpltCaseACase(pos, nextCase, robot));
                pos = nextCase;
            }

            // On se déplace puis on intervient sur l'incendie
            chemin.creerEvenements(simulateur, robot);
            simulateur.ajouteEvenement(new EventIntervenir(robot.getLastDate(), robot, donnees.getIncendies().get(4)));
            chemin.getChemin().clear();
            chemin.addElement(pos, robot.getLastDate());

            // On se déplace deux fois vers le Sud puis deux fois vers l'Est
            for (Direction dir : moves) {
                nextCase = carte.getVoisin(pos, dir);
                chemin.addElement(nextCase, chemin.getLastDate() + CalculPCC.tpsDpltCaseACase(pos, nextCase, robot));
                pos = nextCase;
            }

            // On se déplace puis on intervient sur l'incendie
            chemin.creerEvenements(simulateur, robot);
            simulateur.ajouteEvenement(new EventIntervenir(robot.getLastDate(), robot, donnees.getIncendies().get(5)));
            chemin.getChemin().clear();
            chemin.addElement(pos, robot.getLastDate());

            // On se dirige quatre fois vers l'Ouest
            Direction[] moves2 = { Direction.OUEST, Direction.OUEST, Direction.OUEST, Direction.OUEST };
            for (Direction dir : moves2) {
                nextCase = carte.getVoisin(pos, dir);
                chemin.addElement(nextCase, chemin.getLastDate() + CalculPCC.tpsDpltCaseACase(pos, nextCase, robot));
                pos = nextCase;
            }

            // On se déplace puis on va se remplir
            chemin.creerEvenements(simulateur, robot);
            simulateur.ajouteEvenement(new EventRemplir(robot.getLastDate(), robot));
            chemin.getChemin().clear();

            // On se déplace vers le Nord
            nextCase = carte.getVoisin(pos, Direction.NORD);
            chemin.addElement(nextCase, robot.getLastDate() + CalculPCC.tpsDpltCaseACase(pos, nextCase, robot));
            chemin.creerEvenements(simulateur, robot);
            chemin.getChemin().clear();

            // Avec le robot 2
            robot = donnees.getRobots().get(2);
            pos = robot.getPosition();
            chemin.addElement(pos, robot.getLastDate());

            // On le déplace quatre fois vers l'Ouest
            for (int i = 0; i < 4; i++) {
                nextCase = carte.getVoisin(pos, Direction.OUEST);
                chemin.addElement(nextCase, chemin.getLastDate() + CalculPCC.tpsDpltCaseACase(pos, nextCase, robot));
                pos = nextCase;
            }

            // On se déplace puis on va remplir
            chemin.creerEvenements(simulateur, robot);
            simulateur.ajouteEvenement(new EventRemplir(robot.getLastDate(), robot));

        } catch (FileNotFoundException e) {
            System.out.println("fichier " + fichier + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + fichier + " invalide: " + e.getMessage());
        } catch (CellOutOfMapException e) {
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
        gui.setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 200,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 150);

        initialize(args[0], gui);
    }
}
