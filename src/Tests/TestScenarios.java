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
import Donnees.Incendie;
import Donnees.Robot.Robot;
import Evenements.EventIntervenir;
import Evenements.EventMouvement;
import Evenements.EventRemplir;
import Evenements.Simulateur;
import Exceptions.CellOutOfMapException;
import gui.GUISimulator;
import io.LecteurDonnees;

public class TestScenarios {
    public static void initialize(String fichier, GUISimulator gui) {
        try {
            DonneesSimulation donnees = LecteurDonnees.creerSimulation(fichier);
            Simulateur simulateur = new Simulateur();
            new Simulation(gui, donnees, simulateur, Test.TEST_SCENARIOS, fichier);
            
            Carte carte = donnees.getCarte();
            Robot robot1 = donnees.getRobots().get(0);
            Robot robot2 = donnees.getRobots().get(1);

            Chemin chemin = new Chemin();
            Case pos = robot1.getPosition();
            chemin.addElement(pos, robot1.getLastDate());
            Case nextCase = pos;

            try {
                for (int i = 0; i < 4; i++) {
                    nextCase = carte.getVoisin(pos, Direction.NORD);
                    chemin.addElement(nextCase,
                            chemin.getLastDate() + CalculPCC.tpsDpltCaseACase(pos, nextCase, robot1));
                    pos = nextCase;
                }
            } catch (CellOutOfMapException e) {
                try {
                    System.out.println(e);
                    
                    chemin.creerEvenements(simulateur, robot1);
                    chemin.getChemin().clear();
                    
                    Incendie incendie = donnees.getIncendies().get(4);

                    pos = robot2.getPosition();
                    nextCase = carte.getVoisin(pos, Direction.NORD);

                    simulateur.ajouteEvenement(
                            new EventMouvement(CalculPCC.tpsDpltCaseACase(pos, nextCase, robot2), robot2, nextCase));

                    for (int i = 0; i < Math.min(incendie.getLitres() / robot2.getQteVersement(),
                            robot2.getReservoir() / robot2.getQteVersement()); i++) {
                        simulateur.ajouteEvenement(new EventIntervenir(robot2.getLastDate(), robot2, incendie));
                    }

                    pos = nextCase;
                    chemin.addElement(pos, robot2.getLastDate());

                    Direction[] dirs = { Direction.OUEST, Direction.OUEST };
                    for (Direction dir : dirs) {
                        nextCase = carte.getVoisin(pos, dir);
                        chemin.addElement(nextCase,
                                chemin.getLastDate() + CalculPCC.tpsDpltCaseACase(pos, nextCase, robot2));
                        pos = nextCase;
                    }

                    chemin.creerEvenements(simulateur, robot2);
                    chemin.getChemin().clear();

                    simulateur.ajouteEvenement(new EventRemplir(robot2.getLastDate(), robot2));

                    chemin.addElement(pos, robot2.getLastDate());
                    Direction[] dirs2 = { Direction.EST, Direction.EST };
                    for (Direction dir : dirs2) {
                        nextCase = carte.getVoisin(pos, dir);
                        chemin.addElement(nextCase,
                                chemin.getLastDate() + CalculPCC.tpsDpltCaseACase(pos, nextCase, robot2));
                        pos = nextCase;
                    }

                    chemin.creerEvenements(simulateur, robot2);
                    chemin.getChemin().clear();

                    for (int i = 0; i < Math.min(incendie.getLitres() / robot2.getQteVersement(),
                            robot2.getReservoir() / robot2.getQteVersement()); i++) {
                        simulateur.ajouteEvenement(new EventIntervenir(robot2.getLastDate(), robot2, incendie));
                    }

                    nextCase = carte.getVoisin(pos, Direction.OUEST);
                    simulateur.ajouteEvenement(
                            new EventMouvement(robot2.getLastDate() + CalculPCC.tpsDpltCaseACase(pos, nextCase, robot2),
                                    robot2, nextCase));
                } catch (CellOutOfMapException e2) {
                    System.out.println(e2);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("fichier " + fichier + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + fichier + " invalide: " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestScenarios <nomDeFichier>");
            System.exit(1);
        }
        // crée la fenêtre graphique dans laquelle dessiner
        GUISimulator gui = new GUISimulator(800, 600, Color.BLACK);
        gui.setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 200,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 150);

        initialize(args[0], gui);
    }

}