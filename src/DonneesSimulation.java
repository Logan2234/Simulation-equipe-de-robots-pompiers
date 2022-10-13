import java.util.Arrays;

public class DonneesSimulation {
    
    private Carte carte;
    private Incendie incendies[];
    private Robot robots[];
    
    public DonneesSimulation(Carte carte, Incendie[] incendies, Robot[] robots) {
        this.carte = carte;
        this.incendies = incendies;
        this.robots = robots;
    }
    
    public Carte getCarte() {
        return carte;
    }
    
    public Incendie[] getIncendies() {
        return incendies;
    }

    public Robot[] getRobots() {
        return robots;
    }
    
    @Override
    public String toString() {
        return "DonneesSimulation [carte=" + carte + ", incendies=" + Arrays.toString(incendies) + ", robots="
                + Arrays.toString(robots) + "]";
    }
    
}
