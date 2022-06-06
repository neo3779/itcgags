/*----------------------------------------------------------------------------*\
 *
 * @(#)Input.java	1.0
 * Version 1.0
 * The <code>Input</code> class performs keyboard input
 * @version 	Input 1.0, 09/10/04
 * @author 	  Claude C. Chibelushi
 *
 * @since     JDK1.1
 *----------------------------------------------------------------------------*/

 // CUSTOMISE THE "TO DO" COMMENTS AS APPROPRIATE


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


/**
 * A <code>Input</code> is a module that performs keyboard input
 * <p>
 *
 * @version   1.0, 09/10/04
 * @author    Claude C. Chibelushi
 * @since     JDK1.1
 */
public class Input  extends KeyAdapter
    {
    public final static int  QUIT   = 0;	/* flag for escape key press */
    public final static int  UP     = 1;	/* flag for up cursor key press */
    public final static int  DOWN   = 2;	/* flag for down cursor key press */
    public final static int  LEFT   = 3;	/* flag for left cursor key press */
    public final static int  RIGHT  = 4;	/* flag for right cursor key press */
    public final static int  GAME   = 5;        /* flag for g key press */
    public final static int  MOVIE  = 6;        /* flag for m key press */
    public final static int  STOP   = 7;        /* flag for s key press */
    public final static int  NONE   = 8;	/* flag for other (or initially no) key press */

    public static int        eventFlag;         /* key-press flag variable */



    /**
      * Constructs and initialises an input module
      * @since       JDK1.1
      */
    public Input()
        {
        /* TO DO: add relevant code */

        reset();
        }


    /**
      * Checks key state of "arrow keys"
      * @param       event     a key-event object.
      * @since       JDK1.1
      */
    public void keyPressed(KeyEvent event)
        {
        switch(event.getKeyCode())
            {
            case KeyEvent.VK_UP:
                eventFlag = UP;
                break;
            case KeyEvent.VK_DOWN:
                eventFlag = DOWN;
                break;
            case KeyEvent.VK_LEFT:
                eventFlag = LEFT;
                break;
            case KeyEvent.VK_RIGHT:
                eventFlag = RIGHT;
                break;
            case KeyEvent.VK_G:
                eventFlag = GAME;
                break;
            case KeyEvent.VK_M:
                eventFlag = MOVIE;
                break;
            case KeyEvent.VK_S:
                eventFlag = STOP;
                break;
            case KeyEvent.VK_ESCAPE:
                eventFlag = QUIT;
                break;
            default:
                eventFlag = NONE;
                break;
            }
        }

    /**
      * Checks whether escape key has been typed
      * Note: typing means both a key press and a release.
      * @param       event     a key-event object.
      * @since       JDK1.1
      */
    public void keyTyped(KeyEvent event)
        {
        // quit if escape key hit
        if (event.getKeyChar() == 27)
            {
            eventFlag = QUIT;
            }
        }

    /**
      * Checks for most recent keyboard event.
      * @since       JDK1.1
      */
    public static int checkUserInput()
        {
        return eventFlag;
        }

    /**
      * Resets keyboard event flag.
      * @since       JDK1.1
      */
    public static void reset()
        {
        eventFlag = NONE;
        }

    }
