package Evenements;

import Donnees.Robot.Robot;

public class EventRemplir extends Evenement {
    private Robot robot;

    public EventRemplir(long date, Robot robot) {
        super(date);
        this.robot = robot;
    }

    @Override
    public void execute() {
        robot.remplirReservoir();
    }
}
