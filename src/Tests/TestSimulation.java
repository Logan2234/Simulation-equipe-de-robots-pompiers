package Tests;

import java.awt.Color;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.zip.DataFormatException;

import Autre.CalculPCC;
import Autre.Chemin;
import Donnees.Carte;
import Donnees.Case;
import Donnees.Direction;
import Donnees.DonneesSimulation;
import Donnees.Incendie;
import Donnees.Robot.Robot;
import Evenements.EventIntervenir;
import Evenements.EventRemplir;
import Evenements.Simulateur;
import Exceptions.CellOutOfMapException;
import gui.GUISimulator;
import io.LecteurDonnees;

public class TestSimulation {
    public static void initialize(String fichier, GUISimulator gui) {
        try {
            LecteurDonnees lecteur = new LecteurDonnees();
            DonneesSimulation donnees = lecteur.creerSimulation(fichier);
            Simulateur simulateur = new Simulateur();
            Simulation simulation = new Simulation(gui, donnees, simulateur, Test.TestSimulation, fichier);

            Carte carte = donnees.getCarte();
            Robot robot = donnees.getRobots()[0];
            Chemin chemin = new Chemin();
            Case pos = robot.getPosition();
            chemin.addElement(pos, robot.getLastDate());
            Case nextCase;

            // On veut récupérer dans ce test le 5ème incendie, on skip donc les 4 premiers
            Iterator<Incendie> iter = donnees.getIncendies().iterator();
            iter.next();
            iter.next();
            iter.next();
            iter.next();
            Incendie incendie = iter.next();

            Direction[] moves = { Direction.SUD, Direction.SUD, Direction.EST, Direction.EST };

            for (Direction dir : moves) {
                nextCase = carte.getVoisin(pos, dir);
                chemin.addElement(nextCase, chemin.getLastDate() + CalculPCC.tpsDpltCaseACase(pos, nextCase, robot));
                pos = nextCase;
            }

            chemin.creerEvenements(simulateur, robot);
            simulateur.ajouteEvenement(new EventIntervenir(robot.getLastDate(), robot, incendie));
            chemin.getChemin().clear();
            chemin.addElement(pos, robot.getLastDate());

            for (Direction dir : moves) {
                nextCase = carte.getVoisin(pos, dir);
                chemin.addElement(nextCase, chemin.getLastDate() + CalculPCC.tpsDpltCaseACase(pos, nextCase, robot));
                pos = nextCase;
            }

            incendie = iter.next();

            chemin.creerEvenements(simulateur, robot);
            simulateur.ajouteEvenement(new EventIntervenir(robot.getLastDate(), robot, incendie));
            chemin.getChemin().clear();
            chemin.addElement(pos, robot.getLastDate());

            Direction[] moves2 = { Direction.OUEST, Direction.OUEST, Direction.OUEST, Direction.OUEST };
            for (Direction dir : moves2) {
                nextCase = carte.getVoisin(pos, dir);
                chemin.addElement(nextCase, chemin.getLastDate() + CalculPCC.tpsDpltCaseACase(pos, nextCase, robot));
                pos = nextCase;
            }

            chemin.creerEvenements(simulateur, robot);
            simulateur.ajouteEvenement(new EventRemplir(robot.getLastDate(), robot));
            chemin.getChemin().clear();

            nextCase = carte.getVoisin(pos, Direction.NORD);
            chemin.addElement(nextCase, robot.getLastDate() + CalculPCC.tpsDpltCaseACase(pos, nextCase, robot));
            chemin.creerEvenements(simulateur, robot);
            chemin.getChemin().clear();

            robot = donnees.getRobots()[2];
            pos = robot.getPosition();
            chemin.addElement(pos, robot.getLastDate());

            for (int i = 0; i < 4; i++) {
                nextCase = carte.getVoisin(pos, Direction.OUEST);
                chemin.addElement(nextCase, chemin.getLastDate() + CalculPCC.tpsDpltCaseACase(pos, nextCase, robot));
                pos = nextCase;
            }

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
