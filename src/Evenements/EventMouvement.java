package Evenements;

import Donnees.Carte;
import Donnees.Case;
import Donnees.Direction;
import Donnees.Robot.Robot;
import Exceptions.CellOutOfMapException;

public class EventMouvement extends Evenement {
    private Robot robot;
    private Direction dir;
    private Case nextCase;

    public EventMouvement(long date, Robot robot, Direction dir) {
        super(date);
        this.robot = robot;
        this.dir = dir;
        this.nextCase = null;
    }

    public EventMouvement(long date, Robot robot, Case _case) {
        super(date);
        this.robot = robot;
        this.dir = null;
        this.nextCase = _case;
    }

    @Override
    public void execute() throws CellOutOfMapException {
        if (this.dir != null) {
            Case pos = robot.getPosition();
            Carte carte = pos.getCarte();
            Case new_pos = carte.getVoisin(pos, this.dir);
            robot.setPosition(new_pos);
        } else {
            robot.setPosition(this.nextCase);
        }
    }

    @Override
    public String toString() {
        return "EventMouvement [date=" + this.getDate() + "]";
    }

}
