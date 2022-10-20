import java.util.LinkedList;

public class EventIntervenir extends Evenement{
    private Robot robot;
    private LinkedList<Incendie> incendies;

    public EventIntervenir(long date, Robot robot, LinkedList<Incendie> incendies){
        super(date);
        this.robot = robot;
        this.incendies = incendies;
    }

    @Override
    public void execute(){
        // TODO: Tout est faux la
        // int i =0;
        // while (i < incendies.length && !(this.incendies[i].getPosition().equals(robot.getPosition()))){
        //     i++;
        // }
        // if (i >= incendies.length){
        //     throw new IllegalArgumentException("Il n'y a pas d'incendies ici");
        // }
        // int qteRequise = this.incendies[i].getLitres();
        // int qteVersee = Math.min(qteRequise, this.robot.getReservoir());
        // this.robot.deverserEau(qteVersee);
        // if (qteRequise == qteVersee){
        //     this.incendies.
        // }
        // else
        //     this.incendies[i].eteindre(qteVersee);
        
        // TODO: IL FAUT ABSOLUMENT RETIRER LES LITRES NECESSAIRES DU FEU 
    }
}
