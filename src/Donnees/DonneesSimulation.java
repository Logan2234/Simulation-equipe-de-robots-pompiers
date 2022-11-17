package Donnees;

import java.util.Arrays;
import java.util.Set;

import Donnees.Robot.Robot;

public class DonneesSimulation {
    private final Carte carte;
    private final Set<Incendie> incendies; // TODO: Mettre une ArrayList pour les robots et les incendies
    private final Robot robots[];

    /**
     * @param carte     : carte de notre simulation
     * @param incendies : liste des incendies de départ de la simulation
     * @param robots    : table des robots à disposition
     */
    public DonneesSimulation(Carte carte, Set<Incendie> incendies, Robot[] robots) {
        this.carte = carte;
        this.incendies = incendies;
        this.robots = robots;
    }

    /**
     * @return Carte : carte actuelle de la simulation
     */
    public Carte getCarte() {
        return carte;
    }

    /**
     * @return List<Incendie> : liste des incendies actuels dans la simulation
     */
    public Set<Incendie> getIncendies() {
        return incendies;
    }

    /**
     * @return Robot[] : table des robots à disposition
     */
    public Robot[] getRobots() {
        return robots;
    }

    /**
     * @return String : comporte la {@code carte}, la liste des {@code incendies},
     *         la table des {@code robots}
     */
    @Override
    public String toString() {
        return "DonneesSimulation [carte=" + carte + ", incendies=" + incendies + ", robots=" + Arrays.toString(robots)
                + "]";
    }

}
