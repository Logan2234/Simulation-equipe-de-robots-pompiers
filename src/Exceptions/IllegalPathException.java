package Exceptions;

public class IllegalPathException extends Exception {
    public IllegalPathException() {
        super("Ce robot ne peut pas parcourir ce chemin");
    }
}