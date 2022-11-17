package Exceptions;

public class NoPathAvailableException extends Exception {
    public NoPathAvailableException() {
        super("Il n'existe pas de chemin disponible pour ce robot entre les deux cases.");
    }
}