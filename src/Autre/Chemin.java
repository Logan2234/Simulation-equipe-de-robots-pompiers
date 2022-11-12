package Autre;

import java.util.LinkedList;

import Donnees.Case;
import Donnees.Robot.Robot;
import Evenements.EventMouvement;
import Evenements.Simulateur;
import Exceptions.IllegalCheminRobotException;

public class Chemin {

    private LinkedList<AssociationTempsCase> chemin;

    public Chemin() {
        this.chemin = new LinkedList<AssociationTempsCase>();
    }

    public LinkedList<AssociationTempsCase> getChemin() {
        return chemin;
    }

    public void addElement(Case caseSuiv, long date) {
        this.chemin.add(new AssociationTempsCase(caseSuiv, date));
    }

    public AssociationTempsCase getElem(int index) {
        if (index == -1) {
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
     * 
     * @exception IllegalCheminRobotException Le chemin n'est pas traversable par le
     *                                        robot
     */
    public void creerEvenements(Simulateur simulateur, Robot robot) throws IllegalCheminRobotException {
        AssociationTempsCase oldCase = this.chemin.get(0);
        for (AssociationTempsCase tc : this.chemin) {
            // Si c'est la première case ça sert à rien de s'y déplacer
            if (tc.getCase() != robot.getPosition()) {
                Case _case = tc.getCase(); 
                tc.setT(CalculPCC.tpsDpltCaseACase(oldCase.getCase(), _case, robot) + oldCase.getT());
                if (robot.getVitesse(_case.getNature()) == 0) {
                    throw new IllegalCheminRobotException();
                }
                simulateur.ajouteEvenement(new EventMouvement(tc.getT(), robot, _case));
            }
            oldCase = tc;
        }
    }

    @Override
    public String toString() {
        return "Chemin [chemin=" + chemin + "]";
    }
}

class AssociationTempsCase {

    private Case _case;
    private long t;

    public AssociationTempsCase(Case _case, long t) {
        this._case = _case;
        this.t = t;
    }

    public Case getCase() {
        return _case;
    }

    public long getT() {
        return t;
    }

    public void setT(long t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "AssociationTempsCase [_case=" + _case + ", t=" + t + "]";
    }
}