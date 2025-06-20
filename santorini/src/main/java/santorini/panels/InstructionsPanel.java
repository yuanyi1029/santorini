package santorini.panels;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the instructions panel in the game.
 * Displays gameplay rules and objectives to the player in a scrollable HTML format.
 * Implements StaticPanel as it does not require game state context during initialization.
 *
 * Created by:
 * @author Diana Wijaya
 */
public class InstructionsPanel extends Panel implements StaticPanel {

    // Constructor

    /**
     * Constructor.
     */
    public InstructionsPanel() {
        super();
    }

    // Methods

    /**
     * Initializes the UI components of the instructions panel. This includes a Return to Menu button
     *and a scrollable pane that displays a styled HTML-based descriptiom of the game rules, objectives,
     * setup, turn structure, and win/lose conditions.
     */
    @Override
    public void initializeUI() {
        setLayout(null);

        // ====================================================================
        // Back Button
        // ====================================================================

        // Set the size and position of the button
        JButton returnMenuButton = new JButton("Return To Menu");
        returnMenuButton.setBounds(50, 620, 150, 40);

        // Add action listener to the button
        buttons.put(ButtonName.RETURN_MENU_BUTTON.toString(), returnMenuButton);
        add(returnMenuButton);

        // ====================================================================
        // Instructions Pane
        // ====================================================================

        // Create a JEditorPane to display the instructions in HTML format
        JEditorPane instructionsPane = new JEditorPane();
        instructionsPane.setContentType("text/html");
        instructionsPane.setEditable(false);
        instructionsPane.setOpaque(false);
        instructionsPane.setFont(new Font("SansSerif", Font.PLAIN, 16));

        // Set the HTML content with game rules and objectives
        instructionsPane.setText(
                "<html><body style='text-align:center; font-family:SansSerif; font-size:14px;'>" +
                        "<h2>Objective</h2>" +
                        "The objective of Santorini is to outmaneuver your opponent by strategically moving and building. " +
                        "You win by moving one of your workers onto the third level of any building, or if your opponent " +
                        "cannot make any legal moves on their turn.<br><br>" +

                        "<h2>Setup</h2>" +
                        "• The game is played on a 5x5 grid board.<br>" +
                        "• Each player controls two workers.<br>" +
                        "• Players take turns placing their workers on any unoccupied spaces during the setup phase.<br><br>" +

                        "<h2>Turn Structure</h2>" +
                        "Each player’s turn consists of two sequential actions: <b>Move</b> and <b>Build</b>.<br><br>" +

                        "<b>Move:</b><br>" +
                        "• Select one of your workers.<br>" +
                        "• Move to any adjacent space (including diagonals).<br>" +
                        "• You may move up by only one level at a time.<br>" +
                        "• You may move down any number of levels.<br>" +
                        "• You cannot move into an occupied space or one with a dome.<br><br>" +

                        "<b>Build:</b><br>" +
                        "• After moving, the same worker must build on an adjacent space.<br>" +
                        "• Build one level higher or place a dome on the third level.<br>" +
                        "• Domed spaces are no longer accessible for movement.<br><br>" +

                        "<h2>Winning the Game</h2>" +
                        "• Win immediately if one of your workers moves onto a third-level space.<br><br>" +

                        "<h2>Losing the Game</h2>" +
                        "• Lose if, on your turn, neither of your workers can move or build.<br><br>"
        );
        instructionsPane.setCaretPosition(0);

        // Create a JScrollPane to make the instructions scrollable
        JScrollPane scrollPane = new JScrollPane(instructionsPane);
        scrollPane.setBounds(140, 40, 800, 560);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        add(scrollPane);

        // ====================================================================
        // Background
        // ====================================================================

        // Load the background image and set it to the panel
        ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/instructions-background.png"));
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setBounds(0, 0, 1080, 720);
        add(backgroundLabel);
        setComponentZOrder(backgroundLabel, getComponentCount() - 1);
    }
}
