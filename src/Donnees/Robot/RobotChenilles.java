package Donnees.Robot;

import Donnees.Case;
import Donnees.NatureTerrain;

public class RobotChenilles extends Robot {
    public RobotChenilles(Case position) {
        super(position, 2000, 60/3.6, 8, 100, 300);
    }

    public RobotChenilles(Case position, double vitesse) {
        super(position, 2000, Math.min(vitesse, 80)/3.6, 8, 100, 300);
    }

    @Override
    public void setPosition(Case new_case) {
        NatureTerrain new_terrain = new_case.getNature();
        if (new_terrain == NatureTerrain.EAU || new_terrain == NatureTerrain.ROCHE) {
            throw new IllegalArgumentException("Le robot à chenilles ne peut pas aller sur de l'eau ou des rochers");
        }
        super.setPosition(new_case);
    }

    @Override
    public double getVitesse(NatureTerrain nature) {
        if (nature == NatureTerrain.FORET)
            return this.getVitesseDefaut() / 2;
        else if (nature != NatureTerrain.EAU && nature != NatureTerrain.ROCHE)
            return this.getVitesseDefaut();
        else
            return 0;
    }

    @Override
    public String toString() {
        return "RobotChenilles " + super.toString();
    }
}
