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

/**
 * @param incendiesRob : HashMap qui a pour clé les incendies
 *                      non-éteints et en valeur le set avec les robots qui s'en occupe.
 */
public class ChefAvance extends Chef{
    private HashMap<Incendie, Set<Robot>> incendiesRob;
    private ArrayList<Case> casesAvecEau;
    private Carte carte;
    
    /**
     * Va implémenter la stratégie avancée pour le chef pompier. Il faudra ensuite appelée la méthode stratégie pour exécuter le pompier.
     * 
     * @param donnees    : Données utilisées pour la simulation
     * @param simulateur : Simulateur à initialiser avant
     */
    public ChefAvance(DonneesSimulation donnees, Simulateur simulateur) {
        super(donnees, simulateur);
        this.incendiesRob = new HashMap<Incendie, Set<Robot>>();
        for (Incendie incendie : donnees.getIncendies()) {
            incendiesRob.put(incendie, new HashSet<>());
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

    
    /** 
     * 
     * Implémente la stratégie avancée (avec remplissage d'eau et envoi de plusieurs robots simultanément).
     * On va traiter un incendie à la fois, tant qu'on en a qui ne sont pas éteints.
     * 
     * C'est cette méthode qui va envoyer des robots se remplir ou demandé à éteindre un incendie.
     * Elle gère aussi les tables {@code incendiesRob} et {@code occupes}.
     * 
     * @throws NoMoreFireException Si tous les incendies ont été éteints
     */
    public void strategie() throws NoMoreFireException{
        // Si il reste des incendies pas encore éteints
        if (!incendiesRob.isEmpty()){
            for (Incendie incendie : donnees.getIncendies()){
                // Si l'incendie est éteient on passe au suivant
                if (!incendiesRob.containsKey(incendie)) {
                    continue;
                }
                Set<Robot> robotsListe = incendiesRob.get(incendie);
                // Si on a au moins un robot en charge de l'incendie
                if (!robotsListe.isEmpty()){
                    // On va parcourir la liste des robots qui s'en occupe
                    for (Robot robot : robotsListe){
                        // Si le robot est vide, on l'envoie se remplir et il ne s'occupe plus de l'incendie
                        if (robot.getReservoir() == 0){
                            robotsListe.remove(robot);
                            try{
                                vaRemplirEau(robot);
                            } catch ( NoWaterException e){
                                System.out.println("Pas d'eau dans la carte. Le robot ne peux pas remplir le réservoir.");
                            }
                        }

                        // Si l'incendie a été éteint, on met à jour occupes
                        if (incendie.getLitres() == 0){
                            occupes.remove(robot);
                        }
                    }
                    // Si l'incendie n'est pas éteint, on met à jour et on va chercher des robots
                    if (incendie.getLitres() > 0) { 
                        incendiesRob.put(incendie, robotsListe);
                        gestionIncendies(incendie);
                    }
                    // Si il a été éteint, on le retire de incendiesRob
                    else incendiesRob.remove(incendie);


                } else
                    // SI aucun robot lui a été envoyé, on lui en envoie un
                    gestionIncendies(incendie);
            }
            simulateur.ajouteEvenement(new EventChefOrdonne(simulateur.getDateSimulation(), this));
        } else 
            throw new NoMoreFireException(); 
    }
    
    
    /**
     * Va essayer d'éteindre {@code incendie} en appelant un robot à la fois et en cherchant le plus proche robot disponible.
     * 
     * @param incendie : incendie à traiter
     */
    protected void gestionIncendies(Incendie incendie){
        boolean robotTrouve = false;
        Robot robotAMobiliser = donnees.getRobots().get(0);
        long tempsDuRobot = Long.MAX_VALUE;
        Chemin cheminDuRobot = new Chemin();
        // On cherche un robot
        for (Robot robot : donnees.getRobots()){
            // S'il est disponible
            if (!occupes.contains(robot)){
                // On essaie de l'envoyer sur l'incendie et voir le temps nécessaire pour y aller
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
        // Si on a trouvé un robot disponible, ça veut dire que robotAMobiliser est le plus proche de l'incendie sélectionné.
        if (robotTrouve){
            donneOrdre(robotAMobiliser, incendie, cheminDuRobot);
            occupes.add(robotAMobiliser);
            // On ajoute le robot à la liste de robot qui s'occupent de l'incendie
            Set<Robot> nouvelleListe = incendiesRob.get(incendie);
            nouvelleListe.add(robotAMobiliser);
            incendiesRob.put(incendie, nouvelleListe);
        }
    }

    
    /** 
     * Méthode pour envoyer un robot éteindre un incendie.
     * 
     * @param robot : robot à envoyer sur l'incendie
     * @param incendie : incendie à éteindre
     * @param chemin : chemin pour se rendre à l'incendie
     */
    protected void donneOrdre(Robot robot, Incendie incendie, Chemin chemin){

        chemin.creerEvenements(simulateur, robot);

        if (incendie.getLitres()  > 0 ){
            // Création de toutes les interventions unitaires sur l'incendie
            for (int i = 0; i <= Math.min(incendie.getLitres() / robot.getQteVersement(),
                                robot.getReservoir() / robot.getQteVersement()); i++)
                simulateur.ajouteEvenement(new EventIntervenir(robot.getLastDate(), robot, incendie));
        }
    }

    /**
     * Renvoie le chemin idéal vers la source d'eau la plus proche pour remplir le
     * reservoir d'un robot.
     * 
     * @param robot : robot qui doit aller remplir son reservoir
     * @return Chemin : chemin le plus court vers une source d'eau
     * @throws PasDeCheminException S'il n'y a aucune source d'eau atteignable par le robot
     * @throws NoWaterException S'il n'y a pas d'eau dans la carte
     */
    private Chemin ouAllerRemplirReservoir(Robot robot) throws NoWaterException, NoPathAvailableException { 
        if (casesAvecEau.size() == 0)
            throw new NoWaterException();
        // Initialisation du chemin
        Chemin cheminARetourner = new Chemin();
        long tempsDeplacement = Long.MAX_VALUE;
        Case positionRobot = robot.getPosition();
        boolean ilYAUnChemin = false;
        // On va regarder toutes les cases avec de l'eau et chercher la plus proche
        for (Case caseEau : casesAvecEau) {
            if (robot.getCapacite() == 10000) { // Si c'est un drone : remplissage au-dessus de l'eau
                try {
                    Chemin cheminVersEau = CalculPCC.dijkstra(carte, positionRobot, caseEau, robot, robot.getLastDate());
                    // Actualisation du chemin vers eau si on en trouve un plus court
                    if (cheminVersEau.getTempsChemin() < tempsDeplacement) {
                        tempsDeplacement = cheminVersEau.getTempsChemin();
                        cheminARetourner = cheminVersEau;
                        ilYAUnChemin = true;
                    }
                } catch (NoPathAvailableException e) {
                    // Si on ne peut pas accéder à la case on cherche un autre robot
                    continue;
                }
            } else { // Pour les autres robots qui se remplissent à côté de l'eau (et non pas au-dessus)
                // On va regarder pour chaque direction si on peut placer le robot à côté.
                for (Direction direction : Direction.values()) {
                    try {
                        if (positionRobot.getCarte().voisinExiste(caseEau, direction, robot)) {
                            Chemin cheminVersEau = CalculPCC.dijkstra(carte, positionRobot,
                                    positionRobot.getCarte().getVoisin(caseEau, direction), robot, robot.getLastDate());
                            // Actualisation du chemin vers eau si on en trouve un plus court
                            if (cheminVersEau.getTempsChemin() < tempsDeplacement) {
                                tempsDeplacement = cheminVersEau.getTempsChemin();
                                cheminARetourner = cheminVersEau;
                                ilYAUnChemin = true;
                            }
                        } else {
                            // Si on ne peut pas accéder à la case par la direction voulue on cherche une autre direction
                            continue;
                        }
                    } catch (NoPathAvailableException e) {
                        // Si on ne peut pas accéder à la case on cherche un autre robot
                        continue;
                    } catch (CellOutOfMapException e) {
                        // Si on ne peut pas accéder à la case on cherche un autre robot (cette exception 
                        // ne devrait jamais être attrapée).
                        continue;
                    }
                }
            }
        }
        if (!ilYAUnChemin)
            throw new NoPathAvailableException();
        return cheminARetourner;
    }
    
    
    /** 
     * Méthode qui va envoyer le robot {@code robot} se remplir à la source la plus proche
     * 
     * @param robot : robot à remplir
     * @throws NoWaterException S'il n'y a pas d'eau dans la carte
     */
    private void vaRemplirEau(Robot robot) throws NoWaterException{
        try {
            // Se déplace jusqu'à la source
            Chemin chemin = ouAllerRemplirReservoir(robot);
            chemin.creerEvenements(simulateur, robot);
            // Se remplit
            simulateur.ajouteEvenement(new EventRemplir(robot.getLastDate(), robot));
            occupes.remove(robot);
        } catch (NoWaterException e){
            throw e;
        } catch (NoPathAvailableException e){
            System.out.println("Pas possible de remplir reservoir pour " + robot.toString());
            return;
        } 
    }
}
   