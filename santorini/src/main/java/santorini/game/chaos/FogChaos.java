package santorini.game.chaos;

import santorini.board.Board;
import santorini.game.Position;
import santorini.utils.Logger;

import java.util.*;

/**
 * The FogChaos effect randomly covers towers with fog on the board based on
 * random intervals
 *
 * Created by:
 * author Yuan Yi
 */
public class FogChaos extends Chaos {

    // Attributes

    /**
     * The minimum interval for which the effect executes.
     */
    private static final int MINIMUM_INTERVAL = 1;

    /**
     * The maximum interval for which the effect executes.
     */
    private static final int MAXIMUM_INTERVAL = 1;

    /**
     * The turn duration where a tower is covered by fog.
     */
    private static final int FOG_DURATION = 3;

    /**
     * The current positiosn that are affected by fog.
     */
    private Map<Position, Integer> foggedPositions;

    // Constructor

    /**
     * Constructor.
     */
    public FogChaos() {
        super(MINIMUM_INTERVAL, MAXIMUM_INTERVAL);
        this.foggedPositions = new HashMap<>();
    }

    // Methods

    /**
     * Covers random towers on the board with fog.
     *
     * @param board The game board of the current game.
     * @param affectedTowers The number of towers affected by the effect.
     */
    @Override
    public void apply(Board board, int affectedTowers) {
        Logger.getInstance().log("FOG CHAOS - RANDOM POSITIONS HAVE BEEN COVERED BY FOG!");
        for (int i = 0; i < affectedTowers; i++) {
            Position position;
            do {
                int x = random.nextInt(board.getWidth());
                int y = random.nextInt(board.getHeight());
                position = new Position(x, y);
            } while (board.isOccupied(position));

            if (!foggedPositions.containsKey(position)) {
                board.getTower(position).setFogged(true);
                foggedPositions.put(position, FOG_DURATION);
            }
            else {
                int remaining = foggedPositions.get(position);
                foggedPositions.put(position, remaining + FOG_DURATION);
            }
        }
    }

    /**
     * Removes the fog of certain towers when the duration has expired.
     *
     * @param board The game board of the current game.
     * @param affectedTowers The number of towers affected by the effect.
     */
    @Override
    public void tick(Board board, int affectedTowers) {
        List<Position> foggedPositionsToRemove = new ArrayList<>();

        for (Position position : foggedPositions.keySet()) {
            int remaining = foggedPositions.get(position) - 1;

            if (remaining == 0) {
                board.getTower(position).setFogged(false);
                foggedPositionsToRemove.add(position);
            }
            else {
                foggedPositions.put(position, remaining);
            }
        }

        for (Position position : foggedPositionsToRemove) {
            foggedPositions.remove(position);
        }

        super.tick(board, affectedTowers);
    }
}