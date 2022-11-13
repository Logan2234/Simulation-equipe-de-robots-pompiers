package Exceptions;

public class EmptyRobotsException extends Exception{
    public EmptyRobotsException() {
        super("Tous les robots ont vidés leur réservoir");
    }
}
