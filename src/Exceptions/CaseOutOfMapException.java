package Exceptions;

public class CaseOutOfMapException extends Exception {
    public CaseOutOfMapException() {
        super("Le robot voulait accéder à une case en dehors de la carte");
    }
}