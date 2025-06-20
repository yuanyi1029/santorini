package santorini.game.modifier;

import santorini.board.Board;
import santorini.game.chaos.BuildChaos;
import santorini.game.chaos.Chaos;
import santorini.game.chaos.DestroyChaos;
import santorini.game.chaos.FogChaos;

import java.util.ArrayList;
import java.util.List;

/**
 * The ChaosModifier stores a list of chaos effects for a chaos game mode, and
 * exeutes the tick method for each chaos effect
 *
 * Created by:
 * author Yuan Yi
 */
public class ChaosModifier extends GameModifier {

    // Attributes

    /**
     * The maximum number of affected towers.
     */
    private final int MAXIMUM_AFFECTED = 3;

    /**
     * The list of chaos effects of the chaos game mode modifier.
     */
    private List<Chaos> chaosEffects;

    // Constructor

    /**
     * Constructor.
     */
    public ChaosModifier() {
        this.chaosEffects = new ArrayList<>();
        initializeAllChaos();
    }

    // Methods

    /**
     * Executes the logic of the modifier of a specific game mode. Calls the
     * tick method of each chaos effect to signal an end of a round, which
     * would potentially apply the chaos effect.
     *
     * @param board The game board of the current game.
     * @param turnNumber The current turn number.
     */
    @Override
    public void executeModifier(Board board, int turnNumber) {
        int affectedTowers = Math.min((turnNumber - 1) / 4 + 1, MAXIMUM_AFFECTED);

        for (Chaos chaos : chaosEffects) {
            chaos.tick(board, affectedTowers);
        }
    }

    /**
     * Initializes all chaos effects of the chaos game mode modifier.
     */
    private void initializeAllChaos() {
        chaosEffects.add(new BuildChaos());
        chaosEffects.add(new DestroyChaos());
        chaosEffects.add(new FogChaos());
    }
}
