package Evenements;

import java.util.PriorityQueue;

import Exceptions.CellOutOfMapException;
import Exceptions.NoFireException;
import Exceptions.NoWaterException;

public class Simulateur {

    private static long dateSimulation;
    private static long dateDernierEvenement;
    private static PriorityQueue<Evenement> evenementsActuels;

    public Simulateur() {
        dateSimulation = 0;
        dateDernierEvenement = 0;
        evenementsActuels = new PriorityQueue<Evenement>();
    }

    public static long getDateSimulation() {
        return dateSimulation;
    }

    public long getDateDernierEvenement() {
        return dateDernierEvenement;
    }

    public static void restart() {
        dateSimulation = 0;
        evenementsActuels.clear();
    }

    public static void incrementeDate() {
        dateSimulation += 1;
    }
    
    public static void addDate(long temps) {
        dateSimulation += temps;
    }

    public static void updateDate(long temps){
        dateSimulation -= temps;
    }

    public void ajouteEvenement(Evenement e) {
        evenementsActuels.add(e);
        dateDernierEvenement = e.getDate();
    }

    public static void execute() throws NoFireException, CellOutOfMapException, NoWaterException {
        if (evenementsActuels.size() > 0) {
            Evenement event = evenementsActuels.element();
            while (event.getDate() == dateSimulation) {
                event = evenementsActuels.poll();
                event.execute();
                if (!simulationTerminee())
                    event = evenementsActuels.element();
                else
                    break;
            }
        }
    }

    public static boolean simulationTerminee() {
        return (evenementsActuels.size() == 0);
    }

    @Override
    public String toString() {
        return "Simulateur [dateSimulation=" + dateSimulation + ", evenementsActuels=" + evenementsActuels + "]";
    }
}