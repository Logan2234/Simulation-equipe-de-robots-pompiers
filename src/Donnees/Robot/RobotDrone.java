package Donnees.Robot;

import Donnees.Case;
import Donnees.NatureTerrain;
import Exceptions.NoWaterException;

public class RobotDrone extends Robot {
    /**
     * Va paramétrer la vitesse, le tmpVersement, la qteVersement, tmpRemplissage et l'image liés au drone
     * @param position : spécifie la position actuelle du robot
     */
    public RobotDrone(Case position) {
        super(position, 10000, 100 / 3.6, 30, 10000, 1800, "assets/Drone.png");
    }

    /**
     * @param position : spécifie la position actuelle du robot
     * @param vitesse  : indique la vitesse (en km/h) par défaut du robot, sera stockée en m/s
     */
    public RobotDrone(Case position, int vitesse) {
        super(position, 10000, Math.min(vitesse, 150) / 3.6, 30, 10000, 1800, "assets/Drone.png");
    }


    
    /** 
     * @param nature : non utilisé mais nécessaire pour l'override et éviter la surcharge
     * @return double : la vitesse du drone sur n'importe quel nature de terrain car cela ne l'affecte pas
     */
    @Override
    public double getVitesse(NatureTerrain nature) {
        return vitesseDefaut;
    }

    /**
     * Remplit le réservoir du drone avec {@code fillReservoir} s'il est bien
     * au-dessus de l'eau.
     * 
     * @exception NoWaterException On n'est pas au-dessus d'une source d'eau
     */
    @Override
    public void remplirReservoir() throws NoWaterException {
        if (this.getPosition().getNature() == NatureTerrain.EAU)
            this.fillReservoir();
        else
            throw new NoWaterException();
    }

    /**
     * @return String : affiche le type de robot, la {@code position}, la
     *         {@code capacite} maximale, le {@code reservoir} actuel et la
     *         {@code vitesseDefaut}
     *         du robot.
     */
    @Override
    public String toString() {
        return "RobotDrone " + super.toString();
    }
}
