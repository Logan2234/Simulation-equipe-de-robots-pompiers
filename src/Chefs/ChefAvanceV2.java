package Chefs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import Autre.CalculPCC;
import Autre.Chemin;
import Donnees.Carte;
import Donnees.Case;
import Donnees.Direction;
import Donnees.DonneesSimulation;
import Donnees.Incendie;
import Donnees.NatureTerrain;
import Donnees.Robot.Robot;
import Evenements.EventChefOrdonne;
import Evenements.EventIntervenir;
import Evenements.EventRemplir;
import Evenements.Simulateur;
import Exceptions.CellOutOfMapException;
import Exceptions.NoMoreFireException;
import Exceptions.NoWaterException;
import Exceptions.NoPathAvailableException;

public class ChefAvanceV2 extends Chef{
    private HashMap<Incendie, Set<Robot>> incendies_rob;
    private ArrayList<Case> casesAvecEau;
    private Carte carte;
    

    public ChefAvanceV2(DonneesSimulation donnees, Simulateur simulateur) {
        super(donnees, simulateur);
        this.incendies_rob = new HashMap<Incendie, Set<Robot>>();
        for (Incendie incendie : donnees.getIncendies()) {
            incendies_rob.put(incendie, new HashSet<>());
        }
        this.carte = donnees.getCarte();

        // On va déclarer les cases qui contiennent de l'eau
        ArrayList<Case> casesAvecEau = new ArrayList<Case>();
        for (int i = 0; i < carte.getNbLignes(); i++) {
            for (int j = 0; j < carte.getNbColonnes(); j++) {
                if (carte.getCase(i, j).getNature() == NatureTerrain.EAU)
                    casesAvecEau.add(carte.getCase(i, j));
            }
        }
        this.casesAvecEau = casesAvecEau;
    }

    public void strategie() throws NoMoreFireException{
        if (!incendies_rob.isEmpty()){
            for (Incendie incendie : donnees.getIncendies()){
                if (!incendies_rob.containsKey(incendie)) {
                    continue;
                }
                
                Set<Robot> robotsListe = incendies_rob.get(incendie);
                if (!robotsListe.isEmpty()){

                    for (Robot robot : robotsListe){
                        if (robot.getReservoir() == 0){
                            robotsListe.remove(robot);
                            try{
                                vaRemplirEau(robot);
                            } catch ( NoWaterException e){
                                System.out.println("Pas d'eau dans la carte. Le robot ne peux pas remplir le réservoir.");
                            }
                        }

                        if (incendie.getLitres() <= 0){
                            occupes.remove(robot);
                        }
                    }

                    if (incendie.getLitres() > 0) { 
                        incendies_rob.put(incendie, robotsListe);
                        gestionIncendies(incendie);
                    }
                    else incendies_rob.remove(incendie);


                } else
                    gestionIncendies(incendie);
            }
            simulateur.ajouteEvenement(new EventChefOrdonne(simulateur.getDateSimulation(), this));
        } else 
            throw new NoMoreFireException(); 
    }
    
    protected void gestionIncendies(Incendie incendie){
        boolean robotTrouve = false;
        Robot robotAMobiliser = donnees.getRobots().get(0);
        long tempsDuRobot = Long.MAX_VALUE;
        Chemin cheminDuRobot = new Chemin();
        for (Robot robot : donnees.getRobots()){
            if (!occupes.contains(robot)){
                try {
                    Chemin chemin = CalculPCC.dijkstra(carte, robot.getPosition(), incendie.getPosition(), robot, robot.getLastDate());
                    if (chemin.getTempsChemin() < tempsDuRobot){
                        robotTrouve = true;
                        robotAMobiliser = robot;
                        tempsDuRobot = chemin.getTempsChemin();
                        cheminDuRobot = chemin;
                    }
                } catch (NoPathAvailableException e) {
                    continue;
                }
            } 
        }

        if (robotTrouve){
            donneOrdre(robotAMobiliser, incendie, cheminDuRobot);
            occupes.add(robotAMobiliser);
            Set<Robot> nouvelleListe = incendies_rob.get(incendie);
            nouvelleListe.add(robotAMobiliser);
            incendies_rob.put(incendie, nouvelleListe);
        }
    }

    protected void donneOrdre(Robot robot, Incendie incendie, Chemin chemin){

        chemin.creerEvenements(simulateur, robot);

        // Création de toutes les interventions unitaires sur l'incendie
        for (int i = 0; i <= Math.min(incendie.getLitres() / robot.getQteVersement(),
                            robot.getReservoir() / robot.getQteVersement()); i++)
            simulateur.ajouteEvenement(new EventIntervenir(robot.getLastDate(), robot, incendie));
    }

    /**
     * Renvoie le chemin idéal vers la source d'eau la plus proche pour remplir le
     * reservoir d'un robot.
     * 
     * @param robot - Robot qui doit aller remplir son reservoir
     * @return Chemin - Chemin idéal vers une source d'eau
     * @throws PasDeCheminException - Exception s'il n'y a pas de chemins possibles
     *                              du robot vers une source d'eau
     * @throws NoWaterException      - Exception s'il n'y a pas d'eau dans la carte
     */
    public Chemin ouAllerRemplirReservoir(Robot robot) throws NoWaterException, NoPathAvailableException { 
        if (casesAvecEau.size() == 0)
            throw new NoWaterException();
        // Initialisation du chemin
        Chemin cheminARetourner = new Chemin();
        long tempsDeplacement = Long.MAX_VALUE;
        Case positionRobot = robot.getPosition();
        boolean ilYAUnChemin = false;
        for (Case caseEau : casesAvecEau) {
            if (robot.getCapacite() == 10000) { // Car on remplit le réservoir au-dessus
                try {
                    Chemin cheminVersEau = CalculPCC.dijkstra(carte, positionRobot, caseEau, robot, robot.getLastDate());
                    // Actualisation du chemin vers eau si on en trouve un plus court
                    if (cheminVersEau.getTempsChemin() < tempsDeplacement) {
                        tempsDeplacement = cheminVersEau.getTempsChemin();
                        cheminARetourner = cheminVersEau;
                        ilYAUnChemin = true;
                    }
                } catch (NoPathAvailableException e) {
                    continue;
                }
            } else {
                for (Direction direction : Direction.values()) {
                    try {
                        if (positionRobot.getCarte().voisinExiste(caseEau, direction, robot)) {
                            Chemin cheminVersEau = CalculPCC.dijkstra(carte, positionRobot,
                                    positionRobot.getCarte().getVoisin(caseEau, direction), robot, robot.getLastDate());
                            if (cheminVersEau.getTempsChemin() < tempsDeplacement) {
                                tempsDeplacement = cheminVersEau.getTempsChemin();
                                cheminARetourner = cheminVersEau;
                                ilYAUnChemin = true;
                            }
                        } else {
                            continue;
                        }
                    } catch (NoPathAvailableException e) {
                        continue;
                    } catch (CellOutOfMapException e) {
                        continue;
                    }
                }
            }
        }
        if (!ilYAUnChemin)
            throw new NoPathAvailableException();
        return cheminARetourner;
    }
    
    public void vaRemplirEau(Robot robot) throws NoWaterException{
        try {
            Chemin chemin = ouAllerRemplirReservoir(robot);
            chemin.creerEvenements(simulateur, robot);
            simulateur.ajouteEvenement(new EventRemplir(robot.getLastDate(), robot));
            occupes.remove(robot);
        } catch (NoWaterException e){
            throw e;
        } catch (NoPathAvailableException e){
            System.out.println("Pas possible de remplir reservoir.");
            return;
        } 
    }
}
   