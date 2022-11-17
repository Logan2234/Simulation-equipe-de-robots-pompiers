package Evenements;

import Donnees.Robot.Robot;
import Exceptions.NoWaterException;

public class EventRemplir extends Evenement {
    private final Robot robot;

    public EventRemplir(long date, Robot robot) {
        super(date + robot.getTmpRemplissage());
        this.robot = robot;
        robot.setLastDate(date + robot.getTmpRemplissage());
    }

    /**
     * @throws NoWaterException : Exception levée dans le cas où il n'y a pas d'eau
     *                          à proximité
     */
    @Override
    public void execute() throws NoWaterException {
        robot.remplirReservoir();
    }

    /**
     * @return String : Affiche la date de l'évènement
     */
    @Override
    public String toString() {
        return "EventRemplir [date=" + this.getDate() + "]";
    }
}
