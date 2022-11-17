package Evenements;

import Chefs.Chef;
import Exceptions.NoMoreFireException;

public class EventChefOrdonne extends Evenement {
    private final Chef chef;

    public EventChefOrdonne(long date, Chef chef) {
        super(date + 50);
        this.chef = chef;
    }

    @Override
    public void execute() throws NoMoreFireException {
        chef.strategie();
    }

    /**
     * @return String : Affiche la date de l'évènement
     */
    @Override
    public String toString() {
        return "EventChefOrdonne [date=" + this.getDate() + "]";
    }
}
