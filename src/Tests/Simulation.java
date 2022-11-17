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
    private GUISimulator gui;
    private DonneesSimulation donnees;
    private Test testAppelant;
    private String fichier;
    private Simulateur simulateur;

    public Simulation(GUISimulator gui, DonneesSimulation donnees, Simulateur simulateur, Test test, String fichier) {
        this.gui = gui;
        this.donnees = donnees;
        this.testAppelant = test;
        this.fichier = fichier;
        this.simulateur = simulateur;

        gui.setSimulable(this);

        // Force l'affichage correct de la carte (c'est à dire à la bonne taille)
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
        }

        draw();
    }

    /**
     * Fonction de dessin exécuté à chaque execution de {@code next}
     */
    private void draw() {
        gui.reset();
        Dessin.dessin(this.donnees, this.gui, this.simulateur);
    }

    /**
     * Fonction de gui.jar overridée permettant d'exécuter les évènements
     * correspondant à {@code dateSimulation} avant de l'incrémenter puis de
     * redessiner le résultat
     */
    @Override
    public void next() {
        if (!simulateur.simulationTerminee()){
            simulateur.incrementeDate();
        }
        try {
            simulateur.execute();
        } catch (CellOutOfMapException e) {
            System.out.println(e);
        } catch (NoFireException e) {
            System.out.println(e);
        } catch (NoWaterException e) {
            System.out.println(e);
        }
        draw();
    }

    /**
     * Fonction de gui.jar overridée permettant de réinitialiser la simulation
     */
    @Override
    public void restart() {
        // On remet à zéro le simulateur
        simulateur.restart();

        // On réinitialise les données en fonction du test appelant
        switch (testAppelant) {
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
            case TestAvance:
                TestAvance.initialize(fichier, gui);
                break;
            default:
                break;
        }
    }
}
