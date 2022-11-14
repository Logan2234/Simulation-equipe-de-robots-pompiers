package Evenements;

import java.util.PriorityQueue;

import Exceptions.CellOutOfMapException;
import Exceptions.NoFireException;
import Exceptions.NoWaterException;

public class Simulateur {

    private long dateSimulation;
    private PriorityQueue<Evenement> evenementsActuels;

    public Simulateur() {
        this.dateSimulation = 0;
        this.evenementsActuels = new PriorityQueue<Evenement>();
    }

    public long getDateSimulation() {
        return dateSimulation;
    }

    public void restart() {
        this.dateSimulation = 0;
        this.evenementsActuels.clear();
    }

    public void incrementeDate() {
        this.dateSimulation += 1;
    }

    public void incrementeDate(long n) {
        this.dateSimulation += n;
    }

    public void ajouteEvenement(Evenement e) {
        this.evenementsActuels.add(e);
    }

    public void execute() throws NoFireException, CellOutOfMapException, NoWaterException {
        if (this.evenementsActuels.size() > 0) {
            Evenement event = this.evenementsActuels.element();
            while (event.getDate() == this.dateSimulation) {
                event = this.evenementsActuels.poll();
                event.execute();
                if (!simulationTerminee())
                    event = this.evenementsActuels.element();
                else
                    break;
            }
        }
    }

    public boolean simulationTerminee() {
        return (this.evenementsActuels.size() == 0);
    }

    @Override
    public String toString() {
        return "Simulateur [dateSimulation=" + this.dateSimulation + ", evenementsActuels=" + this.evenementsActuels
                + "]";
    }
}