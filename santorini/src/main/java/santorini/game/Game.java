package santorini.game;

import santorini.game.modifier.ChaosModifier;
import santorini.game.modifier.GameModifier;
import santorini.utils.Logger;
import santorini.actions.Action;
import santorini.board.Board;
import santorini.board.MovementValidator;
import santorini.players.Player;
import santorini.players.Worker;
import santorini.utils.SaveConfig;
import santorini.utils.SaveManager;
import santorini.utils.Saveable;

import java.util.ArrayList;
import java.util.List;
/**
 * The core singleton class representing the active game controller.
 * Manages the game state, turn progression, win conditions, phase transitions, and subscriber updates.
 * Acts as the central interface for game logic execution.
 *
 * Created by:
 * author Yuan Yi, Zi Yang, Diana, Hsu Chyi
 */
public class Game implements Saveable {

    // Constants

    /**
     * Singleton instance
     */
    private static Game instance;

    /**
     * The current game state
     */
    private GameState gameState;

    /**
     * The list of subscribers to be notified of game state changes
     */
    private List<Subscriber> subscribers;

    /**
     * The movement validator for checking valid moves
     */
    private MovementValidator movementValidator;

    /**
     * The game modifier of a specific game
     */
    private GameModifier modifier;

    // Constructor

    /**
     * Constructor.
     */
    public Game() {
        this.subscribers = new ArrayList<>();
        SaveManager.getInstance().registerSaveable(this);
    }

    /**
     * Constructor for loading a game.
     *
     * @param buildString The string to be parsed as a game.
     */
    public Game(String buildString) {
        Game.getInstance().setModifier(buildString);
    }

    // Methods

    /**
     * Returns the singleton instance of the Game class.
     *
     * @return the Game instance
     */
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    // Getters and Setters

    /**
     * Returns the game state.
     *
     * @return the game state
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Sets the game state and initializes the movement validator.
     *
     * @param gameState the new game state
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        this.movementValidator = new MovementValidator(gameState.getBoard());
        updateSubscribers();
    }

    /**
     * Sets the game modifier of a game.
     *
     * @param modifier The game modifier of a game.
     */
    public void setModifier(GameModifier modifier) {
        this.modifier = modifier;
    }

    /**
     * Sets the game modifier of a game based on a string, used for loading
     * in a game modifier to the game.
     *
     * @param buildString The name of a modifier as a string.
     */
    public void setModifier(String buildString) {
        String className = buildString.split("\\.")[3];
        setModifier(GameModifier.parseName(className));
    }

    /**
     * Gets the list of subscribers.
     *
     * @return the list of subscribers
     */
    public List<Subscriber> getSubscribers() {
        return subscribers;
    }

    /**
     * Sets the list of subscribers.
     *
     * @param subscribers the new list of subscribers
     */
    public void setSubscribers(List<Subscriber> subscribers) {
        this.subscribers = subscribers;
    }

    /**
     * Adds a subscriber to the list of subscribers.
     *
     * @param subscriber the subscriber to add
     */
    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * Removes a subscriber from the list of subscribers.
     *
     * @param subscriber the subscriber to remove
     */
    public void removeSubscriber(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    /**
     * Updates all subscribers with the current game state.
     *
     * This method is called whenever the game state changes. All subscribers
     * are notified to update their views or perform any necessary actions.
     */
    public void updateSubscribers() {
        for (Subscriber subscriber : subscribers) {
            subscriber.update(gameState);
        }
    }

    /**
     * Processes a player's turn by executing the given action with the specified worker.
     * Updates the game state accordingly and checks for win conditions.
     *
     * @param player the player whose turn it is
     * @param worker the worker to perform the action with
     * @param action the action to execute
     */
    public void processTurn(Player player, Worker worker, Action action) {
        // Check if the player and action are not null
        if (worker != null && action != null) {
            boolean result = executeAction(player, worker, action);
            if (result) {

                boolean powerResult = player.getGod().executePower(gameState, action);

                // If in the move phase, decrement moves remaining
                if (gameState.getGamePhase() == GamePhase.MOVE) {
                    gameState.decreaseMovesRemaining();
                    Logger.getInstance().log("Moves Remaining: " + gameState.getMovesRemaining());
                }

                // If in the build phase, decrement builds remaining
                else if (gameState.getGamePhase() == GamePhase.BUILD) {
                    gameState.decreaseBuildsRemaining();
                    Logger.getInstance().log("Builds Remaining: " + gameState.getBuildsRemaining());
                }
            }
        }

        boolean endTurn = false;

        // If the game phase is MOVE and moves remaining is 0, switch to BUILD phase
        if (gameState.getMovesRemaining() == 0 && gameState.getBuildsRemaining() > 0) {
            gameState.setGamePhase(GamePhase.BUILD);
            Logger.getInstance().log("Build Phase Now");
        }

        // If the game phase is BUILD and builds remaining is 0, end the turn
        if (gameState.getMovesRemaining() == 0 && gameState.getBuildsRemaining() == 0) {
            endTurn = true;
        }

        // If the turn is ending, advance to the next player
        if (endTurn) {
            do {
                gameState.advanceTurn();
            } while (!isPlayerCanMove(gameState.getCurrentPlayer()));
            gameState.resetMovesAndBuilds();
            gameState.resetPreviousPositions();
            gameState.getCurrentPlayer().resetTurn();
            gameState.increaseTurnNumber();
            if (gameState.getHasTurnNumberLooped()) {
                Logger.getInstance().log(String.format("Turn %d", gameState.getActualTurnNumber() + 1));
            }
            Logger.getInstance().log(String.format("%s's Turn", gameState.getCurrentPlayer().toString()));
        }

        // Check if there is a winner after processing the turn
        Player winner = determineWinner();
        if (winner != null) {
            gameState.setWinner(winner);
        }

        if (endTurn && modifier != null) {
            modifier.executeModifier(gameState.getBoard(), gameState.getTurnNumber());
        }

        // Update all subscribers with the new game state
        updateSubscribers();
    }

    /**
     * Method to process a passive state check and trigger win check.
     */
    public void processTurn() {
        Player winner = determineWinner();

        // If a winner is found, set the winner in the game state
        if (winner != null) {
            gameState.setWinner(winner);
        }

        // Update all subscribers with the new game state
        updateSubscribers();
    }

    /**
     * Executes a game action and updates the board state.
     *
     * @param player The player performing the action.
     * @param worker The worker involved.
     * @param action The action to execute.
     * @return true if the action succeeded; false otherwise.
     */
    public boolean executeAction(Player player, Worker worker, Action action) {
        Board board = gameState.getBoard();
        boolean result = action.execute(player, worker, board);
        gameState.setBoard(board);
        return result;
    }

    /**
     * Evaluates all players and game state to determine if a win condition has been met.
     *
     * @return The winning player if found, null otherwise.
     */
    public Player determineWinner() {
        List<Player> players = new ArrayList<>(gameState.getPlayers());
        Worker selectedWorker = gameState.getSelectedWorker();
        Player currentPlayer = gameState.getCurrentPlayer();

        // Check if the current player has reached the top
        for (Player player : players) {
            if (isPlayerReachedTop(player)) {
                return player;
            }
        }

        List<Player> moveablePlayers = new ArrayList<>();

        // Check if the current player can move
        for (Player player : players) {
            if (isPlayerCanMove(player)) {
                moveablePlayers.add(player);
            }
        }

        // If only one player can move, they are the winner
        if (moveablePlayers.size() == 1) {
            return moveablePlayers.get(0);
        }

        // Check if the selected worker can move
        if (!isWorkerCanMove(selectedWorker)) {
            for (Player player : players) {
                if (!player.equals(currentPlayer)) {
                    return player;
                }
            }
        }

        // If both players can still move, no winner yet
        return null;
    }

    /**
     * Returns the current player.
     *
     * @return the current player
     */
    public Player getWinner() {
        return gameState.getWinner();
    }

    /**
     * Checks if the player has reached the top of the board.
     *
     * @param player The player to check.
     * @return true if the player has reached the top, false otherwise.
     */
    public boolean isPlayerReachedTop(Player player) {
        Board board = gameState.getBoard();

        // Loop through each worker of the player, checking if any are at the third level
        for (Worker worker : player.getWorkers()) {
            if (board.getHeightOf(worker) == 3) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the player can move.
     *
     * @param player The player to check.
     * @return true if the player can move, false otherwise.
     */
    public boolean isPlayerCanMove(Player player) {

        // Loop through each worker of the player, checking if any can move
        for (Worker worker : player.getWorkers()) {
            if (isWorkerCanMove(worker)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the worker can move.
     *
     * @param worker The worker to check.
     * @return true if the worker can move, false otherwise.
     */
    public boolean isWorkerCanMove(Worker worker) {
        Board board = gameState.getBoard();
        List<Position> moveablePositions = movementValidator.getMoveablePositions(board.getPositionOf(worker));

        // Check if the worker can move
        if (!moveablePositions.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Forces the current phase to end (used to skip remaining moves/builds).
     * Transitions to build phase or ends the turn depending on current state.
     */
    public void forceEndPhase() {

        // If the game is in move and there are moves remaining, set moves to 0 and switch to build phase
        if (gameState.getGamePhase() == GamePhase.MOVE && gameState.getMovesRemaining() > 0) {
            gameState.setMovesRemaining(0);
            gameState.setGamePhase(GamePhase.BUILD);
            Logger.getInstance().log("Force End Move Phase. Now Build Phase.");
        }

        // If the game is in build and there are builds remaining, set builds to 0 and end the turn
        else if (gameState.getGamePhase() == GamePhase.BUILD && gameState.getBuildsRemaining() > 0) {
            gameState.setBuildsRemaining(0);
            Logger.getInstance().log("Force End Build Phase. Ending Turn.");
        }

        updateSubscribers();
    }

    @Override
    public String save() {
        StringBuilder builder = new StringBuilder();

        builder.append(SaveConfig.INNER_KEY).append(modifier.getClass().getName()).append(SaveConfig.NEWLINE);

        return builder.toString();
    }
}