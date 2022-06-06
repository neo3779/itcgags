import javax.swing.*;
import java.applet.AudioClip;
import java.awt.*;

/**
 *
 * <p>Title: The Sneezing Monster Game</p>
 * <p>An interactive animation with Munchies and user-controlled Monster</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * @author Alex
 * @version 1.0   September 2008
 */
public class MonsterGame extends ICGGSPanel
{
    // game constants
    private final int NUM_MUNCHIES = 25;   // the number of munchies to be displayed

    // game information
    private int score;  // player score 
    private int boardWidth, boardHeight;  // game board dimensions

    // Munchie data
    private int[] munchieXPos;  // x coordinate of top left corner of each munchie image
    private int[] munchieYPos;   // y coordinate of top left corner of each munchie image
    private int[] munchieAnimationCount;  // current animation counter for each munchie
    private Image munchieImage1, munchieImage2;  // the images to display
    private int munchieWidth, munchieHeight;  // munchie dimensions
    private int munchieCount = 0;
    
    // Monsters max
    private  int monsterMaxSize = 30;
    private  int monsterMinSize = 10;
    
    // Monster data
    private int monsterXPos, monsterYPos;  // coordinates of top left corner of monster
    private int monsterSize;  // size of monster
    private int monsterSpeed;  // monster speed in pixels per frame
    private Color monsterColour;  
    private int monsterDirection;  // direction monster is moving
    private int monsterAnimationCount;  // animation counter for monster
    private AudioClip monsterBumpWallSound;  // sound to play when monster hits a wall
    private AudioClip eatRed;
    private AudioClip eatBlue;
    private AudioClip die;
    
    // Monster2 data
    private int monsterXPos2, monsterYPos2;  // coordinates of top left corner of monster
    private int monsterSize2;  // size of monster
    private int monsterSpeed2;  // monster speed in pixels per frame
    private Color monsterColour2;  

    private long time = (System.currentTimeMillis()/1000)+60;
    private boolean timer = false; 
    private long countDown;
    
    /**
     * Constructor - creates a game using the two images and audio clip
     * @param image1 Image - the first image for the munchies
     * @param image2 Image - the second image for the munchies
     * @param theAudio AudioClip - sound to be played when the monster hits a wall
     */
    public MonsterGame(Image image1, Image image2, AudioClip theAudio,AudioClip theEatBad, AudioClip theEat, AudioClip theDie, String name)

    {
        // initialise Munchie information
        // create the arrays, need one "slot" for each munchie
        munchieXPos = new int[NUM_MUNCHIES];
        munchieYPos = new int[NUM_MUNCHIES];
        munchieAnimationCount = new int[NUM_MUNCHIES];
        // store the munchie images
        munchieImage1 = image1;
        munchieImage2 = image2;
        // the munchie is the same size as the loaded image
        munchieWidth = image1.getWidth(null);
        munchieHeight = image1.getHeight(null);
        

        // initialise the Monster information
        monsterColour = Color.blue;
        monsterBumpWallSound = theAudio;
        eatRed = theEatBad;
        eatBlue = theEat;
        die = theDie;
        
        monsterSize = 10;
        
        // initialise the Monster information
        monsterColour2 = Color.orange;
        monsterSize2 = 30;
        
        // initialise background colour
        this.setBackground(Color.BLACK);
    }

    /**
     * Use to reset the game
     * sets score to zero
     * sets up Monster and munchies in random positions
     */

    public void resetGame()
    {
        if (timer = true)
        {
            time = (System.currentTimeMillis()/1000)+60;
            score = 0;
            monsterSize = 10;
            monsterSize2 = 30;
        }
        
        // make the game board the same size as the panel
        boardWidth = this.getWidth();
        boardHeight = this.getHeight() - 20;
        munchieCount = 0;
        
        // reset the Monster to a set postion
        monsterXPos = 1;
        monsterYPos = 1;
        
        // reset the Monster to a set postion
        monsterXPos2 = boardWidth - monsterSize2 - 1;
        monsterYPos2 = boardHeight - monsterSize2 - 1;
        
        monsterSpeed = 10;
        monsterSpeed2 = 7;    
        monsterAnimationCount = 0;

        boolean clear = false;
        int validxpos=0;
        int validypos=0;
        // reset all the munchies to random positions and different animation counts
     
        munchieXPos[0] = (int)(Math.random() * (boardWidth - munchieWidth));
        munchieYPos[0] = (int)(Math.random() * (boardHeight - munchieHeight));
        for (int i = 1; i < NUM_MUNCHIES; i++) 
        {
            clear = false;
            while(!clear)
            {
                clear = true;
                validxpos = (int)(Math.random() * (boardWidth - munchieWidth));
                validypos = (int)(Math.random() * (boardHeight - munchieHeight));
                for (int j = 0 ; j < i; j++)
                {
                    if ((munchieXPos[j]==validxpos) && (munchieYPos[j]==validypos)||(munchieXPos[j]< validxpos) 
                    && (munchieXPos[j] + munchieWidth > validxpos  ) || (munchieXPos[j]< validxpos + munchieWidth) 
                    && (munchieXPos[j] > validxpos) || (munchieYPos[j] < validypos) && (munchieYPos[j]  + munchieHeight > validypos )|| (munchieYPos[j]< validypos + munchieWidth) 
                    && (munchieYPos[j] > validypos))
                    {
                       clear = false; 
                    }
                }           
            }
            munchieAnimationCount[i] = i % 10;
            munchieXPos[i] = validxpos;
            munchieYPos[i] = validypos;

        }
    }

    /**
     * overrides superclass method
     * processes user input
     * @param input int - code for the lastest user input
     */
    public void processInput(int input)
    {
         if (input > 0 && input < 5)  // if the input is a valid direction
            monsterDirection = input;  // set the current direction to the input
    }

    /**
     * overrides superclass method
     * updates the game objects
     */
    public void update()
    {
        
        
        // update moster direction and animation
         switch (monsterDirection)
        {
          case Input.RIGHT:
              // monster is moving right, increase its X-coordinate
              monsterXPos = monsterXPos + monsterSpeed;
              if (monsterXPos > (boardWidth - monsterSize)) // has the monster hit the right wall?
              {
                  monsterXPos = boardWidth - monsterSize;  // move it back to just touching right wall
                  monsterBumpWallSound.play();  // play an appropriate sound
              }
                  
              if (monsterXPos > monsterXPos2)
              {
                  monsterXPos2 = monsterXPos2 + monsterSpeed2;
              }
              else
              {
                  monsterXPos2 = monsterXPos2 - monsterSpeed2;
              }
             
              if (monsterXPos2 > (boardWidth - monsterSize2)) // has the monster hit the right wall?
              {
                  monsterXPos2 = boardWidth - monsterSize2;  // move it back to just touching right wall
              }
                  
              break;
          case Input.LEFT:
              // monster is smoving left, increase its X-coordinate
              monsterXPos = monsterXPos - monsterSpeed;
              if (monsterXPos < 0) // has the monster hit the left wall?
              {
                  monsterXPos = 0;  // move it back to just touching left wall
                  monsterBumpWallSound.play();  // play an appropriate sound
              }
             
              if (monsterXPos > monsterXPos2)
              {
                  monsterXPos2 = monsterXPos2 + monsterSpeed2;
              }
              else
              {
                  monsterXPos2 = monsterXPos2 - monsterSpeed2;
              }
              
              if (monsterXPos2 < 0) // has the monster hit the left wall?
              {
                  monsterXPos2 = 0;  // move it back to just touching left wall
              }
              break;
              
         case Input.UP:
              // monster is moving up, increase its X-coordinate
              monsterYPos = monsterYPos - monsterSpeed;
              if (monsterYPos < 0) // has the monster hit the left wall?
              {
                  monsterYPos = 0;  // move it back to just touching left wall
                  monsterBumpWallSound.play();  // play an appropriate sound
              }
              
              if (monsterYPos > monsterYPos2)
              {
                  monsterYPos2 = monsterYPos2 + monsterSpeed2;
              }
              else
              {
                  monsterYPos2 = monsterYPos2 - monsterSpeed2;
              } 
              
              if (monsterYPos2 < 0) // has the monster hit the left wall?
              {
                  monsterYPos2 = 0;  // move it back to just touching left wall
              }
              
             break;
              
         case Input.DOWN:
              // monster is moving down, increase its X-coordinate
              monsterYPos = monsterYPos + monsterSpeed;
              if (monsterYPos > (boardHeight - monsterSize)) // has the monster hit the left wall?
              {
                  monsterYPos = boardHeight - monsterSize;  // move it back to just touching left wall
                  monsterBumpWallSound.play();  // play an appropriate sound
              }
              
              if (monsterYPos > monsterYPos2)
              {
                  monsterYPos2 = monsterYPos2 + monsterSpeed2;
              }
              else
              {
                  monsterYPos2 = monsterYPos2 - monsterSpeed2;
              }
              
              if (monsterYPos2 > (boardHeight - monsterSize2)) // has the monster hit the left wall?
              {
                  monsterYPos2 = boardHeight - monsterSize2;  // move it back to just touching left wall
              }
              
              break;
              
        }
         // increment the monster animation counter 
       monsterAnimationCount++;
       if (monsterAnimationCount >= 10)  // when the counter gets to 10, set it back to 0
           monsterAnimationCount = 0;
       
       // update each munchie animation
        for (int i = 0; i < NUM_MUNCHIES; i++)
        {
           // increment the animation counter 
            munchieAnimationCount[i]++;
            if (munchieAnimationCount[i] >= 10) // when the counter gets to 10, set it back to 0
                munchieAnimationCount[i] = 0;
        }
       
       for (int i = 0; i < NUM_MUNCHIES;i++)
       {
           if(((monsterXPos +((int)monsterSize/2)> munchieXPos[i] ) && (monsterYPos +((int)monsterSize/2)> munchieYPos[i])) 
           && ((monsterXPos  +((int)monsterSize/2)< (munchieXPos[i] + munchieWidth) ) && (monsterYPos+((int)monsterSize/2) < munchieYPos[i] + munchieHeight )) 
           && ((monsterYPos  +((int)monsterSize/2)< munchieYPos[i] + munchieHeight )  && (monsterYPos +((int)monsterSize/2) < munchieYPos[i] + munchieHeight )))
           {
              munchieXPos[i] = boardWidth;
              munchieYPos[i] = boardHeight;
              
              if (munchieAnimationCount[i] < 5)
              {
                score = score + 50;
                time = time +5;
                eatBlue.play(); 
                if (monsterSize > monsterMaxSize)
                {
                    monsterSize = monsterMaxSize;
                }
                else
                {
                    monsterSize = monsterSize + 1;
                }
                
                if (monsterSize2 < monsterMinSize)
                {
                    monsterSize2 = monsterMinSize;
                }
                else
                {
                    monsterSize2 = monsterSize2 - 1;
                }
              }
              else
              {
                score = score - 10;
                time = time -1;
                eatRed.play();
                
                if (monsterSize < monsterMinSize)
                {
                    monsterSize = monsterMinSize;
                }
                else
                {
                    monsterSize = monsterSize - 1;
                }
                
                  if (monsterSize2 > monsterMaxSize)
                {
                    monsterSize2 = monsterMaxSize;
                }
                else
                {
                    monsterSize2 = monsterSize2 + 1;
                }
                
              }
              munchieCount++;  
           }
       }
       //monster hit monster (not working)
       if(((monsterXPos + monsterSize > monsterXPos2 ) && (monsterYPos + monsterSize> monsterYPos2)) 
           && ((monsterXPos + monsterSize < monsterXPos2 + monsterSize2 ) && (monsterYPos + monsterSize < monsterYPos2 + monsterSize2)) 
           && ((monsterYPos  + monsterSize< monsterYPos2 + monsterSize2 )  && (monsterYPos + monsterSize < monsterYPos2 + monsterSize2 )))
       {
           timer=true;
           resetGame();
       }
       
       if (munchieCount==NUM_MUNCHIES)
       {
           resetGame();
       }
       
       if (time <  System.currentTimeMillis()/1000)
       {
         resetGame();
         timer = true;
       } 

      countDown = time - System.currentTimeMillis()/1000;
       
    }

    /**
     * called by system after repaint() call (or Applet moved or resized)
     * @param g Graphics  object for painting on
     */
    public void paintComponent(Graphics g)
    {
      
        super.paintComponent(g);  // paint the default panel

        // paint each of the Munchies in turn

        for (int i = 0; i < NUM_MUNCHIES; i++) {
            Image im;  // this is the image to be displayed
             if (munchieAnimationCount[i] < 5)
            {
                // display the first image when the counter is between 0 and 4
                im = munchieImage1;  
            }
            else
            {
                // display the second image when the counter is between 5 and 9
                im = munchieImage2;   
            }
            // draw the image in the correct position
            g.drawImage(im, munchieXPos[i], munchieYPos[i], null);
        }
        
        // now get ready to paint the monster
        // set the drawing colour to that of the monster
        g.setColor(monsterColour);
                
        int arcAngle;  // how wide is his body? (360 if his mouth is completely closed)
        int startAngle = 0, startAngle2 = 0;  // angle to start drawing the arc at
        // where his mouth is depends on which direction he is moving

        switch (monsterDirection)
        {
            
            case Input.RIGHT: case Input.NONE:
                // this is the angle of the roof of his mouth
                // when his mouth is fully open
                startAngle = 25;
                    if (monsterXPos > monsterXPos2)
                    {
                        startAngle2 = 25;
                    }
                    else
                    {
                        startAngle2 = 205;
                    }
                break;
            case Input.LEFT:
                // this is the angle of the roof of his mouth
                // when his mouth is fully open
                startAngle = 205;
                    if (monsterXPos > monsterXPos2)
                    {
                        startAngle2 = 25;
                    }
                    else
                    {
                        startAngle2 = 205;
                    }
                break;
            case Input.UP:
                // this is the angle of the roof of his mouth
                // when his mouth is fully open
                startAngle = 115;
                    if (monsterYPos > monsterYPos2)
                    {
                        startAngle2 = 295;
                    }
                    else
                    {
                        startAngle2 = 115;
                    }
                break;
            case Input.DOWN: 
                // this is the angle of the roof of his mouth
                // when his mouth is fully open
                startAngle = 295;
                    if (monsterYPos > monsterYPos2)
                    {
                        startAngle2 = 295;
                    }
                    else
                    {
                        startAngle2 = 115;
                    }
                break;
        }

        // calculate the angle of arc to fill
        // and where to start filling
        // this depends on how wide the monster's mouth is open
        // 
        if (monsterAnimationCount < 5)
        {
            // for frames 0 to 4, his mouth gradually closes
            arcAngle = 310 + 10 * monsterAnimationCount;
            startAngle = startAngle - 5*monsterAnimationCount;
        }
        else
        {
            // for frames 5 to 9, it opens again
            arcAngle = 310 + (10  - monsterAnimationCount) * 10;
            startAngle = startAngle - (10 - monsterAnimationCount) * 5;
        }

        // now draw the monster
        g.fillArc(monsterXPos, monsterYPos, monsterSize, monsterSize, startAngle, arcAngle);

        // now get ready to paint the monster
        // set the drawing colour to that of the monster
        g.setColor(monsterColour2);
        
        // now draw the monster
        g.fillArc(monsterXPos2, monsterYPos2, monsterSize2, monsterSize2, startAngle2, arcAngle);
        
        // draw a rectangle to frame the game board
        g.setColor(Color.WHITE);
        g.drawRect(0, 0, boardWidth-1, boardHeight);
        
        g.setColor(Color.WHITE);
        g.drawString("Name: " + ICGGSApplet.name + " Your score is: " + score + " Time remaining: " + countDown, 0, boardHeight+15);
     }
}
