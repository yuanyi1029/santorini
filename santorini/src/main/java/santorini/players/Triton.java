package santorini.players;

import santorini.actions.Action;
import santorini.board.Board;
import santorini.game.GamePhase;
import santorini.game.GameState;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the god Triton in the game.
 * Triton's power allows the player to move again when its worker reaches the perimeter
 * of the board.
 *
 * Created by:
 * @author Yuan Yi
 */
public class Triton extends God {

    // Constructor

    /**
     * Constructor.
     * Initializes the god with its name, description, and allowed moves and builds.
     * Artemis allows 1 moves and 1 build per turn.
     */
    public Triton() {
        super("Triton", "Move Again at Perimeter.", 1, 1);
    }

    /**
     * Executes the power of Triton, grans the player another move when the previous
     * action moves its worker to a perimeter of the board.
     *
     * @return The boolean status whether the power has been executed successfully
     */
    @Override
    public boolean executePower(GameState gameState, Action previousAction) {
        if (gameState.getGamePhase() == GamePhase.MOVE) {
            Board board = gameState.getBoard();
            if (board.isPerimeter(previousAction.getTarget())) {
                gameState.increaseMovesRemaining();
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * Returns the icon representing Triton.
     * The image is scaled for display in the UI.
     *
     * @return The ImageIcon representing Triton.
     */
    @Override
    public ImageIcon draw() {
        Image image = new ImageIcon(getClass().getResource("/triton-profile.png"))
                .getImage()
                .getScaledInstance(120, 200, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(image);
        return icon;
    }
}
