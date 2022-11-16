package Donnees.Robot;

import java.awt.Color;
import java.util.HashMap;

import Donnees.Case;
import Donnees.Direction;
import Donnees.NatureTerrain;
import Exceptions.CellOutOfMapException;
import Exceptions.NoWaterException;

public abstract class Robot {
    private Case position;
    private int capacite; // en L.
    private int reservoir; // en L.
    private int tmpVersement; // en s.
    private int qteVersement; // en L.
    private int tmpRemplissage; // en s.
    private double vitesseDefaut; // en m/s.
    private long lastDate;
    private static Color colRobot;
    private static HashMap<Object, String> noms;

    /**
     * @param position      : spécifie la position actuelle du robot
     * @param capacite      : donne la capacité maximale du réservoir du robot
     * @param vitesse       : indique la vitesse (en km/h) par défaut du robot
     * @param tmpVersement  : indique le temps nécessaire (en s) pour vider la
     *                      qteVersement
     * @param qteVersement  : indique la quantité (en L) que l'on va déverser
     *                      pendant tmpVersement
     * @param tmpReplissage : indique le temps nécessaire (en s) pour un remplissage
     *                      complet
     */
    public Robot(Case position, int capacite, double vitesse, int tmpVersement, int qteVersement, int tmpReplissage) {
        this.position = position;
        this.capacite = capacite;
        this.reservoir = capacite;
        this.vitesseDefaut = vitesse;
        this.tmpVersement = tmpVersement;
        this.qteVersement = qteVersement;
        this.tmpRemplissage = tmpReplissage;
        this.lastDate = 0;
        colRobot = new Color(105, 105, 105);
    }

    /**
     * @param classe : Classe précise du robot
     * @return String : Nom donné au robot en question
     */
    public static String getNom(Object classe) {
        if (noms == null) {
            noms = new HashMap<Object, String>();
            noms.put(RobotChenilles.class, "C");
            noms.put(RobotDrone.class, "D");
            noms.put(RobotPattes.class, "P");
            noms.put(RobotRoues.class, "R");
        }
        return noms.get(classe);
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
     * @return long lastDate : Dernière date utilisée pour le robot
     */
    public long getLastDate() {
        return lastDate;
    }

    /**
     * @param temps : Temps correspondant à la dernière action du robot
     */
    public void setLastDate(long temps) {
        lastDate = temps;
    }

    /**
     * @return Color : Couleur des robots
     */
    public static Color getColor() {
        return colRobot;
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
     * Cette méthode sera uniquement appelée pour remplir le réservoir du drone car
     * il se remplit différemment
     * (i.e. l'eau est sur notre position et non à côté).
     * Pour les autres robots, il faudra appeler {@code remplirReservoir}.
     */
    public void fillReservoir() {
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
     * Eau.
     * 
     * Cette méthode sera override dans le cas du drone.
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
                System.out.println(e);
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
