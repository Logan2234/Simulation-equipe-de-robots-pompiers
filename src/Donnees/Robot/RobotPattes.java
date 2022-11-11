package Donnees.Robot;

import Donnees.Case;
import Donnees.NatureTerrain;

public class RobotPattes extends Robot {
    public RobotPattes(Case position) {
        super(position, -1, 30, 1, 10, -1);
    }

    @Override
    public void setPosition(Case new_case) {
        NatureTerrain new_terrain = new_case.getNature();
        if (new_terrain == NatureTerrain.EAU) {
            throw new IllegalArgumentException("Le robot à pattes ne peut pas aller sur de l'eau");
        }
        super.setPosition(new_case);
    }

    @Override
    public double getVitesse(NatureTerrain nature) {
        if (nature == NatureTerrain.ROCHE)
            return 10;
        else if (nature != NatureTerrain.EAU)
            return this.getVitesseDefaut();
        else
            return 0;
    }

    @Override
    public String toString() {
        return "RobotPattes " + super.toString();
    }
}
