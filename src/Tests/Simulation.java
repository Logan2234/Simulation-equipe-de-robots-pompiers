package Tests;

import Donnees.DonneesSimulation;
import Evenements.Simulateur;
import Exceptions.CellOutOfMapException;
import Exceptions.NoFireException;
import Exceptions.NoWaterException;
import gui.GUISimulator;
import gui.Simulable;
import io.Dessin;

class Simulation implements Simulable {
    /** L'interface graphique associée */
    private GUISimulator gui;
    private DonneesSimulation donnees;
    private Test classeAppelante;
    private String fichier;
    private Simulateur simulateur;
    private int tailleCase;

    public Simulation(GUISimulator gui, DonneesSimulation donnees, Simulateur simulateur, Test classeAppelante, String fichier) {
        this.gui = gui;
        this.donnees = donnees;
        this.classeAppelante = classeAppelante;
        this.fichier = fichier;
        this.simulateur = simulateur;
        this.tailleCase = Math.min((gui.getPanelWidth() - 10) / donnees.getCarte().getNbColonnes(), (gui.getPanelHeight() - 10) / donnees.getCarte().getNbLignes());

        gui.setSimulable(this);

        draw();
    }

    public int getTailleCase(){
        return this.tailleCase;
    }

    private void draw() {
        gui.reset();
        Dessin.dessin(this.donnees, this.gui, this.simulateur, this.tailleCase);
    }

    @Override
    public void next() {
        try {
            simulateur.execute();
        } catch (NoFireException e) {
            System.out.println(e);
        } catch (CellOutOfMapException e) {
            System.out.println(e);
        } catch (NoWaterException e) {
            System.out.println(e);
        }
        simulateur.incrementeDate();
        draw();
    }

    @Override
    public void restart() {
        simulateur.restart();

        switch (classeAppelante) {
            case TestSimulation:
                TestSimulation.initialize(fichier, gui);
                break;
            case TestScenarios:
                TestScenarios.initialize(fichier, gui);
                break;
            case TestDijkstra:
                TestDijkstra.initialize(fichier, gui);
                break;
            case TestBasique:
                TestBasique.initialize(fichier, gui);
                break;
            default:
                break;
        }

    }
}
