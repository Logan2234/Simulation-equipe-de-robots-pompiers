package Donnees;

public class Incendie {

    private Case position;
    private int litres_requis;

    public Incendie(Case position, int litres_requis) {
        this.position = position;
        this.litres_requis = litres_requis;
    }

    public int getLitres() {
        return this.litres_requis;
    }

    public Case getPosition() {
        return this.position;
    }

    public void eteindre(int qteVersee) {
        litres_requis -= qteVersee;
    }

    @Override
    public String toString() {
        return "Incendie [position=" + position + ", litres_requis=" + litres_requis + "]";
    }

}
