package Exceptions;

public class IllegalCheminRobotException extends Exception {
    public IllegalCheminRobotException() {
        super("Ce robot ne peut pas parcourir ce chemin");
    }
}