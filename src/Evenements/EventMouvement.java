package Evenements;

import Donnees.Case;
import Donnees.Robot.Robot;

public class EventMouvement extends Evenement {
    private final Robot robot;
    private final Case nextCase;

    public EventMouvement(long date, Robot robot, Case cell) {
        super(date);
        this.robot = robot;
        this.nextCase = cell;
        robot.setLastDate(date);
    }

    @Override
    public void execute() {
        robot.setPosition(this.nextCase);
    }

    /**
     * @return String : Affiche la date de l'évènement
     */
    @Override
    public String toString() {
        return "EventMouvement [date=" + this.getDate() + "]";
    }

}
