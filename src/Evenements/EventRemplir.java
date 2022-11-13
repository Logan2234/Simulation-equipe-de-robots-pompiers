package Evenements;

import Donnees.Robot.Robot;
import Exceptions.NoWaterException;

public class EventRemplir extends Evenement {
    private Robot robot;
    private Simulateur simulateur;

    public EventRemplir(long date, Robot robot, Simulateur simulateur) {
        super(date + 1);
        this.robot = robot;
        this.simulateur = simulateur;
    }

    @Override
    public void execute() throws NoWaterException {
        simulateur.updateDate(
                (long) (robot.getTmpRemplissage() * (1 - (float) robot.getReservoir() / robot.getCapacite())));
        robot.remplirReservoir();
    }

    @Override
    public String toString() {
        return "EventRemplir [date=" + this.getDate() + "]";
    }
}
