package santorini.game.modifier;

import santorini.board.Board;

/**
 * The GameModifier abstract class serves as a blueprint for future modifiers
 * for different game modes.
 *
 * Created by:
 * author Yuan Yi
 */
public abstract class GameModifier {

    // Methods

    /**
     * Returns an instance of a specific GameModifier based on a string used
     * for loading specific games.
     *
     * @param name the name of the modifier to be parsed
     * @return a new instance of the corresponding GameModifier
     */
    public static GameModifier parseName(String name) {
        if (name.equals("StandardModifier")) {
            return new StandardModifier();
        }
        else if (name.equals("ChaosModifier")) {
            return new ChaosModifier();
        }
        return null;
    }

    /**
     * Executes the logic of the modifier of a specific game mode.
     *
     * @param board The game board of the current game.
     * @param turnNumber The current turn number.
     */
    public abstract void executeModifier(Board board, int turnNumber);
}
