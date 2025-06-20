package santorini.panels;

/**
 * Interface for panels that do not require dynamic game state information during initialization.
 * Typically used for static screens such as menus or instruction panels.
 *
 * Created by:
 * @author Diana Wijaya
 */
public interface StaticPanel {

    /**
     * Initializes the panel UI components.
     * This method should be called when the panel is first displayed.
     */
    void initializeUI();
}
