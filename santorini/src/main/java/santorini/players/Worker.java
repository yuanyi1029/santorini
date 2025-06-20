package santorini.players;

import javax.swing.*;

/**
 * Represents a worker unit in the game.
 * Each worker has an associated icon used for rendering on the game board.
 *
 * Created by:
 * @author Zi Yang
 */
public class Worker {

    // Attributes

    /**
     * The icon used to visually represent the worker.
     */
    private ImageIcon icon;

    // Constructor

    /**
     * Constructor.
     */
    public Worker() {}

    // Methods

    /**
     * Gets the icon used to render the worker.
     *
     * @return The ImageIcon representing the worker.
     */
    public ImageIcon draw() {
        return icon;
    }

    /**
     * Sets the icon used to represent the worker.
     *
     * @param icon The ImageIcon to assign.
     */
    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }
}

