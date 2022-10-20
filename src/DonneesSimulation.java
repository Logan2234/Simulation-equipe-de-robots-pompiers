import java.util.Arrays;
import java.util.LinkedList;

public class DonneesSimulation {    
    private Carte carte;
    private LinkedList<Incendie> incendies;
    private Robot robots[];
    
    public DonneesSimulation(Carte carte, LinkedList<Incendie> incendies, Robot[] robots) {
        this.carte = carte;
        this.incendies = incendies;
        this.robots = robots;
    }
    
    public Carte getCarte() {
        return carte;
    }
    
    public LinkedList<Incendie> getIncendies() {
        return incendies;
    }

    public Robot[] getRobots() {
        return robots;
    }
    
    @Override
    public String toString() {
        return "DonneesSimulation [carte=" + carte + ", incendies=" + incendies + ", robots=" + Arrays.toString(robots)
                + "]";
    }

}
