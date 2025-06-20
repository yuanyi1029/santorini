package santorini.board;

import santorini.game.Position;
import santorini.players.Worker;

/**
 * Defines the core set of actions that can be performed on a game board,
 * including worker placement, movement, and removal.
 * This interface is implemented by the Board class.
 *
 * Created by:
 * author Diana Wijaya
 */
public interface BoardActions {

    // Methods

    /**
     * Adds a worker to a specific position on the board.
     *
     * @param position The position where the worker should be placed.
     * @param worker   The worker to add.
     */
    void addWorker(Position position, Worker worker);

    /**
     * Removes a worker from the board.
     *
     * @param worker The worker to remove.
     */
    void removeWorker(Worker worker);

    /**
     * Moves a worker to a new position on the board.
     *
     * @param newPosition The destination position.
     * @param worker The worker to move.
     */
    void moveWorker(Position newPosition, Worker worker);
}
