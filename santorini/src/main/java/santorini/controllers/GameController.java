package santorini.controllers;

import santorini.actions.BuildAction;
import santorini.actions.MoveAction;
import santorini.board.Board;
import santorini.board.MovementValidator;
import santorini.frames.PanelManager;
import santorini.game.*;
import santorini.panels.ButtonName;
import santorini.panels.GamePanel;
import santorini.panels.PanelName;
import santorini.players.Player;
import santorini.players.Worker;
import santorini.utils.SaveManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * The GameController class manages gameplay interactions for the Santorini game.
 * Handles player actions, UI updates, turn progression, and communicates with the Game model.
 * Extends the generic Controller for GamePanel and implements the Subscriber interface.
 *
 * Created by:
 * author Lai Zi Yang, Diana Wijaya, See Hsu Chyi, Wong Yuan Yi
 */
public class GameController extends Controller<GamePanel> implements Subscriber {

    // Attributes

    /**
     * The current state of the game.
     */
    private GameState gameState;
    /**
     * A list of active positions available for move or build actions.
     */
    private List<Position> activeActionPositions;
    /**
     * A list of active worker positions for the current player.
     */
    private List<Position> activeWorkerPositions;

    // Constructor

    /**
     * Constructor.
     *
     * @param panelManager The PanelManager to switch between game panels.
     */
    public GameController(PanelManager panelManager) {
        super(panelManager);
        panel = new GamePanel();
        activeActionPositions = new ArrayList<>();
        activeWorkerPositions = new ArrayList<>();
    }

    // Methods

    /**
     * Updates the controller with the latest GameState and initializes the UI.
     *
     * @param gameState The current game state to use for rendering and logic.
     */
    @Override
    public void update(GameState gameState) {
        this.gameState = gameState;
        initializePanelUI();
    }

    /**
     * Initializes the UI of the GamePanel and sets up the end phase button and worker selection.
     */
    @Override
    public void initializePanelUI() {
        panel.initializeUI(gameState);
        setupEndPhaseButton();
        setupSaveListener();

        // Only allow worker selection at the start of a turn
        if (gameState.getCurrentPlayer().canSelectWorker()) {
            panel.enableButton(ButtonName.SAVE_GAME_BUTTON.toString());
            panel.disableButton(ButtonName.END_PHASE_BUTTON.toString());
            setupWorkerListener();
        }
    }

    public void setupSaveListener() {
        panel.removeListener(ButtonName.SAVE_GAME_BUTTON.toString());
        ActionListener actionListener = e -> {
            SaveManager.getInstance().saveGame(panel);
        };
        panel.addListener(ButtonName.SAVE_GAME_BUTTON.toString(), actionListener);
    }

    /**
     * Sets up listeners for the current player's workers, allowing them to be selected.
     */
    public void setupWorkerListener() {
        Board board = gameState.getBoard();
        Player currentPlayer = gameState.getCurrentPlayer();
        currentPlayer.setCanSelectWorker(false);
        List<Worker> workers = currentPlayer.getWorkers();

        clearActiveWorkerListeners();
        activeWorkerPositions.clear();

        // Setup action listeners for each workers of the player
        for (Worker worker : workers) {
            Position position = board.getPositionOf(worker);

            ActionListener actionListener = e -> {
                // Set the selected worker in the game state
                gameState.setSelectedWorker(worker);

                Game.getInstance().processTurn();
                if (gameState.getWinner() != null) {
                    switchPanel(PanelName.WINNER.toString());
                    return;
                }

                // Disable all worker buttons
                clearActiveWorkerListeners();
                panel.disableButton(ButtonName.SAVE_GAME_BUTTON.toString());

                // Proceed to the movement phase
                setupMoveListener(position);
            };

            // Assign listeners
            activeWorkerPositions.add(position);
            panel.addListener(position.toString(), actionListener);

            // Highlight worker buttons
            AbstractButton workerButton = panel.getButton(position.toString());
            workerButton.setBackground(Color.YELLOW);
            workerButton.setEnabled(true);
            workerButton.setOpaque(true);
        }
    }

    /**
     * Sets up listeners on valid positions the worker can move to.
     *
     * @param position The position of the worker before moving.
     */
    public void setupMoveListener(Position position) {
        Board board = gameState.getBoard();
        Player currentPlayer = gameState.getCurrentPlayer();
        Worker currentWorker = board.getWorkerAt(position);

        MovementValidator validator = new MovementValidator(board);
        List<Position> adjacentPositions = validator.getMoveablePositions(position);

        // Declare winner if there are no available moves
        if (adjacentPositions.isEmpty() && gameState.getWinner() != null) {
            reset();
            switchPanel(PanelName.WINNER.toString());
            return;
        }

        // Prevent moving back to the original position (for gods with multiple moves)
        adjacentPositions.removeIf(adjacentPosition -> adjacentPosition.equals(gameState.getOriginalWorkerPosition()));

        for (Position adjacentPosition : adjacentPositions) {
            ActionListener actionListener = e -> {
                MoveAction action = new MoveAction(adjacentPosition);

                clearActiveWorkerListeners();
                clearActiveActionListeners();
                Game.getInstance().processTurn(currentPlayer, currentWorker, action);
                if (gameState.getWinner() != null) {
                    reset();
                    switchPanel(PanelName.WINNER.toString());
                    return;
                }

                gameState.setOriginalWorkerPosition(position);
                gameState.setLastWorkerMovePosition(adjacentPosition);

                // Setup move or build listeners depending on the moves remaining
                if (gameState.getMovesRemaining() > 0) {
                    setupMoveListener(adjacentPosition);
                }
                else {
                    setupBuildListener(adjacentPosition);
                }

                checkEndPhaseButton();
            };

            // Assign listeners
            activeActionPositions.add(adjacentPosition);
            panel.addListener(adjacentPosition.toString(), actionListener);

            // Highlight move buttons
            AbstractButton moveButton = panel.getButton(adjacentPosition.toString());
            moveButton.setBackground(Color.GREEN);
            moveButton.setEnabled(true);
            moveButton.setOpaque(true);
        }
    }

    /**
     * Sets up listeners on valid positions for the worker to build.
     *
     * @param position The position of the worker before building.
     */
    public void setupBuildListener(Position position) {
        Board board = gameState.getBoard();
        Player currentPlayer = gameState.getCurrentPlayer();
        Worker currentWorker = board.getWorkerAt(position);

        MovementValidator validator = new MovementValidator(board);
        List<Position> adjacentPositions = validator.getBuildablePositions(position);

        if (adjacentPositions.isEmpty() && gameState.getWinner() != null) {
            reset();
            switchPanel(PanelName.WINNER.toString());
            return;
        }

        adjacentPositions.removeIf(adjacentPosition -> adjacentPosition.equals(gameState.getLastWorkerBuildPosition()));

        for (Position adjacentPosition : adjacentPositions) {
            ActionListener actionListener = e -> {
                BuildAction action = new BuildAction(adjacentPosition);

                clearActiveActionListeners(); // Clear current player's possible builds
                gameState.setLastWorkerBuildPosition(adjacentPosition);
                Game.getInstance().processTurn(currentPlayer, currentWorker, action);
                if (gameState.getHasBuilt() && gameState.getBuildsRemaining() > 0) {
                    setupBuildListener(position);
                }
                if (gameState.getWinner() != null) {
                    reset();
                    switchPanel(PanelName.WINNER.toString());
                    return;
                }

                checkEndPhaseButton();
            };

            // Assign listeners
            activeActionPositions.add(adjacentPosition);
            panel.addListener(adjacentPosition.toString(), actionListener);

            // Highlight build buttons
            AbstractButton buildButton = panel.getButton(adjacentPosition.toString());
            buildButton.setBackground(Color.BLUE);
            buildButton.setEnabled(true);
            buildButton.setOpaque(true);
        }
    }

    /**
     * Enables or disables the End Phase button depending on remaining moves/builds.
     */
    private void checkEndPhaseButton() {
        Player currentPlayer = gameState.getCurrentPlayer();
        if (gameState.getGamePhase() == GamePhase.MOVE) {
            if (gameState.getHasMoved()) {
                panel.enableButton(ButtonName.END_PHASE_BUTTON.toString());
            } else {
                panel.disableButton(ButtonName.END_PHASE_BUTTON.toString());
            }
        }
        else if (gameState.getGamePhase() == GamePhase.BUILD) {
            if (gameState.getHasBuilt()) {
                panel.enableButton(ButtonName.END_PHASE_BUTTON.toString());
            } else {
                panel.disableButton(ButtonName.END_PHASE_BUTTON.toString());
            }
        }
    }

    /**
     * Clears all listeners and highlights for action buttons.
     */
    public void clearActiveActionListeners() {
        for (Position position : activeActionPositions) {
            panel.removeListener(position.toString());

            AbstractButton button = panel.getButton(position.toString());
            button.setBackground(null);
            button.setEnabled(false);
        }
    }

    /**
     * Clears all listeners and highlights for worker buttons.
     */
    public void clearActiveWorkerListeners() {
        for (Position position : activeWorkerPositions) {
            panel.removeListener(position.toString());

            AbstractButton button = panel.getButton(position.toString());
            button.setBackground(null);
            button.setEnabled(false);
        }
    }

    /**
     * Sets up the End Phase button with a listener to advance game phase or turn.
     */
    public void setupEndPhaseButton() {
        panel.removeListener(ButtonName.END_PHASE_BUTTON.toString());
        ActionListener actionListener = e -> {
            clearActiveActionListeners();
            clearActiveWorkerListeners();

            Game game = Game.getInstance();
            GameState state = game.getGameState();

            if (state.getGamePhase() == GamePhase.MOVE) {
                game.forceEndPhase();
                Position lastPosition = state.getLastWorkerMovePosition();
                setupBuildListener(lastPosition);
            }
            else if (state.getGamePhase() == GamePhase.BUILD) {
                game.forceEndPhase();
                game.processTurn(null, null, null); // End turn
            }
        };

        panel.addListener(ButtonName.END_PHASE_BUTTON.toString(), actionListener);
        panel.disableButton(ButtonName.END_PHASE_BUTTON.toString());
    }

    /**
     * Resets the controller state by clearing listeners and active positions.
     */
    public void reset() {
        clearActiveActionListeners();
        clearActiveWorkerListeners();
        activeActionPositions.clear();
        activeWorkerPositions.clear();
    }
}