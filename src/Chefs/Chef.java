package Chefs;

import java.util.HashSet;
import java.util.Set;

import Donnees.DonneesSimulation;
import Donnees.Incendie;
import Donnees.Robot.Robot;
import Evenements.Simulateur;
import Exceptions.NoMoreFireException;

abstract public class Chef {
    protected final DonneesSimulation donnees; // Protected car utilisé dans les sous-classes
    protected final Simulateur simulateur; // Protected car utilisé dans les sous-classes
    protected Set<Robot> occupes; // Protected car utilisé dans les sous-classes

    public Chef(DonneesSimulation donnees, Simulateur simulateur) {
        this.donnees = donnees;
        this.simulateur = simulateur;
        this.occupes = new HashSet<Robot>();
    }

    abstract public void strategie() throws NoMoreFireException;

    abstract protected void gestionIncendies(Incendie incendie);
}
