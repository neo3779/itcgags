import javax.swing.*;
import java.awt.*;

/**
 * <p>Title: ICGGSPanel</p>
 * <p>Description: Abstract superclass for MonsterGame and Movie panels</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author Cathy French
 * @version 1.0
 */
public abstract class ICGGSPanel extends JPanel
{
    /**
     * processes user input
     * @param input int - user input code
     */
    public abstract void processInput(int input);
    /**
     * updates the animation
     */
    public abstract void update();
}
