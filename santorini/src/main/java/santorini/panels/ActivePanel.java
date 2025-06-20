package santorini.panels;

import santorini.game.GameState;


/**
 * Interface for panels that require game state information during initialization.
 * Used for interactive or dynamic panels such as the gameplay screen.
 *
 * Created by:
 * @author Diana Wijaya
 */
public interface ActivePanel {

    /**
     * Initializes the panel UI components with the provided game state.
     * This method should be called when the panel becomes active.
     *
     * @param gameState The current game state used to populate the panel.
     */

    void initializeUI(GameState gameState);
}
