import java.util.*;

public class Simulateur {
    private long dateSimulation;
    Queue<Evenement> queue = new LinkedList<>();

    public Simulateur(){
        this.dateSimulation = 0;
    }

    private void incrementeDate(){
        this.dateSimulation = this.dateSimulation + 1;
    }

    void ajouteEvenement(Evenement e){
        this.queue.add(e);
    }

    private boolean simulationTerminee(){
        if (this.queue.size() == 0){
            return true;
        }
        return false;
    }
}