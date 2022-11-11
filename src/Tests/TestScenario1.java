package Tests;

import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import Donnees.Carte;
import Donnees.Case;
import Donnees.Direction;
import Donnees.DonneesSimulation;
import Donnees.Incendie;
import Donnees.LecteurDonnees;
import Donnees.Robot.Robot;
import Donnees.Robot.RobotRoues;
import Evenements.EventIntervenir;
import Evenements.EventMouvement;
import Evenements.EventRemplir;
import Evenements.Simulateur;
import Exceptions.NoFireException;

import java.awt.Color;

import gui.GUISimulator;
import gui.Rectangle;
import gui.Simulable;

public class TestScenario1 {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestScenario1 <nomDeFichier>");
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
            simulation.Simulation0();
            simulation.Simulation1();
        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
    }
}

class Simulation implements Simulable {
    /** L'interface graphique associée */
    private GUISimulator gui;
    private DonneesSimulation donnees;
    private Simulateur simulateur;

    public Simulation(GUISimulator gui, DonneesSimulation donnees, Simulateur simulateur) {
        this.gui = gui;
        this.donnees = donnees;
        this.simulateur = simulateur;

        gui.setSimulable(this);

        draw();
    }

    public void Simulation0() {
        Robot robot1 = donnees.getRobots()[0];

        this.simulateur.ajouteEvenement(new EventMouvement(0, robot1, Direction.NORD));
        this.simulateur.ajouteEvenement(new EventMouvement(1, robot1, Direction.NORD));
        this.simulateur.ajouteEvenement(new EventMouvement(2, robot1, Direction.NORD));
        this.simulateur.ajouteEvenement(new EventMouvement(3, robot1, Direction.NORD));
    }

    public void Simulation1() {
        RobotRoues robot2 = (RobotRoues) donnees.getRobots()[1];
        this.simulateur.ajouteEvenement(new EventMouvement(6, robot2, Direction.NORD));
        this.simulateur.ajouteEvenement(new EventIntervenir(7, robot2, donnees.getIncendies()));
        this.simulateur.ajouteEvenement(new EventMouvement(8, robot2, Direction.OUEST));
        this.simulateur.ajouteEvenement(new EventMouvement(8, robot2, Direction.OUEST));
        this.simulateur.ajouteEvenement(new EventRemplir(9, robot2));
        this.simulateur.ajouteEvenement(new EventMouvement(10, robot2, Direction.EST));
        this.simulateur.ajouteEvenement(new EventMouvement(10, robot2, Direction.EST));
        this.simulateur.ajouteEvenement(new EventIntervenir(11, robot2, donnees.getIncendies()));
        this.simulateur.ajouteEvenement(new EventMouvement(12, robot2, Direction.OUEST));
    }

    private void draw() {
        gui.reset();

        Carte carte = donnees.getCarte();

        int largeur_case = 800 / carte.getNbColonnes();
        int hauteur_case = 600 / carte.getNbLignes();

        for (int i = 0; i < carte.getNbLignes(); i++) {
            for (int j = 0; j < carte.getNbColonnes(); j++) {
                Case current_case = carte.getCase(i, j);
                switch (current_case.getNature()) {
                    case EAU:
                        gui.addGraphicalElement(new Rectangle(hauteur_case / 2 + largeur_case * j,
                                hauteur_case / 2 + i * hauteur_case, Color.BLUE, Color.BLUE, hauteur_case));
                        break;
                    case FORET:
                        gui.addGraphicalElement(new Rectangle(hauteur_case / 2 + largeur_case * j,
                                hauteur_case / 2 + i * hauteur_case, Color.GREEN, Color.GREEN, hauteur_case));
                        break;
                    case ROCHE:
                        gui.addGraphicalElement(new Rectangle(hauteur_case / 2 + largeur_case * j,
                                hauteur_case / 2 + i * hauteur_case, Color.GRAY, Color.GRAY, hauteur_case));
                        break;
                    case HABITAT:
                        gui.addGraphicalElement(new Rectangle(hauteur_case / 2 + largeur_case * j,
                                hauteur_case / 2 + i * hauteur_case, Color.ORANGE, Color.ORANGE, hauteur_case));
                        break;
                    case TERRAIN_LIBRE:
                        gui.addGraphicalElement(new Rectangle(hauteur_case / 2 + largeur_case * j,
                                hauteur_case / 2 + i * hauteur_case, Color.WHITE, Color.WHITE, hauteur_case));
                        break;
                    default:
                        break;
                }
            }
        }
        for (Incendie incendie : donnees.getIncendies()) {
            Case pos = incendie.getPosition();
            int i = pos.getLigne();
            int j = pos.getColonne();
            gui.addGraphicalElement(new Rectangle(hauteur_case / 2 + largeur_case * j,
                    hauteur_case / 2 + i * hauteur_case, Color.RED, Color.RED, hauteur_case / 2));
        }
        for (Robot robot : donnees.getRobots()) {
            Case pos = robot.getPosition();
            int i = pos.getLigne();
            int j = pos.getColonne();
            gui.addGraphicalElement(new Rectangle(hauteur_case / 2 + largeur_case * j,
                    hauteur_case / 2 + i * hauteur_case, Color.magenta, Color.magenta, hauteur_case / 2));
        }
    }

    @Override
    public void next() {
        try {
            simulateur.execute();
        } catch (NoFireException e) {
            System.out.println(e);
        }
        simulateur.incrementeDate();
        draw();
    }

    @Override
    public void restart() {
        simulateur.restart();
    }
}