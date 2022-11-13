package Donnees;

import java.util.HashMap;
import java.lang.Math;

import Exceptions.IllegalCheminRobotException;
import Exceptions.PasDeCheminException;

import Autre.CalculPCC;
import Autre.Chemin;
import Donnees.Robot.Robot;
import Evenements.EventIntervenir;
import Evenements.Simulateur;

public class ChefBasique {
    private DonneesSimulation donnees;
    private Simulateur simulateur;
    private HashMap<Incendie, Robot> incendies_rob;
    private HashMap<Robot, Long> occupes;
    private CalculPCC calculateur;

    public ChefBasique(DonneesSimulation donnees, Simulateur simulateur){
        this.donnees = donnees;
        this.simulateur = simulateur;
        this.occupes = new HashMap<Robot,Long>();
        this.incendies_rob = new HashMap<Incendie, Robot>();
        calculateur = new CalculPCC(donnees, simulateur);
        for (Incendie incendie : donnees.getIncendies()){
            incendies_rob.put(incendie, null);
        }
    }

    public long donneOrdre(Robot robot, Incendie incendie, long date) throws PasDeCheminException {
        try{
            // Calcul du plus court chemin entre le robot et l'incendie
            Chemin chemin = new Chemin();
            chemin = calculateur.dijkstra(robot.getPosition(), incendie.getPosition(), robot);
            // Si on a un chemin (car on a pas attrappé d'exception) on va attribuer l'incendie au robot
            incendies_rob.put(incendie, robot);
            chemin.creerEvenements(this.simulateur, robot); // le robot va jusqu'à l'incendie
            long new_date = date + chemin.getTempsChemin() + Math.min(robot.getReservoir(),incendie.getLitres())*robot.getTmpVersement()/robot.getQteVersement();
            occupes.put(robot, new_date); // le robot est donc occupé à partir de l'instant t jusqu'à la fin du déplacement et du versement de l'eau
            // System.out.println(chemin.toString());
            if (robot.getReservoir() >= incendie.getLitres()){ // Si on a réussi à éteindre l'incendie
                incendies_rob.remove(incendie);
            } 
            if (robot.getReservoir() <= incendie.getLitres()) { // Si le robot est vidé, il est occupé jusqu'à la fin
                occupes.put(robot, Long.MAX_VALUE);
            }
            simulateur.ajouteEvenement(new EventIntervenir(date + chemin.getTempsChemin(), robot, incendie));
            return new_date;
            
        } catch (IllegalCheminRobotException e){
            System.out.println(e);
            return date;
        }
    }

    private boolean robotsVides(){
        for (Robot robot:donnees.getRobots()){
            if (occupes.containsKey(robot) && occupes.get(robot) < Long.MAX_VALUE){
                return false;
            } else if (!occupes.containsKey(robot)){
                return false;
            }
        }
        return true;
    }

    public long gestionIncendies(long date){
        for (Incendie incendie : donnees.getIncendies()){

            Robot robotIncendie = incendies_rob.get(incendie);
            System.out.println(incendie.toString());
            // Si aucun robot n'est attribué : 
            if (robotIncendie == null){
                for (Robot robot : this.donnees.getRobots()){
                    System.out.println(robot.toString());
                    // Si le robot a fini sa tâche, il n'est plus occupé
                    if (occupes.containsKey(robot) && occupes.get(robot) < date) {
                        occupes.remove(robot);
                    }
                    // Si le robot n'est pas occupé, on va l'envoyé éteindre l'incendie
                    if (!occupes.containsKey(robot)){
                        try {
                            date = donneOrdre(robot, incendie, date);
                            break; // et on arrête de chercher un robot puisqu'on l'a déjà trouvé
                        } catch (PasDeCheminException e){
                            // Si il n'y a pas de chemin disponible entre le robot et l'incendie, on va appeler un autre robot
                            continue;
                        }
                    }
                }
            }
        }
        return date;
    }

    public void strategie(){
        //à chaque pas n, on va 
        long date = simulateur.getDateSimulation();
        while (!incendies_rob.isEmpty() && !robotsVides()){
            date = gestionIncendies(date);
        }
        if (robotsVides()) {
            System.out.println("Tous les robots sont vides\n"); //TODO : transformer en exception
            for (Robot robot : donnees)
        }
        if (incendies_rob.isEmpty()) {
            System.out.println("Tous les incendies sont éteints\n");
        }
    }
}
