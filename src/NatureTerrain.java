import java.awt.Color;

public enum NatureTerrain {
    EAU(Color.BLUE),
    FORET(Color.GREEN),
    ROCHE(Color.GRAY),
    TERRAIN_LIBRE(Color.WHITE),
    HABITAT(Color.ORANGE);

    private Color color;

    private NatureTerrain(Color color){
        this.color = color;
    }

    public Color getColor(){
        return this.color;
    }
}
