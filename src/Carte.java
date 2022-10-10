public class Carte {
    private int tailleCases;
    private int nbLignes;
    private int nbColonnes;
    
    public Carte(int nbLignes, int nbColonnes) {
        this.nbLignes = nbLignes;
        this.nbColonnes = nbColonnes;
    }

    public int getTailleCases() {
        return tailleCases;
    }

    public int getNbLignes() {
        return nbLignes;
    }

    public int getNbColonnes() {
        return nbColonnes;
    }
    
    public boolean voisinExiste(Case src, Direction dir){
        // TODO: Cette fonction est à implémenter
        return true;
    }

    public Case getVoisin(Case src, Direction dir){
        // TODO: Cette fonction est à implémenter
        return null;
    }

}
