package Evenements;

import Donnees.Robot.Robot;
import Exceptions.NoWaterException;

public class EventRemplir extends Evenement {
    private Robot robot;

    public EventRemplir(long date, Robot robot) {
        super(date + robot.getTmpRemplissage());
        this.robot = robot;
    }

    @Override
    public void execute() throws NoWaterException {
        robot.remplirReservoir();
    }

    @Override
    public String toString() {
        return "EventRemplir [date=" + this.getDate() + "]";
    }
}
