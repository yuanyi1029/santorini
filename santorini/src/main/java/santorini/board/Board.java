package santorini.board;

import santorini.game.Position;
import santorini.game.WorkerPositions;
import santorini.players.Worker;
import santorini.towers.Tower;
import santorini.utils.SaveConfig;
import santorini.utils.SaveManager;
import santorini.utils.Saveable;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Represents the game board, which manages the placement and state of towers and workers.
 * Implements both {@link BoardState} for read-only access and {@link BoardActions} for modifying the board.
 *
 * Created by:
 * author Diana, Yuan Yi, Zi Yang, Hsu Chyi
 */
public class Board implements BoardState, BoardActions, Saveable {

    // Attributes

    /**
     * The width (number of columns) of the board.
     */
    private final int width;

    /**
     * The height (number of rows) of the board.
     */
    private final int height;

    /**
     * A mapping of board positions to towers.
     */
    private final Map<Position, Tower> towers;

    /**
     * Tracks the positions of all workers on the board.
     */
    private final WorkerPositions workerPositions;

    // Constructor

    /**
     * Constructor.
     *
     * @param width  The width of the board.
     * @param height The height of the board.
     */
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.towers = new HashMap<>();
        this.workerPositions = new WorkerPositions();

        initializeBoard();
    }

    /**
     * Constructor for loading a game.
     *
     * @param buildString The string to be parsed as a board.
     */
    public Board(String buildString) {
        String[] lines = buildString.trim().split(SaveConfig.NEWLINE_REGEX);
        String[] dimensionString = lines[0].trim().split(SaveConfig.DELIMITER_REGEX);

        this.width = Integer.parseInt(dimensionString[0]);
        this.height = Integer.parseInt(dimensionString[1]);
        this.towers = new HashMap<>();
        this.workerPositions = new WorkerPositions();

        initializeBoard(buildString);
    }

    /**
     * Populates the board with an empty tower at each position.
     */
    private void initializeBoard() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Position position = new Position(x, y);
                towers.put(position, new Tower());
            }
        }
    }

    /**
     * Populates the board with previously built towers
     *
     * @param buildString The string to be parsed as a board.
     */
    private void initializeBoard(String buildString) {
        String[] lines = buildString.trim().split(SaveConfig.NEWLINE_REGEX);

        for (int x = 0; x < width; x++) {
            String[] rowString = lines[x + 1].trim().split(SaveConfig.DELIMITER_REGEX);

            for (int y = 0; y < width; y++) {
                String[] towerString = rowString[y].split(SaveConfig.COMMA_REGEX);
                int towerHeight = Integer.parseInt(towerString[0]);

                Position position = new Position(x, y);
                Tower tower = new Tower();

                tower.setFogged(Boolean.parseBoolean(towerString[1]));
                for (int i = 0; i < towerHeight; i++) {
                    tower.buildFloor();
                }
                towers.put(position, tower);
            }
        }
    }

    // Inherited Methods from BoardState

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public Tower getTower(Position position) {
        return towers.get(position);
    }

    @Override
    public boolean isValidPosition(Position position) {
        return position.x() >= 0 && position.x() < width &&
            position.y() >= 0 && position.y() < height;
    }

    @Override
    public boolean isPerimeter(Position position) {
        return position.x() == 0 || position.y() == 0 || position.x() == width - 1 || position.y() == height - 1;
    }

    @Override
    public boolean isOccupied(Position position) {
        return workerPositions.occupied(position);
    }

    @Override
    public Worker getWorkerAt(Position position) {
        return workerPositions.getWorkerAt(position);
    }

    @Override
    public Position getPositionOf(Worker worker) {
        return workerPositions.getPositionOf(worker);
    }

    @Override
    public int getHeightOf(Worker worker) {
        Position position = getPositionOf(worker);
        return getTower(position).getHeight();
    }

    // Inherited Methods from BoardActions

    @Override
    public void addWorker(Position position, Worker worker) {
        if (isValidPosition(position) && !isOccupied(position)) {
            workerPositions.addWorker(position, worker);
        }
    }

    @Override
    public void removeWorker(Worker worker) {
        workerPositions.removeWorker(worker);
    }

    @Override
    public void moveWorker(Position newPosition, Worker worker) {
        if (isValidPosition(newPosition) && !isOccupied(newPosition)) {
            workerPositions.moveWorker(newPosition, worker);
        }
    }

    @Override
    public String save() {
        StringBuilder builder = new StringBuilder();

        // Save board size (width and height)
        builder.append(width).append(SaveConfig.DELIMITER).append(height).append(SaveConfig.NEWLINE);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Position position = new Position(x, y);
                Tower tower = towers.get(position);
                builder.append(tower.getHeight()).append(SaveConfig.COMMA).append(tower.getFogged());
                builder.append(SaveConfig.DELIMITER);
            }
            builder.append(SaveConfig.NEWLINE);
        }
        return builder.toString();
    }
}
