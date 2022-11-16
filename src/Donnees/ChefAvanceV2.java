package Donnees;

import java.util.ArrayList;
import java.util.HashMap;

import Autre.CalculPCC;
import Autre.Chemin;
import Donnees.Robot.Robot;
import Donnees.Robot.RobotDrone;
import Evenements.EventIntervenir;
import Evenements.EventRemplir;
import Evenements.Simulateur;
import Exceptions.CellOutOfMapException;
import Exceptions.IllegalPathException;
import Exceptions.PasDeCheminException;
import Exceptions.PasEauDansCarte;

public class ChefAvanceV2 {
    private Carte carte;
    private DonneesSimulation donnees;
    private Simulateur simulateur;
    private HashMap<Incendie, ArrayList<Robot>> incendies_rob;
    private ArrayList<Robot> occupes;
    private CalculPCC calculateur;
    private ArrayList<Case> casesAvecEau;

    //TODO : encapsulation des méthodes (private, protected, public ...)
    //TODO : documentation de la classe et du constructeur (cf chef basique)

    public ChefAvanceV2(DonneesSimulation donnees, Simulateur simulateur) {
        this.carte = donnees.getCarte();
        this.donnees = donnees;
        this.simulateur = simulateur;
        this.occupes = new ArrayList<Robot>();
        this.incendies_rob = new HashMap<Incendie, ArrayList<Robot>>();
        calculateur = new CalculPCC(donnees);
        for (Incendie incendie : donnees.getIncendies()) {
            incendies_rob.put(incendie, new ArrayList<Robot>());
        }

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

    public void strategie(){
        while (!incendies_rob.isEmpty()){
            for (Incendie incendie : donnees.getIncendies()){
                if (!incendies_rob.containsKey(incendie)) {
                    continue;
                }
                try{
                    gestionIncendies(incendie);
                } catch (PasEauDansCarte e){
                    System.out.println("Il n'y a pas d'eau dans la mappe. Les robots ne peuvent pas remplir son reservoir.");
                }
            }
        }
    }
    
    public void gestionIncendies(Incendie incendie) throws PasEauDansCarte{
        boolean robotTrouve = false;
        Robot robotAMobiliser = new Robot(incendie.getPosition(), 0, 0, 0, 0, 0);
        long tempsDuRobot = Long.MAX_VALUE;
        Chemin cheminDuRobot = new Chemin();
        for (Robot robot : donnees.getRobots()){
            if (incendie.getLitres() == 0){
                for (Robot robotAux : incendies_rob.get(incendies_rob)){
                    occupes.remove(robotAux);
                }
                incendies_rob.remove(incendie);
                return;
            }
            if (robot.getReservoir() == 0){
                for (Incendie incendieAux : incendies_rob.keySet()){
                    ArrayList<Robot> robotList = incendies_rob.get(incendieAux);
                    if (robotList.contains(robot)){
                        robotList.remove(robot);
                        break;
                    }
                }
                if (!occupes.contains(robot)) occupes.add(robot);
                vaRemplirEau(robot);
                continue;
            if (!occupes.contains(robot)){
                }
                try {
                    Chemin chemin = calculateur.dijkstra(robot.getPosition(), incendie.getPosition(), robot, robot.getLastDate());
                    if (chemin.getTempsChemin() < tempsDuRobot){
                        robotTrouve = true;
                        robotAMobiliser = robot;
                        tempsDuRobot = chemin.getTempsChemin();
                        cheminDuRobot = chemin;
                    }
                } catch (PasDeCheminException e) {
                    continue;
                }
            } else {
                ArrayList<Robot> robotList = incendies_rob.get(incendie);
                if (!robotList.contains(robot)) {//! ça je l'ai fait puisque c'était fait au basique mais je ne comprends pas pourquoi.
                    occupes.remove(robot);
                }
            }
            
        }

        if (robotTrouve){
            try {
                ArrayList<Robot> nouvelleListe = incendies_rob.get(incendie);
                nouvelleListe.add(robotAMobiliser);
                incendies_rob.put(incendie, nouvelleListe);
                occupes.add(robotAMobiliser);
                donneOrdre(robotAMobiliser, incendie, cheminDuRobot);
            } catch (IllegalPathException e) {
                System.out.println(e);
            }
        }
    }

    public void donneOrdre(Robot robot, Incendie incendie, Chemin chemin){
        try{
            chemin.creerEvenements(simulateur, robot);
            if (robot.getCapacite() != -1) { // si ce n'est pas un robot à pattes
                for (int i = 0; i < Math.min(incendie.getLitres() / robot.getQteVersement(),
                        robot.getReservoir() / robot.getQteVersement()); i++) {
                    simulateur.ajouteEvenement(new EventIntervenir(robot.getLastDate(), robot, incendie));
                }
            } else { // le robot à pattes va verser son eau
                for (int i = 0; i < incendie.getLitres() / robot.getQteVersement(); i++) {
                    simulateur.ajouteEvenement(new EventIntervenir(robot.getLastDate(), robot, incendie));
                }
            }
        } catch (IllegalPathException e) {
            System.out.println(e);
        }
    }

    /**
     * Renvoie le chemin idéal vers la source d'eau la plus proche pour remplir le
     * reservoir d'un robot.
     * 
     * @param robot - Robot qui doit aller remplir son reservoir
     * @return Chemin - Chemin idéal vers une source d'eau
     * @throws PasDeCheminException - Exception s'il n'y a pas de chemins possibles
     *                              du robot vers une source d'eau
     * @throws PasEauDansCarte      - Exception s'il n'y a pas d'eau dans la carte
     */
    public Chemin ouAllerRemplirReservoir(Robot robot) throws PasEauDansCarte, PasDeCheminException { 
        if (casesAvecEau.size() == 0)
            throw new PasEauDansCarte();
        // Initialisation du chemin
        Chemin cheminARetourner = new Chemin();
        long tempsDeplacement = Long.MAX_VALUE;
        Case positionRobot = robot.getPosition();
        boolean ilYAUnChemin = false;

        for (Case caseEau : casesAvecEau) {
            if (robot.getCapacite() == 10000) { // Car on remplit le réservoir au-dessus
                try {
                    Chemin cheminVersEau = calculateur.dijkstra(positionRobot, caseEau, robot, robot.getLastDate());
                    // Actualisation du chemin vers eau si on en trouve un plus court
                    if (cheminVersEau.getTempsChemin() < tempsDeplacement) {
                        tempsDeplacement = cheminVersEau.getTempsChemin();
                        cheminARetourner = cheminVersEau;
                        ilYAUnChemin = true;
                    }
                } catch (PasDeCheminException e) {
                    continue;
                }
            } else {
                for (Direction direction : Direction.values()) {
                    try {
                        if (positionRobot.getCarte().voisinExiste(caseEau, direction, robot)) {
                            Chemin cheminVersEau = calculateur.dijkstra(positionRobot,
                                    positionRobot.getCarte().getVoisin(caseEau, direction), robot, robot.getLastDate());
                            if (cheminVersEau.getTempsChemin() < tempsDeplacement) {
                                tempsDeplacement = cheminVersEau.getTempsChemin();
                                cheminARetourner = cheminVersEau;
                                ilYAUnChemin = true;
                            }
                        } else {
                            continue;
                        }
                    } catch (PasDeCheminException e) {
                        continue;
                    } catch (CellOutOfMapException e) {
                        continue;
                    }
                }
            }
        }
        if (!ilYAUnChemin)
            throw new PasDeCheminException();
        return cheminARetourner;
    }
    
    public void vaRemplirEau(Robot robot) throws PasEauDansCarte{
        try {
            Chemin chemin = ouAllerRemplirReservoir(robot);
            chemin.creerEvenements(simulateur, robot);
            simulateur.ajouteEvenement(new EventRemplir(robot.getLastDate(), robot));
        } catch (PasEauDansCarte e){
            throw e;
        } catch (PasDeCheminException e){
            System.out.println("Pas possible de remplir reservoir.");
            return;
        } catch (IllegalPathException e) {
            System.out.println(e);
            return;
        }
    }
}
   