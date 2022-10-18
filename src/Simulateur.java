import java.util.*;

public class Simulateur {
    private long dateSimulation;
    private Queue<Evenement> queue = new LinkedList<>();

    public Simulateur(){
        this.dateSimulation = 0;
    }

    public long getDateSimulation(){
        return this.dateSimulation;
    }

    public void restart(){
        this.dateSimulation = 0;

        // On vide la queue
        Evenement poubelle = new Evenement();
        while (!simulationTerminee()){
            poubelle = this.queue.poll();
        }
    }

    void incrementeDate(){
        this.dateSimulation = this.dateSimulation + 1
    }

    void ajouteEvenement(Evenement e){
        this.queue.add(e);
    }

    void execute(){
        Evenement event = new Evenement();
        while (!simulationTerminee()){
            event = this.queue.poll();
            event.execute;
        }
    }

    boolean simulationTerminee(){
        if (this.queue.size() == 0){
            return true;
        }
        return false;
    }
}