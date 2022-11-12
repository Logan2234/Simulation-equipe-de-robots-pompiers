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

    //! Il faudra compléter avec donneOrdre de Basique
    public void donneOrdre(Robot robot, Incendie incendie) throws PasDeCheminException{
        try{
            incendies_rob.put(incendie, robot);
            occupes.add(robot);
            Chemin chemin = new Chemin();
            chemin = calculateur.dijkstra(robot.getPosition(), incendie.getPosition(), robot);
            chemin.creerEvenements(this.simulateur, robot);

        } catch (IllegalCheminRobotException e){
            System.out.println(e);
        }

    }

    public void strategie(){
        while (!incendies_rob.isEmpty()){
            for (Incendie incendie : incendies_rob.keySet()){

            }
        }
    }
}
