package Evenements;

import Donnees.Case;
import Donnees.Robot.Robot;

public class EventMouvement extends Evenement {
    private final Robot robot;
    private final Case nextCase;

    /**
     * Evenement qui, quand il sera {@code execute}, va déplacer à la date {@code date} le robot {@code robot} sur la case {@code cell}.
     * Les méthodes qui l'appelleront vérifieront que le robot peut se déplacer sur la case demandée.
     * 
     * @param date : date où le robot est sur la case
     * @param robot : robot qui est déplacé
     * @param cell : case où il est déplacé
     */
    public EventMouvement(long date, Robot robot, Case cell) {
        super(date);
        this.robot = robot;
        this.nextCase = cell;
        robot.setLastDate(date);
    }

    /**
     * Va modifier la position du robot
     */
    @Override
    public void execute() {
        robot.setPosition(this.nextCase);
    }

    /**
     * @return String : affiche la date de l'évènement
     */
    @Override
    public String toString() {
        return "EventMouvement [date=" + this.getDate() + "]";
    }

}
