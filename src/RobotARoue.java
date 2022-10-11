public class RobotARoue{
    
    private double vitesseDefault = 80;
    private int tempsRemplissage = 10;
    // Intervention unitaire : 100 litres / 5secs
    
    public RobotARoue(Case position){
        super(position, 5000);
    }

    @Override
    public double getVitesse(NatureTerrain nature){
        if (nature == EAU || nature == TERRAIN_LIBRE) {
            return this.vitesseDefault;
        }

        return (double) 0;
    }

    @Override
    public void remplirReservoir(){
        // TODO : if (super.position.getLigne() != 0 && )
    }


}