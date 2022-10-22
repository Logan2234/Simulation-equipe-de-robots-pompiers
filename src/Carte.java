import java.util.Arrays;

public class Carte {

    private int tailleCases;
    private int nbLignes;
    private int nbColonnes;
    private Case[][] tab_cases;

    public Carte(int nbLignes, int nbColonnes) {
        this.nbLignes = nbLignes;
        this.nbColonnes = nbColonnes;
        tab_cases = new Case[nbLignes][nbColonnes];
    }

    public int getTailleCases() {
        return tailleCases;
    }

    public void setTailleCases(int tailleCases) {
        this.tailleCases = tailleCases;
    }

    public int getNbLignes() {
        return nbLignes;
    }

    public int getNbColonnes() {
        return nbColonnes;
    }

    public void setTab_cases(Case[][] tab_cases) {
        this.tab_cases = tab_cases;
    }

    public Case getCase(int lig, int col) {
        return tab_cases[lig][col];
    }

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

    public Case getVoisin(Case src, Direction dir) throws IllegalArgumentException {
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
        }
        else
            throw new IllegalArgumentException("Le robot est sorti de la carte");
        return src;
    }

    @Override
    public String toString() {
        return "Carte [tailleCases=" + tailleCases + ", nbLignes=" + nbLignes + ", nbColonnes=" + nbColonnes
                + ", tab_cases=" + Arrays.toString(tab_cases) + "]";
    }
}
