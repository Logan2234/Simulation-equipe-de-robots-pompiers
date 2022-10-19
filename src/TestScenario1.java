import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import java.awt.Color;

import java.util.*;

import gui.GUISimulator;
import gui.Rectangle;
import gui.Simulable;
import gui.Text;

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
    private Queue<Queue<Evenement>> evenements = new LinkedList();
    RobotRoues robot2 = donnees.getRobots()[1];
    Queue<Evenement> un = new LinkedList(); un.add(new EventMouvement(1, robot2, NORD));
    evenements.add(un);

    Queue<Evenement> deux = new LinkedList(); deux.add(new EventIntervenir(robot2, 2, donnees.getIncendies()));
    evenements.add(deux);

    Queue<Evenement> trois = new LinkedList(); trois.add(new EventMouvement(3, robot2, OUEST));
    evenements.add(trois); evenements.add(trois);

    Queue<Evenement> quatre = new LinkedList(); quatre.add(new EventMouvement(4, robot2));
    evenements.add(quatre);

    Queue<Evenement> cinq = new LinkedList(); cinq.add(new EventMouvement(5, robot2, EST));
    evenements.add(cinq); evenements.add(cinq);

    Queue<Evenement> six = new LinkedList(); six.add(new EventIntervenir(robot2, 6, donnees.getIncendies()));
    evenements.add(six);
    
    public Simulation(GUISimulator gui, DonneesSimulation donnees) {
        this.gui = gui;
        this.donnees = donnees;
        this.simulateur = simulateur;

        gui.setSimulable(this);

        draw();

        Queue<Evenement> eventActuel;
        while (evenements.size() != 0){
            eventActuel = this.evenements.poll();
            eventActuel.execute();
            draw();
        }
        
    }

    private void draw() {
        gui.reset();

        Carte carte = donnees.getCarte();

        int largeur_case = 800 / carte.getNbColonnes();
        int hauteur_case = 600 / carte.getNbLignes();

        for (int i = 0; i < carte.getNbLignes(); i++){
            for (int j = 0; j < carte.getNbColonnes(); j++){
                Case current_case = carte.getCase(i, j);
                switch (current_case.getNature()) {
                    case EAU:
                        gui.addGraphicalElement(new Rectangle(hauteur_case/2 + largeur_case*j, hauteur_case/2 + i*hauteur_case, Color.BLUE, Color.BLUE, hauteur_case));
                        break;
                    case FORET:
                        gui.addGraphicalElement(new Rectangle(hauteur_case/2 + largeur_case*j, hauteur_case/2 + i*hauteur_case, Color.GREEN, Color.GREEN, hauteur_case));
                        break;
                    case ROCHE:
                        gui.addGraphicalElement(new Rectangle(hauteur_case/2 + largeur_case*j, hauteur_case/2 + i*hauteur_case, Color.GRAY, Color.GRAY, hauteur_case));
                        break;
                    case HABITAT:
                        gui.addGraphicalElement(new Rectangle(hauteur_case/2 + largeur_case*j, hauteur_case/2 + i*hauteur_case, Color.ORANGE, Color.ORANGE, hauteur_case));
                        break;
                    case TERRAIN_LIBRE:
                        gui.addGraphicalElement(new Rectangle(hauteur_case/2 + largeur_case*j, hauteur_case/2 + i*hauteur_case, Color.WHITE, Color.WHITE, hauteur_case));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void next() {
        simulateur.incrementeDate();
    }

    @Override
    public void restart() {
        simulateur.restart();
    }
}