package santorini.actions;

import santorini.utils.Logger;
import santorini.board.Board;
import santorini.game.Position;
import santorini.players.Player;
import santorini.players.Worker;

/**
 * Represents a move action performed by a worker.
 * The worker will to move to a new position if the move is valid
 * based on occupancy and height difference rules.
 *
 * Created by:
 * @author Diana Wijaya
 */
public class MoveAction extends Action {

    // Constructor

    /**
     * Constructor.
     *
     * @param target The position where the worker will move to.
     */
    public MoveAction(Position target) {
        super(target, "Move");
    }

    // Methods

    /**
     * Executes the move action.
     * The worker moves to the target position if the position is not occupied,
     * climbable, and not completed.
     *
     * @param player The owner of the worker invovled in the action.
     * @param worker The worker performing to move.
     * @param board The game board on which the move takes place.
     * @return true if the move was successful, false otherwise.
     */
    @Override
    public boolean execute(Player player, Worker worker, Board board) {
        // Get the current position of the worker
        Position current = board.getPositionOf(worker);

        // Move to worker to the target tower
        board.moveWorker(target, worker);
        Logger.getInstance().log(String.format("%s's worker at (%d, %d) moved to (%d, %d) ", player, current.x(), current.y(), target.x(), target.y()));
        return true;
    }
}
