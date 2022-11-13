package Autre;

// PCC signifie "Plus Court Chemin"
import java.util.ArrayList;

import Donnees.Case;
import Donnees.Robot.Robot;
import Exceptions.CellOutOfMapException;
import Donnees.Direction;
import Donnees.DonneesSimulation;
import Donnees.Robot.Robot;
import Exceptions.CellOutOfMapException;
import Exceptions.PasDeCheminException;

public class CalculPCC {
    private DonneesSimulation donnees;

    public CalculPCC(DonneesSimulation donnees) {
        this.donnees = donnees;
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
        int taille_cases = caseCourante.getCarte().getTailleCases();

        double vitesseInit = robot.getVitesse(caseCourante.getNature());
        double vitesseSuiv = robot.getVitesse(caseSuiv.getNature());

        if (vitesseSuiv == 0) {
            return Long.MAX_VALUE;
        }

        long temps = (long) (taille_cases / ((vitesseInit + vitesseSuiv) / 2));

        return temps;
    }

    /**
     * Calcul le temps nécessaire à un robot donné pour se déplacer
     * d'une case à une autre case dans un chemin.
     * 
     * @param chemin - Chemin à parcourir
     * @param robot  - Robot effectuant le déplacement
     * @return Temps pris (en s) par à un robot pour se déplacer du centre d'une
     *         case à
     *         une autre.
     */
    public int tpsDpltChemin(Chemin chemin, Robot robot) {
        int tempsTotal = 0;
        for (int i = 0; i < (chemin.getChemin()).size() - 1; i++)
            tempsTotal += tpsDpltCaseACase(chemin.getElem(i).getCase(), chemin.getElem(i++).getCase(), robot);
        return tempsTotal;
    }

    /**
     * Calcul du chemin optimal entre deux cases. La fonction suit l'algorithme de
     * Dijkstra.
     * 
     * @param caseCourante - Case de départ
     * @param caseSuiv     - Case d'arrivée
     * @param robot        - Robot effectuant le déplacement
     * @return Chemin optimal que le robot devrait prendre pour aller d'une case à
     *         l'autre.
     */
    public Chemin dijkstra(Case caseCourante, Case caseSuiv, Robot robot) throws PasDeCheminException {
        long distance[][] = new long[this.donnees.getCarte().getNbLignes()][this.donnees.getCarte().getNbColonnes()];
        Chemin chemins[][] = new Chemin[this.donnees.getCarte().getNbLignes()][this.donnees.getCarte().getNbColonnes()];
        ArrayList<Coordonees> ouverts = new ArrayList<Coordonees>();
        for (int i = 0; i < this.donnees.getCarte().getNbLignes(); i++) {
            for (int j = 0; j < this.donnees.getCarte().getNbColonnes(); j++) {
                distance[i][j] = Long.MAX_VALUE;
                ouverts.add(new Coordonees(i, j));
                chemins[i][j] = new Chemin();
            }
        }
        distance[caseCourante.getLigne()][caseCourante.getColonne()] = 0;
        chemins[caseCourante.getLigne()][caseCourante.getColonne()].addElement(caseCourante, 0);

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
            caseMinimale = caseCourante.getCarte().getCase(minCoordonees.getI(), minCoordonees.getJ());
            if (robot.getVitesse(caseMinimale.getNature()) == 0)
                continue;

            // On cherche le chemin

            long distanceCaseMinimale = distance[minCoordonees.getI()][minCoordonees.getJ()];

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
                    // System.out.println(caseMinimale.toString());
                    // System.out.println(caseCourante.toString());
                    // System.out.println(caseATraiter.toString());
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

        if (distance[caseSuiv.getLigne()][caseSuiv.getColonne()] == Long.MAX_VALUE)
            throw new PasDeCheminException();

        return chemins[caseSuiv.getLigne()][caseSuiv.getColonne()];
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
