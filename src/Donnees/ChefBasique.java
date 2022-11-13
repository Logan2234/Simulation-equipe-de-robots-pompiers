package Donnees;

import java.util.HashMap;

import java.lang.Math;

import Exceptions.IllegalCheminRobotException;
import Exceptions.PasDeCheminException;
import Exceptions.EmptyRobotsException;

import Autre.CalculPCC;
import Autre.Chemin;
import Donnees.Robot.Robot;
import Evenements.EventIntervenir;
import Evenements.Simulateur;

public class ChefBasique {
    private DonneesSimulation donnees;
    private Simulateur simulateur;
    private HashMap<Incendie, RobotLitres> incendies_rob;
    private HashMap<Robot, Long> occupes;
    private CalculPCC calculateur;

    public ChefBasique(DonneesSimulation donnees, Simulateur simulateur){
        this.donnees = donnees;
        this.simulateur = simulateur;
        this.occupes = new HashMap<Robot,Long>();
        this.incendies_rob = new HashMap<Incendie, RobotLitres>();
        calculateur = new CalculPCC(donnees, simulateur);
        for (Incendie incendie : donnees.getIncendies()){
            incendies_rob.put(incendie, new RobotLitres(null, incendie.getLitres()));
        }
    }

    
    /** 
     * Fonction donnant les ordres de mouvement et extinction de feux à un robot
     * 
     * @param robot                      - Robot qui devra subir les ordres
     * @param incendie                   - Incendie à éteindre
     * @param date                       - Date du début de l'action
     * @return long                      - // TODO
     * @throws PasDeCheminException      - Il n'existe pas de chemin possible entre le robot et l'incendie
     */
    private long donneOrdre(Robot robot, Incendie incendie, long date) throws PasDeCheminException {
        try{
            int litres_restants = incendies_rob.get(incendie).getLitres();
            System.out.println(litres_restants);
            // Calcul du plus court chemin entre le robot et l'incendie
            Chemin chemin = new Chemin();
            chemin = calculateur.dijkstra(robot.getPosition(), incendie.getPosition(), robot);
            // Si on a un chemin (car on a pas attrappé d'exception) on va attribuer l'incendie au robot
            incendies_rob.put(incendie, new RobotLitres(robot, litres_restants));
            chemin.creerEvenements(this.simulateur, robot); // le robot va jusqu'à l'incendie
            long new_date = date + chemin.getTempsChemin() + Math.min(robot.getReservoir(),litres_restants)*robot.getTmpVersement()/robot.getQteVersement();
            occupes.put(robot, new_date);
            if (robot.getReservoir() >= litres_restants){ // Si on a réussi à éteindre l'incendie
                incendies_rob.remove(incendie);
            } else {
                incendies_rob.put(incendie, new RobotLitres(null, litres_restants - robot.getReservoir()));
            }
            simulateur.ajouteEvenement(new EventIntervenir(date + chemin.getTempsChemin(), robot, incendie));
            return new_date;
            
        } catch (IllegalCheminRobotException e){
            System.out.println(e);
            return date;
        }
    }

    
    /** 
     * TODO
     * 
     * @return boolean
     */
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

    
    /** 
     * Fonction que, à chaque date introduite, décidira quel robot envoyer à quel incendie et mettra
     * en place la stratégie d'extinction à l'instant donné.
     * 
     * @param date     - date de la réalisation des actions
     * @return long    - // TODO : Pas compris
     */
    private long gestionIncendies(long date){
        for (Incendie incendie : donnees.getIncendies()){
            if (!incendies_rob.containsKey(incendie)){
                continue; // Si l'incendie est déjà éteint, on ne va pas envoyer de robot dessus
            }
            RobotLitres robotIncendie = incendies_rob.get(incendie);
            System.out.println(incendie.toString());
            // Si aucun robot n'est affecté à l'incendie : 
            if (robotIncendie.getRobot() == null){
                for (Robot robot : this.donnees.getRobots()){
                    System.out.println(robot.toString());
                    // Si le robot a fini sa tâche, il n'est plus occupé
                    if (occupes.containsKey(robot) && occupes.get(robot) < date) {
                        if (robot.getReservoir() <= 0) { // Si il a vidé son eau, il est occupé indéfiniment
                            occupes.put(robot, Long.MAX_VALUE);
                        } else { // Si il lui reste de l'eau, il n'est plus occupé et n'est plus rattaché à l'incendie
                            incendies_rob.put(incendie, new RobotLitres(null, incendies_rob.get(incendie).getLitres()));
                            occupes.remove(robot);
                        }
                    }
                    // Si le robot n'est pas occupé, on va l'envoyé éteindre l'incendie
                    if (!occupes.containsKey(robot)){
                        try {
                            date = donneOrdre(robot, incendie, date);
                            return date; // et on arrête de chercher un robot puisqu'on l'a déjà trouvé
                        } catch (PasDeCheminException e){
                            // S'il n'y a pas de chemin disponible entre le robot et l'incendie, on va appeler un autre robot
                            continue;
                        }
                    }
                }
            }
        }
        return date; //n'arrive que si on n'a aucun robot qui ne peut accéder à aucun incendie
    }

    
    /** 
     * Fonction executant la stratégie d'extinction des feux du Chef des Pompiers Basique
     * 
     * @throws EmptyRobotsException - La liste des robots est vide
     */
    public void strategie() throws EmptyRobotsException{
        long date = simulateur.getDateSimulation();
        while (!incendies_rob.isEmpty() && !robotsVides()){
            date = gestionIncendies(date);
        }
        if (incendies_rob.isEmpty()) {
            System.out.println("Tous les incendies sont éteints\n");
        } else if (robotsVides()) {
            throw new EmptyRobotsException();
        }
    }
}

/**
 * Sous-classe représentant un tuple robot, litres nécessaires (pour la hashmap incendies_rob)
 */
class RobotLitres {
    private Robot rob;
    private int litres;

    public RobotLitres(Robot i, int l) {
        rob = i;
        litres = l;
    }

    public Robot getRobot() {
        return rob;
    }

    public int getLitres() {
        return litres;
    }


    @Override
    public String toString(){
        return "[Robot " + rob.toString() + " : a besoin de " + litres + " L pour cet incendie]";
    }
}

