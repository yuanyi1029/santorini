package santorini.utils;

/**
 * Interface for components that can be saved to a text file.
 * Implementing classes will be able to save its contents and return a string
 * which will be saved in a text file, allowing them to be loaded accordingly
 * in the future.
 *
 * Created by:
 * author Yuan Yi
 */
public interface Saveable {

    /**
     * Called when the game is being saved and parses a components current
     * contents to a string, enabling it to be loaded in the future.
     *
     * @return The current state of a component represented as a string.
     */
    String save();
}
