package Donnees;

import java.util.Arrays;

import Donnees.Robot.Robot;
import Exceptions.CellOutOfMapException;

public class Carte {

    private int tailleCases; // En m.
    private int nbLignes;
    private int nbColonnes;
    private Case[][] tab_cases;

    /**
     * Initialise la carte de notre situation
     * 
     * @param nbLignes   : nombre de lignes
     * @param nbColonnes : nombre de colonnes
     */
    public Carte(int nbLignes, int nbColonnes) {
        this.nbLignes = nbLignes;
        this.nbColonnes = nbColonnes;
        tab_cases = new Case[nbLignes][nbColonnes];
    }

    /**
     * @return int : taille d'un côté d'une case (de forme carrée)
     */
    public int getTailleCases() {
        return tailleCases;
    }

    /**
     * Modifie la taille d'un côté de nos cases (de forme carrée)
     * 
     * @param tailleCases : nouvelle taille des cases
     */
    public void setTailleCases(int tailleCases) {
        this.tailleCases = tailleCases;
    }

    /**
     * @return int : le nombre de ligne de notre carte
     */
    public int getNbLignes() {
        return nbLignes;
    }

    /**
     * @return int : le nombre de colonnes de notre carte
     */
    public int getNbColonnes() {
        return nbColonnes;
    }

    /**
     * Crée notre table avec toutes les cases
     * 
     * @param tab_cases : table de cases
     */
    public void setTab_cases(Case[][] tab_cases) {
        this.tab_cases = tab_cases;
    }

    /**
     * Renvoie la case de la ligne {@code lig} et de la colonne {@code col}
     *
     * @param lig : indicée à partir de 0
     * @param col : indicée à partir de 0
     */
    public Case getCase(int lig, int col) {
        return tab_cases[lig][col];
    }

    /**
     * Vérifie si la case {@code src} possède un voisin dans la direction
     * {@code dir} demandée.
     * 
     * @param src
     * @param dir
     * @return boolean : true si {@code src} possède une case adjacente pour
     *         {@code dir} donnée sinon false.
     */
    public boolean voisinExiste(Case src, Direction dir) {
        switch (dir) {
            case NORD:
                if (src.getLigne() == 0)
                    return false;
                break;
            case SUD:
                if (src.getLigne() == this.nbLignes - 1)
                    return false;
                break;
            case OUEST:
                if (src.getColonne() == 0)
                    return false;
                break;
            case EST:
                if (src.getColonne() == this.nbColonnes - 1)
                    return false;
                break;
            default:
                break;
        }
        return true;
    }

    
    /** 
     * @param src : la case regardée
     * @param dir : direction regardée par rapport à la case
     * @param robot : robot concerné par la recherche d'une case voisine
     * @return boolean : true si le robot peut se déplacer sur la case voisine
     */
    public boolean voisinExiste(Case src, Direction dir, Robot robot) {
        try {
            boolean natureCompatible = robot.getVitesse(this.getVoisin(src, dir).getNature()) != 0 && robot.getVitesse(src.getNature()) != 0;
            return this.voisinExiste(src, dir) && natureCompatible;
        } catch (CellOutOfMapException e){
            return false;
        }
    }

    /**
     * Donne le voisin pour la direction donnée
     * 
     * @param src : la case regardée
     * @param dir : direction regardée par rapport à la case
     * @return Case : case voisine
     * @throws IllegalArgumentException Le voisin demandé correspond à une case en
     *                                  dehors de la carte
     */
    public Case getVoisin(Case src, Direction dir) throws CellOutOfMapException {
        if (voisinExiste(src, dir)) {
            int ligne = src.getLigne();
            int colonne = src.getColonne();
            switch (dir) {
                case NORD:
                    return tab_cases[ligne - 1][colonne];
                case SUD:
                    return tab_cases[ligne + 1][colonne];
                case OUEST:
                    return tab_cases[ligne][colonne - 1];
                case EST:
                    return tab_cases[ligne][colonne + 1];
                default:
                    break;
            }
        } else
            throw new CellOutOfMapException();
        return src;
    }

    /**
     * @return String : comporte la {@code tailleCases}, {@code nbLignes},
     *         {@code nbColonnes} et la {@code tab_cases} l'ensemble des cases
     */
    @Override
    public String toString() {
        return "Carte [tailleCases=" + tailleCases + ", nbLignes=" + nbLignes + ", nbColonnes=" + nbColonnes
                + ", tab_cases=" + Arrays.toString(tab_cases) + "]";
    }
}
