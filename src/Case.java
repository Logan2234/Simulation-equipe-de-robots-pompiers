public class Case {

    private NatureTerrain nature;
    private int ligne;
    private int colonne;

    public Case(NatureTerrain nature, int ligne, int colonne) {
        this.nature = nature;
        this.ligne = ligne;
        this.colonne = colonne;
    }

    public NatureTerrain getNature() {
        return nature;
    }

    public int getLigne() {
        return ligne;
    }

    public int getColonne() {
        return ligne;
    }

    @Override
    public String toString() {
        return "Case [nature=" + nature + ", ligne=" + ligne + ", colonne=" + colonne + "]";
    }

}
