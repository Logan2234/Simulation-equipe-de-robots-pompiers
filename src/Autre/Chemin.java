package Autre;

import java.util.LinkedList;

import Donnees.Case;
import Donnees.Robot.Robot;
import Evenements.EventMouvement;
import Evenements.Simulateur;
import Exceptions.IllegalPathException;

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

    public Chemin deepCopyChemin(){
        Chemin cheminCopy = new Chemin();
        for(AssociationTempsCase temp : this.chemin){
            cheminCopy.addElement(temp.getCase(), temp.getT());
        }
        return cheminCopy;
    }
    
    /** 
     * Ajoute un élément au chemin
     * 
     * @param caseSuiv  - Nouveau élément à ajouter
     * @param date      - Date de passage par le nouveau élément
     */
    public void addElement(Case caseSuiv, long date) {
        this.chemin.add(new AssociationTempsCase(caseSuiv, date));
    }

    
    /** 
     * Retourne la date du dernier élément du chemin
     * 
     * @return Date du dernier élément du chemin
     */
    public long getLastDate(){
        return this.chemin.getLast().getT();
    }

    public long getTempsChemin(){
        return this.chemin.getLast().getT() - this.chemin.getFirst().getT();
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
* 
     * @exception IllegalPathException Le chemin n'est pas traversable par le robot
     *         
     */
    public void creerEvenements(Simulateur simulateur, Robot robot) throws IllegalPathException {
        for (AssociationTempsCase tc : this.chemin) {
            // Si c'est la première case ça sert à rien de s'y déplacer
            if (tc.getCase() != robot.getPosition()) {
                Case _case = tc.getCase(); 
                simulateur.ajouteEvenement(new EventMouvement(tc.getT(), robot, _case));
            }
        }
    }

    
    /** 
     * @return String : Affiche la chaîne de caractères de la transcription du chemin à String
     */
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