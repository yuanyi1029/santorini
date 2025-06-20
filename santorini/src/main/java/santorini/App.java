package santorini;

import santorini.frames.MainFrame;

import javax.swing.*;

/**
 * Entry point for the application.
 * Initializes and starts the user interface using Java Swing.
 *
 * Created by:
 * @author Diana Wijaya
 */
public class App {

    // Methods

    /**
     * The main method which runs the application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.startFrame();
        });
    }
}