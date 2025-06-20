package santorini.game.modifier;

import santorini.board.Board;

/**
 * The StandardModifier acts as a GameModifier for a standard game mode.
 * Currently does nothing because a standard game mode has no requirements
 * for effect modifications.
 *
 * Created by:
 * author Yuan Yi
 */
public class StandardModifier extends GameModifier {

    // Methods

    /**
     * Executes the logic of the modifier of the standard game mode, does
     * nothing because a standard game mode has no requirements for effect
     * modifications.
     *
     * @param board The game board of the current game.
     * @param turnNumber The current turn number.
     */
    @Override
    public void executeModifier(Board board, int turnNumber) {

    }
}
