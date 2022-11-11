package Donnees.Robot;

import Donnees.Case;
import Donnees.Direction;
import Donnees.NatureTerrain;

public class RobotDrone extends Robot {

    /**
     * @param position : spécifie la position actuelle du robot
     */
    public RobotDrone(Case position) {
        super(position, 10000, 100/3.6, 30, 10000, 1800);
    }

    /**
     * @param position : spécifie la position actuelle du robot
     * @param vitesse : indique la vitesse (en km/h) par défaut du robot
     */
    public RobotDrone(Case position, int vitesse) {
        super(position, 10000, Math.min(vitesse, 150)/3.6, 30, 10000, 1800);
    }

    /** 
     * Remplit le réservoir du drone avec {@code fillReservoir} s'il est bien au-dessus de l'eau.
     * @exception IllegalArgumentException on n'est pas au-dessus d'une source d'eau
     */
    @Override
    public void remplirReservoir() {
        boolean aboveWater = false;
        Case pos = this.getPosition();
        for (Direction dir : Direction.values()) {

            if (pos.getCarte().getVoisin(pos, dir).getNature() == NatureTerrain.EAU) {
                aboveWater = true;
                break;
            }
        }
        if (aboveWater)
            this.fillReservoir();
        else
            throw new IllegalArgumentException(
                    "Il n'est pas possible de remplir le réservoir du drône sans être au-dessus d'une source d'eau");
    }

    
    /** 
     * @return String : affiche le type de robot, la {@code position}, la {@code capacite} maximale, le {@code reservoir} actuel et la {@code vitesseDefaut}
     * du robot.
     */
    @Override
    public String toString() {
        return "RobotDrone " + super.toString();
    }
}
