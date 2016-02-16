package pacman;

/**
 * Rowan Lindsay and Nat Redfern
 * Period 5
 * feb 2, 2016
 * Wall is a rock that has a custom image and the ability to 'extend' itself
 */
 
import info.gridworld.grid.*;
import info.gridworld.actor.*;

public class Wall extends Rock
{
    // pre: direction is a basic straight direction (0,90 etc)
    // post: creates a new Wall
    public Wall(int direction)
    {
        super();
        setColor(null);
        setDirection(direction);
        // direction = direction of length of wall
    }
    
    // pre: numSpaces does not extend beyond the bounds of the grid, direction
    // is a basic straight direction
    // post: places walls extending in a direction from this wall
    public void extend(int numSpaces, int direction)
    {
        Grid<Actor> gr = getGrid();
        Location baseLoc = getLocation();
        for(int i = 0; i < numSpaces; i++)
        {
            Location nextLoc = baseLoc.getAdjacentLocation(direction);
            Wall nextSegment = new Wall(direction);
            nextSegment.putSelfInGrid(gr,nextLoc);
            baseLoc = nextLoc;
        }
    }
}