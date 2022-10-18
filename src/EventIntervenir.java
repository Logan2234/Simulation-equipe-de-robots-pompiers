public class EventIntervenir extends Evenement{
    private Robot robot;
    private Incendie[] incendies;

    public EventIntervenir(Robot robot, long date, Incendie[] incendies){
        super(date);
        this.robot = robot;
        this.incendies = incendies;
    }

    @Override
    public void execute(){
        int i =0;
        while (i < incendies.length && !(this.incendies[i].getPosition().equals(robot.getPosition()){
            i++;
        }
        if (i >= incendies.length){
            throw new IllegalArgumentException("il n'y a pas d'incendies ici");
        }
        this.robot.deverserEau(this.incendies[i].getLitres());
    }
}
