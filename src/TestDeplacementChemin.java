import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import java.awt.Color;

import java.util.*;

import gui.GUISimulator;
import gui.Rectangle;
import gui.Simulable;
import gui.Text;

public class TestDeplacementChemin {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestDeplacementChemin <nomDeFichier>");
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
            Chemin c = new Chemin();
            Case pos = donnees.getRobots()[0].getPosition();
            Case next = donnees.getCarte().getVoisin(pos, Direction.NORD);
            Case next2 = donnees.getCarte().getVoisin(next, Direction.NORD);
            Case next3 = donnees.getCarte().getVoisin(next2, Direction.OUEST);
            Case next4 = donnees.getCarte().getVoisin(next3, Direction.SUD);
            c.addElement(pos, 0);
            c.addElement(next, 0);
            c.addElement(next2, 1);
            c.addElement(next3, 2);
            c.addElement(next4, 3);
            c.CreerEvenements(simulateur, donnees.getRobots()[0]);
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
    private Queue<Evenement> evenements = new LinkedList<Evenement>();

    public Simulation(GUISimulator gui, DonneesSimulation donnees, Simulateur simulateur) {
        this.gui = gui;
        this.donnees = donnees;
        this.simulateur = simulateur;

        gui.setSimulable(this);

        draw();
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
        simulateur.execute();
        simulateur.incrementeDate();
        draw();
    }

    @Override
    public void restart() {
    }
}
