import java.awt.*;
import javax.swing.*;
/**
 *
 * <p>Title: Movie </p>
 * <p>Description: Holds an array of images and displays them as a movie</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * @author Cathy French
 * @version 1.0
 */
public class Movie extends ICGGSPanel
{
    private Image[] theImages;  // images making up the movie 
    private int imageCounter;   // current image to be shown  
    /**
     * Constructor - initialises the array of images, the image counter and sets the background colour
     * @param images Image[]  - array of images to be shown as movie
     */
    public Movie(Image[] images)
    {
        theImages = images;
        imageCounter = 0;
        this.setBackground(Color.BLACK);
    }

    /**
     * overrides superclass method to process input
     * @param input  the current user 
     */
    public void processInput(int input)
    {
        // ignore any input (it's a movie!)
    }


    /**
     * overrides superclass method to advance to the
     * next movie image
     */
    public void update()
    {
        imageCounter++;
        if (imageCounter >= theImages.length)
            imageCounter = 0;
    }

    /**
     * displays the current image
     * @param g Graphics - the graphics context
     */
    public void paintComponent(Graphics g)
    {
        // draw the background of the panel
        super.paintComponent(g);
        // draw the current movie image so it fills the panel
        g.drawImage(theImages[imageCounter], 0, 0, this.getWidth(), this.getHeight(), Color.BLACK, null);
    }




}
