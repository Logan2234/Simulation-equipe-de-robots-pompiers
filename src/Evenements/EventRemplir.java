package Evenements;

import Donnees.Robot.Robot;
import Exceptions.NoWaterException;

public class EventRemplir extends Evenement {
    private Robot robot;

    public EventRemplir(long date, Robot robot) {
        super(date);
        this.robot = robot;
    }

    @Override
    public void execute() throws NoWaterException {
        Simulateur.updateDate(
                (long) (robot.getTmpRemplissage() * (1 - (float) robot.getReservoir() / robot.getCapacite())) + 1);
        robot.remplirReservoir();
    }

    @Override
    public String toString() {
        return "EventRemplir [date=" + this.getDate() + "]";
    }
}
