package santorini.game;

import santorini.players.Worker;

import java.util.*;

/**
 * Manages the mapping between workers and their positions on the game board.
 * Supports efficient bidirectional lookup between Position and Worker.
 * Ensures position occupancy rules and allows safe addition, removal, and movement of workers.
 *
 * Created by:
 * author Yuan Yi, Zi Yang, Diana, Hsu Chyi
 */
public class WorkerPositions {

    // Attributes

    /**
     * Maps a position to the worker currently occupying it.
     */
    private Map<Position, Worker> positionToWorker;

    /**
     * Maps a worker to their current position on the board.
     */
    private Map<Worker, Position> workerToPosition;

    // Constructor

    /**
     * Constructor.
     */
    public WorkerPositions() {
        positionToWorker = new HashMap<>();
        workerToPosition = new HashMap<>();
    }

    // Methods

    /**
     * Adds a worker to a specific board position.
     *
     * @param position The position to place the worker.
     * @param worker The worker to add.
     * @throws IllegalArgumentException if the position is already occupied or the worker already exists on the board.
     */

    public void addWorker(Position position, Worker worker) {

        // Check if the position is already occupied or the worker already exists
        if (positionToWorker.containsKey(position)) {
            throw new IllegalArgumentException();
        }
        if (workerToPosition.containsKey(worker)) {
            throw new IllegalArgumentException();
        }

        // Add the worker to the position and update the mappings
        positionToWorker.put(position, worker);
        workerToPosition.put(worker, position);
    }

    /**
     * Removes a worker from the board.
     *
     * @param worker The worker to remove.
     */
    public void removeWorker(Worker worker) {
        Position position = workerToPosition.get(worker);
        positionToWorker.remove(position);
        workerToPosition.remove(worker);
    }

    /**
     * Moves a worker to a new position.
     *
     * @param newPosition The new position to move the worker to.
     * @param worker The worker to move.
     * @throws IllegalArgumentException if the target position is already occupied.
     */
    public void moveWorker(Position newPosition, Worker worker) {

        // If the new position is already occupied, throw an exception
        if (occupied(newPosition)) {
            throw new IllegalArgumentException();
        }
        else {

            // Remove the worker from its old position, add it to the new position, and update the mappings
            Position oldPosition = workerToPosition.get(worker);
            workerToPosition.put(worker, newPosition);
            positionToWorker.put(newPosition, worker);

            positionToWorker.remove(oldPosition);
        }
    }

    /**
     * Checks if a worker is currently on the board.
     *
     * @param worker The worker to check.
     * @return true if the worker is present; false otherwise.
     */
    public boolean contains(Worker worker) {
        return workerToPosition.containsKey(worker);
    }

    /**
     * Checks if a position on the board is occupied by a worker.
     *
     * @param position The position to check.
     * @return true if the position is occupied; false otherwise.
     */
    public boolean occupied(Position position) {
        return positionToWorker.containsKey(position);
    }

    /**
     * Retrieves the position of a given worker.
     *
     * @param worker The worker to query.
     * @return The position of the worker.
     */
    public Position getPositionOf(Worker worker) {
        return workerToPosition.get(worker);
    }

    /**
     * Retrieves the positions of a list of workers.
     *
     * @param workers A list of workers.
     * @return A list of their respective positions.
     */
    public List<Position> getPositionsOf(List<Worker> workers) {
        List<Position> positions = new ArrayList<>();

        // Iterate through the list of workers and get their positions
        for (Worker worker : workers) {
            positions.add(getPositionOf(worker));
        }
        return positions;
    }

    /**
     * Retrieves the worker at a given board position.
     *
     * @param position The position to query.
     * @return The worker at that position, or null if unoccupied.
     */
    public Worker getWorkerAt(Position position) {
        return positionToWorker.get(position);
    }
}