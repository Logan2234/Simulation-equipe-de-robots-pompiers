package Chefs;

import java.util.HashSet;

import Donnees.DonneesSimulation;
import Donnees.Incendie;
import Donnees.Robot.Robot;
import Evenements.Simulateur;

abstract public class Chef {
    protected DonneesSimulation donnees; // Protected car utilisé dans les sous-classes
    protected Simulateur simulateur; // Protected car utilisé dans les sous-classes
    protected HashSet<Robot> occupes; // Protected car utilisé dans les sous-classes

    public Chef(DonneesSimulation donnees, Simulateur simulateur){
        this.donnees = donnees;
        this.simulateur = simulateur;
        this.occupes = new HashSet<Robot>();
    }

    abstract public void strategie();
    
    abstract protected void gestionIncendies(Incendie incendie);

}
