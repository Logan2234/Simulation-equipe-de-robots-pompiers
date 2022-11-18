package Autre;

// PCC signifie "Plus Court Chemin"
import java.util.ArrayList;

import Donnees.Carte;
import Donnees.Case;
import Donnees.Direction;
import Donnees.Robot.Robot;
import Exceptions.CellOutOfMapException;
import Exceptions.NoPathAvailableException;

public class CalculPCC {

    private CalculPCC() {
        throw new IllegalStateException(
                "Cette classe ne doit pas être instanciée puisque toutes ses fonctions utiles sont statiques");
    }

    /**
     * Calcul le temps nécessaire à un robot donné pour se déplacer
     * d'une case à une autre case adjacente.
     * 
     * @param caseCourante - Case de départ
     * @param caseSuiv     - Case d'arrivée
     * @param robot        - Robot effectuant le déplacement
     * @return Temps pris (en s) par à un robot pour se déplacer du centre d'une
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
     * Dijkstra.
     * 
     * @param caseCourante : Case de départ
     * @param caseArrivee     : Case d'arrivée
     * @param robot        : Robot effectuant le déplacement
     * @param date         : Date d'origine de la construction du chemin
     * @param carte        : Carte de la simulation
     * @return Chemin optimal que le robot prendra pour aller d'une case à l'autre
     */
    public static Chemin dijkstra(Carte carte, Case caseCourante, Case caseArrivee, Robot robot, long date)
            throws NoPathAvailableException {

        // Si la case d'arrivée n'est pas accessible on a pas besoin de faire tout le calcul
        if (robot.getVitesse(caseArrivee.getNature()) == 0)
            throw new NoPathAvailableException();
        
        // Sinon on commence à faire Dijkstra
        long[][] distance = new long[carte.getNbLignes()][carte.getNbColonnes()];
        Chemin[][] chemins = new Chemin[carte.getNbLignes()][carte.getNbColonnes()];
        ArrayList<Coordonees> ouverts = new ArrayList<>();

        for (int i = 0; i < carte.getNbLignes(); i++) {
            for (int j = 0; j < carte.getNbColonnes(); j++) {
                distance[i][j] = Integer.MAX_VALUE;
                ouverts.add(new Coordonees(i, j));
                chemins[i][j] = new Chemin();
            }
        }

        distance[caseCourante.getLigne()][caseCourante.getColonne()] = 0;
        chemins[caseCourante.getLigne()][caseCourante.getColonne()].addElement(caseCourante, date);

        Case caseATraiter;
        Case caseMinimale;
        long minDistance;
        Coordonees minCoordonees;

        while (!ouverts.isEmpty()) {

            // On cherche valeur minimale de distance
            minDistance = distance[ouverts.get(0).getI()][ouverts.get(0).getJ()];
            minCoordonees = ouverts.get(0); // On initialise avec le premier élèment, mais que importe
            for (int i = 0; i < ouverts.size(); i++) {
                if (distance[ouverts.get(i).getI()][ouverts.get(i).getJ()] < minDistance) {
                    minCoordonees = ouverts.get(i);
                    minDistance = distance[ouverts.get(i).getI()][ouverts.get(i).getJ()];
                }
            }

            ouverts.remove(minCoordonees);

            int I = minCoordonees.getI();
            int J = minCoordonees.getJ();
            caseMinimale = caseCourante.getCarte().getCase(I, J);

            // On test si on peut si le robot peut se déplacer sur la caseMinimale
            if (robot.getVitesse(caseMinimale.getNature()) == 0)
                continue;

            // On cherche le chemin
            long distanceCaseMinimale = distance[I][J];


            if (caseCourante.getCarte().voisinExiste(caseMinimale, Direction.NORD, robot)) {
                try {
                    caseATraiter = caseCourante.getCarte().getVoisin(caseMinimale, Direction.NORD);
                    long temps = tpsDpltCaseACase(caseMinimale, caseATraiter, robot);
                    long tempsTotal = temps + distanceCaseMinimale;
                    if (tempsTotal < distance[I - 1][J]) {
                        distance[I - 1][J] = tempsTotal;
                        chemins[I - 1][J] = chemins[I][J].deepCopyChemin();
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
                        chemins[I][J - 1].addElement(caseATraiter, chemins[I][J].getLastDate() + temps);
                    }
                } catch (CellOutOfMapException e) {
                    System.out.println(e);
                }
            }
        }

        if (distance[caseArrivee.getLigne()][caseArrivee.getColonne()] == Integer.MAX_VALUE)
            throw new NoPathAvailableException();

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
