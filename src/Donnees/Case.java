package Donnees;

public class Case {

    private final NatureTerrain nature;
    private final int ligne;
    private final int colonne;
    // Pour trouver les voisins d'une case il faut qu'il y ait un lien entre les
    // cases et la carte
    private Carte carte;

    /**
     * 
     * @param nature  : nature de la case
     * @param ligne   : ligne où se situe la case
     * @param colonne : colonne où se situe la case
     * @param carte   : carte où est située la case
     */
    public Case(NatureTerrain nature, int ligne, int colonne, Carte carte) {
        this.nature = nature;
        this.ligne = ligne;
        this.colonne = colonne;
        this.carte = carte;
    }

    /**
     * @return Carte : carte associée à la case
     */
    public Carte getCarte() {
        return this.carte;
    }

    /**
     * @return NatureTerrain : nature de la case
     */
    public NatureTerrain getNature() {
        return this.nature;
    }

    /**
     * @return int : ligne de la case
     */
    public int getLigne() {
        return this.ligne;
    }

    /**
     * @return int : colonne de la case
     */
    public int getColonne() {
        return this.colonne;
    }

    /**
     * @return String : comporte la {@code nature}, la {@code ligne} et la
     *         {@code colonne} de la case.
     */
    @Override
    public String toString() {
        return "Case [nature=" + nature + ", ligne=" + ligne + ", colonne=" + colonne + "]";
    }

    /**
     * Regarde si l'objet est identique à la case appelée.
     * 
     * @param obj : objet de comparaison
     * @return boolean : true si on est sur la même ligne et même colonne, on ne comparera pas la nature ni si on est sur la même carte.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass())
            return false;
        Case other = (Case) obj;
        if (ligne != other.ligne || colonne != other.colonne)
            return false;
        return true;
    }
}
