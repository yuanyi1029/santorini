package santorini.towers;

import santorini.board.Board;
import santorini.game.GameMode;
import santorini.game.Position;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Stack;

/**
 * Represents a tower in the game. A tower is made up of a stack of floors and can be built up to a maximum height.
 * Towers are placed on the board and can be interacted with by players and workers during gameplay.
 *
 * Created by:
 * @author Yuan Yi
 */
public class Tower {

    // Constants

    /**
     * The maximum height a tower can reach (corresponds to a dome).
     */
    private static final int MAXIMUM_HEIGHT = 4;

    // Attributes

    /**
     * A stack of floors representing the structure of the tower.
     */
    private Stack<Floor> floors;

    /**
     * A boolean status whether this tower is covered by fog.
     */
    private boolean fogged;

    // Constructor

    /**
     * Constructor.
     * Constructs a new Tower starting with an EMPTY floor.
     */
    public Tower() {
        this.floors = new Stack<>();
        floors.add(FloorFactory.createFloor(FloorType.EMPTY));
        this.fogged = false;
    }

    // Getters and Setters

    /**
     * Gets the stack of floors composing the tower.
     *
     * @return The stack of floors.
     */
    public Stack<Floor> getFloors() {
        return floors;
    }

    /**
     * Sets the stack of floors composing the tower.
     *
     * @param floors The stack of floors to set.
     */
    public void setFloors(Stack<Floor> floors) {
        this.floors = floors;
    }

    /**
     * Gets the status whether a tower is covered by fog.
     *
     * @return The status whether a tower is covered by fog.
     */
    public boolean getFogged() {
        return fogged;
    }

    /**
     * Sets the status of a tower whether it is coverd by fog.
     *
     * @param fogged The new status whether the tower is covered by fog.
     */
    public void setFogged(boolean fogged) {
        this.fogged = fogged;
    }

    // Methods

    /**
     * Gets the current height (top floor level) of the tower.
     *
     * @return The level of the top floor.
     */
    public int getHeight() {
        return floors.peek().getLevel();
    }

    /**
     * Checks whether the tower has reached its maximum height (i.e., has a dome).
     *
     * @return true if the tower is complete, false otherwise.
     */
    public boolean isComplete() {
        return getHeight() == MAXIMUM_HEIGHT;
    }

    /**
     * Checks whether the tower can be destroyed by 1 floor.
     *
     * @return true if a floor can be destroyed from the tower, false otherwise.
     */
    public boolean isDestroyable() {
        return getHeight() > 0;
    }

    /**
     * Checks whether a dome can be built on this tower.
     *
     * @return true if the tower is one level below maximum, false otherwise.
     */
    public boolean canBuildDome() {
        return getHeight() == MAXIMUM_HEIGHT - 1;
    }

    /**
     * Checks whether the top floor of the tower is climbable.
     *
     * @return true if climbable; false otherwise.
     */
    public boolean canClimb() {
        return floors.peek().canClimb();
    }

    /**
     * Builds the next floor on top of the tower, if not already complete.
     */
    public void buildFloor() {
        if (!isComplete()) {
            Floor currentFloor = floors.peek();
            floors.push(FloorFactory.createNextFloor(currentFloor));
        }
    }

    /**
     * Destroys the current floor of the tower, if it is destroyable.
     */
    public void destroyFloor() {
        if (isDestroyable()) {
            floors.pop();
        }
    }

    /**
     * Renders the tower by drawing its topmost floor.
     *
     * @return The ImageIcon representing the top floor of the tower.
     */
    public ImageIcon draw(Board board, Position position) {
        if (fogged && !board.isOccupied(position)) {
            int horizontalLength = (620 - (board.getWidth() - 1) * 5) / board.getWidth() - 10;
            int verticalLength = (620 - (board.getHeight() - 1) * 5) / board.getHeight() - 10;
            int size = Math.min(horizontalLength, verticalLength);
            Image image = new ImageIcon(getClass().getResource("/floorfog.png"))
                    .getImage()
                    .getScaledInstance(size, size, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(image);
            return icon;
        }
        else {
            return floors.peek().draw(board);
        }
    }
}
