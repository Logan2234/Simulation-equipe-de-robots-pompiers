import java.util.LinkedList;

public class Chemin {
    private LinkedList<AssociationTempsCase> chemin;

    public Chemin() {
        this.chemin = new LinkedList<AssociationTempsCase>();
    }

    public LinkedList<AssociationTempsCase> getChemin() {
        return chemin;
    }

    public void addElement(Case caseSuiv, int date) {
        this.chemin.add(new AssociationTempsCase(caseSuiv, date));
    }

    public AssociationTempsCase getElem(int index) {
        return this.chemin.get(index);
    }

    /**
     * Créé la liste d'évènements correspondant au déplacement le long du chemin
     * 
     * @param simulateur : Simulateur permettant l'ajout d'évènements
     * @param robot      : Robot concerné par le déplacement sur le chemin
     */
    public void CreerEvenements(Simulateur simulateur, Robot robot) {
        for (AssociationTempsCase tc : this.chemin) {
            // Si c'est la première case ça sert à rien de s'y déplacer
            if (tc.get_case() != this.chemin.get(0).get_case()) {
                simulateur.ajouteEvenement(new EventMouvement(tc.getT(), robot, tc.get_case()));
            }
        }
    }

}

class AssociationTempsCase {
    private Case _case;
    private int t;

    public AssociationTempsCase(Case _case, int t) {
        this._case = _case;
        this.t = t;
    }

    public Case get_case() {
        return _case;
    }

    public int getT() {
        return t;
    }
}