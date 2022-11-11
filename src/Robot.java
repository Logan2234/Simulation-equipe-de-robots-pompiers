public abstract class Robot {
    
    private Case position;
    private int capacite;
    private int reservoir;
    private int tmpVersement; // en s.
    private int qteVersement;
    private int tmpRemplissage; // en s.
    
    private double vitesseDefaut;
    
    public Robot(Case position, int capacite, double vitesse, int tmpVersement, int qteVersement, int tmpReplissage) {
        this.position = position;
        this.capacite = capacite;
        this.reservoir = capacite;
        this.tmpVersement = tmpVersement;
        this.qteVersement = qteVersement;
        this.tmpRemplissage = tmpReplissage;
    }
    
    public Case getPosition() {
        return this.position;
    }
    
    public int getReservoir(){
        return this.reservoir;
    }

    public double getVitesseDefaut() {
        return this.vitesseDefaut;
    }
    
    public double getVitesse(NatureTerrain nature){
        return this.vitesseDefaut;
    }
    
    public int getCapacite() {
        return capacite;
    }

    public int getTmpVersement() {
        return tmpVersement;
    }
    
    public int getQteVersement() {
        return qteVersement;
    }
    
    public int getTmpRemplissage() {
        return tmpRemplissage;
    }
    
    /* 
    * Méthode de base si on a aucune contrainte en fonction de la nature du terrain.
     * Sinon on override cette fonction dans les classes filles.
     */
    public void setPosition(Case new_pos) {
        this.position = new_pos;
    }
    
    // Méthode uniquement utilisée par le drone qui remplit son reservoir différemment (cf. RobotDrone.java)
    public void fillReservoir() {
        this.reservoir = this.capacite;
    }
    
    public void deverserEau(int quantite) {
            this.reservoir -= quantite;
    }

    public void remplirReservoir() {
        boolean waterNear = false;
        for (Direction dir : Direction.values()){
            if (this.position.getCarte().getVoisin(this.position, dir).getNature() == NatureTerrain.EAU){
                waterNear = true;
                break;
            }
        }
        if (waterNear)
        this.reservoir = this.capacite;
        else
        throw new IllegalArgumentException("Il n'est pas possible de remplir un réservoir sans être à proximité d'une source d'eau");
    }
    
    @Override
    public String toString() {
        return "[position=" + position + ", capacite=" + capacite + ", reservoir=" + reservoir
                + ", vitesse_defaut=" + vitesseDefaut + "]";
    }

    
}
