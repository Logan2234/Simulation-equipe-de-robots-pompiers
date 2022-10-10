public abstract class Robot{
    private Case position;
    private int capacite;
    private int reservoir;
    
    public Robot(Case position, int capacite) {
    	this.position = position;
    	this.capacite = capacite;
        this.reservoir = capacite;
    }
    
    public Case getPosition() {
    	return this.position;
    }
    
    public void setPositon(Case position) {
    	this.position;
    }
    
    public double getVitesse(NatureTerrain nature) {
        // C'est une vitesse par défaut, mais pas vraie pour les robots terrestres. Il FAUT override
    	return (double) 100;
    }
    
    public void deverserEau(int quantite) {
    	if (quantite > reservoir){
            throw new IllegalArgumentException("On ne peut pas deverser plus d'eau que eau dans le réservoir !");
        } else {
            this.reservoir = this.reservoir - quantite;
        }
    }
    
    public void remplirReservoir() {
    	this.reservoir = this.capacite
    }
}
