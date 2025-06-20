package santorini.controllers;

import santorini.frames.PanelManager;
import santorini.panels.ButtonName;
import santorini.panels.InstructionsPanel;
import santorini.panels.PanelName;

import java.awt.event.ActionListener;

/**
 * The InstructionsController class manages interactions for the instructions screen.
 * Initializes the instructions panel UI and handles user actions such as returning to the main menu.
 *
 * Created by:
 * author Lai Zi Yang, Diana Wijaya, See Hsu Chyi, Wong Yuan Yi
 */
public class InstructionsController extends Controller<InstructionsPanel> {

    // Constructor

    /**
     * Constructor.
     *
     * @param panelManager The PanelManager to switch between game panels.
     */
    public InstructionsController(PanelManager panelManager) {
        super(panelManager);
        panel = new InstructionsPanel();
    }

    // Methods

    /**
     * Initializes the InstructionsPanel UI and sets up the return to menu button listener.
     */
    @Override
    public void initializePanelUI() {
        panel.initializeUI();
        setupReturnMenuListener();
    }

    /**
     * Sets up the listener for the return to menu button, navigates to the menu panel.
     */
    public void setupReturnMenuListener() {
        ActionListener actionListener = e -> {
            switchPanel(PanelName.MENU.toString());
        };
        panel.addListener(ButtonName.RETURN_MENU_BUTTON.toString(), actionListener);
    }
}
