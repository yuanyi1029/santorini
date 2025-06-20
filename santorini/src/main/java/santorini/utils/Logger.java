package santorini.utils;

import javax.swing.*;

/**
 * The Logger class is a utility class used to log in-game messages and debug information.
 * It supports displaying logs in a GUI component and saving them via the Saveable interface.
 *
 * Created by:
 * @author Yuan Yi
 */
public class Logger implements Saveable {

    // Attributes

    /**
     * The singleton instance of the Logger.
     */
    private static Logger instance;

    /**
     * A StringBuilder to hold all logged messages.
     */
    private StringBuilder logs;

    /**
     * The JTextArea used to display log messages in the GUI.
     */
    private JTextArea logTextArea;

    // Constructor

    /**
     * Constructor.
     */
    public Logger() {
        logs = new StringBuilder();

        SaveManager.getInstance().registerSaveable(this);
    }

    /**
     * Returns the singleton instance of the Logger.
     * If it does not exist yet, it is created.
     *
     * @return The single instance of the Logger.
     */
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    // Methods

    /**
     * Sets the JTextArea to which logs should be written.
     * If there are existing logs, they will be displayed in the text area.
     *
     * @param logTextArea The JTextArea component for displaying logs.
     */
    public void setLogTextArea(JTextArea logTextArea) {
        this.logTextArea = logTextArea;
        if (logTextArea != null && !logs.isEmpty()) {
            logTextArea.setText(logs.toString());
            logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
        }
    }

    /**
     * Logs a new message to both the internal log store and the GUI (if attached).
     *
     * @param log The message to log.
     */
    public void log(String log) {
        String formattedLog = log + "\n";
        logs.append(formattedLog);

        if (logTextArea != null) {
            logTextArea.append(formattedLog);
            logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
        }
    }

    /**
     * Retrieves all logs currently stored.
     *
     * @return A string representing the current logs.
     */
    public String getLogs() {
        return logs.toString();
    }

    /**
     * Clears all logs from both the internal store and the GUI display.
     */
    public void clearLogs() {
        logs = new StringBuilder();
        if (logTextArea != null) {
            logTextArea.setText("");
        }
    }

    @Override
    public String save() {
        return logs.toString();
    }
}