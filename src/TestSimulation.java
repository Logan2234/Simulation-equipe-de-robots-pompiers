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
    private Dessin fonctionDessin;

    public Simulation(GUISimulator gui, DonneesSimulation donnees, Simulateur simulateur) {
        this.gui = gui;
        this.donnees = donnees;
        this.simulateur = simulateur;
        this.fonctionDessin = new Dessin(this.donnees, this.gui);
        
        gui.setSimulable(this);

        draw();
    }

    private void draw() {
        gui.reset();
        fonctionDessin.dessin();
    }

    @Override
    public void next() {
        try {
            simulateur.execute();
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
        simulateur.incrementeDate();
        draw();
    }

    @Override
    public void restart() {
    }
}