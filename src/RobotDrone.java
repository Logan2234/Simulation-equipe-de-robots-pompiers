public class RobotDrone extends Robot {
    public RobotDrone(Case position) {
        super(position, 10000, 100);
    }

    public RobotDrone(Case position, int vitesse) {
        super(position, 10000, vitesse);
    }

    @Override
    public void remplirReservoir() {
        // TODO
    }

    @Override
    public double getVitesse(NatureTerrain nature) {
        return super.getVitesseDefaut();
    }
}
