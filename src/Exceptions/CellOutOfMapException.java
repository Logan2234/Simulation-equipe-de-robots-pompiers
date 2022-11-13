package Exceptions;

public class CellOutOfMapException extends Exception {
    public CellOutOfMapException() {
        super("Le robot voulait accéder à une case en dehors de la carte");
    }
}