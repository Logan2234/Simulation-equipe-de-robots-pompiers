package Donnees.Robot;

import Donnees.Case;
import Donnees.NatureTerrain;

public class RobotRoues extends Robot {
    /**
     * @param position : spécifie la position actuelle du robot
     */
    public RobotRoues(Case position) {
        super(position, 5000, 80/3.6, 5, 100, 600, "assets/Roues.png");
    }

    /**
     * @param position : spécifie la position actuelle du robot
     * @param vitesse : indique la vitesse (en km/h) par défaut du robot
     */
    public RobotRoues(Case position, double vitesse) {
        super(position, 5000, vitesse/3.6, 5, 100, 600, "assets/Roues.png");
    }

    
    /** 
     * Modifie la position du robot à condition que la nouvelle case ne soit pas de l'eau ou des rochers.
     * 
     * @param newCase : la nouvelle case où le déplacer
     * 
     * @exception IllegalArgumentException on va dans l'eau ou dans les rochers
     */
    @Override
    public void setPosition(Case newCase) {
        NatureTerrain newTerrain = newCase.getNature();
        if (newTerrain == NatureTerrain.EAU || newTerrain == NatureTerrain.ROCHE
                || newTerrain == NatureTerrain.FORET) {
            throw new IllegalArgumentException(
                    "Le robot à roues ne peut se déplacer que sur du terrain libre ou des habitats.");
        }
        super.setPosition(newCase);
    }

    
    /** 
     * Renvoie la vitesse (en m/s) du robot pour la nature du terrain où il se situe.
     * 
     * @param nature : nature du terrain
     * @return double : la vitesse (en m/s)
     */
    @Override
    public double getVitesse(NatureTerrain nature) {
        if (nature == NatureTerrain.HABITAT || nature == NatureTerrain.TERRAIN_LIBRE) {
            return vitesseDefaut;
        }
        return 0;
    }

    
    /** 
     * @return String : affiche le type de robot, la {@code position}, la {@code capacite} maximale, le {@code reservoir} actuel et la {@code vitesseDefaut}
     * du robot.
     */
    @Override
    public String toString() {
        return "RobotRoues " + super.toString();
    }
}