package santorini.towers;

/**
 * Enum representing the different types of floors that can be built in the game.
 * Each floor type has an associated level, climbability, and image path for rendering.
 *
 * Created by:
 * @author Yuan Yi
 */
public enum FloorType {

    // Enum Constants

    EMPTY(0, true, "/floor0.png"),
    LEVEL_ONE(1, true, "/floor1.png"),
    LEVEL_TWO(2, true, "/floor2.png"),
    LEVEL_THREE(3, true, "/floor3.png"),
    DOME(4, false, "/floor4.png");

    // Attributes

    /**
     * The level of the floor (0 for empty, 1-3 for levels, 4 for dome).
     */
    private final int level;

    /**
     * Indicates if the floor can be climbed.
     */
    private final boolean climbable;

    /**
     * The image path for rendering the floor.
     */
    private final String imagePath;

    // Constructor

    /**
     * Constructs a FloorType with level, climbability, and image path.
     *
     * @param level The numeric level of the floor.
     * @param climbable Whether the floor is climbable.
     * @param imagePath The path to the floor's image.
     */
    FloorType(int level, boolean climbable, String imagePath) {
        this.level = level;
        this.climbable = climbable;
        this.imagePath = imagePath;
    }

    // Getters

    /**
     * Gets the numeric level of the floor type.
     *
     * @return The level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Returns whether the floor type is climbable.
     *
     * @return true if climbable; false otherwise.
     */
    public boolean getClimbable() {
        return climbable;
    }

    /**
     * Gets the image path for rendering the floor type.
     *
     * @return The image file path.
     */
    public String getImagePath() {
        return imagePath;
    }

    // Methods

    /**
     * Gets the next floor type in the construction sequence.
     *
     * @return The next FloorType, or null if at the top level (DOME).
     */
    public FloorType getNextFloorType() {
        if (this == EMPTY) return LEVEL_ONE;
        if (this == LEVEL_ONE) return LEVEL_TWO;
        if (this == LEVEL_TWO) return LEVEL_THREE;
        if (this == LEVEL_THREE) return DOME;
        return null;
    }
}
