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

    public double getVitesseDefaut() {
        return this.vitesse_defaut;
    }

    public void setPositon(Case position) {
    	this.position = position;
    }

    public abstract double getVitesse(NatureTerrain nature);

    public void deverserEau(int quantite) {
        if (quantite > reservoir) {
            throw new IllegalArgumentException("On ne peut pas deverser plus d'eau que l'eau dans le réservoir !");
        } else {
            this.reservoir -= quantite;
        }
    }

    public void remplirReservoir() {
        // TODO: Cette fonction est à modifier: prendre en compte la nature du terrain
        // aux alentours pour etre sur qu'on peut remplir.
        // ! Cette fonction sera à overide dans le cas du drone
        this.reservoir = this.capacite;
    }
}
