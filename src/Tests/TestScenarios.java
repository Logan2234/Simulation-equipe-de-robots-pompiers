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
import Evenements.EventMouvement;
import Evenements.EventRemplir;
import Evenements.Simulateur;
import Exceptions.CellOutOfMapException;
import Exceptions.IllegalPathException;
import gui.GUISimulator;

public class TestScenarios {
    public static void initialize(String fichier, GUISimulator gui) {
        LecteurDonnees lecteur;
        DonneesSimulation donnees;
        try {
            lecteur = new LecteurDonnees();
            donnees = lecteur.creerSimulation(fichier);
            Simulation simulation = new Simulation(gui, donnees, Test.TestScenarios, fichier);
            Carte carte = donnees.getCarte();
            Robot robot2 = donnees.getRobots()[1];
            Robot robot1 = donnees.getRobots()[0];
            Simulateur simulateur = new Simulateur();
            Chemin chemin = new Chemin();
            Case pos = robot1.getPosition();
            Case nextCase = pos;
            try {
                for (int i = 0; i < 4; i++) {
                    nextCase = carte.getVoisin(pos, Direction.NORD);
                    chemin.addElement(nextCase, simulateur.getDateDernierEvenement());
                    pos = nextCase;
                }
            } catch (CellOutOfMapException e) {
                try {
                    chemin.creerEvenements(simulateur, robot1);
                    chemin.getChemin().clear();

                    pos = robot2.getPosition();
                    nextCase = pos;

                    nextCase = carte.getVoisin(pos, Direction.NORD);
                    pos = nextCase;

                    simulateur.ajouteEvenement(
                            new EventMouvement(CalculPCC.tpsDpltCaseACase(pos, nextCase, robot2), robot2,
                                    Direction.NORD));

                    for (int i = 0; i < Math.min(donnees.getIncendies().get(4).getLitres() / robot2.getQteVersement(),
                            robot2.getReservoir() / robot2.getQteVersement()); i++) { // ! Attention cette formule de
                                                                                      // calcul du nombre d'intervention
                                                                                      // ne marche pas dans le cas du
                                                                                      // robot à pattes
                        simulateur.ajouteEvenement(
                                new EventIntervenir(simulateur.getDateDernierEvenement(), robot2,
                                        donnees.getIncendies().get(4)));
                    }

                    Direction[] dirs = { Direction.OUEST, Direction.OUEST };

                    for (Direction dir : dirs) {
                        nextCase = carte.getVoisin(pos, dir);
                        chemin.addElement(nextCase, simulateur.getDateDernierEvenement());
                        pos = nextCase;
                    }

                    chemin.creerEvenements(simulateur, robot2);
                    chemin.getChemin().clear();

                    simulateur.ajouteEvenement(new EventRemplir(simulateur.getDateDernierEvenement(), robot2));

                    Direction[] dirs2 = { Direction.EST, Direction.EST };

                    for (Direction dir : dirs2) {
                        nextCase = carte.getVoisin(pos, dir);
                        chemin.addElement(nextCase, simulateur.getDateDernierEvenement());
                        pos = nextCase;
                    }

                    chemin.creerEvenements(simulateur, robot2);
                    chemin.getChemin().clear();

                    for (int i = 0; i < Math.min(donnees.getIncendies().get(4).getLitres() / robot2.getQteVersement(),
                            robot2.getReservoir() / robot2.getQteVersement()); i++) { // ! Attention cette formule de
                                                                                      // calcul du nombre d'intervention
                                                                                      // ne marche pas dans le cas du
                                                                                      // robot à pattes
                        simulateur.ajouteEvenement(
                                new EventIntervenir(simulateur.getDateDernierEvenement(), robot2,
                                        donnees.getIncendies().get(4)));
                    }

                    nextCase = carte.getVoisin(pos, Direction.OUEST);

                    simulateur.ajouteEvenement(
                            new EventMouvement(
                                    simulateur.getDateDernierEvenement()
                                            + CalculPCC.tpsDpltCaseACase(pos, nextCase, robot2),
                                    robot2,
                                    Direction.OUEST));
                } catch (IllegalPathException e1) {
                    System.out.println(e1);
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

        initialize(args[0], gui);
    }

}