package Donnees;

import java.util.ArrayList;
import java.util.HashMap;

import Exceptions.IllegalCheminRobotException;
import Exceptions.PasDeCheminException;
import Exceptions.PasEauDansCarte;
import Autre.CalculPCC;
import Autre.Chemin;
import Donnees.Robot.Robot;
import Donnees.NatureTerrain;
import Evenements.Simulateur;

public class ChefAvance {
    private Carte carte;
    private DonneesSimulation donnees;
    private Simulateur simulateur;
    private HashMap<Incendie, Robot> incendies_rob;
    private ArrayList<Robot> occupes;
    private CalculPCC calculateur;
    private ArrayList<Case> casesAvecEau;

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

        // On va déclarer les cases qui contiennent de l'eau 
        ArrayList<Case> casesAvecEau = new ArrayList<Case>();
        for (int i = 0; i < carte.getNbLignes(); i++){
            for (int j = 0; j < carte.getNbColonnes(); j++){
                if (carte.getCase(i, j).getNature() == NatureTerrain.EAU) casesAvecEau.add(carte.getCase(i, j));
            }
        }
        this.casesAvecEau = casesAvecEau;
    }

    public Chemin ouAllerRemplirReservoir(Robot robot) throws PasEauDansCarte, PasDeCheminException{ // TODO : Attention, un drone != robot terrestre
        if (casesAvecEau.size() == 0) throw new PasEauDansCarte();

        Chemin cheminARetourner = new Chemin();
        long tempsDeplacement = Long.MAX_VALUE;
        Case positionRobot = robot.getPosition();
        boolean ilYAUnChemin = false;

        for (Case caseEau : this.casesAvecEau){
            try {
                Chemin cheminVersEau = calculateur.dijkstra(positionRobot, caseEau, robot);

                if (cheminVersEau.getTempsChemin() < tempsDeplacement) {
                    tempsDeplacement = cheminVersEau.getTempsChemin();
                    cheminARetourner = cheminVersEau;
                    ilYAUnChemin = true;

                }

            } catch (PasDeCheminException e) {
                continue;
            }
        }

        if (!ilYAUnChemin) throw new PasDeCheminException();


        return cheminARetourner;

    }

    public void gestionIncendies(Incendie incendie){

        // Cherchons le robot le plus proche de l'incendie
        boolean robotTrouve = false;
        Robot robotAMobiliser = donnees.getRobots()[0]; // On initialise avec un robot random
        Chemin cheminAParcourir = new Chemin(); // On initialise avec un chemin random
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
            } else {
                if (incendie.getLitres() == 0) { // Si l'incendie est éteint
                    incendies_rob.remove(incendie);
                    occupes.remove(robot);
                }
                if (robot.getReservoir() == 0){ // Si le réservoir du robot est vide
                    try {
                        Chemin cheminVersEau = ouAllerRemplirReservoir(robotAMobiliser);
                        if (!occupes.contains(robot)) occupes.add(robot);
                        // TODO : executer ordre de aller remplir réservoir
                    } catch (PasEauDansCarte e) {
                        continue;
                    } catch (PasDeCheminException e) {
                        continue;
                    }
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
