package santorini.game.chaos;

import santorini.board.Board;

import java.util.Random;

/**
 * The Chaos Effect within a chaos game mode. Performs a tick every round and
 * applies the effect upon the timer reaches 0
 *
 * Created by:
 * author Yuan Yi
 */
public abstract class Chaos {

    // Attributes

    /**
     * The minimum interval for which the effect executes.
     */
    protected int minimumInterval;

    /**
     * The maximum interval for which the effect executes.
     */
    protected int maximumInterval;

    /**
     * The randomizer used for resetting the countdown.
     */
    protected Random random;

    /**
     * The countdown before the chaos effect executes.
     */
    protected int countdown;

    // Constructor

    /**
     * Constructor.
     *
     * @param minimumInterval The minimum interval for which the effect executes.
     * @param maximumInterval The maximum interval for which the effect executes.
     */
    public Chaos(int minimumInterval, int maximumInterval) {
        this.minimumInterval = minimumInterval;
        this.maximumInterval = maximumInterval;
        this.random = new Random();
        resetCountdown();
    }

    // Methods

    /**
     * Resets the countdown to a random value based on the intervals
     */
    public void resetCountdown() {
        countdown = random.nextInt(maximumInterval - minimumInterval + 1) + minimumInterval;
    }

    /**
     * Performs a tick for each turn to reduce the countdown and potentially apply
     * a chaos effect
     *
     * @param board The game board of the current game.
     * @param affectedTowers The number of towers affected by the effect.
     */
    public void tick(Board board, int affectedTowers) {
        if (countdown == 0) {
            apply(board, affectedTowers);
            resetCountdown();
        }
        else {
            countdown -= 1;
        }
    }

    /**
     * Applies a chaos effect to the board.
     *
     * @param board The game board of the current game.
     * @param affectedTowers The number of towers affected by the effect.
     */
    public abstract void apply(Board board, int affectedTowers);
}
