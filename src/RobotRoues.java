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
    public void setPosition(Case new_case){
        NatureTerrain new_terrain = new_case.getNature();
        if (new_terrain == NatureTerrain.EAU || new_terrain == NatureTerrain.ROCHE || new_terrain == NatureTerrain.FORET){
            throw new IllegalArgumentException("Le robot à roues ne peut se déplacer que sur du terrain libre ou des habitats.");
        }
        super.setPosition(new_case);
    }

    @Override
    public double getVitesse(NatureTerrain nature) {
        if (nature == NatureTerrain.HABITAT || nature == NatureTerrain.TERRAIN_LIBRE) {
            return this.getVitesseDefaut();
        }
        return 0;
    }

    @Override
    public String toString() {
        return "RobotRoues " + super.toString();
    }    
}