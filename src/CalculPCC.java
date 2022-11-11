// PCC signifie "Plus Court Chemin"
import java.util.*;

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
     * @return Temps nécessaire à un robot pour se déplacer du centre d'une case à
     *         une autre.
     */
    public double tpsDpltCaseACase(Case caseCourante, Case caseSuiv, Robot robot) {
        // On part du principe que le robot se déplace jusqu'au centre de la case
        // suivante
        int taille_cases = caseCourante.getCarte().getTailleCases();

        double vitesseInit = robot.getVitesse(caseCourante.getNature());
        double vitesseSuiv = robot.getVitesse(caseSuiv.getNature());

        double temps = taille_cases / 2 * (vitesseInit + vitesseSuiv);

        return temps;
    }

    public double tpsDpltChemin(Chemin chemin, Robot robot) {
        double tempsTotal = 0;
        for (int i = 0; i < (chemin.getChemin()).size() - 1; i++)
            tempsTotal += tpsDpltCaseACase(chemin.getElem(i).get_case(), chemin.getElem(i++).get_case(), robot);
        return tempsTotal;
    }

    public Chemin dijkstra(Case caseCourante, Case caseSuiv, Robot robot) {
        double distance[][] = new double[this.donnees.getCarte().getNbLignes()][this.donnees.getCarte().getNbColonnes()];
        Chemin chemins[][] = new Chemin[this.donnees.getCarte().getNbLignes()][this.donnees.getCarte().getNbColonnes()];
        distance[caseCourante.getLigne()][caseCourante.getColonne()] = 0;
        chemins[caseCourante.getLigne()][caseCourante.getColonne()].addElement(caseCourante, 0);
        ArrayList<Coordonees> ouverts = new ArrayList<Coordonees>();
        for (int i = 0; i < this.donnees.getCarte().getNbLignes(); i++) {
            for (int j = 0; j < this.donnees.getCarte().getNbColonnes(); j++) {
                distance[i][j] = Double.MAX_VALUE;
                ouverts.add(new Coordonees(i, j));
            }
        }

        Case caseATraiter;
        Case caseMinimale;
        double minDistance;
        Coordonees minCoordonees;
        while (!ouverts.isEmpty()){

            // On cherche valeur minimale de distance 
            minDistance = Double.MAX_VALUE;
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
            double distanceCaseMinimale = distance[minCoordonees.getI()][minCoordonees.getJ()];
            
            if (caseCourante.getCarte().voisinExiste(caseCourante, NORD)){
                caseATraiter = caseCourante.getCarte().getVoisin(caseCourante, NORD);
                double temps = tpsDpltCaseACase(caseCourante, caseATraiter, robot);
                double tempsTotal = temps + distanceCaseMinimale;
                if (tempsTotal < distance[minCoordonees.getI() - 1][minCoordonees.getJ()]){
                    distance[minCoordonees.getI() - 1][minCoordonees.getJ()] = tempsTotal;
                    chemins[minCoordonees.getI() - 1][minCoordonees.getJ()] = chemins[minCoordonees.getI() - 1][minCoordonees.getJ()];
                    chemins[minCoordonees.getI() - 1][minCoordonees.getJ()].addElement(caseMinimale, chemins[minCoordonees.getI()][minCoordonees.getJ()].getLastDate() + temps);
                }
            }

            

        }


        return null;
    }

}

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
