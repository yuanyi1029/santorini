package santorini.board;

import santorini.game.Position;
import santorini.players.Worker;

import santorini.towers.Tower;

/**
 * Represents the read-only state of the board, allowing querying of dimensions,
 * tower structures, worker positions, and occupancy status.
 * This interface is implemented by the Board class.
 *
 * Created by:
 * author Diana Wijaya
 */
public interface BoardState {

    // Methods

    /**
     * Gets the width of the board (number of columns).
     *
     * @return The width of the board.
     */
    int getWidth();

    /**
     * Gets the height of the board (number of rows).
     *
     * @return The height of the board.
     */
    int getHeight();

    /**
     * Retrieves the tower located at a specific board position.
     *
     * @param position The board position to query.
     * @return The tower at the specified position.
     */
    Tower getTower(Position position);

    /**
     * Checks whether a given position is within the bounds of the board.
     *
     * @param position The position to validate.
     * @return true if the position is valid; false otherwise.
     */
    boolean isValidPosition(Position position);

    /**
     * Checks whether a given position is within the perimeter of the board.
     *
     * @param position The position to validate.
     * @return true if the position is a perimeter of the board; false otherwise.
     */
    boolean isPerimeter(Position position);

    /**
     * Checks whether a given position is currently occupied by a worker.
     *
     * @param position The position to check.
     * @return true if a worker is occupying the position; false otherwise.
     */
    boolean isOccupied(Position position);

    /**
     * Gets the worker located at a specific position on the board.
     *
     * @param position The position to query.
     * @return The worker at the specified position, or null if unoccupied.
     */
    Worker getWorkerAt(Position position);

    /**
     * Gets the current position of a given worker.
     *
     * @param worker The worker to locate.
     * @return The position of the worker.
     */
    Position getPositionOf(Worker worker);

    /**
     * Gets the height of the tower that a specific worker is currently standing on.
     *
     * @param worker The worker to query.
     * @return The height of the tower the worker is on.
     */
    int getHeightOf(Worker worker);
}
