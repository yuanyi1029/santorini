package santorini.panels;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the main menu panel of the game.
 * Contains buttons to start the game, view instructions, or quit the application.
 * Implements the StaticPanel interface as it does not require dynamic game state.
 *
 * Created by:
 * @author Diana Wijaya, Yuan Yi
 */
public class MenuPanel extends Panel implements StaticPanel {

    // Constructor

    /**
     * Constructor.
     */
    public MenuPanel() {
        super();
    }

    /**
     * Initializes the UI components of the menu panel. This includes setting up the Start Game,
     * Instructions, and Quit buttons, and adding a background image.
     */
    @Override
    public void initializeUI() {
        setLayout(null);

        // ====================================================================
        // Game Mode Radio Buttons
        // ====================================================================

        // Create radio buttons for game mode selection
        JRadioButton standardGameButton = new JRadioButton("Standard Game", true);
        JRadioButton chaosGameButton = new JRadioButton("Chaos Game");

        // Group the radio buttons so only one can be selected
        ButtonGroup gameModeGroup = new ButtonGroup();
        gameModeGroup.add(standardGameButton);
        gameModeGroup.add(chaosGameButton);

        // Style the radio buttons
        standardGameButton.setOpaque(false);
        chaosGameButton.setOpaque(false);
        standardGameButton.setForeground(java.awt.Color.WHITE);
        chaosGameButton.setForeground(java.awt.Color.WHITE);
        standardGameButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        chaosGameButton.setFont(new Font("SansSerif", Font.BOLD, 16));

        // Position the radio buttons
        int radioButtonY = 560;
        standardGameButton.setBounds(380, radioButtonY, 150, 30);
        chaosGameButton.setBounds(560, radioButtonY, 150, 30);

        buttons.put(ButtonName.STANDARD_GAME_BUTTON.toString(), standardGameButton);
        buttons.put(ButtonName.CHAOS_GAME_BUTTON.toString(), chaosGameButton);

        // Radio buttons are managed by ButtonGroup, no need to store in buttons map
        add(standardGameButton);
        add(chaosGameButton);

        // ====================================================================
        // Button Group
        // ====================================================================

        // Set the size and position of the button panel (moved lower)
        int panelWidth = 4 * 150 + 3 * 30;
        int panelHeight = 40;
        int buttonWidth = 150;
        int buttonHeight = 40;
        int spacing = 30;

        // Create a panel to hold the buttons (moved down from 580 to 620)
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setOpaque(false);
        buttonPanel.setBounds(195, 600, panelWidth, panelHeight);

        // Load Game Button
        JButton loadGameButton = new JButton("Load Game");
        loadGameButton.setBounds(0, 0, buttonWidth, buttonHeight);
        buttons.put(ButtonName.LOAD_GAME_BUTTON.toString(), loadGameButton);

        // Start Game Button
        JButton startGameButton = new JButton("Start Game!");
        startGameButton.setBounds(buttonWidth + spacing, 0, buttonWidth, buttonHeight);
        buttons.put(ButtonName.START_GAME_BUTTON.toString(), startGameButton);

        // Instructions Button
        JButton instructionsButton = new JButton("Instructions");
        instructionsButton.setBounds(2 * (buttonWidth + spacing), 0, buttonWidth, buttonHeight);
        buttons.put(ButtonName.INSTRUCTIONS_BUTTON.toString(), instructionsButton);

        // Quit Button
        JButton quitButton = new JButton("Quit");
        quitButton.setBounds(3 * (buttonWidth + spacing), 0, buttonWidth, buttonHeight);
        buttons.put(ButtonName.QUIT_BUTTON.toString(), quitButton);

        // Add buttons to the panel
        buttonPanel.add(loadGameButton);
        buttonPanel.add(startGameButton);
        buttonPanel.add(instructionsButton);
        buttonPanel.add(quitButton);
        add(buttonPanel);

        // ====================================================================
        // Background
        // ====================================================================

        // Load the background image and set it to the panel
        ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/menu-background.png"));
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setBounds(0, 0, 1080, 720);
        add(backgroundLabel);
        setComponentZOrder(backgroundLabel, getComponentCount() - 1);
    }
}
