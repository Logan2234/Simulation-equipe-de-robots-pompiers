package Evenements;

import Chefs.Chef;
import Exceptions.NoMoreFireException;
import Exceptions.NoMoreRobotsException;

public class EventChefOrdonne extends Evenement {
    private final Chef chef;

    /**
     * Va incrémenter la date d'un pas (de 50 ici) puis qui va actualiser le chef et
     * 
     * @param date : date actuelle de la simulation
     * @param chef : chef choisi dans les tests
     */
    public EventChefOrdonne(long date, Chef chef) {
        super(date + 50);
        this.chef = chef;
    }

    
    /** 
     * Rappelle à nouveau la stratégie du chef choisi
     * 
     * @throws NoMoreFireException Les feux ont été éteints
     * @throws NoMoreRobotsException Les robots ont été vidés
     */
    @Override
    public void execute() throws NoMoreFireException, NoMoreRobotsException {
        chef.strategie();
    }

    /**
     * @return String : affiche la date de l'évènement
     */
    @Override
    public String toString() {
        return "EventChefOrdonne [date=" + this.getDate() + "]";
    }
}
