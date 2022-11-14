package Donnees;

import java.util.ArrayList;
import java.util.HashMap;

import Autre.CalculPCC;
import Autre.Chemin;
import Donnees.Robot.Robot;
import Donnees.Robot.RobotDrone;
import Evenements.EventRemplir;
import Evenements.Simulateur;
import Exceptions.CellOutOfMapException;
import Exceptions.IllegalPathException;
import Exceptions.PasDeCheminException;
import Exceptions.PasEauDansCarte;

public class ChefAvance {
    private Carte carte;
    private DonneesSimulation donnees;
    private Simulateur simulateur;
    private HashMap<Incendie, Robot> incendies_rob;
    private ArrayList<Robot> occupes;
    private CalculPCC calculateur;
    private ArrayList<Case> casesAvecEau;

    public ChefAvance(DonneesSimulation donnees, Simulateur simulateur) {
        this.carte = donnees.getCarte();
        this.donnees = donnees;
        this.simulateur = simulateur;
        this.occupes = new ArrayList<Robot>();
        this.incendies_rob = new HashMap<Incendie, Robot>();
        calculateur = new CalculPCC(donnees);
        for (Incendie incendie : donnees.getIncendies()) {
            incendies_rob.put(incendie, null);
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

        Chemin cheminARetourner = new Chemin();
        long tempsDeplacement = Long.MAX_VALUE;
        Case positionRobot = robot.getPosition();
        boolean ilYAUnChemin = false;

        for (Case caseEau : this.casesAvecEau) {
            if (robot instanceof RobotDrone) {
                try {
                    Chemin cheminVersEau = calculateur.dijkstra(positionRobot, caseEau, robot, robot.getLastDate());

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

    /**
     * Fonction qui, pour chaque incendie, décide quel robot envoyer pour réaliser
     * l'extinction et commande les robots pour
     * réaliser les extinctions ou leurs remplissages d'eau.
     * 
     * @param incendie - Incendie à traiter par les robots
     */
    public void gestionIncendies(Incendie incendie) {

        // Cherchons le robot le plus proche de l'incendie
        boolean robotTrouve = false;
        Robot robotAMobiliser = donnees.getRobots()[0]; // On initialise avec un robot random
        Chemin cheminAParcourir = new Chemin(); // On initialise avec un chemin random
        long tempsDeplacement = Long.MAX_VALUE;

        for (Robot robot : donnees.getRobots()) {
            if (!occupes.contains(robot)) {
                try {
                    Chemin chemin = new Chemin();
                    chemin = calculateur.dijkstra(robot.getPosition(), incendie.getPosition(), robot, robot.getLastDate());

                    if (chemin.getTempsChemin() < tempsDeplacement) {
                        robotTrouve = true;
                        robotAMobiliser = robot;
                        cheminAParcourir = chemin;
                        tempsDeplacement = chemin.getTempsChemin();
                    }
                } catch (PasDeCheminException e) {
                    continue;
                }
            } else {
                if (incendie.getLitres() == 0) { // Si l'incendie est éteint
                    incendies_rob.remove(incendie);
                    occupes.remove(robot);
                }
                if (robot.getReservoir() == 0) { // Si le réservoir du robot est vide
                    try {
                        Chemin cheminVersEau = ouAllerRemplirReservoir(robotAMobiliser);
                        if (!occupes.contains(robot))
                            occupes.add(robot);
                        cheminVersEau.creerEvenements(this.simulateur, robot); // le robot va jusqu'à l'eau
                        simulateur.ajouteEvenement(new EventRemplir(robot.getLastDate(), robot));
                    } catch (PasEauDansCarte e) {
                        continue;
                    } catch (PasDeCheminException e) {
                        continue;
                    } catch (IllegalPathException e) {
                        System.out.println(e);
                        continue;
                    }
                }

            }
        }

        // Maintenant, si on a trouvé un robot, on l'envoie travailler
        try {
            if (robotTrouve) {
                incendies_rob.put(incendie, robotAMobiliser);
                cheminAParcourir.creerEvenements(this.simulateur, robotAMobiliser);
            }
        } catch (IllegalPathException e) {
            System.out.println(e);
        }

    }

    /**
     * Fonction executant la stratégie d'extinction des feux du Chef des Pompiers
     * Avancé
     */
    public void strategie() {
        // On supposera qu'il existe toujours une source d'eau accessible par tous les robopts sur la carte
        while (!incendies_rob.isEmpty()) {
            for (Incendie incendie : donnees.getIncendies()) {
                // Si il est déjà éteint, on ne va pas traiter son cas
                if (!incendies_rob.containsKey(incendie)) {
                    continue;
                }
                if (incendies_rob.get(incendie) == null) {
                    gestionIncendies(incendie);
                } // TODO else avec libération des robots
            }
        }
    }
}
