package Exceptions;

public class PasDeCheminException extends Exception {
    public PasDeCheminException() {
        super("Il n'existe pas de chemin disponible pour ce robot entre les deux cases.");
    }
}