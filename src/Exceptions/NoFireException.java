package Exceptions;

public class NoFireException extends Exception {
    public NoFireException() {
        super("Il n'y a pas d'incendie sur cette case");
    }
    
}
