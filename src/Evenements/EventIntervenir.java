package Evenements;

import java.util.LinkedList;

import Donnees.Incendie;
import Donnees.Robot.Robot;

public class EventIntervenir extends Evenement {
    private Robot robot;
    private LinkedList<Incendie> incendies;

    public EventIntervenir(long date, Robot robot, LinkedList<Incendie> incendies) {
        super(date);
        this.robot = robot;
        this.incendies = incendies;
    }

    @Override
    public void execute() {
        int i = 0;
        while (i < incendies.size() && !(this.incendies.get(i).getPosition().equals(robot.getPosition())))
            i++;

        if (i >= incendies.size())
            throw new IllegalArgumentException("Il n'y a pas d'incendies ici");

        int qteRequise = this.incendies.get(i).getLitres();
        int qteVersee = Math.min(qteRequise, this.robot.getReservoir());
        this.robot.deverserEau(qteVersee);
        if (qteRequise == qteVersee)
            this.incendies.remove(i);
        else
            this.incendies.get(i).eteindre(qteVersee);
    }
}
