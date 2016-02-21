package pacman;

public class PacMap {
    
    // Game Info
    public static final String CURRENT_VERSION = "0.5.2a";
    public static final String GAME_MESSAGE = 
            "GridPacMan v" + CURRENT_VERSION + 
            "   |   Use arrow keys to move - avoid the ghosts";
    
    // Game Constants
    public static final int PHASE_LENGTH = 30;
    public static final String START_MODE = "SCATTER";
    
    // Input
    public static final String PACMAN_TURN_BUTTON_0 = "UP";
    public static final String PACMAN_TURN_BUTTON_180 = "DOWN";
    public static final String PACMAN_TURN_BUTTON_270 = "LEFT";
    public static final String PACMAN_TURN_BUTTON_90 = "RIGHT";
    
    // Sounds
        // Game
    public static final String INTRO_MUSIC = "./src/pacman/sounds/pacman_beginning.wav";
        // Pacman
    public static final String PACMAN_ACT = "./src/pacman/sounds/pacman_chomp.wav";
    public static final String PACMAN_DEATH = "./src/pacman/sounds/pacman_death.wav";
    public static final String PACMAN_EAT_FRUIT = "./src/pacman/sounds/pacman_eatfruit.wav";
    public static final String PACMAN_EAT_GHOST = "./src/pacman/sounds/pacman_eatghost.wav";
}
