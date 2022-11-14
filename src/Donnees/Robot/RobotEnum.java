package Donnees.Robot;

public enum RobotEnum {
    RobotChenilles(RobotChenilles.class, "C"),
    RobotDrone(RobotDrone.class, "D"),
    RobotPattes(RobotPattes.class, "P"),
    RobotRoues(RobotRoues.class, "R");

    private Object classe;
    private String nom;

    private RobotEnum(Object classe, String nom) {
        this.classe = classe;
        this.nom = nom;
    }

    private String getNom() {
        return this.nom;
    }

    private Object getClasse() {
        return this.classe;
    }

    public static String getNom(Object classe){
        for (RobotEnum robot : RobotEnum.values()){
            if (robot.getClasse() == classe)
                return robot.getNom();
        }
        return null;
    }
}
