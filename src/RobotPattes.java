public class RobotPattes extends Robot{
    private double vitesse_default = 30;

    public RobotPattes(Case position){
        super(position, 1/0);
    }
    
    @Override
    public double getVitesse(NatureTerrain nature) {
        //if (nature == rocher)
            return 10;
        //else if (nature != eau) {return vitesse_default;}
        //else {return 0}
    }
}
