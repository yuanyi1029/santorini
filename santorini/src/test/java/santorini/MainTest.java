package santorini;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import santorini.actions.BuildAction;
import santorini.actions.MoveAction;
import santorini.board.Board;
import santorini.game.Game;
import santorini.game.GameFactory;
import santorini.game.GameState;
import santorini.game.Position;
import santorini.players.Artemis;
import santorini.players.Demeter;
import santorini.players.God;

import org.junit.jupiter.api.Test;
import santorini.players.Player;
import santorini.players.Worker;
import santorini.towers.Tower;

class MainTest {
    @Test
    void testGameStateBuilder() {
        // Create a mock Board
        GameFactory gameFactory = new GameFactory();
        gameFactory.createStandardGame();

        GameState gameState = Game.getInstance().getGameState();

        // Check the size of the standard game board
        assert gameState.getBoard().getWidth() == 5;
        assert gameState.getBoard().getHeight() == 5;

        for (int i = 0; i < gameState.getPlayers().size(); i++) {

            // Check if the players are created correctly
            assert gameState.getPlayers().get(i).getName().equals("Player " + (i+1));

            // Check that each player has a god assigned
            God god = gameState.getPlayers().get(i).getGod();
            assert god.getClass().equals(Demeter.class) || god.getClass().equals(Artemis.class);

            // Check if each player has 2 workers
            assert gameState.getPlayers().get(i).getWorkers().size() == 2;

            // Check if worker position is within the map
            for (int j = 0; j < gameState.getPlayers().get(i).getWorkers().size(); j++) {
                Position workerPosition = gameState.getBoard().getPositionOf(gameState.getPlayers().get(i).getWorkers().get(0));
                assert workerPosition.x() < gameState.getBoard().getWidth();
                assert workerPosition.y() < gameState.getBoard().getHeight();
            }
        }
    }

    @Test
    void testGameWinConditionClimbToTop() {
        // Create a mock Board
        Board board = new Board(5, 5);

        // Create 2 players
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");

        // Assign gods
        player1.setGod(new Demeter());
        player2.setGod(new Artemis());

        // Add workers to players
        player1.addWorker(new Worker());
        player1.addWorker(new Worker());
        player2.addWorker(new Worker());
        player2.addWorker(new Worker());

        // Create game state
        GameState gameState = new GameState(board, List.of(player1, player2), 0);
        Game.getInstance().setGameState(gameState);
        Game game = Game.getInstance();

        // Set position for the player to move from
        Tower originalTower = board.getTower(new Position(1, 0));
        originalTower.buildFloor();
        originalTower.buildFloor();

        // Add worker to the original position
        board.addWorker(new Position(1, 0), gameState.getPlayers().get(0).getWorkers().get(0));

        // Add a level 3 tower on top of it
        Tower moveToTower = board.getTower(new Position(0, 0));
        moveToTower.buildFloor();
        moveToTower.buildFloor();
        moveToTower.buildFloor();

        // Move the player to the 3 levels grid, simulating a win condition
        game.processTurn(player1, gameState.getPlayers().get(0).getWorkers().get(0), new MoveAction(new Position(0,0)));

        assert game.determineWinner().equals(gameState.getPlayers().get(0));
    }

    @Test
    void testGameWinConditionPlayerStuckInBottomRight() {
        // Create a mock Board
        Board board = new Board(5, 5);

        // Create 2 players
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");

        // Assign gods
        player1.setGod(new Demeter());
        player2.setGod(new Artemis());

        // Add workers to players
        player1.addWorker(new Worker());
        player1.addWorker(new Worker());
        player2.addWorker(new Worker());
        player2.addWorker(new Worker());

        // Create game state
        GameState gameState = new GameState(board, List.of(player1, player2), 0);
        Game game = Game.getInstance();
        game.setGameState(gameState);

        // Position player 1's workers in the bottom right corner
        board.addWorker(new Position(3, 3), player1.getWorkers().get(0));
        board.addWorker(new Position(3, 4), player1.getWorkers().get(1));

        // Position player 2's workers elsewhere
        board.addWorker(new Position(0, 0), player2.getWorkers().get(0));
        board.addWorker(new Position(0, 1), player2.getWorkers().get(1));

        // Build towers around player1's workers to trap them
        Tower tower1 = board.getTower(new Position(2, 2));
        tower1.buildFloor();
        tower1.buildFloor();
        tower1.buildFloor();

        Tower tower2 = board.getTower(new Position(2, 3));
        tower2.buildFloor();
        tower2.buildFloor();
        tower2.buildFloor();

        Tower tower3 = board.getTower(new Position(2, 4));
        tower3.buildFloor();
        tower3.buildFloor();
        tower3.buildFloor();

        Tower tower4 = board.getTower(new Position(3, 2));
        tower4.buildFloor();
        tower4.buildFloor();
        tower4.buildFloor();

        Tower tower5 = board.getTower(new Position(4, 2));
        tower5.buildFloor();
        tower5.buildFloor();
        tower5.buildFloor();

        Tower tower6 = board.getTower(new Position(4, 3));
        tower6.buildFloor();
        tower6.buildFloor();
        tower6.buildFloor();

        Tower tower7 = board.getTower(new Position(4, 4));
        tower7.buildFloor();
        tower7.buildFloor();
        tower7.buildFloor();

        // Check if game correctly identifies the stuck player
        Player winner = game.determineWinner();

        assertEquals(player2, winner);
    }


    @Test
    void testBuildActionScenarios() {
        // Create a mock board
        Board board = new Board(5, 5);

        // Create player and worker
        Player player = new Player("Player 1");
        Worker worker = new Worker();
        player.addWorker(worker);

        // Place worker on board at a position
        board.addWorker(new Position(2, 2), worker);

        // Build on an empty adjacent cell
        Position buildPos = new Position(2, 3);
        BuildAction buildAction = new BuildAction(buildPos);
        assertTrue(buildAction.execute(player, worker, board));
        assertEquals(1, board.getTower(buildPos).getHeight());

        // Build progressively to max height
        Position towerPos = new Position(3, 2);
        BuildAction towerBuild = new BuildAction(towerPos);

        assertTrue(towerBuild.execute(player, worker, board)); // level 1
        assertEquals(1, board.getTower(towerPos).getHeight());

        assertTrue(towerBuild.execute(player, worker, board)); // level 2
        assertEquals(2, board.getTower(towerPos).getHeight());

        assertTrue(towerBuild.execute(player, worker, board)); // level 3
        assertEquals(3, board.getTower(towerPos).getHeight());

        assertTrue(towerBuild.execute(player, worker, board)); // dome
        assertEquals(4, board.getTower(towerPos).getHeight());
        assertTrue(board.getTower(towerPos).isComplete());

        // Build successfully at multiple new locations
        Position[] positions = { new Position(1, 1), new Position(1, 3), new Position(3, 3) };
        for (Position pos : positions) {
            assertTrue(new BuildAction(pos).execute(player, worker, board));
            assertEquals(1, board.getTower(pos).getHeight());
        }
    }


    @Test
    void testMoveActionScenarios() {
        // Create a mock board
        Board board = new Board(5, 5);

        // Create player and worker
        Player player = new Player("Player 1");
        Worker worker = new Worker();
        player.addWorker(worker);

        // Place worker on board
        Position initialPos = new Position(2, 2);
        board.addWorker(initialPos, worker);

        // Move to a new position (same level)
        Position newPos = new Position(2, 3);
        MoveAction moveAction = new MoveAction(newPos);
        assertTrue(moveAction.execute(player, worker, board));
        assertEquals(newPos, board.getPositionOf(worker));

        // Move to a higher position (up one level)
        Position upOneLevel = new Position(3, 3);
        Tower upTower = board.getTower(upOneLevel);
        upTower.buildFloor(); // Height 1

        MoveAction upMove = new MoveAction(upOneLevel);
        assertTrue(upMove.execute(player, worker, board));
        assertEquals(upOneLevel, board.getPositionOf(worker));

        // Move down multiple levels (from high to ground)
        Position highPos = new Position(4, 4);
        Tower highTower = board.getTower(highPos);
        highTower.buildFloor();
        highTower.buildFloor(); // Height 2

        board.removeWorker(worker);
        board.addWorker(highPos, worker);

        Position groundPos = new Position(3, 4); // No floors
        MoveAction downMove = new MoveAction(groundPos);
        assertTrue(downMove.execute(player, worker, board));
        assertEquals(groundPos, board.getPositionOf(worker));
    }
}
