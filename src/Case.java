public class Case {

    private NatureTerrain nature;
    private int ligne;
    private int colonne;
    // Pour trouver les voisins d'une case il faut qu'il y ait un lien entre les
    // cases et la carte
    private static Carte carte; // TODO: C'est pas bon ca imagine plusieurs instances de données

    public Case(NatureTerrain nature, int ligne, int colonne) {
        this.nature = nature;
        this.ligne = ligne;
        this.colonne = colonne;
    }

    public static void setCarte(Carte carte) {
        Case.carte = carte;
    }

    public static Carte getCarte() {
        return carte;
    }

    public NatureTerrain getNature() {
        return this.nature;
    }

    public int getLigne() {
        return this.ligne;
    }

    public int getColonne() {
        return this.colonne;
    }

    @Override
    public String toString() {
        return "Case [nature=" + nature + ", ligne=" + ligne + ", colonne=" + colonne + "]";
    }

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
