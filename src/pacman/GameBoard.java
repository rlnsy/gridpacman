package pacman;

import info.gridworld.actor.Actor;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import java.util.ArrayList;
import java.util.Random;

public class GameBoard {
    
    private final Grid<Actor> gr;
    private final int NUMBER_OF_CHERRIES = 10;
    private final Location[] CHERRY_LOCS = {new Location(23,20),new Location(21,5)};
    private final ArrayList<Location> DOT_LOCS = new ArrayList<Location>();
    private final ArrayList<Location> NO_DOT_ZONE;
    private final ArrayList<Location> CHERRY_LOCATIONS;
    private final Location[] GHOST_SCATTER_LOCATIONS = {new Location(0,26),
        new Location(0,0), new Location(30,26), new Location(30,0)};
    
    public GameBoard(Grid<Actor> gr) {
        this.gr = gr;
        NO_DOT_ZONE = getNoDotLocations();
        makeBackground(true);
        CHERRY_LOCATIONS = getCherryLocations();
    }
    
    public void makeBackground(boolean firstRun) {
        placeDots();
        
        int rows = gr.getNumRows();
        int cols = gr.getNumCols();
        // define the dimensions of the grid
        ArrayList<Location> tileLocations = new ArrayList<Location>();
        for(int r = 0; r < rows; r++) {
            for(int c = 0; c < cols; c++) {
                Location checkLoc = new Location(r,c);
                if(!(gr.getOccupiedLocations().contains(checkLoc))) {
                    tileLocations.add(checkLoc);
                    if(firstRun)
                        DOT_LOCS.add(checkLoc);
                }
            }
        } // get all empty locations on the grid
        
        for(Location loc : tileLocations) {
            BackTile tile = new BackTile();
            tile.putSelfInGrid(gr,loc);
        }
    }
    
    // Pre: none
    // Post: places dots on the grid
    public void placeDots() {
        for(Location loc : DOT_LOCS) {
            Object o = gr.get(loc);
            if((o instanceof BackTile || o == null) && !(NO_DOT_ZONE.contains(loc))) {
                if(CHERRY_LOCATIONS.contains(loc)) {
                    Cherry cher = new Cherry();
                    cher.putSelfInGrid(gr, loc);
                }
                else {
                    Dot dot = new Dot();
                    dot.putSelfInGrid(gr, loc);
                }
            }
        }
    }
    
    public ArrayList<Location> getNoDotLocations() {
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
    
    public ArrayList<Location> getCherryLocations() {
        ArrayList<Location> cherryLocs = new ArrayList<Location>();
        Random random = new Random();
        for(int i = 1; i <= (int)(NUMBER_OF_CHERRIES * 2.5); i++) {
            Location randLoc = DOT_LOCS.get(random.nextInt(DOT_LOCS.size()));
            cherryLocs.add(randLoc);
        }
        return cherryLocs;
    }
    
    // Pre: none 
    // Post: scans for locations and returns array list
    public ArrayList<Location> scanLocations(Location loc1,Location loc2) {
        ArrayList<Location> scan = new ArrayList<Location>();
        for(int r = loc1.getRow(); r <= loc2.getRow(); r++) {
            for(int c = loc1.getCol(); c <= loc2.getCol(); c++)
                scan.add(new Location (r,c));
        }
        return scan;
    }
    
    // Pre: none 
    // Post: returns valid dot locations
    public ArrayList<Location> getDotLocs() {
        return DOT_LOCS;
    }
    
    public Location[] getScatLocs() {
        return GHOST_SCATTER_LOCATIONS;
    }
    
    // The following are methods for constructing the initial game board shapes
    
    // Pre: none 
    // Post: constructs the board
    public void makeBoard() {   // make border
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
    public void makeGhostHouse(Grid<Actor> gr) {
        makeCell(12,10,16,17);
        BackTile cover1 = new BackTile();
        cover1.putSelfInGrid(gr,new Location(12,13));
        BackTile cover2 = new BackTile();
        cover2.putSelfInGrid(gr,new Location(12,14));
    }
    
    // Pre: points have to be located diagonally from each other
    // Post: makes a standard cell wall
    public void makeCell(int x1, int y1, int x2, int y2) {
        int xExt = (y2-y1)-1;
        int yExt = (x2-x1)-1;
        
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
    public void makeExceptionCell(int x1, int y1, int x2, int y2) {
        int xExt = (y2-y1)-1;
        int yExt = (x2-x1)-1;
        
        // Right facing
        if(x1-x2<=1) {
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
        if(x1-x2>1) {
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
     public void makeExceptionCellDown(int x1, int y1, int x2, int y2) {
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
    public void makeExceptionCellLeft(int x1, int y1, int x2, int y2)  {
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
    public void makeOuterWall(Grid<Actor> gr) {
        makeCell(0,0,30,27);
        createEmptyProtrusion(gr,90,9,0,13,5);
        createEmptyProtrusion(gr,90,15,0,19,5);
        createEmptyProtrusion(gr,270,9,22,13,27);
        createEmptyProtrusion(gr,270,15,22,19,27);
    }
    // Pre: grid must be large enough 
    // Post: creates an empty wall protrusion 
    public void createEmptyProtrusion(Grid<Actor> gr, int direction,int x1, int y1, int x2, int y2) {
        makeCell(x1,y1,x2,y2);
        if(direction == 90) {
            CornerWall corner1 = new CornerWall(0);
            corner1.putSelfInGrid(gr,new Location(x1,y1));
            CornerWall corner2 = new CornerWall(90);
            corner2.putSelfInGrid(gr,new Location(x2,y1));
            for(int i = x1+1; i <= x2-1; i++) {
                BackTile cover = new BackTile();
                cover.putSelfInGrid(gr,new Location(i,y1));
            }
        }
        else if(direction == 270) {
            CornerWall corner1 = new CornerWall(270);
            corner1.putSelfInGrid(gr,new Location(x1,y2));
            CornerWall corner2 = new CornerWall(180);
            corner2.putSelfInGrid(gr,new Location(x2,y2));
            for(int i = x1+1; i <= x2-1; i++) {
                BackTile cover = new BackTile();
                cover.putSelfInGrid(gr,new Location(i,y2));
            }
        }
    }
}
