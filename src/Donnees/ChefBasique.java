package Donnees;

import java.util.ArrayList;
import java.util.HashMap;

import Donnees.Robot.Robot;

public class ChefBasique {
    private Carte carte;
    private DonneesSimulation donnees;
    private HashMap<Incendie, Robot> incendies_rob;
    private ArrayList<Robot> occupes;

    public ChefBasique(Carte carte, DonneesSimulation donnees){
        this.carte = carte;
        this.donnees = donnees;
        this.occupes = new ArrayList<Robot>();
        this.incendies_rob = new HashMap<Incendie, Robot>();
        for (Incendie incendie :donnees.getIncendies()){
            incendies_rob.put(incendie, null);
        }
    }
}
