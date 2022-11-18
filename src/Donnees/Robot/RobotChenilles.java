package Donnees.Robot;

import Donnees.Case;
import Donnees.NatureTerrain;

public class RobotChenilles extends Robot {
    /**
     * Va paramétrer la vitesse, le tmpVersement, la qteVersement, tmpRemplissage et l'image liés au robot à chenilles
     * @param position : spécifie la position actuelle du robot
     */
    public RobotChenilles(Case position) {
        super(position, 2000, 60 / 3.6, 8, 100, 300, "assets/Chenilles.png");
    }

    /**
     * @param position : spécifie la position actuelle du robot
     * @param vitesse  : spécifie la vitesse (en km/h) par défaut du robot (elle sera stockée en m/s)
     */
    public RobotChenilles(Case position, double vitesse) {
        super(position, 2000, Math.min(vitesse, 80) / 3.6, 8, 100, 300, "assets/Chenilles.png");
    }

    /**
     * Modifie la position du robot à condition que la nouvelle case ne soit pas de
     * l'eau ou de la roche.
     * 
     * @param newCase : la nouvelle case où le déplacer
     * 
     * @exception IllegalArgumentException On va dans l'eau ou dans les rochers
     */
    @Override
    public void setPosition(Case newCase) {
        NatureTerrain newTerrain = newCase.getNature();
        if (newTerrain == NatureTerrain.EAU || newTerrain == NatureTerrain.ROCHE) {
            throw new IllegalArgumentException("Le robot à chenilles ne peut pas aller sur de l'eau ou des rochers");
        }
        super.setPosition(newCase);
    }

    /**
     * Renvoie la vitesse (en m/s) du robot pour la nature du terrain demandée.
     * 
     * @param nature : nature du terrain
     * @return double : la vitesse (en m/s)
     */
    @Override
    public double getVitesse(NatureTerrain nature) {
        if (nature == NatureTerrain.FORET)
            return vitesseDefaut / 2;
        else if (nature != NatureTerrain.EAU && nature != NatureTerrain.ROCHE)
            return vitesseDefaut;
        else
            return 0;
    }

    /**
     * @return String : affiche le type de robot, la {@code position}, la
     *         {@code capacite} maximale, le {@code reservoir} actuel et la
     *         {@code vitesseDefaut}
     *         du robot.
     */
    @Override
    public String toString() {
        return "RobotChenilles " + super.toString();
    }
}
