package santorini.game;

/**
 * Interface for components that subscribe to game state updates.
 * Implementing classes will be notified when the game state changes,
 * allowing them to react accordingly.
 *
 * Created by:
 * author Yuan Yi
 */
public interface Subscriber {

    /**
     * Called when the game state is updated.
     *
     * @param gameState The new game state.
     */
    void update(GameState gameState);
}
