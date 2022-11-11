import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import java.awt.Color;

import gui.GUISimulator;
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

            // TODO: Faire un chemin à la main allant jusqu'à un incendie, déverser l'eau
            // TODO: Remplir le robot sur de l'eau, tout ça pour tester le temps.

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