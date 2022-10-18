public class EventRemplir extends Evenement {
    private Robot robot;

    public EvenementRemplir(int date, Robot robot){
        super(date);
        this.robot = robot;
    }

    @Override
    public void execute(){
        robot.remplirReservoir();
    }
}
