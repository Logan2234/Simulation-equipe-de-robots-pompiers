package Evenements;

import Donnees.Case;
import Donnees.Robot.Robot;
import Exceptions.CellOutOfMapException;

public class EventMouvement extends Evenement {
    private Robot robot;
    private Case nextCase;

    public EventMouvement(long date, Robot robot, Case _case) {
        super(date);
        this.robot = robot;
        this.nextCase = _case;
        robot.setLastDate(date);
    }

    @Override
    public void execute() throws CellOutOfMapException {
        robot.setPosition(this.nextCase);
    }

    @Override
    public String toString() {
        return "EventMouvement [date=" + this.getDate() + "]";
    }

}
