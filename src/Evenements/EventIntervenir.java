package Evenements;

import Donnees.Incendie;
import Donnees.Robot.Robot;
import Exceptions.NoFireException;

public class EventIntervenir extends Evenement {
    private final Robot robot;
    private final Incendie incendie;

    /**
     * Evenement qui, quand il sera {@code execute}, va faire éteindre à {@code robot}
     * un incendie {@code incendie} à la date {@code date} 
     * @param date : date du début de l'événement
     * @param robot : robot qui va verser son eau
     * @param incendie : incendie que l'on va éteindre
     */
    public EventIntervenir(long date, Robot robot, Incendie incendie) {
        super(date + robot.getTmpVersement());
        this.robot = robot;
        this.incendie = incendie;
        robot.setLastDate(date + robot.getTmpVersement());
    }

    /**
     * Va verser l'eau sur l'incendie en débittant la quantité d'eau nécessaire et en diminuant l'eau nécessaire
     * à éteindre le feu
     * 
     * @throws NoFireException Exception levée dans le cas où il n'y a pas de feu
     *                         sur la case de notre robot
     */
    @Override
    public void execute() throws NoFireException {
        if (!this.incendie.getPosition().equals(robot.getPosition()))
            throw new NoFireException();

        int qteRequise = this.incendie.getLitres();
        int qteVersee = Math.min(qteRequise, Math.min(this.robot.getQteVersement(), this.robot.getReservoir()));
        this.robot.deverserEau(qteVersee);
        this.incendie.eteindre(qteVersee);
    }

    /**
     * @return String : affiche la date de l'évènement
     */
    @Override
    public String toString() {
        return "EventIntervenir [date=" + this.getDate() + "]";
    }
}
