package santorini.towers;

import santorini.board.Board;
import santorini.game.GameMode;

import javax.swing.*;
import java.awt.*;

/**
 * Represents a floor in a tower, which may be climbed or used as part of gameplay.
 * Each floor has a specific type and corresponding level, image, and climbability.
 *
 * Created by:
 * @author Yuan Yi, Zi Yang
 */
public class Floor {

    // Attributes

    /**
     * The type of floor (e.g., level 1, 2, 3, dome).
     */
    private final FloorType floorType;

    // Constructor

    /**
     * Constructor.
     *
     * @param floorType The type of the floor.
     */
    public Floor(FloorType floorType) {
        this.floorType = floorType;
    }

    // Getters

    /**
     * Gets the floor type.
     *
     * @return The floor type.
     */
    public FloorType getFloorType() {
        return floorType;
    }

    /**
     * Gets the level of the floor.
     *
     * @return The level of the floor.
     */
    public int getLevel() {
        return floorType.getLevel();
    }

    /**
     * Checks if the floor can be climbed.
     *
     * @return true if the floor is climbable, false otherwise.
     */
    public boolean canClimb() {
        return floorType.getClimbable();
    }

    /**
     * Renders the floor's image as a scaled ImageIcon for display on the board.
     * The scaling depends on the standard board dimensions.
     *
     * @return The rendered ImageIcon of the floor.
     */
    public ImageIcon draw(Board board) {
        int horizontalLength = (620 - (board.getWidth() - 1) * 5) / board.getWidth() - 10;
        int verticalLength = (620 - (board.getHeight() - 1) * 5) / board.getHeight() - 10;
        int size = Math.min(horizontalLength, verticalLength);
        Image image = new ImageIcon(getClass().getResource(floorType.getImagePath()))
                .getImage()
                .getScaledInstance(size, size, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(image);
        return icon;
    }
}