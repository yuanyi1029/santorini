package santorini.players;

import santorini.utils.SaveConfig;
import santorini.utils.SaveManager;
import santorini.utils.Saveable;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a player in the game. Each player has a name, a god power,
 * a set of workers, and control flags for turn management.
 *
 * Created by:
 * @author Zi Yang, Hsu Chyi
 */
public class Player implements Saveable {

    // Attributes

    /**
     * The name of the player.
     */
    private String name;

    /**
     * The god associated with the player, which may affect gameplay abilities.
     */
    protected God god;

    /**
     * The list of workers controlled by the player.
     */
    protected List<Worker> workers;

    /**
     * A flag indicating whether the player is allowed to select a worker this turn.
     */
    private boolean canSelectWorker;

    // Constructor

    /**
     * Constructor.
     *
     * @param name The name of the player.
     */
    public Player(String name) {
        this.name = name;
        this.workers = new ArrayList<>();
        canSelectWorker = true;
    }

    public Player(String buildString, boolean fromSave) {
        if (fromSave) {
            String[] lines = buildString.split(SaveConfig.DELIMITER_REGEX);
            this.name = lines[0];
            this.god = God.parseName(lines[1]);
            this.workers = new ArrayList<>();
            this.canSelectWorker = Boolean.parseBoolean(lines[2]);
        }
    }

    // Getters and Setters

    /**
     * Gets the name of the player.
     *
     * @return The player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the player's name.
     *
     * @param name The new name for the player.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the god assigned to the player.
     *
     * @param god The god object.
     */
    public void setGod(God god) {
        this.god = god;
    }

    /**
     * Gets the god associated with the player.
     *
     * @return The player's god.
     */
    public God getGod() {
        return god;
    }

    /**
     * Gets the list of workers controlled by the player.
     *
     * @return The list of workers.
     */
    public List<Worker> getWorkers() {
        return workers;
    }

    /**
     * Sets the list of workers for the player.
     *
     * @param workers The list of workers.
     */
    public void setWorkers(List<Worker> workers) {
        this.workers = workers; 
    }

    /**
     * Adds a worker to the player's list of workers.
     *
     * @param worker The worker to add.
     */
    public void addWorker(Worker worker) {
        workers.add(worker);
    }

    /**
     * Checks whether the player can select a worker during their turn.
     *
     * @return true if the player can select a worker; false otherwise.
     */
    public boolean canSelectWorker() {
        return canSelectWorker;
    }

    /**
     * Sets whether the player can select a worker during their turn.
     *
     * @param canSelectWorker The value to set.
     */
    public void setCanSelectWorker(boolean canSelectWorker) {
        this.canSelectWorker = canSelectWorker;
    }

    // Methods

    /**
     * Initializes the icon for each worker based on the player's index.
     *
     * @param playerIndex The index of the player (used to load a distinct icon).
     */
    public void initialiseWorkerIcon(int playerIndex) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/worker" + (playerIndex + 1) + ".png"));
        for (Worker worker : workers) {
            worker.setIcon(icon);
        }
    }

    /**
     * Resets the turn status to allow worker selection.
     */
    public void resetTurn() {
        canSelectWorker = true;
    }

    @Override
    public String save() {
        StringBuilder builder = new StringBuilder();

        builder.append(name).append(SaveConfig.DELIMITER);
        builder.append(god.getName()).append(SaveConfig.DELIMITER);
        builder.append(!canSelectWorker).append(SaveConfig.DELIMITER);

        return builder.toString();
    }

    /**
     * Returns the string representation of the player.
     *
     * @return The player's name.
     */
    public String toString() {
        return name;
    }
}
