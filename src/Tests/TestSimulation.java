package Tests;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

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
import Exceptions.CellOutOfMapException;
import Exceptions.IllegalPathException;
import gui.GUISimulator;

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
            Case nextCase;
            
            Direction[] moves = { Direction.SUD, Direction.SUD, Direction.EST, Direction.EST };

            for (Direction dir : moves) {
                nextCase = carte.getVoisin(pos, dir);
                chemin.addElement(nextCase, simulateur.getDateDernierEvenement());
                pos = nextCase;
            }

            chemin.creerEvenements(simulateur, robot);
            simulateur.ajouteEvenement(new EventIntervenir(simulateur.getDateDernierEvenement(), robot, donnees.getIncendies().get(4)));
            chemin.getChemin().clear();
            
            for (Direction dir : moves) {
                nextCase = carte.getVoisin(pos, dir);
                chemin.addElement(nextCase, simulateur.getDateDernierEvenement());
                pos = nextCase;
            }
            
            chemin.creerEvenements(simulateur, robot);
            simulateur.ajouteEvenement(new EventIntervenir(simulateur.getDateDernierEvenement(), robot, donnees.getIncendies().get(5)));
            chemin.getChemin().clear();

            Direction[] moves2 = { Direction.OUEST, Direction.OUEST, Direction.OUEST, Direction.OUEST };
            for (Direction dir : moves2) {
                nextCase = carte.getVoisin(pos, dir);
                chemin.addElement(nextCase, simulateur.getDateDernierEvenement());
                pos = nextCase;
            }
            
            chemin.creerEvenements(simulateur, robot);
            simulateur.ajouteEvenement(new EventRemplir(simulateur.getDateDernierEvenement(), robot, simulateur));
            chemin.getChemin().clear();
            
            chemin.addElement(carte.getVoisin(pos, Direction.NORD), simulateur.getDateDernierEvenement());
            chemin.creerEvenements(simulateur, robot);
            chemin.getChemin().clear();
                        
            robot = donnees.getRobots()[2];
            pos = robot.getPosition();
            
            for (int i = 0; i < 4; i++) {
                nextCase = carte.getVoisin(pos, Direction.OUEST);
                chemin.addElement(nextCase, 0);
                pos = nextCase;    
            }
            
            chemin.creerEvenements(simulateur, robot);
            simulateur.ajouteEvenement(new EventRemplir(simulateur.getDateDernierEvenement(), robot, simulateur));
            

        } catch (IllegalPathException e) {
            System.out.println(e);
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

        initialize(args[0], gui);
    }
}
