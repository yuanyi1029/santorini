package santorini.controllers;

import santorini.frames.PanelManager;
import santorini.panels.Panel;

/**
 * Abstract base class for all controllers that manage a specific type of Panel.
 * Provides functionality to switch panels and access the current panel.
 *
 * @param <T> The specific type of Panel that it handles.
 *
 * Created by:
 * @author Lai Zi Yang, Diana Wijaya, See Hsu Chyi, Wong Yuan Yi
 */
public abstract class Controller<T extends Panel>  {

    // Attributes

    /**
     * The panel manager responsible for switching between panels.
     */
    private PanelManager panelManager;
    /**
     * The panel associated with this controller.
     */
    protected T panel;

    // Constructor

    /**
     * Constructor.
     *
     * @param panelManager The panel manager used for switching panels.
     */
    public Controller(PanelManager panelManager) {
        this.panelManager = panelManager;
    }

    // Methods

    /**
     * Requests the panel manager to switch to a panel with the given name.
     *
     * @param panelName The name of the panel to switch to.
     */
    public void switchPanel(String panelName) {
        panelManager.switchPanel(panelName);
    }

    /**
     * Gets the panel associated with this controller.
     *
     * @return The panel managed by this controller.
     */
    public Panel getPanel() {
        return panel;
    }

    /**
     * Initializes the panel's UI components.
     * This method must be implemented by subclasses to define specific panel setup logic.
     */
    public abstract void initializePanelUI();
}