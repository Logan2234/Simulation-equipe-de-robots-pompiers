public class RobotDrone extends Robot {
    public RobotDrone(Case position) {
        super(position, 10000, 100);
    }

    public RobotDrone(Case position, int vitesse) {
        super(position, 10000, vitesse);
    }

    @Override
    public void remplirReservoir() {
        boolean aboveWater = false;
        for (Direction dir : Direction.values()){
            if (Case.getCarte().getVoisin(this.getPosition(), dir).getNature() == NatureTerrain.EAU){
                aboveWater = true;
                break;
            }
        }
        if (aboveWater)
            this.fillReservoir();
        else
            throw new IllegalArgumentException("Il n'est pas possible de remplir le réservoir du drône sans être au-dessus d'une source d'eau");
    }
}
