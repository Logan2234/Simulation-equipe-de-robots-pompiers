public class RobotDrone extends Robot{
    private double vitesse_default = 50;
    
    @Override
    public double getVitesse(NatureTerrain nature) {
        return vitesse_default;
    }
}
