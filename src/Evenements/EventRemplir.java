package Evenements;

import Donnees.Robot.Robot;
import Exceptions.NoWaterException;

public class EventRemplir extends Evenement {
    private Robot robot;

    public EventRemplir(long date, Robot robot) {
        super(date + (long) (robot.getTmpRemplissage() * 1 - robot.getReservoir() / robot.getCapacite()));
        this.robot = robot;
    }

    @Override
    public void execute() throws NoWaterException {
        robot.remplirReservoir();
    }
}
