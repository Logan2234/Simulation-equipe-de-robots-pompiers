package Autre;

// PCC signifie "Plus Court Chemin"
import java.util.ArrayList;

import Donnees.Case;
import Donnees.DonneesSimulation;
import Donnees.Robot.Robot;
import Evenements.Simulateur;
import Donnees.Direction;

public class CalculPCC {
    private DonneesSimulation donnees;
    private Simulateur simulateur;

    public CalculPCC(DonneesSimulation donnees, Simulateur simulateur) {
        this.donnees = donnees;
        this.simulateur = simulateur;
    }

    /**
     * Calcul le temps nécessaire à un robot donné pour se déplacer
     * d'une case à une autre case adjacente.
     * 
     * @param caseCourante - Case de départ
     * @param caseSuiv     - Case d'arrivée
     * @param robot        - Robot effectuant le déplacement
     * @return Temps pris (en s) par à un robot pour se déplacer du centre d'une case à
     *         une autre.
     */
    public int tpsDpltCaseACase(Case caseCourante, Case caseSuiv, Robot robot) {
        // On part du principe que le robot va jusqu'au centre de la case d'après
        int taille_cases = caseCourante.getCarte().getTailleCases();

        double vitesseInit = robot.getVitesse(caseCourante.getNature());
        double vitesseSuiv = robot.getVitesse(caseSuiv.getNature());

        int temps = (int)(taille_cases / ((vitesseInit + vitesseSuiv) / 2));

        return temps;
    }


    
    /** 
     * Calcul le temps nécessaire à un robot donné pour se déplacer
     * d'une case à une autre case dans un chemin. 
     * @param chemin - Chemin à parcourir
     * @param robot  - Robot effectuant le déplacement
     * @return Temps pris (en s) par à un robot pour se déplacer du centre d'une case à
     *         une autre.
     */
    public int tpsDpltChemin(Chemin chemin, Robot robot) {
        int tempsTotal = 0;
        for (int i = 0; i < (chemin.getChemin()).size() - 1; i++)
            tempsTotal += tpsDpltCaseACase(chemin.getElem(i).getCase(), chemin.getElem(i++).getCase(), robot);
        return tempsTotal;
    }

    
    /** 
     * Calcul du chemin optimal entre deux cases. La fonction suit l'algorithme de Dijkstra.
     * 
     * @param caseCourante - Case de départ
     * @param caseSuiv     - Case d'arrivée
     * @param robot        - Robot effectuant le déplacement
     * @return Chemin optimal que le robot devrait prendre pour aller d'une case à l'autre.
     */
    public Chemin dijkstra(Case caseCourante, Case caseSuiv, Robot robot) {
        int distance[][] = new int[this.donnees.getCarte().getNbLignes()][this.donnees.getCarte().getNbColonnes()];
        Chemin chemins[][] = new Chemin[this.donnees.getCarte().getNbLignes()][this.donnees.getCarte().getNbColonnes()];
        distance[caseCourante.getLigne()][caseCourante.getColonne()] = 0;
        chemins[caseCourante.getLigne()][caseCourante.getColonne()].addElement(caseCourante, 0);
        ArrayList<Coordonees> ouverts = new ArrayList<Coordonees>();
        for (int i = 0; i < this.donnees.getCarte().getNbLignes(); i++) {
            for (int j = 0; j < this.donnees.getCarte().getNbColonnes(); j++) {
                distance[i][j] = Integer.MAX_VALUE;
                ouverts.add(new Coordonees(i, j));
            }
        }

        Case caseATraiter;
        Case caseMinimale;
        int minDistance;
        Coordonees minCoordonees;
        while (!ouverts.isEmpty()){

            // On cherche valeur minimale de distance 
            minDistance = Integer.MAX_VALUE;
            minCoordonees = ouverts.get(0); // On initialise avec le premier élèment, mais que importe
            for (int i = 0; i < ouverts.size(); i++) {
                if (distance[ouverts.get(i).getI()][ouverts.get(i).getJ()] < minDistance) {
                    minCoordonees = ouverts.get(i);
                    minDistance = distance[ouverts.get(i).getI()][ouverts.get(i).getJ()];
                }
            }

            ouverts.remove(minCoordonees);

            // On cherche le chemin

            caseMinimale = caseCourante.getCarte().getCase(minCoordonees.getI(), minCoordonees.getJ());
            int distanceCaseMinimale = distance[minCoordonees.getI()][minCoordonees.getJ()];
            
            if (caseCourante.getCarte().voisinExiste(caseMinimale, Direction.NORD)){
                caseATraiter = caseCourante.getCarte().getVoisin(caseMinimale, Direction.NORD);
                int temps = tpsDpltCaseACase(caseMinimale, caseATraiter, robot);
                int tempsTotal = temps + distanceCaseMinimale;
                if (tempsTotal < distance[minCoordonees.getI() - 1][minCoordonees.getJ()]){
                    distance[minCoordonees.getI() - 1][minCoordonees.getJ()] = tempsTotal;
                    chemins[minCoordonees.getI() - 1][minCoordonees.getJ()] = chemins[minCoordonees.getI()][minCoordonees.getJ()];
                    chemins[minCoordonees.getI() - 1][minCoordonees.getJ()].addElement(caseATraiter, chemins[minCoordonees.getI()][minCoordonees.getJ()].getLastDate() + temps);
                }
            }
            if (caseCourante.getCarte().voisinExiste(caseMinimale, Direction.EST)){
                caseATraiter = caseCourante.getCarte().getVoisin(caseMinimale, Direction.EST);
                int temps = tpsDpltCaseACase(caseMinimale, caseATraiter, robot);
                int tempsTotal = temps + distanceCaseMinimale;
                if (tempsTotal < distance[minCoordonees.getI()][minCoordonees.getJ() + 1]){
                    distance[minCoordonees.getI()][minCoordonees.getJ() + 1] = tempsTotal;
                    chemins[minCoordonees.getI()][minCoordonees.getJ() + 1] = chemins[minCoordonees.getI()][minCoordonees.getJ()];
                    chemins[minCoordonees.getI()][minCoordonees.getJ() + 1].addElement(caseATraiter, chemins[minCoordonees.getI()][minCoordonees.getJ()].getLastDate() + temps);
                }
            }
            if (caseCourante.getCarte().voisinExiste(caseMinimale, Direction.SUD)){
                caseATraiter = caseCourante.getCarte().getVoisin(caseMinimale, Direction.SUD);
                int temps = tpsDpltCaseACase(caseMinimale, caseATraiter, robot);
                int tempsTotal = temps + distanceCaseMinimale;
                if (tempsTotal < distance[minCoordonees.getI() + 1][minCoordonees.getJ()]){
                    distance[minCoordonees.getI() + 1][minCoordonees.getJ()] = tempsTotal;
                    chemins[minCoordonees.getI() + 1][minCoordonees.getJ()] = chemins[minCoordonees.getI()][minCoordonees.getJ()];
                    chemins[minCoordonees.getI() + 1][minCoordonees.getJ()].addElement(caseATraiter, chemins[minCoordonees.getI()][minCoordonees.getJ()].getLastDate() + temps);
                }
            }
            if (caseCourante.getCarte().voisinExiste(caseMinimale, Direction.OUEST)){
                caseATraiter = caseCourante.getCarte().getVoisin(caseMinimale, Direction.OUEST);
                int temps = tpsDpltCaseACase(caseMinimale, caseATraiter, robot);
                int tempsTotal = temps + distanceCaseMinimale;
                if (tempsTotal < distance[minCoordonees.getI()][minCoordonees.getJ() - 1]){
                    distance[minCoordonees.getI()][minCoordonees.getJ() - 1] = tempsTotal;
                    chemins[minCoordonees.getI()][minCoordonees.getJ() - 1] = chemins[minCoordonees.getI()][minCoordonees.getJ()];
                    chemins[minCoordonees.getI()][minCoordonees.getJ() - 1].addElement(caseATraiter, chemins[minCoordonees.getI()][minCoordonees.getJ()].getLastDate() + temps);
                }
            }
        }

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
}
