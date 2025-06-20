package santorini.game;

import santorini.utils.SaveConfig;
import santorini.utils.Saveable;

/**
 * Represents a coordinate on the game board using x (column) and y (row) values.
 * This record is immutable and provides a string representation for display.
 *
 * Created by:
 * author Yuan Yi
 *
 * @param x The x-coordinate (column index) of the position.
 * @param y The y-coordinate (row index) of the position.
 */
public record Position(int x, int y) implements Saveable {

    public Position(String buildString) {
        this(
            Integer.parseInt(buildString.substring(1, buildString.length() - 1).split(SaveConfig.COMMA_REGEX)[0].trim()),
            Integer.parseInt(buildString.substring(1, buildString.length() - 1).split(SaveConfig.COMMA_REGEX)[1].trim())
        );
    }

    @Override
    public String save() {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(x).append(SaveConfig.COMMA).append(y).append(")");
        return builder.toString();
    }

    /**
     * Returns a string representation of the position in the format (x,y).
     *
     * @return A formatted string of the position.
     */
    @Override
    public String toString() {
        return String.format("(%d,%d)", x, y);
    }
}
