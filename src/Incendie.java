public class Incendie {

    private Case position;
    private int litres_requis;

    public Incendie(Case position, int litres_requis) {
        this.position = position;
        this.litres_requis = litres_requis;
    }

    @Override
    public String toString() {
        return "Incendie [position=" + position + ", litres_requis=" + litres_requis + "]";
    }

}
