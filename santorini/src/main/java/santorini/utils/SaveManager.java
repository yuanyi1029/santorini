package santorini.utils;

import santorini.game.GameFactory;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The SaveManager class handles saving and loading the game state in the Santorini game.
 * It maintains a list of objects that implement the Saveable interface and are
 * capable of serializing their own state into a savable string format.
 *
 * Created by:
 * @author Yuan Yi
 */
public class SaveManager {

    // Attributes

    /**
     * Singleton instance
     */
    private static SaveManager instance;

    /**
     * List of saveable game components
     */
    private List<Saveable> saveables;

    // Constructor

    /**
     * Constructor.
     */
    public SaveManager() {
        this.saveables = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of the SaveManager class.
     *
     * @return the Game instance
     */
    public static SaveManager getInstance() {
        if (instance == null) {
            instance = new SaveManager();
        }
        return instance;
    }

    // Methods

    /**
     * Registers a Saveable object to be included in the save file.
     *
     * @param saveable the Saveable object to register.
     */
    public void registerSaveable(Saveable saveable) {
        if (!saveables.contains(saveable)) {
            saveables.add(saveable);
        }
    }

    /**
     * Removes a Saveable object from the saveable list.
     *
     * @param saveable the Saveable object to remove.
     */
    public void removeSaveable(Saveable saveable) {
        saveables.remove(saveable);
    }

    /**
     * Prompts the user to select a file location and saves the current game state
     * to a text file. Each saveable object is saved by its class name and serialized data.
     *
     * @param component the GUI component used to anchor the file dialog.
     * @return true if saving was successful; false otherwise.
     */
    public boolean saveGame(Component component) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Game");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Documents (*.txt)", "txt"));
        int selection = fileChooser.showSaveDialog(component);

        if (selection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getAbsolutePath().toLowerCase().endsWith(".txt")) {
                file = new File(file.getAbsolutePath() + ".txt");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(saveables.size() + SaveConfig.NEWLINE);

                for (Saveable saveable : saveables) {
                    String className = saveable.getClass().getName();
                    String data = saveable.save();

                    writer.write(className + SaveConfig.NEWLINE);
                    writer.write(data + SaveConfig.NEWLINE);
                }

                JOptionPane.showMessageDialog(component, "Game has been Saved!");
                return true;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(component, "Failed to Save Game!", "Save Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }

    /**
     * Prompts the user to select a save file and loads the game state using the GameFactory.
     *
     * @param component the GUI component used to anchor the file dialog.
     * @return true if loading was successful; false otherwise.
     */
    public boolean loadGame(Component component) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Game");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Documents (*.txt)", "txt"));
        int selection = fileChooser.showOpenDialog(component);

        if (selection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            GameFactory gameFactory = new GameFactory();
            boolean result = gameFactory.loadGame(file);

            if (result) {
                JOptionPane.showMessageDialog(component, "Game has been Loaded!");
                return true;
            }
            else {
                JOptionPane.showMessageDialog(component, "Failed to Load Game!", "Load Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }

    /**
     * Parses the contents of a save file into a map of class names to serialized data blocks.
     * Used by the GameFactory to reconstruct game objects.
     *
     * @param file the file to parse.
     * @return a map of class names to their respective saved data strings.
     * @throws IOException if an error occurs while reading the file.
     */
    public static Map<String, String> parseFile(File file) throws IOException {
        Map<String, String> buildStringMap = new HashMap<>();
        List<String> lines = Files.readAllLines(Paths.get(file.getAbsolutePath()));

        int numberOfObjects = Integer.parseInt(lines.get(0));
        int parsedObjects = 0;
        int startingIndex = 1;

        while (startingIndex < lines.size() && parsedObjects < numberOfObjects) {
            String className = lines.get(startingIndex++).trim();
            StringBuilder dataBuilder = new StringBuilder();

            while (startingIndex < lines.size()) {
                String line = lines.get(startingIndex).trim();
                if (line.startsWith(SaveConfig.OUTER_KEY)) {
                    break;
                }

                dataBuilder.append(line).append(SaveConfig.NEWLINE);
                startingIndex++;
            }

            buildStringMap.put(className, dataBuilder.toString().trim());
            parsedObjects++;
        }

        return buildStringMap;
    }
}
