package io;

import java.awt.Color;
import java.awt.image.ImageObserver;
import java.util.LinkedList;

import Donnees.Carte;
import Donnees.Case;
import Donnees.DonneesSimulation;
import Donnees.Incendie;
import Donnees.NatureTerrain;
import Donnees.Robot.Robot;
import Evenements.Simulateur;
import gui.GUISimulator;
import gui.Oval;
import gui.Rectangle;
import gui.Text;
import gui.ImageElement;

public class Dessin {

    /**
     * Fonction permettant de dessiner toutes les cases de la carte
     * 
     * @param carte      : Carte utilisée pour dessiner les cases
     * @param gui        : Interface graphique sur laquelle dessiner les cases
     * @param tailleCase : Taille des cases telles qu'elles doivent apparaître sur
     *                   le dessin (non pas la taille fictive des cases)
     */
    private static void dessinCases(Carte carte, GUISimulator gui, int tailleCase) {
        for (int i = 0; i < carte.getNbLignes(); i++) {
            for (int j = 0; j < carte.getNbColonnes(); j++) {
                Case current_case = carte.getCase(i, j);
                if (current_case.getNature() == NatureTerrain.FORET) {
                    // if (carte.getCase(i, j + 1).getNature() == NatureTerrain.FORET) {
                    //     gui.addGraphicalElement(new ImageElement(tailleCase * j + tailleCase / 2,
                    //             i * tailleCase, current_case.getNature().getImagePath(), tailleCase, tailleCase, gui));
                    // }
                    // if (carte.getCase(i + 1, j).getNature() == NatureTerrain.FORET) {
                    //     gui.addGraphicalElement(new ImageElement(tailleCase * j,
                    //             i * tailleCase + tailleCase / 2, current_case.getNature().getImagePath(), tailleCase,
                    //             tailleCase, gui));
                    // }
                    gui.addGraphicalElement(new ImageElement(tailleCase * j,
                            i * tailleCase, current_case.getNature().getImagePath(), tailleCase, tailleCase, gui));
                } else if (current_case.getNature() == NatureTerrain.TERRAIN_LIBRE) {
                    gui.addGraphicalElement(new ImageElement(tailleCase * j,
                            i * tailleCase, current_case.getNature().getImagePath(), tailleCase, tailleCase, gui));
                } else
                    gui.addGraphicalElement(new Rectangle(tailleCase / 2 + tailleCase * j,
                            tailleCase / 2 + i * tailleCase, current_case.getNature().getColor(),
                            current_case.getNature().getColor(), tailleCase));
            }
        }
    }

    /**
     * Fonction permettant de dessiner l'ensemble des incendies
     * 
     * @param incendies  : Liste chaînée des incendies à dessiner
     * @param gui        : Interface graphique sur laquelle dessiner les cases
     * @param tailleCase : Taille des cases telles qu'elles doivent apparaître sur
     *                   le dessin (non pas la taille fictive des cases)
     */
    private static void dessinIncendies(LinkedList<Incendie> incendies, GUISimulator gui, int tailleCase) {
        for (Incendie incendie : incendies) {
            if (incendie.getLitres() > 0) {
                Case pos = incendie.getPosition();
                int i = pos.getLigne();
                int j = pos.getColonne();
                gui.addGraphicalElement(new Oval(tailleCase / 2 + tailleCase * j,
                        tailleCase / 2 + i * tailleCase, Color.GRAY, Incendie.getColor(), tailleCase / 2 + 10));
                gui.addGraphicalElement(new Text(tailleCase / 2 + tailleCase * j, tailleCase / 2 + tailleCase * i,
                        Color.BLACK, Integer.toString(incendie.getLitres())));
            }
        }
    }

    /**
     * Fonction permettant de dessiner l'ensemble des robots
     * 
     * @param robots     : Liste des robots à dessiner
     * @param gui        : Interface graphique sur laquelle dessiner les cases
     * @param tailleCase : Taille des cases telles qu'elles doivent apparaître sur
     *                   le dessin (non pas la taille fictive des cases)
     */
    private static void dessinRobots(Robot[] robots, GUISimulator gui, int tailleCase) {
        for (Robot robot : robots) {
            Case pos = robot.getPosition();
            int i = pos.getLigne();
            int j = pos.getColonne();
            gui.addGraphicalElement(new Rectangle(tailleCase / 2 + tailleCase * j,
                    tailleCase / 2 + i * tailleCase, Color.GRAY, Robot.getColor(), tailleCase / 2));
            // Dessin du texte représentant le type de robot
            gui.addGraphicalElement(new Text(tailleCase / 2 + tailleCase * j, tailleCase / 2 + tailleCase * i,
                    Color.WHITE, Robot.getNom(robot.getClass())));
            // Dessin de la jauge
            if (robot.getReservoir() != 0)
                gui.addGraphicalElement(new Rectangle(tailleCase / 2 + tailleCase * j + 3 * tailleCase / 10,
                        tailleCase * i + tailleCase / 2
                                + (int) ((tailleCase / 4) * (1 - (float) robot.getReservoir() / robot.getCapacite())),
                        Color.BLUE, Color.BLUE, 3,
                        (int) (tailleCase / 2 * ((float) robot.getReservoir() / robot.getCapacite()))));
        }
    }

    /**
     * Fonction permettant d'afficher la date en tout moment de la simulation
     * 
     * @param gui        : Interface graphique sur laquelle dessiner les cases
     * @param simulateur : Simulateur utilisé pour la simulation. Permet de
     *                   récupérer {@code dateSimulation}.
     */
    private static void dessinDateSimulation(GUISimulator gui, Simulateur simulateur) {
        gui.addGraphicalElement(
                new Text(gui.getPanelWidth() - 150, gui.getPanelHeight() / 2, Color.WHITE,
                        "Date: " + Long.toString(simulateur.getDateSimulation())));
    }

    /**
     * Unique fonction publique de la classe Dessin. Elle permet de dessiner
     * l'ensemble des données sur l'interface
     * 
     * @param donnees    : Données de la simulation à dessiner
     * @param gui        : Interface graphique sur laquelle dessiner les cases
     * @param simulateur : Simulateur utilisé pour la simulation. Utilisé par la
     *                   fonction {@code dessinDateSimulation} pour dessiner
     *                   {@code dateSimulation} sur l'interface graphique associée
     * @param tailleCase : Taille des cases telles qu'elles doivent apparaître sur
     *                   le dessin (non pas la taille fictive des cases)
     */
    public static void dessin(DonneesSimulation donnees, GUISimulator gui, Simulateur simulateur, int tailleCase) {
        dessinCases(donnees.getCarte(), gui, tailleCase);
        dessinRobots(donnees.getRobots(), gui, tailleCase);
        dessinIncendies(donnees.getIncendies(), gui, tailleCase);
        dessinDateSimulation(gui, simulateur);
    }

}
