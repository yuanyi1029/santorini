package santorini.panels;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * An abstract base class for all UI panels in the game.
 * Manage buttons and labels through string keys for dynamic access and interaction.
 * Provides utility methods for enabling/disabling buttons, setting label text,
 * and attaching/removing action listeners.
 *
 * Created by:
 * @author Diana, Hsu Chyi, Zi Yang, Yuan Yi
 */
public abstract class Panel extends JPanel {

    // Attributes

    /**
     * A hashmap of button keys to their corresponding JButton instances.
     */
    protected HashMap<String, AbstractButton> buttons;

    /**
     * A hashmap of label keys to their corresponding JLabel instances.
     */
    protected HashMap<String, JLabel> labels;

    // Constructor

    /**
     * Constructs a Panel and initializes the button and label maps.
     */
    public Panel() {
        buttons = new HashMap<>();
        labels = new HashMap<>();
    }

    // Methods

    /**
     * Retrieves a JButton associated with the given key.
     *
     * @param key The identifier for the button.
     * @return The JButton, or null if the key is not found.
     */
    public AbstractButton getButton(String key) {
        return buttons.get(key);
    }

    /**
     * Disables the button associated with the given key.
     *
     * @param key The identifier for the button.
     */
    public void disableButton(String key) {
        AbstractButton button = buttons.get(key);
        if (button != null) {
            button.setEnabled(false);
        }
    }

    /**
     * Enables the button associated with the given key.
     *
     * @param key The identifier for the button.
     */
    public void enableButton(String key) {
        AbstractButton button = buttons.get(key);
        if (button != null) {
            button.setEnabled(true);
        }
    }

    /**
     * Adds an action listener to the button with the given key.
     *
     * @param key The identifier for the button.
     * @param actionListener The ActionListener to attach.
     */
    public void addListener(String key, ActionListener actionListener) {
        AbstractButton button = getButton(key);
        if (button != null) {
            button.addActionListener(actionListener);
        }
    }

    /**
     * Removes all action listeners from the button with the given key.
     *
     * @param key The identifier for the button.
     */
    public void removeListener(String key) {
        AbstractButton button = getButton(key);
        if (button != null) {
            for (ActionListener actionListener : button.getActionListeners()) {
                button.removeActionListener(actionListener);
            }
        }
    }

    /**
     * Sets the text of a JLabel associated with the given key.
     *
     * @param key The identifier for the label.
     * @param text The new text to display.
     */
    public void setLabel(String key, String text) {
        JLabel label = labels.get(key);
        if (label != null) {
            label.setText(text);
        }
    }
}