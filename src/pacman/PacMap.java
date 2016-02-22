package pacman;

import info.gridworld.grid.Location;

public class PacMap {
  
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
    
    // Game Modes
    public static final String SCATTER_MODE = "Scatter";
    public static final String CHASE_MODE = "Chase";
    public static final String FRIGHTENED_MODE = "Frightened";
    
    // Game Constants
    public static final int PHASE_LENGTH = 30;
    public static final String START_MODE = SCATTER_MODE;
    public static final int NUMBER_OF_CHERRIES = 10;
    
    // Ghost Scatter Targets
    public static final Location BLINKY_SCATTER_TARGET_LOCATION = new Location(0,26);
    public static final Location PINKY_SCATTER_TARGET_LOCATION = new Location(0,0);
    public static final Location INKY_SCATTER_TARGET_LOCATION = new Location(30,26);
    public static final Location CLYDE_SCATTER_TARGET_LOCATION = new Location(30,0);
    public static final Location[] GHOST_SCATTER_TARGET_LOCATIONS = 
            {PacMap.BLINKY_SCATTER_TARGET_LOCATION,PacMap.PINKY_SCATTER_TARGET_LOCATION,
            PacMap.INKY_SCATTER_TARGET_LOCATION,PacMap.CLYDE_SCATTER_TARGET_LOCATION};
    
    // Game Info
    public static final String CURRENT_VERSION = "0.5.2a";
    public static final String GAME_MESSAGE = 
            "GridPacMan v" + CURRENT_VERSION + 
            "   |   Use arrow keys to move - avoid the ghosts";
    public static final String GAME_OVER_MESSAGE = 
            " { GAME OVER } ";
}
