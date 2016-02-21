package pacman;

// ROWAN LINDSAY + NAT REDFERN
// 2/02/16
// APCS
// a blueprint for Ghost class. Ghost is a special bug that, in chase mode,
// moves to the Location closest to its target Locacation. Method's of acquiring
// targetTiles vary
import java.awt.Color;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import info.gridworld.actor.*;
import java.util.ArrayList;

public class Ghost extends Bug
{
    private final PacMan PAC_MAN;
    private Location targetTile;
    private int moveDirection;
    private PacManGame game;
    private Location scatterTarget;

    // Pre: none 
    // Post: initializes pac man
    public Ghost(PacMan pacMan, PacManGame game, Location scatterTarget)
    {
        this.PAC_MAN=pacMan;
        targetTile = null;
        moveDirection = 0;
        this.game = game;
        this.scatterTarget = scatterTarget;
        setColor(null);
    }
    
    // Pre: none
    // Post: returns pacman modes and acts
    public void act()
    {
        Grid<Actor> gr = getGrid();
        String mode = getGame().getMode();
        if(mode.equals("CHASE"))
            targetTile = chooseTargetTile();
        else if(mode.equals("SCATTER"))
            targetTile = scatterTarget;
        Location next = getLocation();
        if(mode.equals("FRIGHTENED"))
        {
            setColor(Color.blue);
            int listSize = possibleLocations().size();
            if(listSize >= 1)
                next = possibleLocations().get((int)(Math.random()*listSize));
        }
        else
        {
            setColor(null);
            next = bestLocation(possibleLocations());
        }
        moveDirection = getLocation().getDirectionToward(next);
        if(gr.get(next).equals(PAC_MAN) && game.getMode().equals("FRIGHTENED")) {
            game.playAudio(PacManMap.PACMAN_EAT_GHOST);
            removeSelfFromGrid();
        }
        else
            moveTo(next);
    }
    
    // Pre: none
    // Post: returns an array list of locations
    public ArrayList<Location> possibleLocations()
    {
        Grid<Actor> gr = getGrid();

        ArrayList<Location> possibleLocations = new ArrayList<Location>();
        for(Location loc : gr.getValidAdjacentLocations(getLocation()))
        {
            Actor a = gr.get(loc);
            boolean empty = (a instanceof BackTile || a instanceof PacMan || a instanceof Dot);
            int locDirection = getLocation().getDirectionToward(loc);
            boolean notRearMove = !(areOpposite(moveDirection,locDirection));

            if( empty && straightMove(loc) && notRearMove)
                possibleLocations.add(loc);
        }
        return possibleLocations;
    }
    
    // Pre: none
    // Post: Finds best possible locations
    public Location bestLocation(ArrayList<Location> possibleLocations)
    {
        Location bestLocation = getLocation();
        double distanceToTarget = Integer.MAX_VALUE;
        for(Location loc : possibleLocations)
        {
            double thisDistance = getDistance(targetTile,loc);
            if(thisDistance < distanceToTarget)
            {
                distanceToTarget = thisDistance;
                bestLocation = loc;
            }
        }
        return bestLocation;
    }

    // Pre: none
    // Post: chosses target tile
    public Location chooseTargetTile()
    {
        return PAC_MAN.getLocation();
    }
    
    // Pre: d1 and d2 are straight compass directions
    // Post: returns true if two directions are compass opposites
    public boolean areOpposite(int d1, int d2)
    {
        if(d1 > d2)
            return ((d1 - d2) == 180);
        else if(d2 > d1)
            return ((d2 - d1) == 180);
        return false;
    }

    // Pre: none
    // Post: gets distance between two points
    public double getDistance(Location a, Location b)
    {
        double p1=(a.getCol()-b.getCol())*(a.getCol()-b.getCol());
        double p2=(a.getRow()-b.getRow())*(a.getRow()-b.getRow());
        double answer=Math.sqrt(p1+p2);
        return answer;
    }

    // Pre: none
    // Post: returns true if loc is a straight move from the current location
    public boolean straightMove(Location loc)
    {
        int dir = getLocation().getDirectionToward(loc);
        return ((dir % 90) == 0) || ((dir % 90) == 90);
    }

    
    // Pre: none
    // Post: returns target tile
    public Location getTargetTile()
    {
        return targetTile;
    }
    
    // Pre: none
    // Post: returns pac man
    public PacMan getPacMan()
    {
        return PAC_MAN;
    }
    
    // Pre: none 
    // Post: returns game
    public PacManGame getGame()
    {
        return game;
    }
    
    // Pre: none
    // Post: returns scatter target
    public Location getScatterTarget()
    {
        return scatterTarget;
    }
}