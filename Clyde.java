package pacman;

/**
 * Rowan Lindsay and Nat Redfern
 * Period 5
 * feb 2, 2016
 * Clyde is a ghost that alternates between targeting methods depending on its
 * proximity to PacMan
 */

import info.gridworld.grid.Location;

public class Clyde extends Ghost 
{    
    public Clyde(PacMan pacMan, PacManGame game, Location scatterTarget)
    {
        super(pacMan,game,scatterTarget);
    }
    
    // pre:
    // post: returns PacMan's location of Clyde is more than 8 spaces away.
    // otherwise returns his Scatter Location
    public Location chooseTargetTile()
    {
        Location pacLoc = getPacMan().getLocation();
        double distanceToPacMan = getDistance(getLocation(),pacLoc);
        if(distanceToPacMan < 8)
            return getScatterTarget();
        return pacLoc;
    }
}