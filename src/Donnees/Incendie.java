package Donnees;

public class Incendie {

    private final Case position;
    private final int litresRequisInit;
    private int litresRequis;

    /**
     * @param position      : case où se situe l'incencie
     * @param litresRequis : litres requis pour éteindre l'incendie
     */
    public Incendie(Case position, int litresRequis) {
        this.position = position;
        this.litresRequisInit = litresRequis;
        this.litresRequis = litresRequis;
    }

    /**
     * @return int : quantité d'eau nécessaire pour éteindre le feu à un moment
     *         donné
     */
    public int getLitres() {
        return this.litresRequis;
    }

    /**
     * Fonction seulement utilisée pour calculer le % d'extinction d'un feu
     * Ce % n'est calculé uniquement que pour le dessin proportionnel d'un feu
     * 
     * @return int : quantité d'eau nécessaire pour éteindre le feu au départ
     */
    public int getLitresInit() {
        return this.litresRequisInit;
    }

    /**
     * @return Case : case où se situe l'incendie
     */
    public Case getPosition() {
        return this.position;
    }

    /**
     * Va verser de l'eau sur l'incendie et diminuer la quantité de
     * {@code litres_requis}
     * 
     * @param qteVersee : quantité (en L) à verser sur l'incendie
     */
    public void eteindre(int qteVersee) {
        litresRequis -= qteVersee;
    }

    /**
     * @return String : comporte la {@code position} et les {@code litres_requis}
     *         pour éteindre l'incendie
     */
    @Override
    public String toString() {
        return "Incendie [position=" + position + ", litres_requis=" + litresRequis + "]";
    }

}
