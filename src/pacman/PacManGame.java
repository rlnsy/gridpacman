package pacman;

// ROWAN LINDSAY + NAT REDFERN
// APCS
// v0.5.1 -alpha

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
    private final GameBoard BOARD = new GameBoard(getGrid());
    private final int PHASE_LENGTH = 30;
    private final String START_MODE = "SCATTER";
    private int steps;
    private static final String CURRENT_VERSION = "0.5.1a";
    private static final String GAME_MESSAGE = 
            "GridPacMan v" + CURRENT_VERSION + 
            "   |   Use arrow keys to move - avoid the ghosts";
    private String mode;
    private boolean gameOver;
    
    
    // initializes the world
    public static void main(String[] args)
    {
      PacManGame game = new PacManGame();
      game.setMessage(GAME_MESSAGE);
      game.show();
    }
   
   // Pre: none
   // Post: Sets up the grid, pacman, and ghosts
    public PacManGame()
    {
       super(new BoundedGrid<Actor>(31,28));
       gameOver = false;
       
       Grid<Actor> gr = getGrid();
       
       Location[] scatterLocs = BOARD.getScatLocs();
       PAC_MAN = new PacMan(this);
       BLINKY = new Blinky(PAC_MAN,this,scatterLocs[0]);
       PINKY = new Pinky(PAC_MAN,this,scatterLocs[1]);
       INKY = new Inky(PAC_MAN,this,BLINKY,scatterLocs[2]);
       CLYDE = new Clyde(PAC_MAN,this,scatterLocs[3]);
       
       PAC_MAN.putSelfInGrid(gr, new Location (23, 14));
       BLINKY.putSelfInGrid(gr, new Location (8, 21));
       PINKY.putSelfInGrid(gr, new Location (14,13));
       INKY.putSelfInGrid(gr, new Location (14,16));
       CLYDE.putSelfInGrid(gr, new Location (14,15));
       
       mode = START_MODE;
       steps = 0;
       
       //BOARD.makeBackground(true);
       BOARD.makeBoard();
       BOARD.placeDots();
    }
    // Pre: none 
    // Post: rotates character depending upon key pressed
    public boolean keyPressed(String description, Location loc)
    {
        if(!gameOver)
        {
            if (description.equals("UP"))
                PAC_MAN.turnTo(0);
            else if (description.equals("DOWN"))
                 PAC_MAN.turnTo(180);
            else if (description.equals("LEFT"))
                 PAC_MAN.turnTo(270);
            else if (description.equals("RIGHT"))
                 PAC_MAN.turnTo(90);
        }
        return true;  
    }
    
    // Pre: none
    // Post: revises step to move, but to make background black every move step
    public void step()
    {
        Grid<Actor> gr = getGrid();
        if(PAC_MAN.getGrid() == gr && !gameOver)
        {
            ArrayList<Actor> actors = new ArrayList<Actor>();
            for (Location loc : gr.getOccupiedLocations())
                actors.add(gr.get(loc));
            for (Actor a : actors)
            {
                // only act if another actor hasn't removed a
                if (a.getGrid() == gr && PAC_MAN.getGrid() == gr)
                    a.act();
            }
            BOARD.makeBackground(false);
            steps++;
            checkTimer();
            this.setMessage(stepMessage());
        }
        else if(!gameOver)
            endGame();
    }
    
    // Pre: none
    // Post:
    public void checkTimer()
    {
        if(steps >= PHASE_LENGTH)
            switchMode();
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
    
    public String stepMessage()
    {
        return GAME_MESSAGE + '\n' + "Current Mode: " + mode
                + '\t' + '\t' + "Dots eaten: " + PAC_MAN.getDotsEaten();
    }
    
    public void setMode(String mode)
    {
        this.mode = mode;
    }
    
    public void endGame()
    {
        gameOver = true;
        System.out.println(" ** GAME OVER **");
        this.setMessage(" ** GAME OVER **");
    }
    
    public GameBoard getBoard()
    {
        return BOARD;
    }
}