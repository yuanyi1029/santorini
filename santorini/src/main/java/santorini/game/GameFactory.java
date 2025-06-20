package santorini.game;

import santorini.board.Board;
import santorini.players.*;
import santorini.towers.Tower;
import santorini.utils.Logger;
import santorini.utils.SaveManager;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * A factory class responsible for setting up a new game instance.
 * Handles board creation, player and worker initialization, and
 * random god assignment.
 *
 * Currently, it only includes the STANDARD game mode configuration.
 *
 * Created by:
 * author Yuan Yi
 */
public class GameFactory {

    // Attributes

    /**
     * The list of available god powers used for assignment during game initialization.
     */
    private List<God> gods;

    // Constructor

    /**
     * Constructor.
     */
    public GameFactory() {
        gods = new ArrayList<>();
        initializeAllGods();
    }

    /**
     * Creates a new game using the STANDARD game mode.
     * Initializes the board, players, workers, god powers, and places all workers on random unoccupied positions.
     * Sets the resulting GameState in the singleton Game instance.
     */
    public void createStandardGame() {
        // Create Board
        Board board = new Board(GameMode.STANDARD.getBoardWidth(), GameMode.STANDARD.getBoardHeight());

        // Create Players
        List<Player> players = initializePlayers(GameMode.STANDARD.getNumberOfPlayers(), GameMode.STANDARD.getNumberOfWorkers());

        // Initialize Gods
        initializeRandomGods(players);

        // Initialize Workers
        initializeRandomWorkerPositions(board, players);

        GameState gameState = new GameState(board, players, GameMode.STANDARD.getStartingPlayerIndex());

        Game.getInstance().setModifier(GameMode.STANDARD.getGameModifier());
        Game.getInstance().setGameState(gameState);
    }

    /**
     * Creates a new game using the CHAOS game mode.
     * Initializes the board, players, workers, god powers, and places all workers on random unoccupied positions.
     * Sets the resulting GameState in the singleton Game instance.
     */
    public void createChaosGame() {
        // Create Board
        Board board = new Board(GameMode.CHAOS.getBoardWidth(), GameMode.CHAOS.getBoardHeight());

        // Create Players
        List<Player> players = initializePlayers(GameMode.CHAOS.getNumberOfPlayers(), GameMode.CHAOS.getNumberOfWorkers());

        // Initialize Gods
        initializeRandomGods(players);

        // Initialize Workers
        initializeRandomWorkerPositions(board, players);

        GameState gameState = new GameState(board, players, GameMode.CHAOS.getStartingPlayerIndex());

        Game.getInstance().setModifier(GameMode.CHAOS.getGameModifier());
        Game.getInstance().setGameState(gameState);
    }

    /**
     * Loads a previously saved game from a text file.
     * Initializes the game state, game, and logger information, and sets the results in the singleton Game instance.
     */
    public boolean loadGame(File file) {
        try {
            Map<String, String> buildStringMap = SaveManager.parseFile(file);
            String gameStateString = buildStringMap.get(GameState.class.getName());
            String loggerString = buildStringMap.get(Logger.class.getName());
            String gameString = buildStringMap.get(Game.class.getName());

            GameState gameState = new GameState(gameStateString);

            Game.getInstance().setModifier(gameString);
            Game.getInstance().setGameState(gameState);

            Logger.getInstance().log(loggerString);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Creates a list of players with the given number of workers per player.
     * Also assigns the visual icon to each player's workers.
     *
     * @param numberOfPlayers The number of players in the game.
     * @param numberOfWorkers The number of workers per player.
     * @return A list of initialized players.
     */
    private List<Player> initializePlayers(int numberOfPlayers, int numberOfWorkers) {
        List<Player> players = new ArrayList<>();

        // For each player, create a new Player object and add the specified number of workers
        for (int i = 0; i < numberOfPlayers; i++) {
            Player player = new Player(String.format("Player %d", i + 1));
            for (int j = 0; j < numberOfWorkers; j++) {
                Worker worker = new Worker();
                player.addWorker(worker);
            }
            player.initialiseWorkerIcon(i);
            players.add(player);
        }
        return players;
    }

    /**
     * Randomly assigns each player's workers to unoccupied positions on the board.
     *
     * @param board The game board to place workers on.
     * @param players The list of players whose workers will be placed.
     */
    private void initializeRandomWorkerPositions(Board board, List<Player> players) {

        // For each player, randomly place their workers on unoccupied positions on the board
        for (Player player : players) {
            for (Worker worker : player.getWorkers()) {
                Position randomPosition;
                do {
                    int x = (int) (Math.random() * board.getWidth());
                    int y = (int) (Math.random() * board.getHeight());
                    randomPosition = new Position(x, y);
                } while (board.isOccupied(randomPosition));

                board.addWorker(randomPosition, worker);
            }
        }
    }

    /**
     * Randomly assigns unique god powers from the available list to the players.
     *
     * @param players The list of players to assign gods to.
     */
    private void initializeRandomGods(List<Player> players) {

        // Shuffle the list of available gods
        Collections.shuffle(gods);

        // Assign each player a unique god from the shuffled list
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setGod(gods.get(i));
        }
    }

    /**
     * Populates the list of available god powers.
     * Add additional gods to this method to support more powers.
     */
    private void initializeAllGods() {
        gods.add(new Artemis());
        gods.add(new Demeter());
        gods.add(new Triton());
    }
}