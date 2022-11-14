package io;

import gui.Rectangle;
import gui.GUISimulator;
import gui.Text;

import java.awt.Color;
import java.util.HashMap;

import Donnees.Carte;
import Donnees.Case;
import Donnees.DonneesSimulation;
import Donnees.Incendie;
import Donnees.Robot.Robot;
import Donnees.Robot.RobotChenilles;
import Donnees.Robot.RobotDrone;
import Donnees.Robot.RobotPattes;
import Donnees.Robot.RobotRoues;
import Evenements.Simulateur;;

// Comme on utilise ces fonctions dans plusieurs fichiers tests
// différents, on préfère en faire un fichier à part directement
// afin d'éviter une redondance.

public class Dessin {
    private DonneesSimulation donnees;
    private GUISimulator gui;
    private int tailleCase;
    private HashMap<Object, String> assimilationRobotString;
    private Simulateur simulateur;

    public Dessin(DonneesSimulation donnees, GUISimulator gui, Simulateur simulateur) {
        this.donnees = donnees;
        this.gui = gui;
        this.simulateur = simulateur;

        int largeur_case = 800 / donnees.getCarte().getNbColonnes();
        int hauteur_case = 600 / donnees.getCarte().getNbLignes();

        this.tailleCase = Math.min(largeur_case, hauteur_case);
        this.assimilationRobotString = new HashMap<Object, String>();
        assimilationRobotString.put(RobotChenilles.class, "C");
        assimilationRobotString.put(RobotDrone.class, "D");
        assimilationRobotString.put(RobotPattes.class, "P");
        assimilationRobotString.put(RobotRoues.class, "R");
    }

    private void dessinCases() {
        Carte carte = donnees.getCarte();

        for (int i = 0; i < carte.getNbLignes(); i++) {
            for (int j = 0; j < carte.getNbColonnes(); j++) {
                Case current_case = carte.getCase(i, j);
                gui.addGraphicalElement(new Rectangle(tailleCase / 2 + tailleCase * j,
                        tailleCase / 2 + i * tailleCase, Color.BLACK, current_case.getNature().getColor(),
                        tailleCase));
            }
        }
    }

    private void dessinIncendies() {
        for (Incendie incendie : donnees.getIncendies()) {
            if (incendie.getLitres() > 0) {
                Case pos = incendie.getPosition();
                int i = pos.getLigne();
                int j = pos.getColonne();
                gui.addGraphicalElement(new Rectangle(tailleCase / 2 + tailleCase * j,
                        tailleCase / 2 + i * tailleCase, Color.GRAY, Color.RED, tailleCase / 2));
                gui.addGraphicalElement(new Text(tailleCase / 2 + tailleCase * j, tailleCase / 2 + tailleCase * i,
                        Color.BLACK, Integer.toString(incendie.getLitres())));
            }
        }
    }

    private void dessinRobots() {
        int tour = 0;
        for (Robot robot : donnees.getRobots()) {
            tour++;
            Case pos = robot.getPosition();
            int i = pos.getLigne();
            int j = pos.getColonne();
            gui.addGraphicalElement(new Rectangle(tailleCase / 2 + tailleCase * j,
                    tailleCase / 2 + i * tailleCase, Color.GRAY, Color.magenta, tailleCase / 2));
            gui.addGraphicalElement(new Text(tailleCase / 2 + tailleCase * j, tailleCase / 2 + tailleCase * i,
                    Color.BLACK, assimilationRobotString.get(robot.getClass())));
            gui.addGraphicalElement(new Text(650, 50 * tour + 30, Color.WHITE,
                    assimilationRobotString.get(robot.getClass()) + " : " + Integer.toString(robot.getReservoir())));
        }
        gui.addGraphicalElement(new Text(650, 0, Color.WHITE, simulateur.getDateSimulation())
    }

    private void dessinDateSimulation(){
        gui.addGraphicalElement(new Text(650, 30, Color.WHITE, "Date: " + Long.toString(simulateur.getDateSimulation())));
    }

    public void dessin() {
        dessinCases();
        dessinIncendies();
        dessinRobots();
        dessinDateSimulation();
    }

}
