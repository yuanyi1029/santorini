package santorini.utils;

/**
 * The SaveConfig class defines a set of constants used for formatting and parsing
 * save data within the Santorini game. These constants provide a standardized structure
 * for serializing and deserializing objects across different game components.
 *
 * Created by:
 * @author Yuan Yi
 */
public class SaveConfig {
    public static final String NEWLINE = "\n";
    public static final String DELIMITER = "$";
    public static final String OUTER_KEY = "santorini.";
    public static final String INNER_KEY = "+";
    public static final String COMMA = ",";
    public static final String NEWLINE_REGEX = "\\R";
    public static final String DELIMITER_REGEX = "\\$";
    public static final String COMMA_REGEX = "\\,";
}
