package Evenements;

import Exceptions.NoFireException;
import Exceptions.NoMoreFireException;
import Exceptions.NoMoreRobotsException;
import Exceptions.NoWaterException;

public abstract class Evenement implements Comparable<Evenement> {
    private final long date;

    protected Evenement(long date) {
        this.date = date;
    }

    /**
     * @return long : Renvoie la date à laquelle l'évènement s'exécute
     */
    public long getDate() {
        return this.date;
    }

    public abstract void execute() throws NoFireException, NoWaterException, NoMoreFireException, NoMoreRobotsException;

    /**
     * @param e : Évènement à comparer
     * @return int : Comparaison des deux évènement en fonction de leur date
     */
    @Override
    public int compareTo(Evenement e) {
        if (this.date > e.getDate())
            return 1;
        if (this.date < e.getDate())
            return -1;
        return 0;
    }

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