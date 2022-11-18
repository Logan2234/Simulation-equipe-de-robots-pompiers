package Donnees.Robot;

import Donnees.Case;
import Donnees.NatureTerrain;

public class RobotPattes extends Robot {
    /**
     * Va paramétrer la vitesse, le tmpVersement, la qteVersement, tmpRemplissage et l'image liés au robot à pattes
     * @param position : spécifie la position actuelle du robot
     */
    public RobotPattes(Case position) {
        super(position, Integer.MAX_VALUE, 30 / 3.6, 1, 10, -1, "assets/Pattes.png");
    }

    /**
     * Modifie la position du robot à condition que la nouvelle case ne soit pas de
     * l'eau.
     * 
     * @param newCase : la nouvelle case où le déplacer
     * 
     * @exception IllegalArgumentException on va dans l'eau
     */
    @Override
    public void setPosition(Case newCase) {
        NatureTerrain newTerrain = newCase.getNature();
        if (newTerrain == NatureTerrain.EAU) {
            throw new IllegalArgumentException("Le robot à pattes ne peut pas aller sur de l'eau");
        }
        super.setPosition(newCase);
    }

    @Override
    public void remplirReservoir() {
        /* Cet override de fonction permet simplement de ne pas éxecuter remplirReservoir
        de la classe mère puisque ce robot n'a aucun intérêt à se remplir puisque son réservoir
        est infini */
    }

    /**
     * Renvoie la vitesse (en m/s) du robot pour la nature du terrain demandée.
     * 
     * @param nature : nature du terrain
     * @return double : la vitesse (en m/s)
     */
    @Override
    public double getVitesse(NatureTerrain nature) {
        if (nature == NatureTerrain.ROCHE)
            return 10 / 3.6;
        else if (nature != NatureTerrain.EAU)
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
        return "RobotPattes " + super.toString();
    }
}
