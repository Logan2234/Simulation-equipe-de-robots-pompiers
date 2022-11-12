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
import Donnees.Incendie;
import Donnees.LecteurDonnees;
import Donnees.Robot.Robot;
import Evenements.EventIntervenir;
import Evenements.EventRemplir;
import Evenements.Simulateur;
import Exceptions.CaseOutOfMapException;
import Exceptions.IllegalCheminRobotException;
import gui.GUISimulator;

public class TestSimulation {
    public static void initialize(String fichier, GUISimulator gui) {
        try {
            LecteurDonnees lecteur = new LecteurDonnees();
            DonneesSimulation donnees = lecteur.creerSimulation(fichier);
            Simulateur simulateur = new Simulateur();
            Simulation simulation = new Simulation(gui, donnees, simulateur, Test.TestSimulation, fichier);
            CalculPCC calculateur = new CalculPCC(donnees, simulateur);

            Carte carte = donnees.getCarte();
            Robot robot = donnees.getRobots()[0];
            Chemin chemin = new Chemin();
            Case pos = robot.getPosition();
            Case nextCase;
            long date = 0;

            chemin.addElement(pos, 0);

            Direction[] moves = { Direction.SUD, Direction.SUD, Direction.EST, Direction.EST };

            for (Direction dir : moves) {
                nextCase = carte.getVoisin(pos, dir);
                date += calculateur.tpsDpltCaseACase(pos, nextCase, robot);
                System.out.println(date);
                chemin.addElement(nextCase, date);

                pos = nextCase;
            }
            for (Incendie incendie : donnees.getIncendies() ){
                simulateur.ajouteEvenement(new EventIntervenir(date + 2, robot, incendie));}

            for (Direction dir : moves) {
                nextCase = carte.getVoisin(pos, dir);
                date += calculateur.tpsDpltCaseACase(pos, nextCase, robot);
                chemin.addElement(nextCase, date);
                pos = nextCase;
            }

            for (Incendie incendie : donnees.getIncendies() ){
            simulateur.ajouteEvenement(new EventIntervenir(date + 1, robot, incendie));}
            Direction[] moves2 = { Direction.OUEST, Direction.OUEST, Direction.OUEST, Direction.OUEST };

            for (Direction dir : moves2) {
                nextCase = carte.getVoisin(pos, dir);
                date += calculateur.tpsDpltCaseACase(pos, nextCase, robot);
                chemin.addElement(nextCase, date);
                pos = nextCase;
            }

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
