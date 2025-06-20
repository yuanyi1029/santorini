package santorini.players;

import santorini.actions.Action;
import santorini.game.GamePhase;
import santorini.game.GameState;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the god Demeter in the game.
 * Demeter's power allows the player to build twice during their turn.
 *
 * Created by:
 * @author Zi Yang
 */
public class Demeter extends God {

    // Constructor

    /**
     * Constructor.
     * Initializes the god with its name, description, and allowed moves and builds.
     * Demeter allows 1 move and 2 builds per turn.
     */
    public Demeter() {
        super("Demeter", "Build Twice.", 1, 2);
    }

    // Methods

    /**
     * Executes the power of Demeter, always grants the player another build
     *
     * @return The boolean status whether the power has been executed successfully
     */
    @Override
    public boolean executePower(GameState gameState, Action previousAction) {
        if (gameState.getGamePhase() == GamePhase.BUILD && !gameState.getHasBuilt()) {
            gameState.increaseBuildsRemaining();
            return true;
        }
        return false;
    }

    /**
     * Returns the icon representing Demeter.
     * The image is scaled for display in the UI.
     *
     * @return The ImageIcon representing Demeter.
     */
    @Override
    public ImageIcon draw() {
        Image image = new ImageIcon(getClass().getResource("/demeter-profile.png"))
                .getImage()
                .getScaledInstance(120, 200, Image.SCALE_SMOOTH);

        ImageIcon icon = new ImageIcon(image);
        return icon;
    }
}
