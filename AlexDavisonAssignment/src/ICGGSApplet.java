import javax.swing.*;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * <p>Title: ICGGSApplet</p>
 * <p>Description: An applet that can run a game or show a movie</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * @author Cathy French
 * @version 1.1   2006
 */
public class ICGGSApplet extends JApplet implements Runnable
{
    // constants describing possible game states
    private static final int SHOW_MOVIE = 1;
    private static final int PLAY_GAME = 2;
    private static final int STOPPED = 3;
    
    private int gameState;   // the current game state
    public static String name;     // the player's name
    private long frameStartTime;  // the time the last frame was displayed
    private Thread gameLoop;   // thread to run the game and movie 

    // associated objects
    private Movie theMovie;      // Panel to display the movie
    private MonsterGame theGame;  // Panel to display the game
    private ICGGSPanel currentPanel;  // the currently displayed Panel (can be null)

    /**
     * Called when applet is first loaded
     *  sets up the attributes, load media, creates game objects
     *  and sets up interface
     */
    public void init()
    {
        // neither the movie nor the game is currently playing
        currentPanel = null;
        gameState = STOPPED;
        name = "";

        // load up the audio file
       AudioClip theAudio = getAudioClip(getDocumentBase(), "../media/doheth.wav");
       AudioClip theEatBad = getAudioClip(getDocumentBase(), "../media/doh.wav");
       AudioClip theEat = getAudioClip(getDocumentBase(), "../media/yes.wav");
       AudioClip theDie = getAudioClip(getDocumentBase(), "../media/agh.wav");
       
        // create a media tracker to monitor the loading of the images
        MediaTracker theTracker = new MediaTracker(this);

        // load up the images for the movie
        Image movieImages[] = new Image[90];

        for (int i=0; i<movieImages.length; i++)
        {
            movieImages[i] = getImage(getDocumentBase(), "../media/movie" + (i+1) + ".jpg");
            theTracker.addImage(movieImages[i], i);
        }

        // load up the images for the munchie
        Image image1 = getImage(getDocumentBase(), "../media/munchie1.gif");
        theTracker.addImage(image1, 4);
        Image image2 = getImage(getDocumentBase(), "../media/munchie2.gif");
        theTracker.addImage(image2, 5);


        // wait for all the images to be loaded
        try
        {
            theTracker.waitForAll();
        }
        catch (InterruptedException e)
        {
        }

        // create the movie object
        theMovie = new Movie(movieImages);

        // create the game object
       theGame = new MonsterGame(image1, image2, theAudio, theEatBad, theEat, theDie, name);
       

        this.getContentPane().setBackground(Color.BLACK);
        
        // Initialise key-input functionality
        addKeyListener(new Input());    // add keyboard listener to applet
    }

    /**
     * Called when the Applet is ready to start executing
     */
    public void start()
    {
        // ask the user for their name
        name = JOptionPane.showInputDialog(this, "What is your name?");
        // start running the movie/game animation
        if (gameLoop == null) {
            gameLoop = new Thread(this);
            gameLoop.start();
        }
    }

    /**
     * Called when the gameLoop thread is started
     * loops continuously until user quits
     */

    public void run()
    {
        int input = Input.NONE;
        // game loop
        while (input != Input.QUIT)
        {
            // what is the current time?
            frameStartTime = System.currentTimeMillis();
                       
            input = checkInput();
            
            if (gameState != STOPPED)   // only necessary if the movie or game is showing
            {
                currentPanel.processInput(input);
                currentPanel.update();
                currentPanel.repaint();
            }
            else
                showStatus("Press 'G' to start game, 'M' to show movie");
            frameDelay(100);
       }
    }

    /**
     * called when the Applet is stopped
     * stops the gameLoop thread
     */

    public void stop()
    {
        if (gameLoop != null)
        {
            gameLoop.interrupt();   // stops the animation thread
            gameLoop = null;
        }
    }

    /**
     * called from the gameLoop
     * checks for the last key stroke input by the user
     * @return int - Input class constant representing user input
     */
    public int checkInput()
    {
        int input = Input.checkUserInput();
        switch (input)  // how the input is handled depends on the current game state
        {
            case Input.GAME:
                // the user wants to play the game
                if (gameState == STOPPED) // not currently playing a game or movie
                {   
                   // display the panel showing the game
                   currentPanel = theGame;  
                   this.getContentPane().add(BorderLayout.CENTER, currentPanel);
                   this.validate();
                   // set up a new game
                   theGame.resetGame();
                   gameState = PLAY_GAME;
                   showStatus("Use arrow keys to move Monster, S key to stop");
                } // otherwise ignore 
                break;
            case Input.MOVIE:
                // the user wants to see the movie
                if (gameState == STOPPED) // not currently playing a game or movie
                {
                   // display the panel showing the movie
                   currentPanel = theMovie;
                   this.getContentPane().add(BorderLayout.CENTER, currentPanel);
                   this.validate();
                   gameState = SHOW_MOVIE;
                   showStatus("Press S key to stop");
               } // otherwise ignore
               break;
            case Input.STOP:
                // the user wants to stop the movie or game being shown
                if (gameState == SHOW_MOVIE )
                {
                    this.getContentPane().remove(currentPanel);
                    currentPanel = null;
                    JOptionPane.showMessageDialog(this,
                                                "OK " + name + " I've stopped the movie");
                }
                else if (gameState == PLAY_GAME)
                {
                    this.getContentPane().remove(currentPanel);
                    currentPanel = null;
                    JOptionPane.showMessageDialog(this,
                                                "OK " + name + " I've stopped the game");
                }
                gameState = STOPPED;
                showStatus("Press 'G' to start game, 'M' to show movie");
                break;
        }

        Input.reset();  // reset the input module
        requestFocus();  
        this.repaint();  // redisplay the applet
        return input;  // return for further processing (eg to control movement of Monster)
    }



    /**
     * called from the gameLoop
     * pauses between frames
     * @param millis long - minimum frame time
     */

    public void frameDelay(long millis)
    {
        long currentTime = System.currentTimeMillis();  // what time is it now?
        long elapsedTime = frameStartTime - currentTime;  // how long has it been since the start of the frame?
        if (elapsedTime < millis)  // is it time to showing the new frame yet?
        try
        {
            Thread.sleep(millis - elapsedTime);  // pause for the remaining time
        }
        catch(Exception e)
        {
        }
    }

    /**
     * Put any clean-up code here
     */

    public void finaliseGame()
    {
    }
}
