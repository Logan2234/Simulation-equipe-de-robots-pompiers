package Tests;

import java.util.Scanner;

import Donnees.DonneesSimulation;
import Evenements.Simulateur;
import Exceptions.CellOutOfMapException;
import Exceptions.NoFireException;
import Exceptions.NoMoreFireException;
import Exceptions.NoWaterException;
import gui.GUISimulator;
import gui.Simulable;
import io.Dessin;

class Simulation implements Simulable {
    private final GUISimulator gui;
    private final DonneesSimulation donnees;
    private final Test testAppelant;
    private final String fichier;
    private final Simulateur simulateur;
    private boolean simulationTerminee;

    public Simulation(GUISimulator gui, DonneesSimulation donnees, Simulateur simulateur, Test test, String fichier) {
        this.gui = gui;
        this.donnees = donnees;
        this.testAppelant = test;
        this.fichier = fichier;
        this.simulateur = simulateur;

        gui.setSimulable(this);

        System.out.println("\n====================================================================");
        System.out.println("                       DEBUT DE LA SIMULATION                        ");
        System.out.println("====================================================================\n");
        
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
     * correspondant à {@code dateSimulation} après avoir incrémenté la date puis de
     * redessiner le résultat
     */
    @Override
    public void next() {
        
        if (!simulationTerminee) {
            try {
                // Si la simulation n'est pas terminée on incrémente la date
                if (!simulateur.simulationTerminee())
                    simulateur.incrementeDate();

                // On exécute les évènements
                simulateur.execute();
                draw();
            } catch (CellOutOfMapException e) {
                System.out.println(e);
            } catch (NoFireException e) {
                System.out.println(e);
            } catch (NoWaterException e) {
                System.out.println(e);
            } catch (NoMoreFireException e) {
                System.out.println(e.getMessage());
                simulationTerminee = true;
            }

        } else {
            // Pour quitter la simulation
            System.out.println("\n====================================================================");
            System.out.println("                        FIN DE LA SIMULATION                         ");
            System.out.println("====================================================================\n");
            System.out.println("Appuyez sur ENTRER pour quitter...");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            scanner.close();
            System.exit(0);
        }
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
