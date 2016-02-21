package pacman;

// ROWAN LINDSAY + NAT REDFERN
// APCS
// v0.5.2 -alpha

import info.gridworld.grid.*;
import info.gridworld.actor.*;
import info.gridworld.world.*;
import java.util.ArrayList;
import java.io.*;
import javax.sound.sampled.*;

public class PacManGame extends World<Actor>
{
    private final PacMan PAC_MAN;
    private final Blinky BLINKY;
    private final Pinky PINKY;
    private final Inky INKY;
    private final Clyde CLYDE;
    private final GameBoard BOARD = new GameBoard(getGrid());
    private int steps;
    private String mode;
    private boolean gameOver;
    
    // initializes the world
    public static void main(String[] args)
    {
      PacManGame game = new PacManGame();
      game.setMessage(PacManMap.GAME_MESSAGE);
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
       
       mode = PacManMap.START_MODE;
       steps = 0;
       
       //BOARD.makeBackground(true);
       BOARD.makeBoard();
       BOARD.placeDots();
       playAudio(PacManMap.INTRO_MUSIC);
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
        if(steps >= PacManMap.PHASE_LENGTH)
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
    
    public void playAudio(String dir)
    {
        try {
            File yourFile = new File(dir);
            AudioInputStream stream;
            AudioFormat format;
            DataLine.Info info;
            Clip clip;

            stream = AudioSystem.getAudioInputStream(yourFile);
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
            clip.start();
    }
        catch (Exception e) {
            System.out.println(e);
        }
    }
    
    // Pre: none 
    // Post: returns mode
    public String getMode()
    {
        return mode;
    }
    
    public String stepMessage()
    {
        return PacManMap.GAME_MESSAGE + '\n' + "Current Mode: " + mode
                + '\t' + '\t' + "Dots eaten: " + PAC_MAN.getDotsEaten();
    }
    
    public void setMode(String mode)
    {
        this.mode = mode;
    }
    
    public void endGame()
    {
        playAudio(PacManMap.PACMAN_DEATH);
        gameOver = true;
        System.out.println(" ** GAME OVER **");
        this.setMessage(" ** GAME OVER **");
    }
    
    public GameBoard getBoard()
    {
        return BOARD;
    }
}