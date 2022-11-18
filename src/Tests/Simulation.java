package Tests;

import java.util.Scanner;

import Chefs.Chef;
import Donnees.DonneesSimulation;
import Evenements.Simulateur;
import Exceptions.NoMoreFireException;
import Exceptions.NoMoreRobotsException;
import gui.GUISimulator;
import gui.Simulable;
import io.Dessin;

class Simulation implements Simulable {
    private final GUISimulator gui;
    private final DonneesSimulation donnees;
    private final EnumTest testAppelant;
    private final String fichier;
    private final Simulateur simulateur;
    private boolean simulationTerminee;

    /**
     * Méthode permettant seulement de factoriser le code des constructeurs
     */
    private void initialisationCommune() {
        gui.setSimulable(this);

        System.out.println("\n====================================================================");
        System.out.println("                       DEBUT DE LA SIMULATION                        ");
        System.out.println("====================================================================\n");

        // Force l'affichage correct de la carte (c'est à dire à la bonne taille)
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        draw();
    }

    /**
     * Initialise une simulation.
     * 
     * @param gui        : gui
     * @param donnees    : fichier avec les données à tester
     * @param simulateur : initialisé par la méthode {@code initialize}, définie
     *                   dans les tests
     * @param test       : test que l'on souhaite lancer si il s'agit d'un test fait
     *                   par nos soins (dans l'énumérateur Test)
     * @param fichier    : fichier à tester
     */
    public Simulation(GUISimulator gui, DonneesSimulation donnees, Simulateur simulateur, EnumTest test, String fichier) {
        this.gui = gui;
        this.donnees = donnees;
        this.testAppelant = test;
        this.fichier = fichier;
        this.simulateur = simulateur;
        initialisationCommune();
    }

    /**
     * Initialise une simulation.
     * 
     * @param gui        : gui
     * @param donnees    : donnees du fichier envoyé
     * @param simulateur : initialisé par la méthode {@code initialize}, définie
     *                   dans les tests
     * @param test       : test que l'on souhaite lancer si il s'agit d'un test fait
     *                   par nos soins (dans l'énumérateur Test)
     * @param fichier    : fichier avec les données à tester
     * @param chef       : méthode que l'on souhaite appelée (basique ou avancée)
     */
    public Simulation(GUISimulator gui, DonneesSimulation donnees, Simulateur simulateur, EnumTest test, String fichier,
            Chef chef) {
        this.gui = gui;
        this.donnees = donnees;
        this.testAppelant = test;
        this.fichier = fichier;
        this.simulateur = simulateur;
        initialisationCommune();

        try {
            chef.strategie();
        } catch (NoMoreFireException | NoMoreRobotsException e) {
            System.out.println(e);
        }
    }

    /**
     * Méthode de dessin exécutée à chaque exécution de {@code next}
     */
    private void draw() {
        gui.reset();
        Dessin.dessin(this.donnees, this.gui, this.simulateur);
    }

    /**
     * Méthode de gui.jar overridée permettant d'exécuter les événements
     * correspondants à {@code dateSimulation} après avoir incrémenté la date puis
     * de redessiner le résultat
     */
    @Override
    public void next() {

        if (!simulationTerminee) {
            try {
                // On incrémente la date et on exécute les évènements
                simulateur.incrementeDate();
                simulateur.execute();
                draw();
            } catch (NoMoreRobotsException | NoMoreFireException e) {
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
     * Méthode de gui.jar overridée permettant de réinitialiser la simulation
     */
    @Override
    public void restart() {
        // On remet à zéro le simulateur
        simulateur.restart();

        // On réinitialise les données en fonction du test appelant
        switch (testAppelant) {
            case TEST_SIMULATION:
                TestSimulation.initialize(fichier, gui);
                break;
            case TEST_SCENARIOS:
                TestScenarios.initialize(fichier, gui);
                break;
            case TEST_DIJKSTRA:
                TestDijkstra.initialize(fichier, gui);
                break;
            case TEST_BASIQUE:
                TestBasique.initialize(fichier, gui);
                break;
            case TEST_AVANCE:
                TestAvance.initialize(fichier, gui);
                break;
            default:
                break;
        }
    }
}
