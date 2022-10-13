public abstract class Robot {
    private Case position;
    private int capacite;
    private int reservoir;
    
    private double vitesse_defaut;
    
    public Robot(Case position, int capacite, double vitesse) {
        this.position = position;
        this.capacite = capacite;
        this.reservoir = capacite; //TODO: Demander au prof si on commence à 0 ou plein
        this.vitesse_defaut = vitesse;
    }
    
    public Case getPosition() {
        return this.position;
    }

    public void setPositon(Case position) {
        this.position = position;
    }
    
    public void fillReservoir() {
        this.reservoir = this.capacite;
    }

    public double getVitesseDefaut() {
        return this.vitesse_defaut;
    }

    public double getVitesse(NatureTerrain nature){
        return this.vitesse_defaut;
    }

    public void deverserEau(int quantite) {
        if (quantite > reservoir) {
            throw new IllegalArgumentException("On ne peut pas deverser plus d'eau que l'eau dans le réservoir !");
        } else {
            this.reservoir -= quantite;
        }
    }

    public void remplirReservoir() {
        boolean waterNear = false;
        for (Direction dir : Direction.values()){
            if (Case.getCarte().getVoisin(position, dir).getNature() == NatureTerrain.EAU){
                waterNear = true;
                break;
            }
        }
        if (waterNear)
            this.reservoir = this.capacite;
        else
            throw new IllegalArgumentException("Il n'est pas possible de remplir un réservoir sans être à proximité d'une source d'eau");
    }
}
