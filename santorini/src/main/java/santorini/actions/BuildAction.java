package santorini.actions;

import santorini.utils.Logger;
import santorini.board.Board;
import santorini.game.Position;
import santorini.players.Player;
import santorini.players.Worker;
import santorini.towers.Tower;

/**
 * Represents a build action performed by a worker.
 * The worker will build a floor on the target position's tower
 * if the position is unoccupied and the tower is not complete.
 *
 * Created by:
 * @author Diana Wijaya
 */
public class BuildAction extends Action {

    // Constructor

    /**
     * Constructor.
     *
     * @param target The position where the worker will build at.
     */
    public BuildAction(Position target) {
        super(target, "Build");
    }

    // Methods

    /**
     * Executes the build action.
     * The worker builds a floor at the target position if it's not
     * occupied and not completed.
     *
     * @param player The owner of the worker invovled in the action.
     * @param worker The worker performing the build.
     * @param board The game board on which the build takes place.
     * @return true if the build was successful, false otherwise.
     */
    @Override
    public boolean execute(Player player, Worker worker, Board board) {
        // Get the current position of the worker
        Position current = board.getPositionOf(worker);

        Tower targetTower = board.getTower(this.target);

        // Build the extra floor on the tower
        targetTower.buildFloor();
        Logger.getInstance().log(String.format("%s's worker at (%d, %d) built at (%d, %d) ", player, current.x(), current.y(), target.x(), target.y()));
        return true;
    }
}