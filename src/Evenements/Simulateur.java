package Evenements;

import java.util.PriorityQueue;

import Exceptions.CellOutOfMapException;
import Exceptions.NoFireException;
import Exceptions.NoWaterException;

public class Simulateur {

    private long dateSimulation;
    private PriorityQueue<Evenement> evenements;

    public Simulateur() {
        this.dateSimulation = 0;
        this.evenements = new PriorityQueue<Evenement>();
    }

    /**
     * @return long : Date courante de la simulation
     */
    public long getDateSimulation() {
        return dateSimulation;
    }

    /**
     * Redémarre le simulateur. Appelée seulement par la fonction {@code restart} de {@code Simulation.java}
     */
    public void restart() {
        this.dateSimulation = 0;
        this.evenements.clear();
    }

    /**
     * Incrémente la date du simulateur. Appelée seulement par la fonction {@code next} de {@code Simulation.java}
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
        this.evenements.add(e);
    }

    /**
     * Méthode exécutant tout les évènements pour la date courante définie dans {@code dateSimulation}
     * 
     * @throws CellOutOfMapException - Dans le cas où l'évènement est {@code EventMouvement} mais sur une case en dehors de la carte
     * @throws NoFireException - Dans le cas où l'évènement est {@code EventIntervenir} mais sur une case qui n'est pas en feu
     * @throws NoWaterException - Dans le cas où l'évènement est {@code EventRemplir} mais qu'il n'y a pas d'eau à proximité
     */
    public void execute() throws CellOutOfMapException, NoFireException, NoWaterException {
        if (!simulationTerminee()) {
            Evenement event = this.evenements.element();
            while (event.getDate() == this.dateSimulation) {
                event = this.evenements.poll();
                event.execute();
                if (!simulationTerminee())
                    event = this.evenements.element();
                else
                    break;
            }
        }
    }

    /**
     * Méthode renvoyant un booléen correspondant à la finition (ou non) de la simulation
     * 
     * @return boolean : 1 si la simulation est terminée, 0 sinon
     */
    public boolean simulationTerminee() {
        return (this.evenements.size() == 0);
    }

    /**
     * @return String : Retourne le texte incluant la {@code dateSimulation} et la liste des {@code evenements}
     */
    @Override
    public String toString() {
        return "Simulateur [dateSimulation=" + this.dateSimulation + ", evenementsActuels=" + this.evenements
                + "]";
    }
}