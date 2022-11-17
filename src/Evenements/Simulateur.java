package Evenements;

import java.util.PriorityQueue;

import Exceptions.CellOutOfMapException;
import Exceptions.NoFireException;
import Exceptions.NoMoreFireException;
import Exceptions.NoWaterException;

public class Simulateur {

    private long dateSimulation;
    private PriorityQueue<Evenement> evenementsPQueue;

    public Simulateur() {
        this.dateSimulation = 0;
        this.evenementsPQueue = new PriorityQueue<Evenement>();
    }

    /**
     * @return long : Date courante de la simulation
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
     * @param e : Évènement à ajouter à la liste des évènements
     */
    public void ajouteEvenement(Evenement e) {
        this.evenementsPQueue.add(e);
    }

    /**
     * Méthode exécutant tout les évènements pour la date courante définie dans
     * {@code dateSimulation}
     * 
     * @throws CellOutOfMapException - Dans le cas où l'évènement est
     *                               {@code EventMouvement} mais sur une case en
     *                               dehors de la carte
     * @throws NoFireException       - Dans le cas où l'évènement est
     *                               {@code EventIntervenir} mais sur une case qui
     *                               n'est pas en feu
     * @throws NoWaterException      - Dans le cas où l'évènement est
     *                               {@code EventRemplir} mais qu'il n'y a pas d'eau
     *                               à proximité
     * @throws NoMoreFireException   - Dans le cas où l'évènement est
     *                               {@code EventChefOrdonne} mais qu'il n'y a plus
     *                               de feu sur la carte
     */
    public void execute() throws CellOutOfMapException, NoFireException, NoWaterException, NoMoreFireException {
        if (!simulationTerminee()) {
            Evenement event = evenementsPQueue.peek();
            while (event != null && event.getDate() <= dateSimulation) {
                evenementsPQueue.poll().execute();
                event = evenementsPQueue.peek();
            }
        }
    }

    /**
     * Méthode renvoyant un booléen correspondant à la finition (ou non) de la
     * simulation
     * 
     * @return boolean : 1 si la simulation est terminée, 0 sinon
     */
    public boolean simulationTerminee() {
        return (this.evenementsPQueue.size() == 0);
    }

    /**
     * @return String : Retourne le texte incluant la {@code dateSimulation} et la
     *         liste des {@code evenements}
     */
    @Override
    public String toString() {
        return "Simulateur [dateSimulation=" + this.dateSimulation + ", evenementsActuels=" + this.evenementsPQueue
                + "]";
    }
}