package Donnees;

/**
 * Enum des différents terrains.
 * Chaque terrain est associé un chemin vers une image utilisée par la fonction
 * {@code Dessin.dessin}
 */
public enum NatureTerrain {
    EAU("assets/Water.png"),
    FORET("assets/Forest.png"),
    ROCHE("assets/Rocks.png"),
    TERRAIN_LIBRE("assets/Grass.jpg"),
    HABITAT("assets/House.png");

    private String image_path;

    private NatureTerrain(String image_path) {
        this.image_path = image_path;
    }

    public String getImagePath() {
        return this.image_path;
    }
}
