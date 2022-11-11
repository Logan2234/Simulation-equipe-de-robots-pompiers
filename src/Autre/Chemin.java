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

    
    /** 
     * Renvoie le chemin 
     * 
     * @return Chemin sous forme de liste chainée
     */
    public LinkedList<AssociationTempsCase> getChemin() {
        return chemin;
    }

    
    /** 
     * Ajoute un élément au chemin
     * 
     * @param caseSuiv  - Nouveau élément à ajouter
     * @param date      - Date de passage par le nouveau élément
     */
    public void addElement(Case caseSuiv, int date) {
        this.chemin.add(new AssociationTempsCase(caseSuiv, date));
    }

    
    /** 
     * Retourne la date du dernier élément du chemin
     * 
     * @return Date du dernier élément du chemin
     */
    public int getLastDate(){
        return this.chemin.getLast().getT();
    }

    
    /** 
     * Retourne le iº élément du chemin
     * 
     * @param index                 - indice de l'élément à retourner
     * @return AssociationTempsCase - Retourne l'élément et la date de passage par l'élément
     */
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
     */
    public void creerEvenements(Simulateur simulateur, Robot robot) throws IllegalCheminRobotException {
        for (AssociationTempsCase tc : this.chemin) {
            // Si c'est la première case ça sert à rien de s'y déplacer
            if (tc != this.chemin.get(0)) {
                Case _case = tc.getCase();
                if (robot.getVitesse(_case.getNature()) == 0) {
                    throw new IllegalCheminRobotException();
                }
                simulateur.ajouteEvenement(new EventMouvement(tc.getT(), robot, _case));
            }
        }
    }

    
    /** 
     * Fonction renvoyant la transcription du chemin à String
     * 
     * @return String renvoyé
     */
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

    public Case getCase() {
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