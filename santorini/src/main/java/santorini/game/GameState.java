package santorini.game;

import santorini.board.Board;
import santorini.players.*;
import santorini.utils.SaveConfig;
import santorini.utils.SaveManager;
import santorini.utils.Saveable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Represents the current state of the game at any point in time.
 * Stores the board, players, turn management, action counters,
 * phase tracking, and recent worker positions to support game logic.
 *
 * Created by:
 * author Yuan Yi, Zi Yang, Diana, Hsu Chyi
 */
public class GameState implements Saveable {

    // Attributes

    /**
     * The game board containing towers and worker placements.
     */
    private Board board;
    /**
     * The list of players in the game.
     */
    private List<Player> players;
    /**
     * The current turn number of the game.
     */
    private int turnNumber;
    /**
     * The index of the current player in the players list.
     */
    private int currentPlayerIndex;
    /**
     * The player who has won the game, if any.
     */
    private Player winner;
    /**
     * The current phase of the game (e.g., MOVE, BUILD).
     */
    private GamePhase gamePhase;
    /**
     * The condition whether the current player has moved yet.
     */
    private boolean hasMoved;
    /**
     * The condition whether the current player has built yet.
     */
    private boolean hasBuilt;
    /**
     * The number of moves remaining for the current player.
     */
    private int movesRemaining;
    /**
     * The number of builds remaining for the current player.
     */
    private int buildsRemaining;
    /**
     * The original position of the worker before any moves.
     */
    private Position originalWorkerPosition;
    /**
     * The last position where a worker has built.
     */
    private Position lastWorkerBuildPosition;
    /**
     * The last position where a worker moved to.
     */
    private Position lastWorkerMovePosition;
    /**
     * The worker that is currently selected for action.
     */
    private Worker selectedWorker;

    // Constructor

    /**
     * Constructor.
     *
     * @param board The initial game board.
     * @param players The list of players participating.
     * @param currentPlayerIndex The index of the starting player.
     */
    public GameState(Board board, List<Player> players, int currentPlayerIndex) {
        this.board = board;
        this.players = players;
        this.turnNumber = 0;
        this.currentPlayerIndex = currentPlayerIndex;
        this.winner = null;
        resetMovesAndBuilds();
        resetPreviousPositions();

        SaveManager.getInstance().registerSaveable(this);
    }

    /**
     * Constructor for loading a game state.
     *
     * @param buildString The string to be parsed as a game state.
     */
    public GameState(String buildString) {
        HashMap<String, String> innerBuildStringMap = new HashMap<>();
        String[] lines = buildString.split(SaveConfig.NEWLINE_REGEX);
        String[] firstLine = lines[0].trim().split(SaveConfig.DELIMITER_REGEX);
        int startingIndex = 0;

        while (startingIndex < lines.length) {
            if (lines[startingIndex].startsWith(SaveConfig.INNER_KEY)) {
                StringBuilder builder = new StringBuilder();

                String key = lines[startingIndex].substring(1).trim();
                startingIndex++;

                while (startingIndex < lines.length && !lines[startingIndex].startsWith(SaveConfig.INNER_KEY)) {
                    builder.append(lines[startingIndex]);
                    builder.append(SaveConfig.NEWLINE);
                    startingIndex++;
                }

                innerBuildStringMap.put(key, builder.toString());
            } else {
                startingIndex++;
            }
        }

        String boardString = innerBuildStringMap.get(Board.class.getName());
        String playerString = innerBuildStringMap.get(Player.class.getName());

        String[] playerLines = playerString.split(SaveConfig.NEWLINE_REGEX);
        String[] playerConfigString = playerLines[0].trim().split(SaveConfig.DELIMITER_REGEX);

        int numberOfPlayers = Integer.parseInt(playerConfigString[0]);
        int numberOfWorkers = Integer.parseInt(playerConfigString[1]);

        this.board = new Board(boardString);
        this.players = new ArrayList<>();

        for (int i = 0; i < numberOfPlayers; i++) {
            int playerLineIndex = (i * (numberOfWorkers + 1)) + 1;
            String playerData = playerLines[playerLineIndex];

            Player player = new Player(playerData, true);
            for (int j = 0; j < numberOfWorkers; j++) {
                String positionLine = playerLines[playerLineIndex + j + 1];
                Worker worker = new Worker();
                player.addWorker(worker);

                Position position = new Position(positionLine);
                board.addWorker(position, worker);
            }
            player.initialiseWorkerIcon(i);
            this.players.add(player);
        }

        this.turnNumber = Integer.parseInt(firstLine[0]);
        this.currentPlayerIndex = Integer.parseInt(firstLine[1]);
        this.winner = null;
        resetMovesAndBuilds();
        resetPreviousPositions();

        SaveManager.getInstance().registerSaveable(this);
    }

    // Getters and Setters

    /**
     * Gets the game board.
     *
     * @return The current game board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Sets the game board.
     *
     * @param board The new game board.
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Gets the list of players.
     *
     * @return The list of players.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Sets the list of players.
     *
     * @param players The new list of players.
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    /**
     * Gets a specific player by index.
     *
     * @param index The index of the player.
     * @return The player at the specified index.
     */
    public Player getPlayer(int index) {
        return players.get(index);
    }

    /**
     * Gets the current turn number.
     *
     * @return The current turn number.
     */
    public int getTurnNumber() {
        return turnNumber;
    }

    /**
     * Sets the current turn number.
     *
     * @param turnNumber The new turn number.
     */
    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    /**
     * Gets the index of the current player.
     *
     * @return The index of the current player.
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Sets the index of the current player.
     *
     * @param currentPlayerIndex The new index of the current player.
     */
    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    /**
     * Gets the current player.
     *
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Gets the winner of the game.
     *
     * @return The winning player, or null if no winner is set.
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * Sets the winner of the game.
     *
     * @param winner The player who won the game.
     */
    public void setWinner(Player winner) {
        this.winner = winner;
    }

    /**
     * Gets the current game phase.
     *
     * @return The current game phase.
     */
    public GamePhase getGamePhase() {
        return gamePhase;
    }

    /**
     * Sets the current game phase.
     *
     * @param gamePhase The new game phase.
     */
    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    /**
     * Gets the status whether the current player has moved.
     *
     * @return The boolean status whether the current player has moved.
     */
    public boolean getHasMoved() {
        return hasMoved;
    }

    /**
     * Sets whether the current player has moved.
     *
     * @param hasMoved The new boolean status whether the current player has moved.
     */
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    /**
     * Gets the status whether the current player has built.
     *
     * @return The boolean status whether the current player has built.
     */
    public boolean getHasBuilt() {
        return hasBuilt;
    }

    /**
     * Sets whether the current player has built.
     *
     * @param hasBuilt The new boolean status whether the current player has built.
     */
    public void setHasBuilt(boolean hasBuilt) {
        this.hasBuilt = hasBuilt;
    }

    /**
     * Gets the number of moves remaining for the current player.
     *
     * @return The number of moves remaining.
     */
    public int getMovesRemaining() {
        return movesRemaining;
    }

    /**
     * Sets the number of moves remaining for the current player.
     *
     * @param movesRemaining The new number of moves remaining.
     */
    public void setMovesRemaining(int movesRemaining) {
        this.movesRemaining = movesRemaining;
    }

    /**
     * Gets the number of builds remaining for the current player.
     *
     * @return The number of builds remaining.
     */
    public int getBuildsRemaining() {
        return buildsRemaining;
    }

    /**
     * Sets the number of builds remaining for the current player.
     *
     * @param buildsRemaining The new number of builds remaining.
     */
    public void setBuildsRemaining(int buildsRemaining) {
        this.buildsRemaining = buildsRemaining;
    }


    /**
     * Gets the original position of the worker before any moves.
     *
     * @return The original worker position.
     */
    public Position getOriginalWorkerPosition() {
        return originalWorkerPosition;
    }

    /**
     * Sets the original position of the worker before any moves.
     *
     * @param lastWorkerPosition The new original worker position.
     */
    public void setOriginalWorkerPosition(Position lastWorkerPosition) {
        this.originalWorkerPosition = lastWorkerPosition;
    }

    /**
     * Gets the last position where a worker moved to.
     *
     * @return The last worker moved position.
     */
    public Position getLastWorkerMovePosition() {
        return lastWorkerMovePosition;
    }

    /**
     * Sets the last position where a worker moved to.
     *
     * @param lastWorkerMovePosition The new last worker moved position.
     */
    public void setLastWorkerMovePosition(Position lastWorkerMovePosition) {
        this.lastWorkerMovePosition = lastWorkerMovePosition;
    }

    /**
     * Gets the last position where a worker has built.
     *
     * @return The last worker build position.
     */
    public Position getLastWorkerBuildPosition() {
        return lastWorkerBuildPosition;
    }

    /**
     * Sets the last position where a worker has built.
     *
     * @param lastWorkerBuildPosition The new last worker build position.
     */
    public void setLastWorkerBuildPosition(Position lastWorkerBuildPosition) {
        this.lastWorkerBuildPosition = lastWorkerBuildPosition;
    }

    /**
     * Gets the selected worker for action.
     *
     * @return The currently selected worker.
     */
    public Worker getSelectedWorker() {
        return selectedWorker;
    }

    /**
     * Sets the selected worker for action.
     *
     * @param selectedWorker The new selected worker.
     */
    public void setSelectedWorker(Worker selectedWorker) {
        this.selectedWorker = selectedWorker;
    }

    // Methods

    /**
     * Increases the current turn number by 1
     */
    public void increaseTurnNumber() {
        setTurnNumber(getTurnNumber() + 1);
    }

    /**
     * Gets whether the turn number has looped back to the first player
     *
     * @return The boolean status whether the turn number has looped back.
     */
    public boolean getHasTurnNumberLooped() {
        return getTurnNumber() % getPlayers().size() == 0;
    }

    /**
     * Gets the actual turn number of the game, which is the turn number divided by
     * the number of players
     *
     * @return The actual turn number of the game.
     */
    public int getActualTurnNumber() {
        return getTurnNumber() / getPlayers().size();
    }

    /**
     * Decreases the number of moves remaining of the current player
     */
    public void decreaseMovesRemaining() {
        setMovesRemaining(getMovesRemaining() - 1);
        if (!hasMoved) {
            hasMoved = true;
        }
    }
    /**
     * Increases the number of moves remaining of the current player
     */
    public void increaseMovesRemaining() {
        setMovesRemaining(getMovesRemaining() + 1);
    }

    /**
     * Decreases the number of builds remaining of the current player
     */
    public void decreaseBuildsRemaining() {
        setBuildsRemaining(getBuildsRemaining() - 1);
        if (!hasBuilt) {
            hasBuilt = true;
        }
    }

    /**
     * Increases the number of builds remaining of the current player
     */
    public void increaseBuildsRemaining() {
        setBuildsRemaining(getBuildsRemaining() + 1);
    }

    /**
     * Resets the moves and builds for the current player based on their god's abilities.
     */
    public void resetMovesAndBuilds() {
        this.gamePhase = GamePhase.MOVE;
        this.hasMoved = false;
        this.hasBuilt = false;
        this.movesRemaining = 1;
        this.buildsRemaining = 1;
    }

    /**
     * Resets the previous positions of the workers.
     */
    public void resetPreviousPositions() {
        this.originalWorkerPosition = null;
        this.lastWorkerBuildPosition = null;
        this.lastWorkerMovePosition = null;
    }

    /**
     * Advances the turn to the next player in the list.
     */
    public void advanceTurn() {
        this.currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    @Override
    public String save() {
        StringBuilder builder = new StringBuilder();

        // Save turn number & current player index
        builder.append(turnNumber).append(SaveConfig.DELIMITER);
        builder.append(currentPlayerIndex).append(SaveConfig.NEWLINE);

        // Save board
        builder.append(SaveConfig.INNER_KEY).append(Board.class.getName()).append(SaveConfig.NEWLINE);
        builder.append(board.save());

        // Save players
        builder.append(SaveConfig.INNER_KEY).append(Player.class.getName()).append(SaveConfig.NEWLINE);
        builder.append(players.size()).append(SaveConfig.DELIMITER).append(players.get(0).getWorkers().size()).append(SaveConfig.NEWLINE);

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            builder.append(player.save()).append(SaveConfig.NEWLINE);

            List<Worker> workers = player.getWorkers();
            for (int j = 0; j < workers.size(); j++) {
                Position position = board.getPositionOf(workers.get(j));
                builder.append(position.save()).append(SaveConfig.NEWLINE);
            }
        }

        return builder.toString();
    }
}