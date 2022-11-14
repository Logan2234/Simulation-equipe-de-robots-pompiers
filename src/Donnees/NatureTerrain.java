package Donnees;

import java.awt.Color;

public enum NatureTerrain {
    EAU(new Color(135, 206, 235)),
    FORET(new Color(34, 139, 34)),
    ROCHE(new Color(128, 132, 135)),
    TERRAIN_LIBRE(Color.WHITE),
    HABITAT(new Color(220, 85, 57));

    private Color color;

    private NatureTerrain(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }
}
