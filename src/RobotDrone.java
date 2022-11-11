public class RobotDrone extends Robot {
    public RobotDrone(Case position) {
        super(position, 10000, 100, 30, 10000, 1800);
    }

    public RobotDrone(Case position, int vitesse) {
        super(position, 10000, vitesse, 30, 10000, 1800);
    }

    @Override
    public void remplirReservoir() {
        boolean aboveWater = false;
        Case pos = this.getPosition();
        for (Direction dir : Direction.values()) {

            if (pos.getCarte().getVoisin(pos, dir).getNature() == NatureTerrain.EAU) {
                aboveWater = true;
                break;
            }
        }
        if (aboveWater)
            this.fillReservoir();
        else
            throw new IllegalArgumentException(
                    "Il n'est pas possible de remplir le réservoir du drône sans être au-dessus d'une source d'eau");
    }

    @Override
    public String toString() {
        return "RobotDrone " + super.toString();
    }    
}
