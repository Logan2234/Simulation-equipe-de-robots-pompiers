package Exceptions;

public class NoWaterException extends Exception {
    public NoWaterException() {
        super("Il n'y a pas d'eau autour du robot");
    }
}
