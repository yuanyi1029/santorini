package santorini.actions;

import santorini.board.Board;
import santorini.game.Position;
import santorini.players.Player;
import santorini.players.Worker;

/**
 * An abstract class representing an action that can be performed in the game.
 * Each action has a target position and a name, and defines execution logic.
 *
 * Created by:
 * @author Diana Wijaya
 */
public abstract class Action {

    // Attributes

    /**
     * The target position where the action will be applied.
     */
    protected final Position target;

    /**
     * The name of the action.
     */
    protected final String actionName;

    // Constructor

    /**
     * Constructor.
     *
     * @param target The position where the action is applied.
     * @param actionName The name of the action.
     */
    public Action(Position target, String actionName) {
        this.target = target;
        this.actionName = actionName;
    }

    // Getters and Setters

    /**
     * Gets the target position of the action.
     *
     * @return The target position.
     */
    public Position getTarget() {
        return target;
    }

    /**
     * Gets the name of the action.
     *
     * @return The action name.
     */
    public String getActionName() {
        return actionName;
    }

    // Methods

    /**
     * Executes the action in the context of the game.
     *
     * @param player The player performing the action.
     * @param worker The worker involved in the action.
     * @param board The game board where the action takes place.
     * @return true if the action was successful, false otherwise.
     */
    public abstract boolean execute(Player player, Worker worker, Board board);

    /**
     * Returns the string representation of the action.
     *
     * @return The action name.
     */
    public String toString() {
        return actionName;
    }
}
