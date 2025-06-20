package santorini.players;

import santorini.actions.Action;
import santorini.game.GamePhase;
import santorini.game.GameState;

import javax.swing.*;

/**
 * Represents a god power in the game, assigned to a player.
 * Each god has a name, description, and a specific number of allowed moves and builds.
 *
 * Created by:
 * @author Zi Yang, Yuan Yi
 */

public abstract class God {

    // Attributes

    /**
     * The name of the god.
     */
    private String name;

    /**
     * A brief description of the god's power or effect.
     */
    private String description;

    /**
     * The number of moves the god allows per turn.
     */
    private int numberOfMoves;

    /**
     * The number of builds the god allows per turn.
     */
    private int numberOfBuilds;

    // Constructor

    /**
     * Constructor.
     *
     * @param name The name of the god.
     * @param description A description of the god's power.
     * @param numberOfMoves The number of moves allowed per turn.
     * @param numberOfBuilds The number of builds allowed per turn.
     */
    public God(String name, String description, int numberOfMoves, int numberOfBuilds) {
        this.name = name;
        this.description = description;
        this.numberOfMoves = numberOfMoves;
        this.numberOfBuilds = numberOfBuilds;
    }

    // Getters and Setters

    /**
     * Gets the name of the god.
     *
     * @return The god's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the god.
     *
     * @param name The new name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the god's power.
     *
     * @return The god's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the god's power.
     *
     * @param description The new description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the number of moves allowed per turn.
     *
     * @return The number of moves.
     */
    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    /**
     * Gets the number of builds allowed per turn.
     *
     * @return The number of builds.
     */
    public int getNumberOfBuilds() {
        return numberOfBuilds;
    }

    /**
     * Executes the power of a specific god.
     *
     * @return The boolean status whether the power has been executed successfully
     */
    public boolean executePower(GameState gameState, Action previousAction) {
        return false;
    }

    /**
     * Returns an instance of a specific God based on a string used
     * for loading specific games.
     *
     * @param name the name of the modifier to be parsed
     * @return a new instance of the corresponding God
     */
    public static God parseName(String name) {
        if (name.equals("Artemis")) {
            return new Artemis();
        }
        else if (name.equals("Demeter")) {
            return new Demeter();
        }
        else if (name.equals("Triton")) {
            return new Triton();
        }
        return null;
    }

    // Abstract Methods

    /**
     * Returns the icon used to represent the god.
     *
     * @return The ImageIcon representing the god.
     */
    public abstract ImageIcon draw();
}
