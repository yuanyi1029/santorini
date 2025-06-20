package santorini.towers;

/**
 * A factory class for creating Floor objects based on FloorType.
 * Provides utility methods to generate floors or progress to the next level.
 *
 * Created by:
 * @author Yuan Yi
 */
public class FloorFactory {

    // Methods

    /**
     * Creates a floor with the specified floor type.
     *
     * @param floorType The type of floor to create.
     * @return A new Floor object with the given floor type.
     */
    public static Floor createFloor(FloorType floorType) {
        return new Floor(floorType);
    }

    /**
     * Creates the next logical floor based on the current floor.
     *
     * @param currentFloor The current floor.
     * @return The next Floor object in the sequence, or null if there is no next floor.
     */
    public static Floor createNextFloor(Floor currentFloor) {
        FloorType nextFloorType = currentFloor.getFloorType().getNextFloorType();
        return nextFloorType != null ? new Floor(nextFloorType) : null;
    }
}
