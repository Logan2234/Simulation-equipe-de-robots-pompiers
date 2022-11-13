package Donnees;

import java.util.ArrayList;
import java.util.HashMap;

import Exceptions.IllegalCheminRobotException;
import Exceptions.PasDeCheminException;

import Autre.CalculPCC;
import Autre.Chemin;
import Donnees.Robot.Robot;
import Evenements.Simulateur;

public class ChefAvance {
    private Carte carte;
    private DonneesSimulation donnees;
    private Simulateur simulateur;
    private HashMap<Incendie, Robot> incendies_rob;
    private ArrayList<Robot> occupes;
    private CalculPCC calculateur;

    public ChefAvance(Carte carte, DonneesSimulation donnees, Simulateur simulateur){
        this.carte = carte;
        this.donnees = donnees;
        this.simulateur = simulateur;
        this.occupes = new ArrayList<Robot>();
        this.incendies_rob = new HashMap<Incendie, Robot>();
        calculateur = new CalculPCC(donnees, simulateur);
        for (Incendie incendie :donnees.getIncendies()){
            incendies_rob.put(incendie, null);
        }
    }

    public void gestionIncendies(Incendie incendie){

        // Cherchons le robot le plus proche de l'incendie
        boolean robotTrouve = false;
        Robot robotAMobiliser;
        Chemin cheminAParcourir;
        long tempsDeplacement = Long.MAX_VALUE;

        for (Robot robot : donnees.getRobots()){
            if (!occupes.contains(robot)){
                try {
                    Chemin chemin = new Chemin();
                    chemin = calculateur.dijkstra(robot.getPosition(), incendie.getPosition(), robot);

                    if (chemin.getTempsChemin() < tempsDeplacement){
                        robotTrouve = true;
                        robotAMobiliser = robot;
                        cheminAParcourir = chemin;
                        tempsDeplacement = chemin.getTempsChemin();
                    }
                } catch (PasDeCheminException e){
                    continue;
                }
            }
        }

        // Maintenant, si on a trouvé un robot, on l'envoie travailler 
        try {
            if (robotTrouve) cheminAParcourir.creerEvenements(this.simulateur, robotAMobiliser);
        } catch (IllegalCheminRobotException e){
            System.out.println(e);
        }

    }

    public void strategie(){
        while (!incendies_rob.isEmpty()){
            for (Incendie incendie : incendies_rob.keySet()){
                if (incendies_rob.get(incendie) == null){
                    gestionIncendies(incendie);
                }
            }
        }
    }
}
