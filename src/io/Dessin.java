package io;

import gui.Rectangle;
import gui.GUISimulator;
import gui.GraphicalElement;
import gui.*;

import java.awt.Color;
import java.util.HashMap;

import Donnees.Carte;
import Donnees.Case;
import Donnees.DonneesSimulation;
import Donnees.Incendie;
import Donnees.Robot.Robot;
import Donnees.Robot.RobotChenilles;
import Donnees.Robot.RobotDrone;
import Donnees.Robot.RobotEnum;
import Donnees.Robot.RobotPattes;
import Donnees.Robot.RobotRoues;
import Evenements.Simulateur;;

// Comme on utilise ces fonctions dans plusieurs fichiers tests
// différents, on préfère en faire un fichier à part directement
// afin d'éviter une redondance.

public class Dessin {
    private static void dessinCases(DonneesSimulation donnees, GUISimulator gui, int tailleCase) {
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

    private static void dessinIncendies(DonneesSimulation donnees, GUISimulator gui, int tailleCase) {
        for (Incendie incendie : donnees.getIncendies()) {
            if (incendie.getLitres() > 0) {
                Case pos = incendie.getPosition();
                int i = pos.getLigne();
                int j = pos.getColonne();
                gui.addGraphicalElement(new Oval(tailleCase / 2 + tailleCase * j,
                        tailleCase / 2 + i * tailleCase, Color.GRAY, Incendie.getColor(), tailleCase / 2));
                gui.addGraphicalElement(new Text(tailleCase / 2 + tailleCase * j, tailleCase / 2 + tailleCase * i,
                        Color.BLACK, Integer.toString(incendie.getLitres())));
            }
        }
    }

    private static void dessinRobots(DonneesSimulation donnees, GUISimulator gui, int tailleCase) {
        int tour = 0;
        for (Robot robot : donnees.getRobots()) {
            tour++;
            Case pos = robot.getPosition();
            int i = pos.getLigne();
            int j = pos.getColonne();
            gui.addGraphicalElement(new Rectangle(tailleCase / 2 + tailleCase * j,
                    tailleCase / 2 + i * tailleCase, Color.GRAY, Robot.getColor(), tailleCase / 2));
            gui.addGraphicalElement(new Text(tailleCase / 2 + tailleCase * j, tailleCase / 2 + tailleCase * i,
                    Color.WHITE, RobotEnum.getNom(robot.getClass())));
            gui.addGraphicalElement(new Text(650, 50 * tour + 30, Color.WHITE,
                    RobotEnum.getNom(robot.getClass()) + " : " + Integer.toString(robot.getReservoir())));
        }
    }

    private static void dessinDateSimulation(GUISimulator gui, Simulateur simulateur){
        gui.addGraphicalElement(new Text(650, 30, Color.WHITE, "Date: " + Long.toString(simulateur.getDateSimulation())));
    }

    public static void dessin(DonneesSimulation donnees, GUISimulator gui, Simulateur simulateur, int tailleCase) {
        dessinCases(donnees, gui, tailleCase);
        dessinIncendies(donnees, gui, tailleCase);
        dessinRobots(donnees, gui, tailleCase);
        dessinDateSimulation(gui, simulateur);
    }

}
