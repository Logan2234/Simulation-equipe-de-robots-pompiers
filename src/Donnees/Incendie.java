package Donnees;

public class Incendie {

    private Case position;
    private int litres_requis;

    /**
     * 
     * @param position : case où se situe l'incencie
     * @param litres_requis : litres requis pour éteindre l'incendie
     */
    public Incendie(Case position, int litres_requis) {
        this.position = position;
        this.litres_requis = litres_requis;
    }

    
    /** 
     * @return int : litres nécessaires pour l'éteindre
     */
    public int getLitres() {
        return this.litres_requis;
    }

    
    /** 
     * @return Case : case où se situe l'incendie
     */
    public Case getPosition() {
        return this.position;
    }

    
    /** 
     * Va verser de l'eau sur l'incendie et diminuer la quantité de {@code litres_requis}
     * 
     * @param qteVersee : quantité (en L) à verser sur l'incendie
     */
    public void eteindre(int qteVersee) {
        litres_requis -= qteVersee;
    }

    
    /** 
     * @return String : comporte la {@code position} et les {@code litres_requis} pour éteindre l'incendie
     */
    @Override
    public String toString() {
        return "Incendie [position=" + position + ", litres_requis=" + litres_requis + "]";
    }

}
