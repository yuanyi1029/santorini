package santorini.controllers;

import santorini.utils.Logger;
import santorini.frames.PanelManager;
import santorini.game.*;
import santorini.panels.*;

import java.awt.event.ActionListener;

/**
 * The WinnerController class manages interactions for the winner screen.
 * Initializes the winner panel UI and handles user actions such as returning to the menu,
 * restarting the game, or quitting the application.
 *
 * Created by:
 * author Lai Zi Yang, Diana Wijaya, See Hsu Chyi, Wong Yuan Yi
 */
public class WinnerController extends Controller<WinnerPanel>{

    // Constructor

    /**
     * Constructor.
     *
     * @param panelManager The PanelManager to switch between game panels.
     */
    public WinnerController(PanelManager panelManager) {
        super(panelManager);
        panel = new WinnerPanel();
    }

    // Methods

    /**
     * Initializes the WinnerPanel UI and sets up button listeners for winner screen options.
     */
    @Override
    public void initializePanelUI() {
        panel.initializeUI();
        setupWinnerLabel();
        setupReturnMenuListener();
        setupRestartListener();
        setupQuitListener();
    }

    /**
     * Sets the winner's name on the winner label.
     */
    public void setupWinnerLabel() {
        String winner = Game.getInstance().getWinner().getName();
        if (winner != null) {
            panel.setLabel(LabelName.WINNER_LABEL.toString(), winner);
        }
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

    /**
     * Sets up the listener for the restart button, creates a new game and navigates to the game panel.
     */
    public void setupRestartListener() {
        ActionListener actionListener = e -> {
            GameFactory factory = new GameFactory();
            factory.createStandardGame();

            Logger.getInstance().clearLogs();
            Logger.getInstance().log("Game Start!");
            Logger.getInstance().log("Turn 1");
            Logger.getInstance().log(String.format("%s's Turn", Game.getInstance().getGameState().getCurrentPlayer().toString()));

            switchPanel(PanelName.GAME.toString());
        };
        panel.addListener(ButtonName.RESTART_GAME_BUTTON.toString(), actionListener);
    }

    /**
     * Sets up the listener for the quit button, exits the application when clicked.
     */
    public void setupQuitListener() {
        ActionListener actionListener = e -> {
            System.exit(0);
        };
        panel.addListener(ButtonName.QUIT_BUTTON.toString(), actionListener);
    }
}

