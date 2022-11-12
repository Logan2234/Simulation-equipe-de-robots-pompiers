package Evenements;

import java.util.LinkedList;

import Donnees.Incendie;
import Donnees.Robot.Robot;
import Exceptions.NoFireException;

public class EventIntervenir extends Evenement {
    private Robot robot;
    private LinkedList<Incendie> incendies;

    public EventIntervenir(long date, Robot robot, LinkedList<Incendie> incendies) {
        super(date + robot.getTmpVersement());
        this.robot = robot;
        this.incendies = incendies;
    }

    @Override
    public void execute() throws NoFireException {
        int i = 0;
        while (i < incendies.size() && !(this.incendies.get(i).getPosition().equals(robot.getPosition())))
            i++;

        if (i >= incendies.size())
            throw new NoFireException();

            int qteRequise = this.incendies.get(i).getLitres();
            int qteVersee = Math.min(qteRequise, Math.min(this.robot.getQteVersement(), this.robot.getReservoir()));
        this.robot.deverserEau(qteVersee);
        if (qteRequise == qteVersee)
            this.incendies.remove(i);
        else
            this.incendies.get(i).eteindre(qteVersee);
    }

    @Override
    public String toString() {
        return "EventIntervenir [date=" + this.getDate() + "]";
    }
}