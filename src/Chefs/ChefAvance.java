package Chefs;

import java.util.ArrayList;
import java.util.HashMap;

import Autre.Chemin;
import Donnees.Case;
import Donnees.Direction;
import Donnees.DonneesSimulation;
import Donnees.Incendie;
import Donnees.NatureTerrain;
import Donnees.Robot.Robot;
import Evenements.EventIntervenir;
import Evenements.EventRemplir;
import Evenements.Simulateur;
import Exceptions.CellOutOfMapException;
import Exceptions.IllegalPathException;
import Exceptions.PasDeCheminException;

public class ChefAvance extends Chef {

    private HashMap<Incendie, ArrayList<Robot>> incendies_rob;
    private ArrayList<Case> casesAvecEau;

    // TODO : encapsulation des méthodes (private, protected, public ...)
    // TODO : documentation de la classe et du constructeur (cf chef basique)

    public ChefAvance(DonneesSimulation donnees, Simulateur simulateur) {
        super(donnees, simulateur);
        this.incendies_rob = new HashMap<Incendie, ArrayList<Robot>>();
        for (Incendie incendie : donnees.getIncendies()) {
            incendies_rob.put(incendie, new ArrayList<Robot>());
        }

        // On va déclarer les cases qui contiennent de l'eau
        ArrayList<Case> casesAvecEau = new ArrayList<Case>();
        for (int i = 0; i < donnees.getCarte().getNbLignes(); i++) {
            for (int j = 0; j < donnees.getCarte().getNbColonnes(); j++) {
                if (donnees.getCarte().getCase(i, j).getNature() == NatureTerrain.EAU)
                    casesAvecEau.add(donnees.getCarte().getCase(i, j));
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
     * @throws PasDeCheminException Exception s'il n'y a pas de chemins possibles
     *                              du robot vers une source d'eau
     */
    private Chemin ouAllerRemplirReservoir(Robot robot) throws PasDeCheminException {
        if (casesAvecEau.size() == 0) {
            System.out.println("Il n'y a pas d'eau sur cette carte, le robot ne va donc pas se remplir");
            ;
            return null;
        }
        // Initialisation du chemin
        Chemin cheminARetourner = new Chemin();
        long tempsDeplacement = Long.MAX_VALUE;
        Case positionRobot = robot.getPosition();
        boolean ilYAUnChemin = false;

        for (Case caseEau : casesAvecEau) {
            if (robot.getCapacite() == 10000) { // Car on remplit le réservoir au-dessus dans le cas du drone
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

    /**
     * Fonction qui, pour chaque incendie, décide quel robot envoyer pour réaliser
     * l'extinction et commande les robots pour
     * réaliser les extinctions ou leurs remplissages d'eau.
     * 
     * @param incendie - Incendie à traiter par les robots
     */
    @Override
    protected void gestionIncendies(Incendie incendie) {
        // Cherchons le robot le plus proche de l'incendie
        boolean robotTrouve = false;
        Robot robotAMobiliser = donnees.getRobots()[0]; // On initialise avec un robot random
        Chemin cheminAParcourir = new Chemin(); // On initialise avec un chemin random
        long tempsDeplacement = Long.MAX_VALUE;
        // Cherchons le robot le plus proche de l'incendie et le chemin à parcourir
        for (Robot robot : donnees.getRobots()) {
            // Si le robot est disponible, on l'envoie sur l'incendie
            if (!occupes.contains(robot)) {
                // Si il est vide, on le considère comme occupé
                if (robot.getReservoir() == 0) {
                    occupes.add(robot);
                    continue;
                }
                try {
                    Chemin chemin = new Chemin();
                    chemin = calculateur.dijkstra(robot.getPosition(), incendie.getPosition(), robot,
                            robot.getLastDate());
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
                // Si le réservoir du robot est vide, on va essayer de le remplir
                if (robot.getReservoir() == 0) {
                    try {
                        Chemin cheminVersEau = ouAllerRemplirReservoir(robot);
                        if (cheminVersEau == null)
                            continue; // ! S'il n'y a aucun robot à pattes on risque d'avoir un stack overflow ?
                        cheminVersEau.creerEvenements(simulateur, robot); // le robot va jusqu'à l'eau et se remplit
                        simulateur.ajouteEvenement(new EventRemplir(robot.getLastDate(), robot));
                        occupes.remove(robot);
                        // On enlève le robot de la liste de l'intervention sur l'incendie
                        if (incendies_rob.containsKey(incendie) && incendies_rob.get(incendie).contains(robot)) {
                            ArrayList<Robot> nouvelleListe = incendies_rob.get(incendie);
                            nouvelleListe.remove(robot);
                            incendies_rob.put(incendie, nouvelleListe);
                        }
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
        if (robotTrouve) {
            System.out.println(incendie);
            System.out.println(robotAMobiliser);
            System.out.println(occupes);
            try {
                // Mobilisation du robot en l'ajoutant à la liste
                ArrayList<Robot> nouvelleListe = incendies_rob.get(incendie);
                nouvelleListe.add(robotAMobiliser);
                incendies_rob.put(incendie, nouvelleListe);
                // System.out.println(cheminAParcourir);
                occupes.add(robotAMobiliser);
                cheminAParcourir.creerEvenements(simulateur, robotAMobiliser);
                // On regarde si le feu n'a pas été éteint avant
                if (incendie.getLitres() != 0) {
                    System.out.println("On va éteindre ");
                    if (robotAMobiliser.getCapacite() != -1) { // si ce n'est pas un robot à pattes
                        for (int i = 0; i < Math.min(incendie.getLitres() / robotAMobiliser.getQteVersement(),
                                robotAMobiliser.getReservoir() / robotAMobiliser.getQteVersement()); i++) {
                            simulateur.ajouteEvenement(
                                    new EventIntervenir(robotAMobiliser.getLastDate(), robotAMobiliser, incendie));
                        }
                    } else { // le robot à pattes va verser son eau
                        for (int i = 0; i < incendie.getLitres() / robotAMobiliser.getQteVersement(); i++) {
                            simulateur.ajouteEvenement(
                                    new EventIntervenir(robotAMobiliser.getLastDate(), robotAMobiliser, incendie));
                        }
                    }
                    // On démobilise le robot
                    if (incendies_rob.containsKey(incendie) && incendies_rob.get(incendie).contains(robotAMobiliser)) {
                        nouvelleListe.remove(robotAMobiliser);
                        incendies_rob.put(incendie, nouvelleListe);
                    }
                    System.out.println("On a essayé d'éteindre");
                    // On regarde l'état de l'incendie
                    if (incendie.getLitres() == 0) {
                        incendies_rob.remove(incendie);
                        System.out.println("On l'a éteint");
                    }
                    if (robotAMobiliser.getReservoir() == 0 && !occupes.contains(robotAMobiliser)) {
                        occupes.add(robotAMobiliser);
                    }
                    // Sinon on annule l'opération
                } else {
                    System.out.println("On l'a éteint avant nous\n");
                    if (robotAMobiliser.getReservoir() > 0) {
                        occupes.remove(robotAMobiliser);
                    } else if (robotAMobiliser.getReservoir() == 0 && !occupes.contains(robotAMobiliser)) {
                        occupes.add(robotAMobiliser);
                    }
                    incendies_rob.remove(incendie);
                }
            } catch (IllegalPathException e) {
                System.out.println(e);
            }
        }
    }

    /**
     * Fonction executant la stratégie d'extinction des feux du Chef des Pompiers
     * Avancé
     */
    @Override
    public void strategie() {
        // On supposera qu'il existe toujours une source d'eau accessible par tous les
        // robots sur la carte
        while (!incendies_rob.isEmpty()) {
            for (Incendie incendie : donnees.getIncendies()) {
                // Si il est déjà éteint, on ne va pas traiter son cas
                if (!incendies_rob.containsKey(incendie))
                    continue;
                gestionIncendies(incendie);
            }
        }
    }
}
