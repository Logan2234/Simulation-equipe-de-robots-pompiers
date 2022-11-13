package Exceptions;

public class PasEauDansCarte extends Exception {
    public PasEauDansCarte() {
        super("Il n'y a pas d'eau dans la carte.");
    }
}
