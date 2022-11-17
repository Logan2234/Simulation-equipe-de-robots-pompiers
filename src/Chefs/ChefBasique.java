package Chefs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import Autre.CalculPCC;
import Autre.Chemin;
import Donnees.DonneesSimulation;
import Donnees.Incendie;
import Donnees.Robot.Robot;
import Evenements.EventChefOrdonne;
import Evenements.EventIntervenir;
import Evenements.Simulateur;
import Exceptions.NoMoreFireException;
import Exceptions.NoMoreRobotsException;
import Exceptions.NoPathAvailableException;

/**
 * @param incendiesRob : Table de hachage qui a pour clé les incendies
 *                      non-éteints et en valeur le robot qui s'en occupe.
 * @param morts         : Table dynamique qui comporte l'ensemble des robots
 *                      morts (i.e qui n'ont plus d'eau dans le réservoir)
 */
public class ChefBasique extends Chef {

    private HashMap<Incendie, Robot> incendiesRob;
    private Set<Robot> morts;

    /**
     * Va implémenter la stratégie de base pour le chef pompier.
     * 
     * @param donnees    : Données utilisées pour la simulation
     * @param simulateur : Simulateur à initialiser avant
     */
    public ChefBasique(DonneesSimulation donnees, Simulateur simulateur) {
        super(donnees, simulateur);
        this.morts = new HashSet<>();
        this.incendiesRob = new HashMap<>();
        for (Incendie incendie : donnees.getIncendies())
            incendiesRob.put(incendie, null);
    }

    /**
     * Va donner l'ordre à {@code robot} d'éteindre {@code incendie} et programmer
     * en conséquence les événements dans le simulateur.
     * 
     * @param robot    : Il n'est ni occupé, ni mort
     * @param incendie : Il n'est affecté à aucun robot
     * 
     * @throws NoPathAvailableException Le robot choisi ne peut pas atteindre l'incendie
     *                              choisi
     */
    private void donneOrdre(Robot robot, Incendie incendie) throws NoPathAvailableException {
        // Création du chemin et des événements associés
        Chemin chemin = CalculPCC.dijkstra(donnees.getCarte(), robot.getPosition(), incendie.getPosition(), robot,
                simulateur.getDateSimulation());
        System.out.println("chemin calcul \n");
        chemin.creerEvenements(simulateur, robot);

        // Création de toutes les interventions unitaires sur l'incendie
        for (int i = 0; i <= Math.min(incendie.getLitres() / robot.getQteVersement(),
                            robot.getReservoir() / robot.getQteVersement()); i++)
            simulateur.ajouteEvenement(new EventIntervenir(robot.getLastDate(), robot, incendie));
    }

    /**
     * Va essayer d'éteindre {@code incendie} en appelant un robot à la fois et en
     * mettant à jour les tables {@code occupes} et {@code morts}.
     * 
     * @param incendie : Il ne doit avoir aucun robot affecté (dans
     *                 {@code incendies_rob})
     */
    @Override
    protected void gestionIncendies(Incendie incendie) {
        for (Robot robot : donnees.getRobots()) {
            // Si le robot choisi est disponible
            if (!occupes.contains(robot) && !morts.contains(robot)) {
                // On essaie de l'envoyer sur l'incendie, si cela échoue on en prend un autre
                try {
                    System.out.println(incendie);
                    donneOrdre(robot, incendie);
                    System.out.println(robot);
                    occupes.add(robot);
                    incendiesRob.put(incendie, robot);
                    break; // Si on a réussi, on ne va pas envoyer de second robot sur l'incendie
                } catch (NoPathAvailableException e) {
                    System.out.println(e);
                    continue;
                    // On n'a pas besoin d'afficher quoi que ce soit, juste on continue
                }
            }
        }
    }

    /**
     * Implémente la stratégie basique (pas de parallélisme, pas de remplissage
     * d'eau).
     * On va traiter un incendie à la fois, tant qu'on en a qui ne sont pas éteints.
     */
    @Override
    public void strategie() throws NoMoreFireException, NoMoreRobotsException {

        // S'il y a des incendies à éteindre et des robots avec de l'eau, on va choisir un incendie
        if (!incendiesRob.isEmpty() && donnees.getRobots().size() != morts.size()) {
            for (Incendie incendie : donnees.getIncendies()) {

                // Si il est déjà éteint, on ne va pas traiter son cas
                if (!incendiesRob.containsKey(incendie))
                    continue;

                // Si il est en cours de traitement, on regarde son état
                if (incendiesRob.get(incendie) != null) {
                    Robot robot = incendiesRob.get(incendie);

                    // Si le robot est vide il devient inutilisable
                    if (robot.getReservoir() == 0) {
                        incendiesRob.put(incendie, null);
                        occupes.remove(robot);
                        morts.add(robot);
                    }

                    // Si l'incendie est éteint, on met à jour incendies_rob
                    if (incendie.getLitres() <= 0) {
                        occupes.remove(robot);
                        incendiesRob.remove(incendie);
                    }
                }
                // Si on n'a pas d'affectation pour cet incendie, on va lui envoyer un robot
                else
                    gestionIncendies(incendie);
            }
            simulateur.ajouteEvenement(new EventChefOrdonne(simulateur.getDateSimulation(), this));
        } else if (incendiesRob.isEmpty()) {
            throw new NoMoreFireException();
        } else
            throw new NoMoreRobotsException();
    }
}