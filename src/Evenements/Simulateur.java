package Evenements;

import java.util.PriorityQueue;

import Exceptions.NoFireException;
import Exceptions.NoMoreFireException;
import Exceptions.NoMoreRobotsException;
import Exceptions.NoWaterException;

public class Simulateur {

    private long dateSimulation;
    private PriorityQueue<Evenement> evenementsPQueue;

    /**
     * Va initialiser la date du simulateur à 0 et créer une PriorityQueue d'événement ordonnés par leur date.
     */
    public Simulateur() {
        this.dateSimulation = 0;
        this.evenementsPQueue = new PriorityQueue<>();
    }

    /**
     * @return long : date courante de la simulation
     */
    public long getDateSimulation() {
        return dateSimulation;
    }

    /**
     * Redémarre le simulateur. Appelée seulement par la fonction {@code restart} de
     * {@code Simulation.java}
     */
    public void restart() {
        this.dateSimulation = 0;
        this.evenementsPQueue.clear();
    }

    /**
     * Incrémente la date du simulateur. Appelée seulement par la fonction
     * {@code next} de {@code Simulation.java}
     */
    public void incrementeDate() {
        this.dateSimulation += 1;
    }

    /**
     * Ajoute un évènement à {@code PriorityQueue evenementsActuels}
     * 
     * @param e : événement à ajouter à la liste des événements
     */
    public void ajouteEvenement(Evenement e) {
        this.evenementsPQueue.add(e);
    }

    /**
     * Méthode exécutant tout les évènements pour la date courante définie dans
     * {@code dateSimulation}
     * 
     * @throws NoMoreFireException   Dans le cas où l'évènement est
     *                               {@code EventChefOrdonne} mais qu'il n'y a plus
     *                               de feu sur la carte. Exception renvoyée par le
     *                               {@code chef}
     * @throws NoMoreRobotsException Dans le cas où l'évènement est
     *                               {@code EventChefOrdonne} mais qu'il n'y a plus
     *                               de robots disponible sur la carte. Exception
     *                               renvoyée par le {@code chef}
     */
    public void execute() throws NoMoreFireException, NoMoreRobotsException {
        try {
            if (!simulationTerminee()) {
                Evenement event = evenementsPQueue.peek();
                while (event != null && event.getDate() <= dateSimulation) {
                    evenementsPQueue.poll().execute();
                    event = evenementsPQueue.peek();
                }
            }
        } catch (NoFireException | NoWaterException e) {
            // En utilisant Dijkstra on attérit jamais là car est sûr d'aller sur une case
            // en feu / à côté de l'eau
            System.out.println(e);
        }
    }

    /**
     * Méthode renvoyant un booléen selon l'état de la simulation
     * 
     * @return boolean : true si la simulation est terminée, false sinon
     */
    public boolean simulationTerminee() {
        return (this.evenementsPQueue.isEmpty());
    }

    /**
     * @return String : retourne le texte incluant la {@code dateSimulation} et la
     *         liste des {@code evenements}
     */
    @Override
    public String toString() {
        return "Simulateur [dateSimulation=" + this.dateSimulation + ", evenementsActuels=" + this.evenementsPQueue
                + "]";
    }
}