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
        this.incendies_rob = new HashMap<Incendie, Robot>();
        calculateur = new CalculPCC(donnees);
        for (Incendie incendie : donnees.getIncendies()){
            incendies_rob.put(incendie, null);
        }
    }

    

    public void donneOrdre(Robot robot, Incendie incendie){
        try{
            Chemin chemin = new Chemin();
            chemin = calculateur.dijkstra(robot.getPosition(), incendie.getPosition(), robot);
            chemin.creerEvenements(this.simulateur, robot); // le robot va jusqu'à l'incendie
            this.simulateur.ajouteEvenement(new EventIntervenir(simulateur.getDateSimulation(), robot, incendie)); // TODO voir si la date est la bonne
        } catch (IllegalPathException e){
            System.out.println(e);
        }
    }

    public void gestionIncendies(Incendie incendie){
        for (Robot robot : donnees.getRobots()){
            if (!occupes.contains(robot)) {
                occupes.add(robot);
                incendies_rob.put(incendie, robot);
                donneOrdre(robot, incendie);
            } else {
                if (robot.getReservoir() != 0 && !incendies_rob.containsValue(robot)) occupes.remove(robot); // Si le robot peut continuer, on l'efface
            }
        }
    }

    public void strategie() {
        while (!incendies_rob.isEmpty()) {
            for (Incendie incendie : incendies_rob.keySet()) {
                if (incendies_rob.get(incendie) == null) {
                    gestionIncendies(incendie);
                } else {
                    if (incendie.getLitres() <= 0) {
                        if (incendies_rob.get(incendie).getReservoir() > 0) occupes.remove(incendies_rob.get(incendie));
                        incendies_rob.remove(incendie);
                    } 
                }
            }
        }
        System.out.println("Tous les incendies sont éteints\n");
    }
