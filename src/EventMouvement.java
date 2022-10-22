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
    public void execute() throws IllegalArgumentException {
        if (this.dir != null) {
            Case pos = robot.getPosition();
            Carte carte = Case.getCarte();
            Case new_pos = carte.getVoisin(pos, this.dir);
            robot.setPosition(new_pos);
        } else {
            robot.setPosition(this.nextCase);
        }
    }

}
