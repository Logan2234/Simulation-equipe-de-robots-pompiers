package Donnees.Robot;

import Donnees.Case;
import Donnees.Direction;
import Donnees.NatureTerrain;
import Exceptions.CellOutOfMapException;
import Exceptions.NoWaterException;

/**
 * @param position      : position actuelle du robot
 * @param capacite      : capacité maximale du réservoir du robot
 * @param vitesse       : vitesse (en m/s) par défaut du robot
 * @param tmpVersement  : temps nécessaire (en s) pour vider la
 *                      {@code qteVersement}
 * @param qteVersement  : quantité (en L) que l'on va déverser
 *                      pendant {@code tmpVersement}
 * @param tmpReplissage : temps nécessaire (en s) pour un remplissage
 *                      complet
 * @param reservoir     : quantité d'eau actuelle dans le robot 
 * @param lastDate      : dernière date où il a été utilisé.
 * @param imagePath     : path vers l'image pour le représenter
 */
public abstract class Robot {
    private Case position;
    private int reservoir; // en L.
    private long lastDate; // dernière date où il a été utilisé.
    private final int capacite; // en L.
    private final int tmpVersement; // en s.
    private final int qteVersement; // en L.
    private final int tmpRemplissage; // en s.
    protected final double vitesseDefaut; // en m/s. Protected car utilisé dans les sous-classes
    private final String imagePath;

    /**
     * @param position      : spécifie la position actuelle du robot
     * @param capacite      : donne la capacité maximale du réservoir du robot
     * @param vitesse       : indique la vitesse (en km/h) par défaut du robot, elle sera stockée en m/s
     * @param tmpVersement  : indique le temps nécessaire (en s) pour vider la
     *                      {@code qteVersement}
     * @param qteVersement  : indique la quantité (en L) que l'on va déverser
     *                      pendant {@code tmpVersement}
     * @param tmpReplissage : indique le temps nécessaire (en s) pour un remplissage
     *                      complet
     */
    protected Robot(Case position, int capacite, double vitesse, int tmpVersement, int qteVersement, int tmpReplissage,
            String imagePath) {
        this.position = position;
        this.reservoir = capacite;
        this.lastDate = 0;
        this.capacite = capacite;
        this.vitesseDefaut = vitesse;
        this.tmpVersement = tmpVersement;
        this.qteVersement = qteVersement;
        this.tmpRemplissage = tmpReplissage;
        this.imagePath = imagePath;
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
     * @return double : la vitesse (en m/s) par défaut du robot
     */
    public double getVitesseDefaut() {
        return this.vitesseDefaut;
    }

    /**
     * @param nature
     * @return double : la vitesse (en m/s) du robot pour une nature donnée
     */
    public abstract double getVitesse(NatureTerrain nature);

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
     * @return long lastDate : Dernière date utilisée dans la simulation pour le robot
     */
    public long getLastDate() {
        return lastDate;
    }

    
    /** 
     * @return String imagePath : Image associée au robot
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param temps : Temps correspondant à la dernière action du robot
     */
    public void setLastDate(long temps) {
        lastDate = temps;
    }

    /**
     * Modifie la position du robot.
     * Elle sera override dans ses filles.
     * 
     * @param newPos : case indiquant la nouvelle position du robot
     */
    public void setPosition(Case newPos) {
        this.position = newPos;
    }

    /**
     * Cette méthode sera uniquement appelée par {@code remplirReservoir}, elle remplit le réservoir du robot.
     */
    protected void fillReservoir() {
        this.reservoir = this.capacite;
    }

    /**
     * Déverse {@code quantite} sur notre position. On enlève donc à notre réservoir
     * {@code quantite}.
     * 
     * @param quantite : quantité d'eau (en L) à déverser.
     */
    public void deverserEau(int quantite) {
        this.reservoir -= quantite;
    }

    /**
     * Remplit le réservoir d'un robot terrestre s'il est à côté d'une case de type
     * EAU.
     * Cette méthode sera override dans le cas du drone et du robot à pattes.
     * 
     * @exception NoWaterException On n'est pas à côté d'un réservoir
     * 
     */
    public void remplirReservoir() throws NoWaterException {
        boolean waterNear = false;
        for (Direction dir : Direction.values()) {
            try {
                if (this.position.getCarte().getVoisin(this.position, dir).getNature() == NatureTerrain.EAU) {
                    waterNear = true;
                    break;
                }
            } catch (CellOutOfMapException e) {
                continue;
            }
        }
        if (waterNear)
            this.fillReservoir();
        else
            throw new NoWaterException();
    }

    /**
     * @return String : affiche la {@code position}, la {@code capacite} maximale,
     *         le {@code reservoir} actuel et la {@code vitesseDefaut}
     *         du robot.
     * 
     *         Elle sera override dans les classes filles
     */
    @Override
    public String toString() {
        return "[position=" + position + ", capacite=" + capacite + ", reservoir=" + reservoir
                + ", vitesse_defaut=" + vitesseDefaut + "]";
    }
}
