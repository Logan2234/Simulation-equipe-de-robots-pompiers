public class RobotRoues extends Robot {
    // private int tempsRemplissage = 10;
    // Intervention unitaire : 100 litres / 5secs

    public RobotRoues(Case position) {
        super(position, 5000, 80);
    }

    public RobotRoues(Case position, double vitesse) {
        super(position, 5000, vitesse);
    }

    @Override
    public double getVitesse(NatureTerrain nature) {
        if (nature == NatureTerrain.HABITAT || nature == NatureTerrain.TERRAIN_LIBRE) {
            return super.getVitesseDefaut();
        }
        return 0;
    }
}