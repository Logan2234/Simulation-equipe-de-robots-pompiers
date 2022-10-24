import java.util.*;

public class Simulateur {

    private long dateSimulation;
    private Queue<Evenement> evenementsActuels = new LinkedList<>();

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

    void incrementeDate() {
        this.dateSimulation += 1;
    }

    void ajouteEvenement(Evenement e) {
        this.evenementsActuels.add(e);
    }

    void execute() throws IllegalArgumentException {
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

    boolean simulationTerminee() {
        return this.evenementsActuels.size() == 0;
    }

    @Override
    public String toString() {
        return "Simulateur [dateSimulation=" + dateSimulation + ", evenementsActuels=" + evenementsActuels + "]";
    }

}