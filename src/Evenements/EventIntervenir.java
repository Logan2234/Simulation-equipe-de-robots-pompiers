package Evenements;

import Donnees.Incendie;
import Donnees.Robot.Robot;
import Exceptions.NoFireException;

public class EventIntervenir extends Evenement {
    private Robot robot;
    private Incendie incendie;

    public EventIntervenir(long date, Robot robot, Incendie incendie) {
        super(date + robot.getTmpVersement());
        this.robot = robot;
        this.incendie = incendie;
    }

    @Override
    public void execute() throws NoFireException {
        if (!this.incendie.getPosition().equals(robot.getPosition()))
            throw new NoFireException();

        int qteRequise = this.incendie.getLitres();
        int qteVersee;
        if (robot.getCapacite() != -1) { // Si le robot n'est pas un robot à pattes
            qteVersee = Math.min(qteRequise, Math.min(this.robot.getQteVersement(), this.robot.getReservoir()));
            this.robot.deverserEau(qteVersee);
        } else
            qteVersee = Math.min(qteRequise, 10);
        this.incendie.eteindre(qteVersee);
    }

    @Override
    public String toString() {
        return "EventIntervenir [date=" + this.getDate() + "]";
    }
}
