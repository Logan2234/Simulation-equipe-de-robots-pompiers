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

public class ChefAvance {
    private Carte carte;
    private DonneesSimulation donnees;
    private Simulateur simulateur;
    private HashMap<Incendie, ArrayList<Robot>> incendies_rob;
    private ArrayList<Robot> occupes;
    private CalculPCC calculateur;
    private ArrayList<Case> casesAvecEau;

    //TODO : encapsulation des méthodes (private, protected, public ...)
    //TODO : documentation de la classe et du constructeur (cf chef basique)

    public ChefAvance(DonneesSimulation donnees, Simulateur simulateur) {
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
            if (robot instanceof RobotDrone) { // Car on remplit le réservoir dessus
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
    public void gestionIncendies(Incendie incendie) throws PasEauDansCarte{

        // Cherchons le robot le plus proche de l'incendie
        boolean robotTrouve = false;
        Robot robotAMobiliser = donnees.getRobots()[0]; // On initialise avec un robot random
        Chemin cheminAParcourir = new Chemin(); // On initialise avec un chemin random
        long tempsDeplacement = Long.MAX_VALUE;
        // Cherchons le robot le plus proche de l'incendie et le chemin à parcourir
        for (Robot robot : donnees.getRobots()) {
            // Si le robot est disponible, on l'envoie sur l'incendie
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
                // Si l'incendie est éteint, on met à jour les tables
                if (incendie.getLitres() == 0) {
                    incendies_rob.remove(incendie);
                    occupes.remove(robot);
                }
                // Si le réservoir du robot est vide, on va essayer de le remplir
                if (robot.getReservoir() == 0) {
                    try {
                        Chemin cheminVersEau = ouAllerRemplirReservoir(robot);
                        if (!occupes.contains(robot))
                            occupes.add(robot);
                        cheminVersEau.creerEvenements(this.simulateur, robot); // le robot va jusqu'à l'eau
                        simulateur.ajouteEvenement(new EventRemplir(robot.getLastDate(), robot));
                        occupes.remove(robot);
                        if (incendies_rob.containsKey(incendie) && incendies_rob.get(incendie).contains(robot)){
                            ArrayList<Robot> nouvelleListe = incendies_rob.get(incendie);
                            nouvelleListe.remove(robot);
                            incendies_rob.put(incendie, nouvelleListe);
                        }
                    } catch (PasEauDansCarte e) {
                        throw e;
                                    // ou alors ajouter l'exception  EmptyRobots
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
                // Mobilisation du robot
                ArrayList<Robot> nouvelleListe = incendies_rob.get(incendie);
                nouvelleListe.add(robotAMobiliser);
                incendies_rob.put(incendie, nouvelleListe);
                cheminAParcourir.creerEvenements(this.simulateur, robotAMobiliser);
                occupes.add(robotAMobiliser);

                // On regarde si le feu n'a pas été éteint avant
                if (incendie.getLitres() != 0){
                    if (robotAMobiliser.getCapacite() != -1){ // si ce n'est pas un robot à pattes
                        for (int i = 0; i < Math.min(incendie.getLitres() / robotAMobiliser.getQteVersement(), robotAMobiliser.getReservoir() / robotAMobiliser.getQteVersement()); i++) 
                        {
                            simulateur.ajouteEvenement(new EventIntervenir(robotAMobiliser.getLastDate(), robotAMobiliser, incendie));
                        }
                    } else { // le robot à pattes va verser son eau
                        for (int i = 0; i < incendie.getLitres() / robotAMobiliser.getQteVersement(); i++) 
                        {
                            simulateur.ajouteEvenement(new EventIntervenir(robotAMobiliser.getLastDate(), robotAMobiliser, incendie));
                        }
                    }
                } else {
                    occupes.remove(robotAMobiliser);
                    incendies_rob.remove(incendie);
                }
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
        // On supposera qu'il existe toujours une source d'eau accessible par tous les robots sur la carte
        while (!incendies_rob.isEmpty()) {
            for (Incendie incendie : donnees.getIncendies()) {
                // Si il est déjà éteint, on ne va pas traiter son cas
                if (!incendies_rob.containsKey(incendie)) {
                    continue;
                }
                try{
                    gestionIncendies(incendie);
                } catch (PasEauDansCarte e){

                }
            }
        }
    }
}
