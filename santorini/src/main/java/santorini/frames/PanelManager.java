package santorini.frames;

import santorini.controllers.Controller;
import santorini.panels.Panel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages switching between panels using a CardLayout.
 * Maintains mappings between panel names, their UI components, and their controllers.
 * Responsible for initializing the appropriate panel UI before displaying it.
 *
 * Created by:
 * author Yuan Yi, Zi Yang
 */
public class PanelManager {

    // Attributes

    /**
     * The main container panel that holds all other panels.
     */
    private JPanel mainPanel;

    /**
     * The layout manager used to switch between panels.
     */
    private CardLayout mainLayout;

    /**
     * A map of registered panel names to their corresponding Panel objects.
     */
    private Map<String, Panel> panels;

    /**
     * A map of panel names to their associated controllers.
     */
    private Map<String, Controller> controllers;

    // Constructor

    /**
     * Constructor.
     */
    public PanelManager() {
        this.mainLayout = new CardLayout();
        this.mainPanel = new JPanel(mainLayout);
        this.panels = new HashMap<>();
        this.controllers = new HashMap<>();
    }

    // Methods

    /**
     * Returns the main container panel that holds all other panels.
     *
     * @return The main JPanel with CardLayout.
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Registers a new panel and its corresponding controller under a given name.
     *
     * @param panelName The string identifier for the panel.
     * @param panel The Panel instance to register.
     * @param controller The Controller responsible for managing the panel's behavior.
     */
    public void registerPanel(String panelName, Panel panel, Controller controller) {
        panels.put(panelName, panel);
        controllers.put(panelName, controller);
        mainPanel.add(panel, panelName);
    }

    /**
     * Switches the visible panel to the one associated with the given name.
     * Calls the panel controller's initialization method before showing the panel.
     *
     * @param panelName The name of the panel to display.
     */
    public void switchPanel(String panelName) {
        Controller controller = controllers.get(panelName);
        controller.initializePanelUI();
        mainLayout.show(mainPanel, panelName);
    }
}