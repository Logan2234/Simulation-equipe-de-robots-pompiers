package Donnees;

import java.awt.Color;

/**
 * Enum des différents terrains.
 * Chaque terrain est associé à une couleur utilisée par la fonction {@code Dessin.dessin}
 */
public enum NatureTerrain {
    EAU(new Color(135, 206, 235)),
    FORET(new Color(4, 106, 56), "assets/Forest.png"),
    ROCHE(new Color(128, 132, 135)),
    TERRAIN_LIBRE(new Color(48, 183, 0)),
    HABITAT(new Color(220, 85, 57));

    private Color color;
    private String image_path;

    private NatureTerrain(Color color) {
        this.color = color;
    }

    private NatureTerrain(Color color, String image_path) {
        this.color = color;
        this.image_path = image_path;
    }

    public Color getColor() {
        return this.color;
    }

    public String getImagePath() {
        return this.image_path;
    }
}
