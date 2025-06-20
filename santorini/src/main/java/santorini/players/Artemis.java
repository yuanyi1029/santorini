package santorini.players;

import santorini.actions.Action;
import santorini.board.Board;
import santorini.game.GamePhase;
import santorini.game.GameState;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the god Artemis in the game.
 * Artemis's power allows the player to move twice during their turn.
 *
 * Created by:
 * @author Zi Yang
 */
public class Artemis extends God {

    // Constructor

    /**
     * Constructor.
     * Initializes the god with its name, description, and allowed moves and builds.
     * Artemis allows 2 moves and 1 build per turn.
     */
    public Artemis() {
        super("Artemis", "Move Twice.", 2 , 1);
    }

    // Methods

    /**
     * Executes the power of Artemis, always grants the player another move
     *
     * @return The boolean status whether the power has been executed successfully
     */
    @Override
    public boolean executePower(GameState gameState, Action previousAction) {
        if (gameState.getGamePhase() == GamePhase.MOVE && !gameState.getHasMoved()) {
            gameState.increaseMovesRemaining();
            return true;
        }
        return false;
    }

    /**
     * Returns the icon representing Artemis.
     * The image is scaled for display in the UI.
     *
     * @return The ImageIcon representing Artemis.
     */
    @Override
    public ImageIcon draw() {
        Image image = new ImageIcon(getClass().getResource("/artemis-profile.png"))
                .getImage()
                .getScaledInstance(120, 200, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(image);
        return icon;
    }
}
