package santorini.controllers;

import santorini.utils.Logger;
import santorini.frames.PanelManager;
import santorini.game.*;
import santorini.panels.ButtonName;
import santorini.panels.MenuPanel;
import santorini.panels.PanelName;
import santorini.utils.SaveManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * The MenuController manages interactions on the main menu of the Santorini game.
 * Handles button actions for starting the game, showing instructions, or quitting.
 *
 * Created by:
 * author Lai Zi Yang, Diana Wijaya, See Hsu Chyi, Wong Yuan Yi
 */
public class MenuController extends Controller<MenuPanel> {

    // Constructor

    /**
     * Constructor.
     *
     * @param panelManager The PanelManager to switch between game panels.
     */
    public MenuController(PanelManager panelManager) {
        super(panelManager);
        panel = new MenuPanel();
    }

    // Methods

    /**
     * Initializes the menu UI and sets up button listeners for menu options.
     */
    @Override
    public void initializePanelUI() {
        panel.initializeUI();
        setupLoadGameListener();
        setupStartGameListener();
        setupInstructionsListener();
        setupQuitListener();
    }

    public void setupLoadGameListener() {
        ActionListener actionListener = e -> {
            boolean result = SaveManager.getInstance().loadGame(panel);

            if (result) {
                switchPanel(PanelName.GAME.toString());
            }
        };
        panel.addListener(ButtonName.LOAD_GAME_BUTTON.toString(), actionListener);
    }

    /**
     * Sets up the listener for the Start Game button, creates a new game and switch to the game panel.
     */
    public void setupStartGameListener() {
        ActionListener actionListener = e -> {
            GameFactory gameFactory = new GameFactory();
            if (panel.getButton(ButtonName.STANDARD_GAME_BUTTON.toString()).isSelected()) {
                gameFactory.createStandardGame();
            }
            else if (panel.getButton(ButtonName.CHAOS_GAME_BUTTON.toString()).isSelected()) {
                gameFactory.createChaosGame();
            }

            Logger.getInstance().log("Game Start!");
            Logger.getInstance().log("Turn 1");
            Logger.getInstance().log(String.format("%s's Turn", Game.getInstance().getGameState().getCurrentPlayer().toString()));

            switchPanel(PanelName.GAME.toString());
        };
        panel.addListener(ButtonName.START_GAME_BUTTON.toString(), actionListener);
    }

    /**
     * Sets up the listener for the Instructions button, navigates to the instructions panel.
     */
    public void setupInstructionsListener() {
        ActionListener actionListener = e -> {
            switchPanel(PanelName.INSTRUCTIONS.toString());
        };
        panel.addListener(ButtonName.INSTRUCTIONS_BUTTON.toString(), actionListener);
    }

    /**
     * Sets up the listener for the Quit button, exits the application when clicked.
     */
    public void setupQuitListener() {
        ActionListener actionListener = e -> {
            System.exit(0);
        };
        panel.addListener(ButtonName.QUIT_BUTTON.toString(), actionListener);
    }
}