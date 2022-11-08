import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import java.awt.Color;

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

        // crée la fenêtre graphique dans laquelle dessiner
        GUISimulator gui = new GUISimulator(800, 600, Color.BLACK);
        // crée l'invader, en l'associant à la fenêtre graphique précédente
        Simulateur simulateur = new Simulateur();
        Simulation simulation = new Simulation(gui, simulateur, args[0]);
    }
}

class Simulation implements Simulable {
    /** L'interface graphique associée */
    private GUISimulator gui;
    private String fichier_utilise;
    private Simulateur simulateur;
    private DonneesSimulation donnees;

    public Simulation(GUISimulator gui, Simulateur simulateur, String fichier_utilise) {
        this.gui = gui;
        this.fichier_utilise = fichier_utilise;
        this.simulateur = simulateur;

        gui.setSimulable(this);

        initialize();
        draw();
    }

    private void initialize() {
        LecteurDonnees lecteur = new LecteurDonnees();

        try {
            this.donnees = lecteur.creerSimulation(fichier_utilise);
        } catch (FileNotFoundException e) {
            System.out.println("fichier " + fichier_utilise + " inconnu ou illisible");
            return;
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + fichier_utilise + " invalide: " + e.getMessage());
            return;
        }

        System.out.println(donnees.getRobots()[0]);
        Chemin c = new Chemin();
        Chemin c1 = new Chemin();

        Case pos1 = donnees.getRobots()[1].getPosition();
        Case next1 = donnees.getCarte().getVoisin(pos1, Direction.NORD);

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
        c1.addElement(pos1, 0);
        c1.addElement(next1, 1);
        c1.CreerEvenements(simulateur, donnees.getRobots()[1]);
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
        simulateur.resetDate();
        initialize();
        draw();
    }
}
