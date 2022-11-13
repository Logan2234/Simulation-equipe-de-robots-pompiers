package Evenements;

import Donnees.Incendie;
import Donnees.Robot.Robot;
import Exceptions.NoFireException;

public class EventIntervenir extends Evenement {
    private Robot robot;
    private Incendie incendie;

    public EventIntervenir(long date, Robot robot, Incendie incendie) {
        super(date);
        this.robot = robot;
        this.incendie = incendie;
    }

    @Override
    public void execute() throws NoFireException {
        if (!this.incendie.getPosition().equals(robot.getPosition()))
            throw new NoFireException();

        int qteRequise = this.incendie.getLitres();
        int qteVersee = Math.min(qteRequise, this.robot.getReservoir());
        this.robot.deverserEau(qteVersee);
        if (qteRequise == qteVersee)
            ;
        else
            this.incendie.eteindre(qteVersee);
    }
}
