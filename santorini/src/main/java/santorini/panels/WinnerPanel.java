package santorini.panels;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the winner screen displayed at the end of the game.
 * Shows the winning player's name and provides buttons to return to the menu, restart the game,
 * or quit the application.
 * Implements StaticPanel as it does not require dynamic game state during initialization.
 *
 * Created by:
 * author Yuan Yi, Hsu Chyi
 */
public class WinnerPanel extends Panel implements StaticPanel {

    // Constructor

    /**
     * Constructor.
     */
    public WinnerPanel() {
        super();
    }

    // Methods

    /**
     * Initializes the UI components of the winner panel. This includes a label to display
     * the winning player's name centered on the screen, and a button group below it with options
     * to return to the main menu, restart the game, or quit the application. A background image
     * is added behind all components for visual styling.
     */
    @Override
    public void initializeUI() {
        setLayout(null);

        // ====================================================================
        // Winner Label
        // ====================================================================

        // Create a JLabel to display the winner's name
        JLabel winnerLabel = new JLabel("", JLabel.CENTER);
        winnerLabel.setBounds(340, 400, 400, 60);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 36));
        labels.put(LabelName.WINNER_LABEL.toString(), winnerLabel);
        add(winnerLabel);

        // ====================================================================
        // Button Group
        // ====================================================================

        // Set the size and position of the button panel
        int panelWidth = 510;
        int panelHeight = 40;
        int buttonWidth = 150;
        int buttonHeight = 40;
        int spacing = 30;

        // Create a panel to hold the buttons
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setOpaque(false);
        buttonPanel.setBounds(285, 600, panelWidth, panelHeight);

        // Set the size and position of the buttons
        int restartX = (panelWidth - buttonWidth) / 2;
        int returnMenuX = restartX - spacing - buttonWidth;
        int quitX = restartX + spacing + buttonWidth;

        // Return to Menu Button
        JButton returnMenuButton = new JButton("Return to Menu");
        returnMenuButton.setBounds(returnMenuX, 0, buttonWidth, buttonHeight);
        buttons.put(ButtonName.RETURN_MENU_BUTTON.toString(), returnMenuButton);

        // Restart Game Button
        JButton restartGameButton = new JButton("Restart Game");
        restartGameButton.setBounds(restartX, 0, buttonWidth, buttonHeight);
        buttons.put(ButtonName.RESTART_GAME_BUTTON.toString(), restartGameButton);

        // Quit Button
        JButton quitButton = new JButton("Quit");
        quitButton.setBounds(quitX, 0, buttonWidth, buttonHeight);
        buttons.put(ButtonName.QUIT_BUTTON.toString(), quitButton);

        // Add buttons to the panel
        buttonPanel.add(returnMenuButton);
        buttonPanel.add(restartGameButton);
        buttonPanel.add(quitButton);
        add(buttonPanel);

        // ====================================================================
        // Background
        // ====================================================================

        // Load the background image and set it to the panel
        ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/winner-background.png"));
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setBounds(0, 0, 1080, 720);
        add(backgroundLabel);
        setComponentZOrder(backgroundLabel, getComponentCount() - 1);
    }
}
