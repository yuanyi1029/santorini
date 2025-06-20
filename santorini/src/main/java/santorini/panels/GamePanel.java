package santorini.panels;

import santorini.utils.Logger;
import santorini.board.Board;
import santorini.game.GameState;
import santorini.game.Position;
import santorini.players.Player;
import santorini.players.Worker;
import santorini.towers.Tower;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

/**
 * Represents the main gameplay panel where the board, workers, towers, and game log are displayed.
 * Dynamically updates the UI based on the current game state. Implements ActivePanel since it
 * depends on GameState for rendering and interaction.
 *
 * Created by:
 * author Zi Yang, Yuan Yi
 */
public class GamePanel extends Panel implements ActivePanel {

    // Attributes

    /**
     * Stores the layered panes associated with each board position for rendering towers and workers.
     */
    private HashMap<String, JLayeredPane> panes;

    /**
     * The edge length (in pixels) of each individual game cell.
     */
    private int gameCellEdgeLength;

    // Constructor

    /**
     * Constructor
     */
    public GamePanel() {
        super();
        panes = new HashMap<>();
    }

    // Methods

    /**
     * Initializes the UI components of the game panel based on the provided game state.
     * If the board has not been initialized, it will first create the game grid, information panel,
     * logging area, and action controls. Then, it clears the board and updates the visual state of all towers and workers
     * using the latest game data. This method ensures the UI is always consistent with the current game state
     * and triggers revalidation and repainting to reflect changes on the screen.
     *
     * @param gameState The current game state used to update the game board and related UI elements.
     */
    @Override
    public void initializeUI(GameState gameState) {

        // If the board has not been initialized, create it
        if (panes.isEmpty()) {
            initializeBoard(gameState);
        }

        // Clear the board and update towers and workers
        clearBoard();
        updateTowersUI(gameState);
        updateWorkersUI(gameState);

        revalidate();
        repaint();
    }

    /**
     * Creates and sets up the full board layout, including the player info panel,
     * game grid with layered cells, game log area, and end phase button.
     * This is called once at the start of gameplay.
     *
     * @param gameState The game state used to determine board size and player information.
     */
    public void initializeBoard(GameState gameState) {
        Board board = gameState.getBoard();

        // Set up absolute positioning and panel size
        setLayout(null);
        setBounds(0, 0, 1080, 720);

        // ====================================================================
        // Left Column (Player Info and Game Log)
        // ====================================================================
        JPanel leftColumn = new JPanel(new GridBagLayout());
        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.fill = GridBagConstraints.BOTH;

        // Create player info panel
        JPanel infoPanel = new JPanel(new GridLayout(1, gameState.getPlayers().size()));
        infoPanel.setBackground(new Color(230, 230, 250));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Game Information"));

        // Add each player's name, worker icon, and god power
        for (int i = 0; i < gameState.getPlayers().size(); i++) {
            Player player = gameState.getPlayer(i);

            JPanel playerPanel = new JPanel();
            playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
            playerPanel.setOpaque(false);

            // Player name
            JLabel playerNameLabel = new JLabel(player.getName(), JLabel.CENTER);
            playerNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            playerNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            playerPanel.add(playerNameLabel);

            // Worker icon
            ImageIcon workerIcon = player.getWorkers().get(0).draw();
            Image workerImage = workerIcon.getImage();
            ImageIcon scaledIcon = new ImageIcon(workerImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            JLabel workerLabel = new JLabel(scaledIcon);
            workerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            playerPanel.add(workerLabel);

            // God icon
            if (player.getGod() != null) {
                JLabel godIconLabel = new JLabel(player.getGod().draw());
                godIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                playerPanel.add(godIconLabel);
            }

            infoPanel.add(playerPanel);
        }

        // Add info panel to top half of left column
        leftGbc.gridy = 0;
        leftGbc.weightx = 1.0;
        leftGbc.weighty = 0.5;
        leftColumn.add(infoPanel, leftGbc);

        // Create game log area
        JTextArea logTextArea = new JTextArea();
        logTextArea.setFocusable(false);
        logTextArea.setEditable(false);
        logTextArea.setLineWrap(true);
        logTextArea.setWrapStyleWord(true);
        logTextArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        logTextArea.setBackground(Color.WHITE);

        // Scrollable log container
        JScrollPane scrollPane = new JScrollPane(logTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Game Log"));
        scrollPane.setPreferredSize(new Dimension(100, 100));

        // Register log text area to Logger
        Logger.getInstance().setLogTextArea(logTextArea);

        // Add log to bottom half of left column
        leftGbc.gridy = 1;
        leftGbc.weightx = 1.0;
        leftGbc.weighty = 0.5;
        leftColumn.add(scrollPane, leftGbc);

        // ====================================================================
        // Game Grid
        // ====================================================================

        // Create a container for the game panel
        JPanel gamePanelContainer = new JPanel(null);
        gamePanelContainer.setBounds(324, 0, 756, 620);

        // Main grid layout based on board dimensions
        JPanel gamePanel = new JPanel(new GridLayout(board.getWidth(), board.getHeight(), 5, 5));
        gamePanel.setBackground(Color.WHITE);
        int xCells = board.getWidth();
        int yCells = board.getHeight();

        // calculate the size of each cell
        int horizontalLength = (620 - (xCells - 1) * 5) / xCells;
        int verticalLength = (620 - (yCells - 1) * 5) / yCells;
        gameCellEdgeLength = Math.min(horizontalLength, verticalLength);

        // Create each grid cell using JLayeredPane
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Position position = new Position(x, y);

                // Setup layered pane for individual cell
                JLayeredPane pane = new JLayeredPane();
                pane.setPreferredSize(new Dimension(gameCellEdgeLength, gameCellEdgeLength));

                // Base button for cell interaction
                JButton button = new JButton();
                button.setBounds(0, 0, gameCellEdgeLength, gameCellEdgeLength);
                button.setEnabled(false);
                button.putClientProperty("x", x);
                button.putClientProperty("y", y);

                // Store the pane and button in respective maps
                panes.put(position.toString(), pane);
                buttons.put(position.toString(), button);

                pane.add(button, JLayeredPane.DEFAULT_LAYER);
                gamePanel.add(pane);
            }
        }

        // Position the board inside the container
        gamePanel.setBounds(68, 0, 620, 620);
        gamePanelContainer.add(gamePanel);

        // Combine left column and board into a split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftColumn, gamePanelContainer);
        splitPane.setDividerLocation(324);
        splitPane.setEnabled(false);
        splitPane.setDividerSize(0);
        splitPane.setBounds(0, 0, 1080, 620);
        add(splitPane);

        // ====================================================================
        // Bottom Panel (End Phase Button)
        // ====================================================================
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton saveGameButton = new JButton("Save Game");
        buttons.put(ButtonName.SAVE_GAME_BUTTON.toString(), saveGameButton);

        JButton endPhaseButton = new JButton("End Phase");
        endPhaseButton.setEnabled(false); // Start disabled
        buttons.put(ButtonName.END_PHASE_BUTTON.toString(), endPhaseButton);

        bottomPanel.add(saveGameButton);
        bottomPanel.add(endPhaseButton);
        bottomPanel.setBounds(0, 620, 1080, 100);

        add(bottomPanel);
    }


    /**
     * Updates the tower visuals on the board by redrawing the top floor of each tower
     * at every board position using the draw method.
     *
     * @param gameState The current game state.
     */
    public void updateTowersUI(GameState gameState) {
        Board board = gameState.getBoard();

        // Iterate through each position on the board
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Position position = new Position(x, y);
                Tower tower = board.getTower(position);

                // Create JLabel
                JLabel towerLabel = new JLabel(tower.draw(board, position));

                // Set the label bounds to fit the cell
                towerLabel.setBounds(0, 0, gameCellEdgeLength, gameCellEdgeLength);

                // Add the tower label to the layered pane at this board position
                panes.get(position.toString()).add(towerLabel, JLayeredPane.PALETTE_LAYER);
            }
        }
    }

    /**
     * Updates the worker icons on the board. Each player's workers are scaled to fit within
     * their corresponding cell and rendered on top of tower components.
     *
     * @param gameState The current game state.
     */
    public void updateWorkersUI(GameState gameState) {
        // Get the game board and list of players from the game state
        Board board = gameState.getBoard();
        List<Player> players = gameState.getPlayers();

        // Iterate through each player
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);

            // Get the worker icon from the worker
            ImageIcon workerIcon =  player.getWorkers().get(0).draw();
            Image workerImage = workerIcon.getImage();

            // Scale the worker icon to half the size of the cell
            ImageIcon scaledWorkerIcon = new ImageIcon(workerImage.getScaledInstance(gameCellEdgeLength/2, gameCellEdgeLength/2, Image.SCALE_SMOOTH));

            // Add a label for each of the player's workers
            for (Worker worker : players.get(i).getWorkers()) {
                Position position = board.getPositionOf(worker);

                // Create JLabel
                JLabel workerLabel = new JLabel(scaledWorkerIcon);

                // Center the worker label within the cell
                workerLabel.setBounds((gameCellEdgeLength - gameCellEdgeLength / 2) / 2, (gameCellEdgeLength - gameCellEdgeLength / 2) / 2, gameCellEdgeLength/2, gameCellEdgeLength/2);

                // Add the worker to the top layer of the corresponding layered pane
                panes.get(position.toString()).add(workerLabel, JLayeredPane.DRAG_LAYER);
            }
        }
    }

    /**
     * Clears all tower and worker visuals from the board by removing components
     * from the appropriate layers in each cell's layered pane.
     */
    public void clearBoard() {

        // Iterate through each position on the board and remove each component
        for (JLayeredPane pane: panes.values()) {
            for (Component c : pane.getComponentsInLayer(JLayeredPane.PALETTE_LAYER)) {
                pane.remove(c);
            }
            for (Component c : pane.getComponentsInLayer(JLayeredPane.DRAG_LAYER)) {
                pane.remove(c);
            }
        }
    }
}