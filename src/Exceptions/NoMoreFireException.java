package Exceptions;

public class NoMoreFireException extends Exception {
    public NoMoreFireException(){
        super("Tous les incendies sont éteints");
    }
}
