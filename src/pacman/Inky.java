package pacman;

/**
 * Rowan Lindsay and Nat Redfern
 * Inky is a ghost whose targetTile is determined using Blinky
 */

import info.gridworld.grid.Location;

public class Inky extends Ghost {
    private Blinky blinky;
    
    // pre: Blinky is on the Grid, scatterTarget is a valid location
    // post: creates a new Inky
    public Inky(PacMan pacMan, PacManGame game, Blinky blinky, Location scatterTarget) {
        super(pacMan,game,scatterTarget);
        this.blinky = blinky;
    }
    
    // pre: Blinky is still on the grid
    // post: returns the double vector of Blinky's location to Blinky's targetTile
    public Location chooseTargetTile() {
        if(!(blinky.getTargetTile()== null) && blinky.getGrid() != null) {
            Location blinkyLoc = blinky.getLocation();
            Location blinkyTarget = blinky.getTargetTile();
            return doubleDistance(blinkyLoc, blinkyTarget);
        }
        return getLocation();
    }
    
    // pre: none
    // post: returns the doubled extrapulation of the 'vector' between loc1 and loc2
    public Location doubleDistance(Location loc1, Location loc2) {
        int newRow = (2*loc2.getRow())-loc1.getRow();
        int newCol = (2*loc2.getCol())-loc1.getCol();
        return new Location(newRow, newCol);
    }
}
