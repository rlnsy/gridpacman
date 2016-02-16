package pacman;

// ROWAN LINDSAY + NAT REDFERN
// 2/02/16
// APCS
// a blueprint for PacManGame class

import info.gridworld.grid.*;
import info.gridworld.actor.*;
import info.gridworld.world.*;
import java.util.ArrayList;

public class PacManGame extends World<Actor>
{
    private final PacMan PAC_MAN;
    private final Blinky BLINKY;
    private final Pinky PINKY;
    private final Inky INKY;
    private final Clyde CLYDE;
    private final ArrayList<Location> DOT_LOCS = new ArrayList<Location>();
    private final ArrayList<Location> NO_DOT_ZONE;
    private final Location[] GHOST_SCATTER_LOCATIONS = {new Location(0,26),
        new Location(0,0), new Location(30,26), new Location(30,0)};
    private final int PHASE_LENGTH = 16;
    private String mode;
    private final String START_MODE = "SCATTER";
    private int steps;
    
    // initializes the world
    public static void main(String[] args)
    {
      PacManGame world = new PacManGame();
      world.show();
    }
   
   // Pre: none
   // Post: Sets up the grid, pacman, and ghosts
    public PacManGame()
    {
       super(new BoundedGrid<Actor>(31,28));
 
       NO_DOT_ZONE = getNoDotLocations();
       
       Grid<Actor> gr = getGrid();
       
       PAC_MAN = new PacMan(this);
       BLINKY = new Blinky(PAC_MAN,this,GHOST_SCATTER_LOCATIONS[0]);
       PINKY = new Pinky(PAC_MAN,this,GHOST_SCATTER_LOCATIONS[1]);
       INKY = new Inky(PAC_MAN,this,BLINKY,GHOST_SCATTER_LOCATIONS[2]);
       CLYDE = new Clyde(PAC_MAN,this,GHOST_SCATTER_LOCATIONS[3]);
       
       PAC_MAN.putSelfInGrid(gr, new Location (23, 14));
       BLINKY.putSelfInGrid(gr, new Location (8, 21));
       PINKY.putSelfInGrid(gr, new Location (14,13));
       INKY.putSelfInGrid(gr, new Location (14,16));
       CLYDE.putSelfInGrid(gr, new Location (14,15));
       
       mode = START_MODE;
       steps = 0;
       
       makeBackground(gr,true);
       makeBoard(gr);
       placeDots(gr);
    }
    // Pre: none 
    // Post: rotates character depending upon key pressed
    public boolean keyPressed(String description, Location loc)
    {
        if (description.equals("UP"))
            PAC_MAN.turnTo(0);
        else if (description.equals("DOWN"))
             PAC_MAN.turnTo(180);
        else if (description.equals("LEFT"))
             PAC_MAN.turnTo(270);
        else if (description.equals("RIGHT"))
             PAC_MAN.turnTo(90);
        return true;  
    }
    
    // Pre: none
    // Post: revises step to move, but to make background black every move step
    public void step()
    {
        Grid<Actor> gr = getGrid();
        
        ArrayList<Actor> actors = new ArrayList<Actor>();
        for (Location loc : gr.getOccupiedLocations())
            actors.add(gr.get(loc));
        for (Actor a : actors)
        {
            // only act if another actor hasn't removed a
            if (a.getGrid() == gr && PAC_MAN.getGrid() == gr)
                a.act();
        }
        makeBackground(gr,false);
        steps++;
        checkTimer();
    }
    
    // Pre: none
    // Post: makes background black
    public void makeBackground(Grid gr, boolean firstRun)
    {
        //placeDots(gr);
        
        int rows = getGrid().getNumRows();
        int cols = getGrid().getNumCols();
        // define the dimensions of the grid
        ArrayList<Location> tileLocations = new ArrayList<Location>();
        for(int r = 0; r < rows; r++)
        {
            for(int c = 0; c < cols; c++)
            {
                Location checkLoc = new Location(r,c);
                if(!(gr.getOccupiedLocations().contains(checkLoc)))
                {
                    tileLocations.add(checkLoc);
                    if(firstRun)
                        DOT_LOCS.add(checkLoc);
                }
            }
        } // get all empty locations on the grid
        
        for(Location loc : tileLocations)
        {
            BackTile tile = new BackTile();
            tile.putSelfInGrid(gr,loc);
        }
    }
    // Pre: none
    // Post: places dots on the grid
    public void placeDots(Grid gr)
    {
        for(Location loc : DOT_LOCS)
        {
            Object o = gr.get(loc);
            if((o instanceof BackTile || o == null) && !(NO_DOT_ZONE.contains(loc)))
            {
                Dot dot = new Dot();
                dot.putSelfInGrid(gr, loc);
            }
        }
    }
    // Pre: none
    // Post: ___________
    public void checkTimer()
    {
        if(steps >= PHASE_LENGTH)
            switchMode();
        else if(steps >= PHASE_LENGTH/2 && mode.equals("FRIGHTENED"))
        {
            mode = "CHASE";
            steps = 0;
        }
    }
    
    // Pre: none
    // Post: Switches ghost mode
    public void switchMode()
    {
        if(getMode().equals("SCATTER"))
            setMode("CHASE");
        else if(getMode().equals("CHASE"))
            setMode("SCATTER");
        else if(getMode().equals("FRIGHTENED"))
            setMode("CHASE");
        steps = 0;
    }
    // Pre: none 
    // Post: returns mode
    public String getMode()
    {
        return mode;
    }
    // Pre: none 
    // Post: returns valid dot locations
    public ArrayList<Location> getDotLocs()
    {
        return DOT_LOCS;
    }
    
    public void setMode(String mode)
    {
        this.mode = mode;
    }
    
    public ArrayList<Location> getNoDotLocations()
    {
        ArrayList<Location> noDots = new ArrayList<Location>();
        for(Location loc : scanLocations(new Location(10,0),new Location(12,4)))
            noDots.add(loc);
        
        for(Location loc : scanLocations(new Location(16,0),new Location(18,4)))
            noDots.add(loc);
        
        for(Location loc : scanLocations(new Location(10,23),new Location(12,27)))
            noDots.add(loc);
        
        for(Location loc : scanLocations(new Location(16,23),new Location(18,27)))
            noDots.add(loc);
        
        for(Location loc : scanLocations(new Location(13,11),new Location(15,16)))
            noDots.add(loc);
        noDots.add(new Location(12,13));
        noDots.add(new Location(12,14));
        return noDots;
    }
    // Pre: none 
    // Post: scans for locations and returns array list
    public ArrayList<Location> scanLocations(Location loc1,Location loc2)
    {
        ArrayList<Location> scan = new ArrayList<Location>();
        for(int r = loc1.getRow(); r <= loc2.getRow(); r++)
        {
            for(int c = loc1.getCol(); c <= loc2.getCol(); c++)
                scan.add(new Location (r,c));
        }
        return scan;
    }
    
    // The following are methods for constructing the initial game board shapes
    
    // Pre: none 
    // Post: constructs the board
    public void makeBoard(Grid gr)
    {   // make border
        makeOuterWall(gr);
        
        // WALL EXTENSIONS
        
        // top middle extension
        makeExceptionCellDown(0,13,4,14);
        // bottom right extension
        makeExceptionCellLeft(24,27,25,25);
        // bottom left extension
        makeExceptionCell(24,0,25,2);
   
        
        // LEFT HALF OBJECTS
        
        // make top left cell
        makeCell(2,2,4,5);
        // top left2 cell
        makeCell(2,7,4,11);
        // top left bottom cell
        makeCell(6,2,7,5);
        // top left T
        makeCell(6,7,13,8);
        makeExceptionCell(9,8,10,11);
        // line under top left T
        makeCell(15,7,19,8);
        // perpendicular line under above line
        makeCell(21,7,22,11);
        // Bottom left T
        makeCell(27,3,28,11);
        makeExceptionCell(27,7,24,8);
        // Bottom right T
        makeCell(27,16,28,25);
        makeExceptionCell(27,19,24,20);

        
        // MIDDLE OBJECTS
        
        // Bottom middle T
        makeCell(24,10,25,17);
        makeExceptionCellDown(25,13,28,14);
        // Top of bottom middle T - cool T
        makeCell(18,10,19,17);
        makeExceptionCellDown(19,13,22,14);
        // Top T
        makeCell(6,10,7,17);
        makeExceptionCellDown(7,13,10,14);

        // RIGHT HALF OBJECTS
        
        // Bottom flat line
        makeCell(21,16,22,20);
        // Bottom tall line
        makeCell(15,19,19,20);
        // Top left T
        makeCell(6,19,13,20);
        makeExceptionCellLeft(9,19,10,16);
        // top right box
        makeCell(2,22,4,25);
        // middle right box
        makeCell(2,16,4,20);
        //far right line
        makeCell(6,22,7,25);
        
        createEmptyProtrusion(gr,270,9,16,10,19);
        createEmptyProtrusion(gr,90,9,8,10,11);
        makeGhostHouse(gr);
    }
    
    // Pre: none
    // Post: constructs the ghost house 
    public void makeGhostHouse(Grid<Actor> gr)
    {
        makeCell(12,10,16,17);
        BackTile cover1 = new BackTile();
        cover1.putSelfInGrid(gr,new Location(12,13));
        BackTile cover2 = new BackTile();
        cover2.putSelfInGrid(gr,new Location(12,14));
    }
    
    // Pre: points have to be located diagonally from each other
    // Post: makes a standard cell wall
    public void makeCell(int x1, int y1, int x2, int y2)
    {
        int xExt = (y2-y1)-1;
        int yExt = (x2-x1)-1;
        
        Grid<Actor> gr = getGrid();
        CornerWall corner1 = new CornerWall(90);
        corner1.putSelfInGrid(gr,new Location(x1,y1));
        corner1.extend(xExt, 90);
        
        CornerWall corner2 = new CornerWall(180);
        corner2.putSelfInGrid(gr,new Location(x1,y2));
        corner2.extend(yExt, 180);
        
        CornerWall corner3 = new CornerWall(270);
        corner3.putSelfInGrid(gr,new Location(x2,y2));
        corner3.extend(xExt, 270);
        
        CornerWall corner4 = new CornerWall(0);
        corner4.putSelfInGrid(gr,new Location(x2,y1));
        corner4.extend(yExt, 0);
    }
    
    // Pre: put connecting point first, diagonal outer point last
    //      only works for right and up extrusions
    // Post: constructs an exception cell up or to the right
    public void makeExceptionCell(int x1, int y1, int x2, int y2)
    {
        int xExt = (y2-y1)-1;
        int yExt = (x2-x1)-1;
        
        // Right facing
        if(x1-x2<=1)
        {
            Grid<Actor> gr = getGrid();
            CornerWall corner1 = new CornerWall(0);
            corner1.putSelfInGrid(gr,new Location(x1,y1));
            corner1.extend(xExt, 90);
            
            CornerWall corner2 = new CornerWall(180);
            corner2.putSelfInGrid(gr,new Location(x1,y2));
            corner2.extend(yExt, 180);
            
            CornerWall corner3 = new CornerWall(270);
            corner3.putSelfInGrid(gr,new Location(x2,y2));
            corner3.extend(xExt, 270);
            
            CornerWall corner4 = new CornerWall(90);
            corner4.putSelfInGrid(gr,new Location(x2,y1));
            corner4.extend(yExt, 0);
        }
        
        // Top facing
        if(x1-x2>1)
        {
            Grid<Actor> gr = getGrid();
            CornerWall corner1 = new CornerWall(270);
            corner1.putSelfInGrid(gr,new Location(x1,y1));
            corner1.extend((x1-x2), 0);
            
            CornerWall corner2 = new CornerWall(0);
            corner2.putSelfInGrid(gr,new Location(x1,y2));
            corner2.extend(x1-x2, 0);
            
            CornerWall corner3 = new CornerWall(180);
            corner3.putSelfInGrid(gr,new Location(x2,y2));
            //corner3.extend(xExt, 270);
            
            CornerWall corner4 = new CornerWall(90);
            corner4.putSelfInGrid(gr,new Location(x2,y1));
            //corner4.extend(yExt, 0);
        }
    }
    // Pre: put connecting point first, diagonal outer point last
    //      only works for down extrusions
    // Post: constructs an exception cell down
     public void makeExceptionCellDown(int x1, int y1, int x2, int y2)
    {
        Grid<Actor> gr = getGrid();
            CornerWall corner1 = new CornerWall(180);
            corner1.putSelfInGrid(gr,new Location(x1,y1));
            corner1.extend(x2-x1, 180);
            
            CornerWall corner2 = new CornerWall(90);
            corner2.putSelfInGrid(gr,new Location(x1,y2));
            corner2.extend(x2-x1, 180);
            
            CornerWall corner3 = new CornerWall(270);
            corner3.putSelfInGrid(gr,new Location(x2,y2));
       
            
            CornerWall corner4 = new CornerWall(0);
            corner4.putSelfInGrid(gr,new Location(x2,y1));
    }

    // Pre: put connecting point first, diagonal outer point last
    //      only works for Left extrusions
    // Post: constructs an exception cell left
    public void makeExceptionCellLeft(int x1, int y1, int x2, int y2)
    {
        Grid<Actor> gr = getGrid();
            CornerWall corner1 = new CornerWall(270);
            corner1.putSelfInGrid(gr,new Location(x1,y1));
            corner1.extend(y1-y2, 270);
            
            CornerWall corner2 = new CornerWall(90);
            corner2.putSelfInGrid(gr,new Location(x1,y2));

            
            CornerWall corner3 = new CornerWall(0);
            corner3.putSelfInGrid(gr,new Location(x2,y2));
       
            
            CornerWall corner4 = new CornerWall(180);
            corner4.putSelfInGrid(gr,new Location(x2,y1));
            corner4.extend(y1-y2-1, 270);
    }

    // Pre: grid must be large enough
    // Post: constructs a wall around grid
    public void makeOuterWall(Grid<Actor> gr)
    {
        makeCell(0,0,30,27);
        createEmptyProtrusion(gr,90,9,0,13,5);
        createEmptyProtrusion(gr,90,15,0,19,5);
        createEmptyProtrusion(gr,270,9,22,13,27);
        createEmptyProtrusion(gr,270,15,22,19,27);
    }
    // Pre: grid must be large enough 
    // Post: creates an empty wall protrusion 
    public void createEmptyProtrusion(Grid<Actor> gr, int direction,int x1, int y1, int x2, int y2)
    {
        makeCell(x1,y1,x2,y2);
        if(direction == 90)
        {
            CornerWall corner1 = new CornerWall(0);
            corner1.putSelfInGrid(gr,new Location(x1,y1));
            CornerWall corner2 = new CornerWall(90);
            corner2.putSelfInGrid(gr,new Location(x2,y1));
            for(int i = x1+1; i <= x2-1; i++)
            {
                BackTile cover = new BackTile();
                cover.putSelfInGrid(gr,new Location(i,y1));
            }
        }
        else if(direction == 270)
        {
            CornerWall corner1 = new CornerWall(270);
            corner1.putSelfInGrid(gr,new Location(x1,y2));
            CornerWall corner2 = new CornerWall(180);
            corner2.putSelfInGrid(gr,new Location(x2,y2));
            for(int i = x1+1; i <= x2-1; i++)
            {
                BackTile cover = new BackTile();
                cover.putSelfInGrid(gr,new Location(i,y2));
            }
        }
    }
}