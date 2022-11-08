import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import java.awt.Color;

import gui.GUISimulator;
import gui.Rectangle;
import gui.Simulable;

public class TestSimulation {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestLecteurDonnees <nomDeFichier>");
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
            CalculPCC calculateur = new CalculPCC(donnees, simulateur);

            // Création d'un chemin
            Carte carte = donnees.getCarte();
            Chemin c = new Chemin();
            Robot rob = donnees.getRobots()[0];
            Case pos = rob.getPosition();
            Case next = carte.getVoisin(pos, Direction.NORD);
            Case next2 = carte.getVoisin(next, Direction.NORD);
            Case next3 = carte.getVoisin(next2, Direction.OUEST);
            Case next4 = carte.getVoisin(next3, Direction.SUD);
            c.addElement(pos, 0);
            c.addElement(next, (int) calculateur.tpsDpltCaseACase(pos, next, rob) / 100000);
            c.addElement(next2, (int) calculateur.tpsDpltCaseACase(next, next2, rob) / 100000 + c.getElem(-1).getT());
            c.addElement(next3, (int) calculateur.tpsDpltCaseACase(next2, next3, rob) / 100000 + c.getElem(-1).getT());
            c.addElement(next4, (int) calculateur.tpsDpltCaseACase(next3, next4, rob) / 100000 + c.getElem(-1).getT());
            c.CreerEvenements(simulateur, rob);
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
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
        simulateur.incrementeDate();
        System.out.println(simulateur.getDateSimulation());
        draw();
    }

    @Override
    public void restart() {
    }
}