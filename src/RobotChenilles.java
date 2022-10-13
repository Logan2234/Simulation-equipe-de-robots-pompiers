public class RobotChenilles extends Robot {
    public RobotChenilles(Case position) {
        super(position, 2000, 60);
    }

    public RobotChenilles(Case position, double vitesse) {
        super(position, 2000, vitesse);
    }

    @Override
    public double getVitesse(NatureTerrain nature) {
        if (nature == NatureTerrain.FORET)
            return super.getVitesseDefaut() / 2;
        else if (nature != NatureTerrain.EAU && nature != NatureTerrain.ROCHE)
            return super.getVitesseDefaut();
        else 
            return 0;
    }
}
