package Evenements;

import java.util.PriorityQueue;

public class Simulateur {

    private long dateSimulation;
    private PriorityQueue<Evenement> evenementsActuels = new PriorityQueue<Evenement>();

    public Simulateur() {
        this.dateSimulation = 0;
    }

    public long getDateSimulation() {
        return this.dateSimulation;
    }

    public void restart() {
        this.dateSimulation = 0;
        // On vide la queue
        while (!simulationTerminee())
            this.evenementsActuels.poll();
    }

    public void incrementeDate() {
        this.dateSimulation += 1;
    }

    public void ajouteEvenement(Evenement e) {
        this.evenementsActuels.add(e);
    }

    public void execute() throws IllegalArgumentException {
        Evenement event;
        while (!simulationTerminee()) {
            event = this.evenementsActuels.element();
            if (event.getDate() == this.dateSimulation) {
                event = this.evenementsActuels.poll();
                event.execute();
                if (!simulationTerminee())
                    event = this.evenementsActuels.element();
            } else
                break;
        }
    }

    public boolean simulationTerminee() {
        return this.evenementsActuels.size() == 0;
    }

    @Override
    public String toString() {
        return "Simulateur [dateSimulation=" + dateSimulation + ", evenementsActuels=" + evenementsActuels + "]";
    }
}