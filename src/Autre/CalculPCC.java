package Autre;

// PCC signifie "Plus Court Chemin"
import java.util.ArrayList;

import Donnees.Carte;
import Donnees.Case;
import Donnees.Direction;
import Donnees.Robot.Robot;
import Exceptions.CellOutOfMapException;
import Exceptions.NoPathAvailableException;

public final class CalculPCC {

    private CalculPCC() {
        throw new IllegalStateException(
                "Cette classe ne doit pas être instanciée puisque toutes ses fonctions utiles sont statiques");
    }

    /**
     * Calcule le temps nécessaire à un robot donné pour se déplacer
     * d'une case à une autre case adjacente.
     * 
     * @param caseCourante : case de départ
     * @param caseSuiv     : case d'arrivée
     * @param robot        : robot effectuant le déplacement
     * @return long : temps nécessaire (en s) à un robot pour se déplacer du centre d'une
     *         case à une autre.
     */
    public static long tpsDpltCaseACase(Case caseCourante, Case caseSuiv, Robot robot) {
        // On part du principe que le robot va jusqu'au centre de la case d'après
        int tailleCases = caseCourante.getCarte().getTailleCases();
        double vitesseSuiv = robot.getVitesse(caseSuiv.getNature());
        double vitesseInit = robot.getVitesse(caseCourante.getNature());

        if (vitesseSuiv == 0)
            return Long.MAX_VALUE;

        return (long) (tailleCases / ((vitesseInit + vitesseSuiv) / 2));
    }

    /**
     * Calcul du chemin optimal entre deux cases. La fonction suit l'algorithme de
     * Dijkstra. (cf Rapport.pdf pour toute précision sur l'algorithme)
     * 
     * @param caseCourante : case de départ
     * @param caseArrivee     : case d'arrivée
     * @param robot        : robot effectuant le déplacement
     * @param date         : date d'origine de la construction du chemin
     * @param carte        : carte de la simulation
     * @return Chemin : chemin optimal que le robot prendra pour aller d'une case à l'autre
     * 
     * @throws NoPathAvailableException Aucun chemin n'existe entre les deux cases pour le robot donné.
     */
    public static Chemin dijkstra(Carte carte, Case caseCourante, Case caseArrivee, Robot robot, long date)
            throws NoPathAvailableException {

        // Si la case d'arrivée n'est pas accessible on a pas besoin de faire tout le calcul
        if (robot.getVitesse(caseArrivee.getNature()) == 0)
            throw new NoPathAvailableException();
        
        // Sinon on commence à faire Dijkstra
        long[][] distance = new long[carte.getNbLignes()][carte.getNbColonnes()]; // matrice des temps pour arriver à la case depuis le robot
        Chemin[][] chemins = new Chemin[carte.getNbLignes()][carte.getNbColonnes()]; // matrice des chemins optimaux pour arriver à chaque case
        ArrayList<Coordonees> ouverts = new ArrayList<>(); // liste des cases non traitées

        // On initialise les matrices et la liste
        for (int i = 0; i < carte.getNbLignes(); i++) { 
            for (int j = 0; j < carte.getNbColonnes(); j++) {
                distance[i][j] = Integer.MAX_VALUE;
                ouverts.add(new Coordonees(i, j));
                chemins[i][j] = new Chemin();
            }
        }

        // On met à 0 pour la case initiale
        distance[caseCourante.getLigne()][caseCourante.getColonne()] = 0;
        chemins[caseCourante.getLigne()][caseCourante.getColonne()].addElement(caseCourante, date);

        Case caseATraiter;
        Case caseMinimale;
        long minDistance;
        Coordonees minCoordonees;

        while (!ouverts.isEmpty()) { // tant qu'on n'a pas traité toutes les cases...

            // On cherche valeur minimale de distance
            minDistance = distance[ouverts.get(0).getI()][ouverts.get(0).getJ()];
            minCoordonees = ouverts.get(0); // On initialise avec le premier élèment, mais que importe
            // On cherche une case dans ouvert avec le temps de parcours le plus court
            for (int i = 0; i < ouverts.size(); i++) {
                if (distance[ouverts.get(i).getI()][ouverts.get(i).getJ()] < minDistance) {
                    minCoordonees = ouverts.get(i);
                    minDistance = distance[ouverts.get(i).getI()][ouverts.get(i).getJ()];
                }
            }

            // On traite cette case ouverte et on la sort de la liste
            ouverts.remove(minCoordonees);

            int I = minCoordonees.getI();
            int J = minCoordonees.getJ();
            caseMinimale = caseCourante.getCarte().getCase(I, J);

            // On teste si le robot peut se déplacer sur la caseMinimale
            if (robot.getVitesse(caseMinimale.getNature()) == 0)
                continue;

            // On cherche le chemin
            long distanceCaseMinimale = distance[I][J];

            // On regarde pour ses voisins, si passer par ce chemin c'est vraiment le plus court chemin
            if (caseCourante.getCarte().voisinExiste(caseMinimale, Direction.NORD, robot)) {
                try {
                    caseATraiter = caseCourante.getCarte().getVoisin(caseMinimale, Direction.NORD);
                    long temps = tpsDpltCaseACase(caseMinimale, caseATraiter, robot);
                    long tempsTotal = temps + distanceCaseMinimale;
                    if (tempsTotal < distance[I - 1][J]) {
                        distance[I - 1][J] = tempsTotal;
                        chemins[I - 1][J] = chemins[I][J].deepCopyChemin();
                        // Le nouveau chemin est le chemin précédant plus la nouvelle case
                        chemins[I - 1][J].addElement(caseATraiter, chemins[I][J].getLastDate() + temps);
                    }
                } catch (CellOutOfMapException e) {
                    System.out.println(e);
                }
            }

            if (caseCourante.getCarte().voisinExiste(caseMinimale, Direction.EST, robot)) {
                try {
                    caseATraiter = caseCourante.getCarte().getVoisin(caseMinimale, Direction.EST);
                    long temps = tpsDpltCaseACase(caseMinimale, caseATraiter, robot);
                    long tempsTotal = temps + distanceCaseMinimale;
                    if (tempsTotal < distance[I][J + 1]) {
                        distance[I][J + 1] = tempsTotal;
                        chemins[I][J + 1] = chemins[I][J].deepCopyChemin();
                        // Le nouveau chemin est le chemin précédant plus la nouvelle case
                        chemins[I][J + 1].addElement(caseATraiter, chemins[I][J].getLastDate() + temps);
                    }
                } catch (CellOutOfMapException e) {
                    System.out.println(e);
                }
            }

            if (caseCourante.getCarte().voisinExiste(caseMinimale, Direction.SUD, robot)) {
                try {
                    caseATraiter = caseCourante.getCarte().getVoisin(caseMinimale, Direction.SUD);
                    long temps = tpsDpltCaseACase(caseMinimale, caseATraiter, robot);
                    long tempsTotal = temps + distanceCaseMinimale;
                    if (tempsTotal < distance[I + 1][J]) {
                        distance[I + 1][J] = tempsTotal;
                        chemins[I + 1][J] = chemins[I][J].deepCopyChemin();
                        // Le nouveau chemin est le chemin précédant plus la nouvelle case
                        chemins[I + 1][J].addElement(caseATraiter, chemins[I][J].getLastDate() + temps);
                    }
                } catch (CellOutOfMapException e) {
                    System.out.println(e);
                }
            }

            if (caseCourante.getCarte().voisinExiste(caseMinimale, Direction.OUEST, robot)) {
                try {
                    caseATraiter = caseCourante.getCarte().getVoisin(caseMinimale, Direction.OUEST);
                    long temps = tpsDpltCaseACase(caseMinimale, caseATraiter, robot);
                    long tempsTotal = temps + distanceCaseMinimale;
                    if (tempsTotal < distance[I][J - 1]) {
                        distance[I][J - 1] = tempsTotal;
                        chemins[I][J - 1] = chemins[I][J].deepCopyChemin();
                        // Le nouveau chemin est le chemin précédant plus la nouvelle case
                        chemins[I][J - 1].addElement(caseATraiter, chemins[I][J].getLastDate() + temps);
                    }
                } catch (CellOutOfMapException e) {
                    System.out.println(e);
                }
            }
        }

        // Si aucun chemin n'a été trouvé
        if (distance[caseArrivee.getLigne()][caseArrivee.getColonne()] == Integer.MAX_VALUE)
            throw new NoPathAvailableException();

        // Sinon, on renvoie notre chemin
        return chemins[caseArrivee.getLigne()][caseArrivee.getColonne()];
    }

}

/**
 * Sous-classe représentant un tuple de coordonées.
 */
class Coordonees {
    private int i;
    private int j;

    public Coordonees(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    @Override
    public String toString() {
        return "(" + i + "," + j + ")";
    }
}
