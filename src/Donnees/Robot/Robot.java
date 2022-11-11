package Donnees.Robot;

import Donnees.Case;
import Donnees.Direction;
import Donnees.NatureTerrain;

public abstract class Robot {

    private Case position;
    private int capacite; // en L.
    private int reservoir; // en L.
    private int tmpVersement; // en s.
    private int qteVersement; // en L.
    private int tmpRemplissage; // en s.

    private double vitesseDefaut; // en m/s.

    /**
     * @param position : spécifie la position actuelle du robot
     * @param capacite : donne la capacité maximale du réservoir du robot
     * @param vitesse : indique la vitesse (en km/h) par défaut du robot
     * @param tmpVersement : indique le temps nécessaire (en s) pour vider la qteVersement
     * @param qteVersement : indique la quantité (en L) que l'on va déverser pendant tmpVersement
     * @param tmpReplissage : indique le temps nécessaire (en s) pour un remplissage complet
     */
    public Robot(Case position, int capacite, double vitesse, int tmpVersement, int qteVersement, int tmpReplissage) {
        this.position = position;
        this.capacite = capacite;
        this.reservoir = capacite;
        this.vitesseDefaut = vitesse;
        this.tmpVersement = tmpVersement;
        this.qteVersement = qteVersement;
        this.tmpRemplissage = tmpReplissage;
    }

    
    /** 
     * @return Case : la case où est placé le robot
     */
    public Case getPosition() {
        return this.position;
    }

    
    /** 
     * @return int : la quantité (en L) d'eau dans le réservoir actuellement
     */
    public int getReservoir() {
        return this.reservoir;
    }

    
    /** 
     * @return double : la vitesse (en km/h) par défaut du robot
     */
    public double getVitesseDefaut() {
        return this.vitesseDefaut;
    }

    
    /** 
     * @param nature
     * @return double : la vitesse (en km/h) du robot pour une nature donnée
     */
    public double getVitesse(NatureTerrain nature) {
        return this.vitesseDefaut;
    }

    
    /** 
     * @return int : la capacité totale (en L) du robot
     */
    public int getCapacite() {
        return capacite;
    }

    
    /** 
     * @return int : le temps nécessaire (en s) pour verser qteVersement (en L)
     */
    public int getTmpVersement() {
        return tmpVersement;
    }

    
    /** 
     * @return int : la quantité d'eau déversée (en L) pendant tmpVersement (en s)
     */
    public int getQteVersement() {
        return qteVersement;
    }

    
    /** 
     * @return int : le temps de remplissage (en s) si le réservoir est vide
     */
    public int getTmpRemplissage() {
        return tmpRemplissage;
    }

    
    /** 
     * Modifie la position du robot.
     * Elle sera override dans ses filles.
     * 
     * @param new_pos : case indiquant la nouvelle position du robot
     */
    public void setPosition(Case new_pos) {
        this.position = new_pos;
    }

    /** 
     * Cette méthode sera uniquement appelée pour remplir le réservoir du drone car il se remplit différemment 
     * (i.e. l'eau est sur notre position et non à côté).
     * Pour les autres robots, il faudra appeler {@code remplirReservoir}.
     */
    public void fillReservoir() {
        this.reservoir = this.capacite;
    }

    
    /** 
     * Déverse {@code quantite} sur notre position. On enlève donc à notre réservoir {@code quantite}.
     * 
     * @param quantite : quantité d'eau (en L) à déverser.
     */
    public void deverserEau(int quantite) {
        this.reservoir -= quantite;
    }

    /** 
     * Remplit le réservoir d'un robot terrestre s'il est à côté d'une case de type Eau.
     * 
     * Cette méthode sera override dans le cas du drone.
     * @exception IllegalArgumentException on n'est pas à côté d'un réservoir
     * 
     */
    public void remplirReservoir() {
        boolean waterNear = false;
        for (Direction dir : Direction.values()) {
            if (this.position.getCarte().getVoisin(this.position, dir).getNature() == NatureTerrain.EAU) {
                waterNear = true;
                break;
            }
        }
        if (waterNear)
            this.reservoir = this.capacite;
        else
            throw new IllegalArgumentException(
                    "Il n'est pas possible de remplir un réservoir sans être à proximité d'une source d'eau");
    }

    
    /** 
     * @return String : affiche la {@code position}, la {@code capacite} maximale, le {@code reservoir} actuel et la {@code vitesseDefaut}
     * du robot.
     * 
     * Elle sera override dans les classes filles
     */
    @Override
    public String toString() {
        return "[position=" + position + ", capacite=" + capacite + ", reservoir=" + reservoir
                + ", vitesse_defaut=" + vitesseDefaut + "]";
    }

}
