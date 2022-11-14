package Donnees;

import java.util.ArrayList;
import java.util.HashMap;

import Autre.CalculPCC;
import Autre.Chemin;
import Donnees.Robot.Robot;
import Evenements.EventIntervenir;
import Evenements.Simulateur;
import Exceptions.EmptyRobotsException;
import Exceptions.IllegalPathException;
import Exceptions.PasDeCheminException;

/**
 * @param donnees       : Données utilisées pour la simulation
 * @param simulateur    : Simulateur à initialiser avant
 * @param incendies_rob : Table de hachage qui a pour clé les incendies
 *                      non-éteints et en valeur le robot qui s'en occupe.
 * @param calculateur   : Calculateur de chemin qui dépendra des données et du
 *                      simulateur
 * @param morts         : Table dynamique qui comporte l'ensemble des robots
 *                      morts (i.e qui n'ont plus d'eau dans le réservoir)
 */
public class ChefBasique {

    private DonneesSimulation donnees;
    private Simulateur simulateur;
    private HashMap<Incendie, Robot> incendies_rob;
    private ArrayList<Robot> occupes;
    private CalculPCC calculateur;
    private ArrayList<Robot> morts;

    /**
     * Va implémenter la stratégie de base pour le chef pompier.
     * 
     * @param donnees    : Données utilisées pour la simulation
     * @param simulateur : Simulateur à initialiser avant
     */
    public ChefBasique(DonneesSimulation donnees, Simulateur simulateur) {
        this.donnees = donnees;
        this.simulateur = simulateur;
        this.occupes = new ArrayList<Robot>();
        this.morts = new ArrayList<Robot>();
        this.incendies_rob = new HashMap<Incendie, Robot>();
        calculateur = new CalculPCC(donnees);
        for (Incendie incendie : donnees.getIncendies()) {
            incendies_rob.put(incendie, null);
        }
    }

    /**
     * Va donner l'ordre à {@code robot} d'éteindre {@code incendie} et programmer
     * en conséquence les événements dans le simulateur.
     * 
     * @param robot    : Il n'est ni occupé, ni mort
     * @param incendie : Il n'est affecté à aucun robot
     * 
     * @throws PasDeCheminException Le robot choisi ne peut pas atteindre l'incendie
     *                              choisi
     */
    private void donneOrdre(Robot robot, Incendie incendie) throws PasDeCheminException {
        try {
            Chemin chemin = new Chemin();
            chemin = calculateur.dijkstra(robot.getPosition(), incendie.getPosition(), robot, robot.getLastDate());
            chemin.creerEvenements(this.simulateur, robot); // le robot va jusqu'à l'incendie
            if (robot.getCapacite() != -1) { // si ce n'est pas un robot à pattes
                for (int i = 0; i < Math.min(incendie.getLitres() / robot.getQteVersement(),
                        robot.getReservoir() / robot.getQteVersement()); i++) {
                    simulateur.ajouteEvenement(new EventIntervenir(robot.getLastDate(), robot, incendie));
                }
            } else { // le robot à pattes va verser son eau
                for (int i = 0; i < incendie.getLitres() / robot.getQteVersement(); i++) {
                    simulateur.ajouteEvenement(new EventIntervenir(robot.getLastDate(), robot, incendie));
                }
            }
        } catch (IllegalPathException e) {
            System.out.println(e);
        }
    }

    /**
     * Va essayer d'éteindre {@code incendie} en appelant un robot à la fois et en
     * mettant à jour les tables
     * {@code occupes} et {@code morts}.
     * 
     * @param incendie : Il ne doit avoir aucun robot affecté (dans
     *                 {@code incendies_rob})
     */
    private void gestionIncendies(Incendie incendie) {
        for (Robot robot : donnees.getRobots()) {
            // Si le robot choisi est disponible
            if (!occupes.contains(robot)) {
                // Si son réservoir est vide, il est mort et on en cherche un autre
                if (robot.getReservoir() == 0) {
                    if (!morts.contains(robot)) { // nécessaire pour ne pas avoir de doublons
                        this.morts.add(robot);
                    }
                    continue;
                }
                // Sinon on essaie de l'envoyer sur l'incendie, si cela échoue on va prendre un
                // autre robot
                try {
                    System.out.println(robot);
                    System.out.println(incendie);
                    occupes.add(robot);
                    incendies_rob.put(incendie, robot);
                    donneOrdre(robot, incendie);
                    break; // Si on a réussi, on ne va pas envoyer de second robot sur l'incendie
                } catch (PasDeCheminException e) {
                    continue;
                }
                // Si il est occupé
            } else {
                // Si il est mort
                if (robot.getReservoir() == 0 && !morts.contains(robot)) {
                    occupes.remove(robot);
                    morts.add(robot);
                    // Si il lui reste de l'eau, il est de nouveau disponible
                } else if (!incendies_rob.containsValue(robot)) {
                    occupes.remove(robot); // Si le robot peut continuer, on l'efface
                }
            }
        }
    }

    /**
     * Implémente la stratégie basique (pas de parallélisme, pas de remplissage
     * d'eau).
     * On va traiter un incendie à la fois, tant qu'on en a qui ne sont pas éteints.
     * 
     * @throws EmptyRobotsException Si tous nos robots sont vidés avant d'avoir pu
     *                              éteindre les incendies
     */
    public void strategie() throws EmptyRobotsException {
        // Tant qu'il y a des incendies à éteidnre et des robots avec de l'eau
        while (!incendies_rob.isEmpty() && donnees.getRobots().length != morts.size()) {
            for (Incendie incendie : donnees.getIncendies()) {
                // Si il est déjà éteint, on ne va pas traiter son cas
                if (!incendies_rob.containsKey(incendie)) {
                    continue;
                }
                // Si on n'a pas d'affectation pour cet incendie, on va lui envoyer un robot
                if (incendies_rob.get(incendie) == null) {
                    gestionIncendies(incendie);
                    // Si il est en cours de traitement, on regarde son état
                } else {
                    Robot robot = incendies_rob.get(incendie);
                    // Si le robot s'est vidé, on met à jour les tables
                    if (robot.getReservoir() == 0) {
                        occupes.remove(robot);
                        incendies_rob.put(incendie, null);
                        if (!morts.contains(robot)) { // nécessaire pour ne pas avoir de doublons
                            morts.add(robot);
                        }
                    }
                    // Si l'incendie est éteint, on met à jour incendies_rob
                    if (incendie.getLitres() <= 0) {
                        // Si il reste de l'eau dans le réservoir
                        if (robot.getReservoir() > 0 || robot.getReservoir() == -1) {
                            occupes.remove(robot);
                        }
                        incendies_rob.remove(incendie);
                    }
                }
            }
        }
        if (incendies_rob.isEmpty()) {
            System.out.println("Tous les incendies sont éteints\n");
        } else {
            throw new EmptyRobotsException();
        }
    }
}
