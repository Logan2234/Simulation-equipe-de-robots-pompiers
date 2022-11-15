package Evenements;

import Exceptions.CellOutOfMapException;
import Exceptions.NoFireException;
import Exceptions.NoWaterException;

public abstract class Evenement implements Comparable<Evenement> {
    private long date;

    public Evenement(long date) {
        this.date = date;
    }

    /**
     * @return long : Renvoie la date à laquelle l'évènement s'exécute
     */
    public long getDate() {
        return this.date;
    }

    public abstract void execute() throws NoFireException, CellOutOfMapException, NoWaterException;

    @Override
    public int compareTo(Evenement e) {
        if (this.date > e.getDate())
            return 1;
        if (this.date < e.getDate())
            return -1;
        return 0;
    }
}