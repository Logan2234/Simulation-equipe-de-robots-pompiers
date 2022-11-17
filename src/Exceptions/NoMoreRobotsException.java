package Exceptions;

public class NoMoreRobotsException extends Exception {
    public NoMoreRobotsException() {
        super("Tous les robots ont vidé leur réservoir");
    }
}
