package Donnees;

import java.util.ArrayList;
import java.util.HashMap;

import Autre.CalculPCC;
import Autre.Chemin;
import Donnees.Robot.Robot;
import Evenements.EventIntervenir;
import Evenements.Simulateur;
import Exceptions.EmptyRobotsException;
import Exceptions.IllegalPathException;
import Exceptions.PasDeCheminException;

public class ChefBasiqueV2 {
    private DonneesSimulation donnees;
    private Simulateur simulateur;
    private HashMap<Incendie, Robot> incendies_rob;
    private ArrayList<Robot> occupes;
    private CalculPCC calculateur;
    private ArrayList<Robot> morts;


    /**
     * Va implémenter la stratégie de base pour le chef pompier
     * 
     * @param donnees - Données utilisées pour la simulation
     * @param simulateur - Simulateur initialisé avant
     */
    public ChefBasiqueV2(DonneesSimulation donnees, Simulateur simulateur){
        this.donnees = donnees;
        this.simulateur = simulateur;
        this.occupes = new ArrayList<Robot>();
        this.morts = new ArrayList<Robot>();
        this.incendies_rob = new HashMap<Incendie, Robot>();
        calculateur = new CalculPCC(donnees);
        for (Incendie incendie : donnees.getIncendies()){
            incendies_rob.put(incendie, null);
        }
    }

    

    public void donneOrdre(Robot robot, Incendie incendie) throws PasDeCheminException{
        try{
            Chemin chemin = new Chemin();
            chemin = calculateur.dijkstra(robot.getPosition(), incendie.getPosition(), robot);
            chemin.creerEvenements(this.simulateur, robot); // le robot va jusqu'à l'incendie
            this.simulateur.ajouteEvenement(new EventIntervenir(simulateur.getDateDernierEvenement(), robot, incendie)); // TODO voir si la date est la bonne
        } catch (IllegalPathException e){
            System.out.println(e);
        }
    }

    public void gestionIncendies(Incendie incendie){
        for (Robot robot : donnees.getRobots()){
            if (!occupes.contains(robot)) {
                if (robot.getReservoir() == 0) {
                    this.morts.add(robot);
                    continue;
                }
                try {
                    occupes.add(robot);
                    incendies_rob.put(incendie, robot);
                    donneOrdre(robot, incendie);
                } catch (PasDeCheminException e){
                    continue; //on va prendre un autre robot
                }
            } else {
                if (robot.getReservoir() == 0) {
                    occupes.remove(robot);
                    this.morts.add(robot);
                } else if (!incendies_rob.containsValue(robot)){
                    occupes.remove(robot); // Si le robot peut continuer, on l'efface
                } 
            }
        }
    }

    
    public void strategie() {
        while (!incendies_rob.isEmpty() && donnees.getRobots().length == morts.size()) { //gérer cas où les robots sont vidés
            for (Incendie incendie : incendies_rob.keySet()) {
                if (incendies_rob.get(incendie) == null) {
                    gestionIncendies(incendie);
                } else {
                    if (incendie.getLitres() <= 0) { // si l'incendie est éteint
                        if (incendies_rob.get(incendie).getReservoir() > 0 || incendies_rob.get(incendie).getReservoir() == -1){
                            occupes.remove(incendies_rob.get(incendie)); //si il reste de l'eau dans le réservoir
                        }
                        incendies_rob.remove(incendie);
                    // } else {
                    //     incendies_rob.put(incendie, null);
                    }
                }
            }
        } if (incendies_rob.isEmpty()) {System.out.println("Tous les incendies sont éteints\n");}
        else {System.out.println("Tous les robots sont vides\n");}
    }
}
