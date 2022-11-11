package Autre;

import java.util.LinkedList;

import Donnees.Case;
import Donnees.Robot.Robot;
import Evenements.EventMouvement;
import Evenements.Simulateur;

public class Chemin {

    private LinkedList<AssociationTempsCase> chemin;

    public Chemin() {
        this.chemin = new LinkedList<AssociationTempsCase>();
    }

    public LinkedList<AssociationTempsCase> getChemin() {
        return chemin;
    }

    public void addElement(Case caseSuiv, int date) {
        // L'addition à la fin est nécessaire pour avoir un bon ordonnancement du temps
        this.chemin.add(new AssociationTempsCase(caseSuiv, date + this.getElem(-1).getT()));
    }

    public AssociationTempsCase getElem(int index) {
        if (index == -1){
            return this.chemin.getLast();
        }
        if (index >= 0 && index < this.chemin.size())
            return this.chemin.get(index);
        else
            throw new IllegalArgumentException("L'index doit être compris entre -1 et taille du chemin - 1");
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
            if (tc != this.chemin.get(0)) {
                simulateur.ajouteEvenement(new EventMouvement(tc.getT(), robot, tc.get_case()));
            }
        }
    }

    @Override
    public String toString() {
        return "Chemin [chemin=" + chemin + "]";
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

    @Override
    public String toString() {
        return "AssociationTempsCase [_case=" + _case + ", t=" + t + "]";
    }
}