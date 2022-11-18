package Evenements;

import Donnees.Robot.Robot;
import Exceptions.NoWaterException;

public class EventRemplir extends Evenement {
    private final Robot robot;

    /**
     * Evenement qui, quand il sera {@code execute},
     * à {@code date} va remplir le réservoir du robot {@code robot}.
     * 
     * @param date : date où on commence le remplissage du robot
     * @param robot : robot dont on remplit le réservoir
     */
    public EventRemplir(long date, Robot robot) {
        super(date + robot.getTmpRemplissage());
        this.robot = robot;
        robot.setLastDate(date + robot.getTmpRemplissage());
    }

    /**
     * Va exécuter l'événement
     * @throws NoWaterException Si on n'a pas d'eau à proximité
     */
    @Override
    public void execute() throws NoWaterException {
        robot.remplirReservoir();
    }

    /**
     * @return String : affiche la date de l'évènement
     */
    @Override
    public String toString() {
        return "EventRemplir [date=" + this.getDate() + "]";
    }
}
