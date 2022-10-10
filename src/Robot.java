public abstract class Robot{
    private Case position;
    private int capacite;
    
    public Robot(Case position, int capacite) {
    	this.position = position;
    	this.capacite = capacite;
    }
    
    public Case getPosition() {
    	return this.position;
    }
    
    public void setPositon(Case position) {
    	this.position;
    }
    
    public double getVitesse(NatureTerrain nature) {
    	// TODO
    }
    
    public void deverserEau(int quantite) {
    	//TODO
    }
    
    public void remplirReservoir() {
    	// TODO
    }
}
