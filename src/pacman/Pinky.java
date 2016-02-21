package pacman;

/**
 * Rowan Lindsay and Nat Redfern
 * Period 5
 * feb 2, 2016
 * Pinky is a ghost whose target is the space 4 ahead of PacMan
 */
import info.gridworld.grid.Location;

public class Pinky extends Ghost {
    
    public Pinky(PacMan pacMan,PacManGame game,Location scatterTarget) {
        super(pacMan,game,scatterTarget);
    }
    
    // pre: this Pinky's pacMan is on the grid
    // post: returns the Location 4 tiles ahead of PacMan
    public Location chooseTargetTile() {
        PacMan pacman = getPacMan();
        Location targetTile = pacman.getLocation();
        int pacDirection = pacman.getDirection();
        for(int i = 1; i < 4; i++)
            targetTile = targetTile.getAdjacentLocation(pacDirection);
        return targetTile;
    }
}
