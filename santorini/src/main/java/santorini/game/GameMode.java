package santorini.game;

import santorini.game.modifier.ChaosModifier;
import santorini.game.modifier.GameModifier;
import santorini.game.modifier.StandardModifier;

/**
 * Enum representing the configuration for different game modes.
 * Each game mode defines the board size, number of players, number of workers per player,
 * and the starting player index.
 *
 * Currently, only the STANDARD mode is included
 *
 * Created by:
 * author Diana, Yuan Yi
 */
public enum GameMode {

    /**
     * Standard game mode with a 5x5 board, 2 players, 2 workers per player, player 0 as the
     * starting player, and a standard game modifier
     */
    STANDARD(5, 5, 2, 2, 0, new StandardModifier()),

    /**
     * Chaos game mode with a 5x5 board, 2 players, 2 workers per player, player 0 as the
     * starting player, and a chaos game modifier
     */
    CHAOS(5, 5, 2, 2, 0, new ChaosModifier());

    // Attributes

    private final int boardWidth;
    private final int boardHeight;
    private final int numberOfPlayers;
    private final int numberOfWorkers;
    private final int startingPlayerIndex;
    private final GameModifier gameModifier;

    // Constructor

    /**
     * Constructor.
     *
     * @param boardWidth The width of the game board.
     * @param boardHeight The height of the game board.
     * @param numberOfPlayers The number of players in the game.
     * @param numberOfWorkers The number of workers per player.
     * @param startingPlayerIndex The index of the player who starts first.
     * @param gameModifier The game modifier of a game mode.
     */
    GameMode(int boardWidth, int boardHeight, int numberOfPlayers, int numberOfWorkers, int startingPlayerIndex, GameModifier gameModifier) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfWorkers = numberOfWorkers;
        this.startingPlayerIndex = startingPlayerIndex;
        this.gameModifier = gameModifier;
    }

    // Getters

    /**
     * Gets the width of the game board.
     *
     * @return The board width.
     */
    public int getBoardWidth() {
        return boardWidth;
    }

    /**
     * Gets the height of the game board.
     *
     * @return The board height.
     */
    public int getBoardHeight() {
        return boardHeight;
    }

    /**
     * Gets the number of players in this game mode.
     *
     * @return The number of players.
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * Gets the number of workers each player controls.
     *
     * @return The number of workers per player.
     */
    public int getNumberOfWorkers() {
        return numberOfWorkers;
    }

    /**
     * Gets the index of the starting player.
     *
     * @return The starting player's index.
     */
    public int getStartingPlayerIndex() {
        return startingPlayerIndex;
    }

    /**
     * Gets the game modifier of the game mode.
     *
     * @return The game modifier of the game mode.
     */
    public GameModifier getGameModifier() {
        return gameModifier;
    }
}
