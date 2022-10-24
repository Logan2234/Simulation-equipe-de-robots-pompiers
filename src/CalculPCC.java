import java.util.LinkedList;

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
     * @return Temps nécessaire à un robot pour se déplacer du centre d'une case à
     *         une autre.
     */
    public double tpsDpltCaseACase(Case caseCourante, Case caseSuiv, Robot robot) {
        // On part du principe que le robot se déplace jusqu'au centre de la case
        // suivante
        int taille_cases = Case.getCarte().getTailleCases();

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

    public int[][] dijkstra(Case depart){
        int tailleMatrice = this.donnees.getCarte().getNbLignes() * this.donnees.getCarte().getNbLignes();
        int[][] dist;
        dist = new int[this.donnees.getCarte().getNbLignes()][];
        for (int i = 0; i < this.donnees.getCarte().getNbLignes(); i++) {
            dist[i] = new int[this.donnees.getCarte().getNbLignes()];
        }
        return dist;
    }
}
