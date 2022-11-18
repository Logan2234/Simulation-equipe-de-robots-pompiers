package Autre;

import java.util.LinkedList;

import Donnees.Case;
import Donnees.Robot.Robot;
import Evenements.EventMouvement;
import Evenements.Simulateur;

public class Chemin {

    private LinkedList<AssociationCaseTemps> cheminCasesTemps;

    public Chemin() {
        this.cheminCasesTemps = new LinkedList<>();
    }

    
    /** 
     * Renvoie le chemin. Méthode utilisée uniquement dans les tests "à la main"
     * 
     * @return Chemin : chemin sous forme de liste chainée de tuple (case, temps)
     */
    public LinkedList<AssociationCaseTemps> getChemin() {
        return cheminCasesTemps;
    }

    
    /** 
     * @return Chemin : deep-copie du chemin
     */
    public Chemin deepCopyChemin(){
        Chemin cheminCopy = new Chemin();
        for(AssociationCaseTemps temp : this.cheminCasesTemps){
            cheminCopy.addElement(temp.getCase(), temp.getT());
        }
        return cheminCopy;
    }
    
    /** 
     * Ajoute un élément au chemin
     * 
     * @param caseSuiv  : nouvel élément à ajouter au chemin
     * @param date      : date de passage par le nouveau élément
     */
    public void addElement(Case caseSuiv, long date) {
        this.cheminCasesTemps.add(new AssociationCaseTemps(caseSuiv, date));
    }

    
    /** 
     * Retourne la date du dernier élément du chemin
     * 
     * @return long : date du dernier élément du chemin
     */
    public long getLastDate(){
        return this.cheminCasesTemps.getLast().getT();
    }

    
    /** 
     * @return long : temps total pour parcourir le chemin
     */
    public long getTempsChemin(){
        return this.cheminCasesTemps.getLast().getT() - this.cheminCasesTemps.getFirst().getT();
    }

    
    /** 
     * Retourne le iº élément du chemin
     * 
     * @param index                 : indice de l'élément à retourner
     * @return AssociationTempsCase : le tuple (élément, la date de passage par l'élément)
     */
    public AssociationCaseTemps getElem(int index) {
        if (index == -1) {
            return this.cheminCasesTemps.getLast();
        }
        if (index >= 0 && index < this.cheminCasesTemps.size())
            return this.cheminCasesTemps.get(index);
        else
            throw new IllegalArgumentException("L'index doit être compris entre -1 et taille du chemin - 1");
    }

    /**
     * Créé la liste d'évènements correspondant au déplacement le long du chemin
     * 
     * @param simulateur : simulateur permettant l'ajout d'évènements
     * @param robot      : robot concerné par le déplacement sur le chemin
     */
    public void creerEvenements(Simulateur simulateur, Robot robot) {
        for (AssociationCaseTemps tc : this.cheminCasesTemps) {
            // Si c'est la première case ça sert à rien de s'y déplacer
            if (tc.getCase() != robot.getPosition()) {
                simulateur.ajouteEvenement(new EventMouvement(tc.getT(), robot, tc.getCase()));
            }
        }
    }

    
    /** 
     * @return String : affiche la chaîne de caractères de la transcription du chemin à String
     */
    @Override
    public String toString() {
        return "Chemin [chemin=" + cheminCasesTemps + "]";
    }
}

/**
 * Sous-classe permettant d'avoir la date de passage sur chaque élément.
 */
class AssociationCaseTemps {

    private Case cell;
    private long t;

    public AssociationCaseTemps(Case cell, long t) {
        this.cell = cell;
        this.t = t;
    }

    public Case getCase() {
        return cell;
    }

    public long getT() {
        return t;
    }

    public void setT(long t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "AssociationTempsCase [_case=" + cell + ", t=" + t + "]";
    }
}