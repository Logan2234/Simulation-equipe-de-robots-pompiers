package Donnees.Robot;

import Donnees.Case;
import Donnees.NatureTerrain;

public class RobotChenilles extends Robot {
    public RobotChenilles(Case position) {
        super(position, 2000, 60, 8, 100, 300);
    }

    public RobotChenilles(Case position, double vitesse) {
        super(position, 2000, Math.min(vitesse, 80), 8, 100, 300);
    }

    
   /** 
     * Modifie la position du robot à condition que la nouvelle case ne soit pas de l'eau ou de la roche.
     * 
     * @param new_case Case : la nouvelle case où le déplacer
     * 
     * @exception IllegalArgumentException on va dans l'eau ou dans les rochers
     */
    @Override
    public void setPosition(Case new_case) {
        NatureTerrain new_terrain = new_case.getNature();
        if (new_terrain == NatureTerrain.EAU || new_terrain == NatureTerrain.ROCHE) {
            throw new IllegalArgumentException("Le robot à chenilles ne peut pas aller sur de l'eau ou des rochers");
        }
        super.setPosition(new_case);
    }

    
    /** 
     * Renvoie la vitesse (en m/s) du robot pour la nature du terrain où il se situe.
     * 
     * @param nature
     * @return double : la vitesse (en m/s)
     */
    @Override
    public double getVitesse(NatureTerrain nature) {
        if (nature == NatureTerrain.FORET)
            return this.getVitesseDefaut() / 2;
        else if (nature != NatureTerrain.EAU && nature != NatureTerrain.ROCHE)
            return this.getVitesseDefaut();
        else
            return 0;
    }

    
    /** 
     * @return String : affiche le type de robot, la {@code position}, la {@code capacite} maximale, le {@code reservoir} actuel et la {@code vitesseDefaut}
     * du robot.
     */
    @Override
    public String toString() {
        return "RobotChenilles " + super.toString();
    }
}
