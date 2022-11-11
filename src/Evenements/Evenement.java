package Evenements;

public abstract class Evenement implements Comparable<Evenement> {
    private long date;

    public Evenement(long date) {
        this.date = date;
    }

    public long getDate() {
        return this.date;
    }

    public abstract void execute();

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
        if (o instanceof Evenement) {
            Evenement that = (Evenement) o;
            return date == that.date;
        }
        return false;
    }
}