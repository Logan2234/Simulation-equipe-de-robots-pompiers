package Evenements;

import java.util.PriorityQueue;

import Exceptions.CaseOutOfMapException;
import Exceptions.NoFireException;
import Exceptions.NoWaterException;

public class Simulateur {

    private long dateSimulation; // TODO: Changer dans tout les fichiers les int en long lorsqu'on parle de date
    private PriorityQueue<Evenement> evenementsActuels = new PriorityQueue<Evenement>();

    public Simulateur() {
        this.dateSimulation = 0;
    }

    public long getDateSimulation() {
        return this.dateSimulation;
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

    public void execute() throws NoFireException, CaseOutOfMapException, NoWaterException {
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
        return "Simulateur [dateSimulation=" + dateSimulation + ", evenementsActuels=" + evenementsActuels + "]";
    }
}