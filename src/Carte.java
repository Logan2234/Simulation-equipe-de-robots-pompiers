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
    
    public void setTailleCases(int tailleCases){
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

    public boolean voisinExiste(Case src, Direction dir) {
        switch (dir) {
            case NORD:
                if (src.getLigne() == 0)
                    return false;
            case SUD:
                if (src.getLigne() == this.nbLignes - 1)
                    return false;
            case OUEST:
                if (src.getColonne() == 0)
                    return false;
            case EST:
                if (src.getColonne() == this.nbColonnes - 1)
                    return false;
            default:
                break;
        }
        return true;
    }

    public Case getVoisin(Case src, Direction dir) {
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
        return src;
    }
}
