package pacman;

import info.gridworld.grid.Location;

public class Blinky extends Ghost {
    // pre: scatterTarget is a valid location
    // post: creates a new Blinky
    public Blinky(PacMan pacMan, PacManGame game, Location scatterTarget) {
        super(pacMan,game, scatterTarget);
    }
}
