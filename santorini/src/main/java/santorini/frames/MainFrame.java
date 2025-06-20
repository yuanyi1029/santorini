package santorini.frames;

import santorini.controllers.GameController;
import santorini.controllers.InstructionsController;
import santorini.controllers.MenuController;
import santorini.controllers.WinnerController;
import santorini.game.Game;
import santorini.panels.*;

import javax.swing.*;

/**
 * The main application window for the Santorini game.
 * Initializes all controllers and panels, registers them with the PanelManager,
 * and subscribes the GameController to the Game's state updates.
 * Displays the menu panel on startup.
 *
 * Created by:
 * author Yuan Yi, Zi Yang
 */
public class MainFrame extends JFrame {

    // Constants

    /**
     * The title of the game window.
     */
    private static final String WINDOW_NAME = "Santorini!";

    /**
     * The fixed width of the window in pixels.
     */
    private static final int WINDOW_WIDTH = 1080;

    /**
     * The fixed height of the window in pixels.
     */
    private static final int WINDOW_HEIGHT = 720;

    // Attributes

    /**
     * The panel manager responsible for switching between UI panels.
     */
    private PanelManager panelManager;

    // Constructor

    /**
     * Constructor.
     *
     * Initializes the main frame with a title, size, and default close operation.
     */
    public MainFrame() {

        // Set the title, default close operation, and size of the window
        setTitle(WINDOW_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);

        this.panelManager = new PanelManager();

        // Initialize the controllers
        MenuController menuController = new MenuController(panelManager);
        InstructionsController instructionsController = new InstructionsController(panelManager);
        GameController gameController = new GameController(panelManager);
        WinnerController winnerController = new WinnerController(panelManager);

        Game.getInstance().addSubscriber(gameController);

        // Register panels with the panel manager
        panelManager.registerPanel(PanelName.MENU.toString(), menuController.getPanel(), menuController);
        panelManager.registerPanel(PanelName.INSTRUCTIONS.toString(), instructionsController.getPanel(), instructionsController);
        panelManager.registerPanel(PanelName.GAME.toString(), gameController.getPanel(), gameController);
        panelManager.registerPanel(PanelName.WINNER.toString(), winnerController.getPanel(), winnerController);

        add(panelManager.getMainPanel());
    }

    // Methods

    /**
     * Starts and displays the main frame and shows the menu panel as the initial screen.
     */
    public void startFrame() {
        setVisible(true);
        // Initialize the menu panel
        panelManager.switchPanel(PanelName.MENU.toString());
    }
}