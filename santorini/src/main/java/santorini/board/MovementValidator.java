package santorini.board;

import java.util.ArrayList;
import java.util.List;
import santorini.game.Position;
import santorini.towers.Tower;

/**
 * Provides utility methods to validate movement and building rules
 * based on the current state of the board.
 * Uses BoardState to perform position and tower checks without modifying the board.
 *
 * Created by:
 * author Diana Wijaya
 */
public class MovementValidator {

    // Attributes

    /**
     * The board state to validate movements and building actions.
     */
    private final BoardState boardState;

    // Constructor

    /**
     * Constructor.
     */
    public MovementValidator(BoardState boardState) {
        this.boardState = boardState;
    }

    // Methods

    /**
     * Retrieves the tower at the given position on the board.
     *
     * @param position The position to check.
     * @return The Tower at that position.
     */
    public Tower getTower(Position position) {
        return boardState.getTower(position);
    }

    /**
     * Returns a list of adjacent positions that are within the board bounds
     * and not currently occupied by any worker.
     *
     * @param position The current position.
     * @return A list of valid adjacent positions.
     */
    public List<Position> getAdjacentPositions(Position position) {
        List<Position> adjacentPositions = new ArrayList<>();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0) {
                    continue;
                }

                int newX = position.x() + x;
                int newY = position.y() + y;

                // Check if the new position is within the board bounds
                if (!(newX < 0 || newX > boardState.getWidth() - 1 || newY < 0 || newY >
                    boardState.getHeight() - 1)) {
                    Position adjacentPosition = new Position(newX, newY);

                    // Only include positions that are not occupied
                    if (!boardState.isOccupied(adjacentPosition)) {
                        adjacentPositions.add(adjacentPosition);
                    }
                }
            }
        }

        return adjacentPositions;
    }

    /**
     * Determines the list of valid positions a worker can move to from the given position.
     * A move is valid if the destination is climbable, not complete (not a dome), and not more than
     * one level higher than the current tower.
     *
     * @param position The current position of the worker.
     * @return A list of positions the worker can legally move to.
     */
    public List<Position> getMoveablePositions(Position position) {
        Tower currentTower = getTower(position);
        List<Position> adjacentPositions = getAdjacentPositions(position);
        List<Position> moveablePositions = new ArrayList<>();

        // Check each adjacent position to see if it's a valid move
        for (Position availablePosition : adjacentPositions) {
            Tower targetTower = getTower(availablePosition);
            if (targetTower.canClimb() && !targetTower.isComplete() && targetTower.getHeight() - currentTower.getHeight() <= 1) {
                moveablePositions.add(availablePosition);
            }
        }

        return moveablePositions;
    }

    /**
     * Determines the list of positions around the given location where the worker can legally build.
     * A build is valid if the space is not already complete (i.e., does not already have a dome).
     *
     * @param position The current position of the worker.
     * @return A list of adjacent positions where building is allowed.
     */
    public List<Position> getBuildablePositions(Position position) {
        List<Position> adjacentPositions = getAdjacentPositions(position);
        List<Position> buildablePositions = new ArrayList<>();

        // Check each adjacent position to see if it's a valid build
        for (Position availablePosition : adjacentPositions) {
            Tower targetTower = getTower(availablePosition);
            if (!(targetTower.isComplete())) {
                buildablePositions.add(availablePosition);
            }
        }

        return buildablePositions;
    }
}
