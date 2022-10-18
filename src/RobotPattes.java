public class RobotPattes extends Robot {
    public RobotPattes(Case position) {
        super(position, -1, 30);
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
}
