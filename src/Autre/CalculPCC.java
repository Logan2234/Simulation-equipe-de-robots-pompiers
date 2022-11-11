package Autre;

import Donnees.Case;
import Donnees.DonneesSimulation;
import Donnees.Robot.Robot;
import Evenements.Simulateur;

// PCC signifie "Plus Court Chemin"
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

    public double tpsDpltChemin(Chemin chemin, Robot robot) {
        double tempsTotal = 0;
        for (int i = 0; i < (chemin.getChemin()).size() - 1; i++)
            tempsTotal += tpsDpltCaseACase(chemin.getElem(i).getCase(), chemin.getElem(i++).getCase(), robot);
        return tempsTotal;
    }

    public Chemin dijkstra() {
        return null; // TODO
    }
}
