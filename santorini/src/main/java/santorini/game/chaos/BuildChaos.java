package santorini.game.chaos;

import santorini.board.Board;
import santorini.game.Position;
import santorini.towers.Tower;
import santorini.utils.Logger;

/**
 * The BuildChaos effect randomly builds towers on the board based on
 * random intervals
 *
 * Created by:
 * author Yuan Yi
 */
public class BuildChaos extends Chaos {

    // Attributes

    /**
     * The minimum interval for which the effect executes.
     */
    private static final int MINIMUM_INTERVAL = 1;

    /**
     * The maximum interval for which the effect executes.
     */
    private static final int MAXIMUM_INTERVAL = 3;

    // Constructor

    /**
     * Constructor.
     */
    public BuildChaos() {
        super(MINIMUM_INTERVAL, MAXIMUM_INTERVAL);
    }

    // Methods

    /**
     * Builds random tower floors on the board.
     *
     * @param board The game board of the current game.
     * @param affectedTowers The number of towers affected by the effect.
     */
    @Override
    public void apply(Board board, int affectedTowers) {
        Logger.getInstance().log("BOARD CHAOS - RANDOM TOWERS HAVE BEEN BUILT!");
        for (int i = 0; i < affectedTowers; i++) {
            Position position;
            do {
                int x = random.nextInt(board.getWidth());
                int y = random.nextInt(board.getHeight());
                position = new Position(x, y);
            } while (board.isOccupied(position));

            Tower tower = board.getTower(position);
            tower.buildFloor();
        }
    }
}
