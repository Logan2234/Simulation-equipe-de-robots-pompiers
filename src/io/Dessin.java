package io;

import java.awt.Color;
import java.util.List;

import Donnees.Carte;
import Donnees.Case;
import Donnees.DonneesSimulation;
import Donnees.Incendie;
import Donnees.NatureTerrain;
import Donnees.Robot.Robot;
import Evenements.Simulateur;
import gui.GUISimulator;
import gui.ImageElement;
import gui.Rectangle;
import gui.Text;

public class Dessin {

    private Dessin() {
        throw new IllegalStateException(
                "Cette classe ne doit pas être instanciée puisque toutes ses fonctions utiles sont statiques");
    }

    /**
     * Fonction permettant de dessiner toutes les cases de la carte
     * 
     * @param carte      : Carte utilisée pour dessiner les cases
     * @param gui        : Interface graphique sur laquelle dessiner les cases
     * @param tailleCase : Taille des cases telles qu'elles doivent apparaître sur
     *                   le dessin (non pas la taille fictive des cases)
     */
    private static void dessinCases(Carte carte, GUISimulator gui, int tailleCase) {
        // Il faut mettre un fond dans le cas où on dessine des arbres
        for (int i = 0; i < carte.getNbLignes(); i++) {
            for (int j = 0; j < carte.getNbColonnes(); j++) {
                if (carte.getCase(i, j).getNature() == NatureTerrain.FORET)
                    gui.addGraphicalElement(new ImageElement(tailleCase * j,
                            i * tailleCase, NatureTerrain.TERRAIN_LIBRE.getImagePath(), tailleCase, tailleCase, gui));
            }
        }
        // Dessin effectif de toutes les cases
        for (int i = 0; i < carte.getNbLignes(); i++) {
            for (int j = 0; j < carte.getNbColonnes(); j++) {
                NatureTerrain natureCase = carte.getCase(i, j).getNature();
                gui.addGraphicalElement(new ImageElement(tailleCase * j,
                        i * tailleCase, natureCase.getImagePath(), tailleCase, tailleCase, gui));
                // Si c'est une forêt, effet de densité si plusieurs forêts sont collées
                if (natureCase == NatureTerrain.FORET) {
                    if (j + 1 < carte.getNbColonnes() && carte.getCase(i, j + 1).getNature() == NatureTerrain.FORET) {
                        gui.addGraphicalElement(new ImageElement(tailleCase * j + tailleCase / 2,
                                i * tailleCase, natureCase.getImagePath(), tailleCase, tailleCase, gui));
                    }
                    if (i + 1 < carte.getNbLignes() && carte.getCase(i + 1, j).getNature() == NatureTerrain.FORET) {
                        gui.addGraphicalElement(new ImageElement(tailleCase * j, i * tailleCase + tailleCase / 2,
                                natureCase.getImagePath(), tailleCase, tailleCase, gui));
                    }
                }
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
    private static void dessinRobots(List<Robot> robots, GUISimulator gui, int tailleCase) {
        for (Robot robot : robots) {
            Case pos = robot.getPosition();
            int i = pos.getLigne();
            int j = pos.getColonne();

            gui.addGraphicalElement(
                    new ImageElement(j * tailleCase, i * tailleCase, robot.getImagePath(), tailleCase,
                            tailleCase, gui));

            // Dessin de la jauge
            if (robot.getReservoir() != 0) {
                float pourcentageReservoir = (float) robot.getReservoir() / robot.getCapacite();
                gui.addGraphicalElement(new Rectangle(tailleCase / 2 + tailleCase * j + 3 * tailleCase / 7,
                        tailleCase * i + tailleCase / 2 + (int) (((float)tailleCase / 4) * (1 - pourcentageReservoir)),
                        Color.BLUE, Color.BLUE, 3, (int) ((float)tailleCase / 2 * pourcentageReservoir)));
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
    private static void dessinIncendies(List<Incendie> incendies, GUISimulator gui, int tailleCase) {
        for (Incendie incendie : incendies) {
            if (incendie.getLitres() > 0) {
                Case pos = incendie.getPosition();
                int i = pos.getLigne();
                int j = pos.getColonne();
                float pourcentageExtinction = (float) incendie.getLitres() / incendie.getLitresInit();
                gui.addGraphicalElement(new ImageElement(
                        (int) (tailleCase * j + (float)tailleCase / 2 * (1 - pourcentageExtinction)),
                        (int) (i * tailleCase + (float)tailleCase / 2 * (1 - pourcentageExtinction)), "assets/Fire.png",
                        (int) (tailleCase * pourcentageExtinction), (int) (tailleCase * pourcentageExtinction), gui));
            }
        }
    }

    /**
     * Fonction permettant d'afficher la date en tout moment de la simulation
     * 
     * @param gui        : Interface graphique sur laquelle dessiner les cases
     * @param simulateur : Simulateur utilisé pour la simulation. Permet de
     *                   récupérer {@code dateSimulation}.
     */
    private static void dessinDateSimulation(GUISimulator gui, Simulateur simulateur, int tailleCarte) {
        gui.addGraphicalElement(
                new Text(tailleCarte, 25, Color.WHITE, "Date: " + Long.toString(simulateur.getDateSimulation())));
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
    public static void dessin(DonneesSimulation donnees, GUISimulator gui, Simulateur simulateur) {
        int tailleCase = gui.getHeight() / (donnees.getCarte().getNbLignes() + 5);
        dessinCases(donnees.getCarte(), gui, tailleCase);
        dessinRobots(donnees.getRobots(), gui, tailleCase);
        dessinIncendies(donnees.getIncendies(), gui, tailleCase);
        dessinDateSimulation(gui, simulateur, tailleCase * donnees.getCarte().getNbColonnes() + tailleCase);
    }

}
