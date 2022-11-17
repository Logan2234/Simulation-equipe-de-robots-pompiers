package Donnees;

import java.util.List;

import Donnees.Robot.Robot;

public class DonneesSimulation {
    private final Carte carte;
    private final List<Incendie> incendies;
    private final List<Robot> robots;

    /**
     * @param carte     : carte de notre simulation
     * @param incendies : liste des incendies de départ de la simulation
     * @param robots    : table des robots à disposition
     */
    public DonneesSimulation(Carte carte, List<Incendie> incendies, List<Robot> robots) {
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
     * @return List<Incendie> : liste des incendies dans la simulation
     */
    public List<Incendie> getIncendies() {
        return incendies;
    }

    /**
     * @return List<Robot> : liste des robots à disposition
     */
    public List<Robot> getRobots() {
        return robots;
    }

    /**
     * @return String : comporte la {@code carte}, la liste des {@code incendies},
     *         la table des {@code robots}
     */
    @Override
    public String toString() {
        return "DonneesSimulation [carte=" + carte + ", incendies=" + incendies + ", robots=" + robots
                + "]";
    }

}
