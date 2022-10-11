public class RobotChenilles extends Robots{
    private double vitesse_default = 60;

    public RobotChenilles(Case position){
        super(position, 2000);
    }

    @Override
    public double getVitesse(NatureTerrain nature) {
        //if (nature == foret)
            return vitesse_default/2;
        // else if (nature!=eau && nature!=rocher) { return vitesse_default;}
        // else {return 0;}
    }
}
