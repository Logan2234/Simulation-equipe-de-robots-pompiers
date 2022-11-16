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
                vaRemplirEau(robot); // TODO
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
    
}
   