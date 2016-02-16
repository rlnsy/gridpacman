package pacman;
// ROWAN LINDSAY + NAT REDFERN
// 2/02/16
// APCS
// a blueprint for PacMan class. PacMan is a bug whose direction is affected by user keyStrokes.
import info.gridworld.grid.*;
import info.gridworld.actor.*;

public class PacMan extends Bug 
{
    private PacManGame game;
    private int dotsEaten;
    
    // pre: game is an initialized game
    // post: creates a new PacMan
    public PacMan(PacManGame game)
    {
        this.game = game;
        dotsEaten = 0;
        setColor(null);
    }
    
    // pre: PacMan is on a grid
    // post: makes PacMan move
    public void act()
    {
        if (canMove())
            move();
        if(dotsEaten == 20)
            game.setMode("FRIGHTENED");
    }
    
    // pre: PacMan is on a grid
    // post: moves to adjacent location in PacMan's direction, if the location
    // contains a dot, mark it as eaten
    public void move()
    {
        Grid<Actor> gr = getGrid();
        if (gr == null)
            return;
        Location loc = getLocation();
        Location next = loc.getAdjacentLocation(getDirection());
        if(gr.get(next) instanceof Dot)
        {
            game.getDotLocs().remove(next);
            dotsEaten++;
        }  
        if (gr.isValid(next))
            moveTo(next);
        else
            removeSelfFromGrid();
    }
    
    // pre: direction is positive
    // post: turns PacMan this direction unless there is blocking object in
    // that direction
    public void turnTo(int direction)
    {
        Grid<Actor> gr = getGrid();
        Location pacLoc = getLocation();
        Location turnLoc = pacLoc.getAdjacentLocation(direction);
        if(gr.get(turnLoc) instanceof BackTile || gr.get(turnLoc) instanceof Dot)
            setDirection(direction);
    }
}