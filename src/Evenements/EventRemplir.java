package Evenements;

import Donnees.Robot.Robot;

public class EventRemplir extends Evenement {
    private Robot robot;

    public EventRemplir(long date, Robot robot) {
        super(date + (long)(robot.getTmpRemplissage() * 1 - robot.getReservoir() / robot.getCapacite()));
        this.robot = robot;
    }

    @Override
    public void execute() {
        robot.remplirReservoir();
    }
}
