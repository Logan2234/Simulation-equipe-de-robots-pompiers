package Donnees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import Exceptions.IllegalCheminRobotException;
import Exceptions.PasDeCheminException;

import Autre.CalculPCC;
import Autre.Chemin;
import Donnees.Robot.Robot;
import Evenements.EventIntervenir;
import Evenements.Simulateur;

public class ChefBasique {
    private Carte carte;
    private DonneesSimulation donnees;
    private Simulateur simulateur;
    private HashMap<Incendie, Robot> incendies_rob;
    private HashMap<Robot, Long> occupes;
    private CalculPCC calculateur;

    public ChefBasique(Carte carte, DonneesSimulation donnees, Simulateur simulateur){
        this.carte = carte;
        this.donnees = donnees;
        this.simulateur = simulateur;
        this.occupes = new HashMap<Robot,Long>();
        this.incendies_rob = new HashMap<Incendie, Robot>();
        calculateur = new CalculPCC(donnees, simulateur);
        for (Incendie incendie :donnees.getIncendies()){
            incendies_rob.put(incendie, null);
        }
    }

    public void donneOrdre(Robot robot, Incendie incendie, long date) throws PasDeCheminException {
        try{
            Chemin chemin = new Chemin();
            chemin = calculateur.dijkstra(robot.getPosition(), incendie.getPosition(), robot);
            incendies_rob.put(incendie, robot);
            chemin.creerEvenements(this.simulateur, robot);
            occupes.put(robot, date + chemin.getTempsChemin());
            if (robot.getReservoir() >= incendie.getLitres()){
                incendies_rob.remove(incendie);
            } else { // si le robot est vidé
                occupes.put(robot, Long.MAX_VALUE);
            }
            simulateur.ajouteEvenement(new EventIntervenir(date + chemin.getTempsChemin(), robot, incendie));
        } catch (IllegalCheminRobotException e){
            System.out.println(e);
        }
    }

    private boolean robotsVides(){
        for (Robot robot:donnees.getRobots()){
            if (robot.getReservoir() != 0){
                return false;
            } 
        }
        return true;
    }

    public void gestionIncendies(long date){
        for (Incendie incendie : incendies_rob.keySet()){
            
            Robot robotIncendie = incendies_rob.get(incendie);

            // Si aucun robot est attribué : 
            if (robotIncendie == null){
                for (Robot robot : this.donnees.getRobots()){

                    // Si le robot qu'on cherche à attribué n'est pas occupé :
                    if (occupes.containsKey(robot) && occupes.get(robot) < date) {
                        occupes.remove(robot);
                    }
                    if (!occupes.containsKey(robot)){
                        try {
                            donneOrdre(robot, incendie, date);
                            break; // et on arrête de chercher un robot puisqu'on l'a déjà trouvé
                        } catch (PasDeCheminException e){
                            continue;
                        }
                    }
                }
            }
        }
    }

    public void strategie(int n){
        long date = simulateur.getDateSimulation();
        while (!incendies_rob.isEmpty() && !robotsVides()){
            gestionIncendies(date);
            simulateur.incrementeDate(n);
        }
    }
}
