package Donnees;

import java.util.ArrayList;
import java.util.HashMap;

import Donnees.Robot.Robot;
import Evenements.Simulateur;

public class ChefBasique {
    private Carte carte;
    private DonneesSimulation donnees;
    private Simulateur simulateur;
    private HashMap<Incendie, Robot> incendies_rob;
    private ArrayList<Robot> occupes;

    public ChefBasique(Carte carte, DonneesSimulation donnees, Simulateur simulateur){
        this.carte = carte;
        this.donnees = donnees;
        this.simulateur = simulateur;
        this.occupes = new ArrayList<Robot>();
        this.incendies_rob = new HashMap<Incendie, Robot>();
        for (Incendie incendie :donnees.getIncendies()){
            incendies_rob.put(incendie, null);
        }
    }

    public void donneOrdre(Robot robot, Incendie incendie){
        incendies_rob.put(incendie, robot);
        occupes.add(robot);
    }

    public void gestionIncendies(){
        for (Incendie incendie :donnees.getIncendies()){
            Robot robotIncendie = incendies_rob.get(incendie);

            // Si aucun robot est attribué : 
            if (robotIncendie == null){
                for (Robot robot : this.donnees.getRobots()){

                    // Si le robot qu'on cherche à attribué n'est pas occupé :
                    if (!occupes.contains(robot)){
                        donneOrdre(robot, incendie);
                        break; // et on arrête de chercher un robot puisqu'on l'a déjà trouvé
                    }
                }
            }
        }
    }

    public void strategie(){
        while (!incendies_rob.isEmpty() ){ // TODO 

        }
    }
}
