public class EventMouvement extends Evenement {
    private Robot robot;
    private Direction dir;

    public EventMouvement(long date, Robot robot, Direction dir) {
        super(date);
        this.robot = robot;
        this.dir = dir;
    }

    @Override
    public void execute() {
        Case pos = robot.getPosition();
        Carte carte = Case.getCarte();
        Case new_pos = carte.getVoisin(pos, this.dir);
        robot.setPosition(new_pos);
    }
    
}
