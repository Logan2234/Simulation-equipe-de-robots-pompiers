package Evenements;

import Exceptions.NoFireException;
import Exceptions.NoMoreFireException;
import Exceptions.NoMoreRobotsException;
import Exceptions.NoWaterException;

public abstract class Evenement implements Comparable<Evenement> {
    private final long date;

    /**
     * @param date : date où se situe l'événement
     */
    protected Evenement(long date) {
        this.date = date;
    }

    /**
     * @return long : renvoie la date à laquelle l'évènement s'exécute
     */
    public long getDate() {
        return this.date;
    }

    public abstract void execute() throws NoFireException, NoWaterException, NoMoreFireException, NoMoreRobotsException;

    /**
     * @param e : évènement à comparer
     * @return int : 1 si l'événement e se passe avant, -1 sinon
     */
    @Override
    public int compareTo(Evenement e) {
        if (this.date > e.getDate())
            return 1;
        if (this.date < e.getDate())
            return -1;
        return 0;
    }

    
    /** 
     * Méthode equals qui va vérifier si la date est identique
     * 
     * @param o : objet à comparer
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Evenement evenement = (Evenement) o;
        return date == evenement.date;
    }
}