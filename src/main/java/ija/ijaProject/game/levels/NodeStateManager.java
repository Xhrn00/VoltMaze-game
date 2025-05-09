package ija.ijaProject.game.levels;

import ija.ijaProject.common.GameNode;
import ija.ijaProject.common.Position;
import ija.ijaProject.common.Side;
import ija.ijaProject.game.Game;

import java.io.*;
import java.util.*;

/**
 * Manages the saving and loading of node states for game levels.
 * This allows the game to remember the state of nodes (position, rotation)
 * when a player exits a level and returns to it later.
 */
public class NodeStateManager {
    private static NodeStateManager instance;
    private final Map<String, Map<String, NodeState>> nodeStates;
    private final String saveFilePath;
    private final Map<String, Boolean> levelChangesDetected;

    /**
     * Private constructor for singleton pattern.
     */
    private NodeStateManager() {
        nodeStates = new HashMap<>();
        levelChangesDetected = new HashMap<>();
        saveFilePath = "node_states.ser";
        loadNodeStates();
    }

    /**
     * Gets the singleton instance of NodeStateManager.
     *
     * @return The NodeStateManager instance
     */
    public static synchronized NodeStateManager getInstance() {
        if (instance == null) {
            instance = new NodeStateManager();
        }
        return instance;
    }

    /**
     * Records that changes have been made to a level.
     *
     * @param levelNumber The level number
     * @param difficulty The difficulty level
     */
    public void markLevelChanged(int levelNumber, int difficulty) {
        String levelKey = difficulty + "-" + levelNumber;
        levelChangesDetected.put(levelKey, true);
        System.out.println("Changes detected for level " + levelNumber + " at difficulty " + difficulty);
    }

    /**
     * Checks if changes have been detected for a level.
     *
     * @param levelNumber The level number
     * @param difficulty The difficulty level
     * @return true if changes have been detected, false otherwise
     */
    public boolean hasChangesDetected(int levelNumber, int difficulty) {
        String levelKey = difficulty + "-" + levelNumber;
        return levelChangesDetected.getOrDefault(levelKey, false);
    }

    /**
     * Resets the changes detected flag for a level.
     *
     * @param levelNumber The level number
     * @param difficulty The difficulty level
     */
    public void resetChangesDetected(int levelNumber, int difficulty) {
        String levelKey = difficulty + "-" + levelNumber;
        levelChangesDetected.put(levelKey, false);
    }

    /**
     * Checks if a game is completed (all bulbs lit).
     *
     * @param game The game to check
     * @return true if the game is completed, false otherwise
     */
    public boolean isGameCompleted(Game game) {
        return !game.anyBulbLit();
    }

    /**
     * Saves the current state of all nodes in a game level.
     * Only saves if the level is NOT completed AND changes have been made.
     *
     * @param levelNumber The level number
     * @param difficulty The difficulty level
     * @param game The Game instance containing the nodes
     * @return true if the state was saved, false otherwise
     */
    public boolean saveNodeStates(int levelNumber, int difficulty, Game game) {
        // Check if the level is completed
        boolean isLevelCompleted = LevelManager.getInstance().isLevelCompleted(levelNumber, difficulty);

        // Check if changes have been detected
        boolean hasChanges = hasChangesDetected(levelNumber, difficulty);

        System.out.println("Saving state for level " + levelNumber + " at difficulty " + difficulty);
        System.out.println("Level completed: " + isLevelCompleted);
        System.out.println("Changes detected: " + hasChanges);

        // Don't save if the level is completed
        if (isLevelCompleted) {
            System.out.println("Level is completed, not saving state");
            return false;
        }

        // Mark that changes have been detected (we'll save regardless)
        markLevelChanged(levelNumber, difficulty);

        String levelKey = difficulty + "-" + levelNumber;
        Map<String, NodeState> levelNodeStates = new HashMap<>();

        // Save the state of each node
        for (GameNode node : game.getNodes()) {
            if (game.isPlayebleNode(node)) {
                Position position = node.getPosition();
                String nodeId = position.row() + "-" + position.col();

                // Create a NodeState object to store the node's state
                NodeState state = new NodeState();
                state.position = new int[]{position.row(), position.col()};
                state.rotationCount = node.getRotationCount();
                state.connectors = new ArrayList<>();

                // Store the connector sides
                for (Side side : node.getConnectors()) {
                    state.connectors.add(side.ordinal());
                }

                levelNodeStates.put(nodeId, state);
            }
        }

        // Store the node states for this level
        nodeStates.put(levelKey, levelNodeStates);

        // Persist to disk
        saveNodeStatesToDisk();

        System.out.println("Successfully saved state for level " + levelNumber + " at difficulty " + difficulty);
        return true;
    }

    /**
     * Loads the saved state for a specific level and applies it to the game.
     * Only loads if the level is NOT completed and saved state exists.
     *
     * @param levelNumber The level number
     * @param difficulty The difficulty level
     * @param game The Game instance to apply the states to
     * @return true if states were loaded and applied, false otherwise
     */
    public boolean loadNodeStates(int levelNumber, int difficulty, Game game) {
        // Check if the level is completed
        boolean isLevelCompleted = LevelManager.getInstance().isLevelCompleted(levelNumber, difficulty);

        String levelKey = difficulty + "-" + levelNumber;

        // Don't load if the level is completed
        if (isLevelCompleted) {
            System.out.println("Level " + levelNumber + " at difficulty " + difficulty + " is completed, not loading saved state");
            return false;
        }

        // Check if we have saved states for this level
        if (!nodeStates.containsKey(levelKey)) {
            System.out.println("No saved state found for level " + levelNumber + " at difficulty " + difficulty);
            return false;
        }

        Map<String, NodeState> levelNodeStates = nodeStates.get(levelKey);

        // Apply the saved states to each node
        for (GameNode node : game.getNodes()) {
            if (game.isPlayebleNode(node)) {
                Position position = node.getPosition();
                String nodeId = position.row() + "-" + position.col();

                if (levelNodeStates.containsKey(nodeId)) {
                    NodeState savedState = levelNodeStates.get(nodeId);

                    // Reset the node's rotation count
                    node.resetRotationCount();

                    // Rotate the node to match the saved rotation count
                    int targetRotations = savedState.rotationCount % 4; // Ensure it's 0-3
                    for (int i = 0; i < targetRotations; i++) {
                        node.turn();
                    }
                }
            }
        }

        // Reset changes detected flag since we just loaded the state
        resetChangesDetected(levelNumber, difficulty);

        System.out.println("Loaded and applied saved state for level " + levelNumber + " at difficulty " + difficulty);
        return true;
    }

    /**
     * Checks if there are saved states for a specific level.
     *
     * @param levelNumber The level number
     * @param difficulty The difficulty level
     * @return true if saved states exist, false otherwise
     */
    public boolean hasSavedStates(int levelNumber, int difficulty) {
        String levelKey = difficulty + "-" + levelNumber;
        return nodeStates.containsKey(levelKey);
    }

    /**
     * Clears the saved states for a specific level.
     *
     * @param levelNumber The level number
     * @param difficulty The difficulty level
     */
    public void clearNodeStates(int levelNumber, int difficulty) {
        String levelKey = difficulty + "-" + levelNumber;
        if (nodeStates.containsKey(levelKey)) {
            nodeStates.remove(levelKey);
            // Also clear the changes detected flag
            resetChangesDetected(levelNumber, difficulty);
            saveNodeStatesToDisk();
            System.out.println("Cleared saved state for level " + levelNumber + " at difficulty " + difficulty);
        }
    }

    /**
     * Saves all node states to disk.
     */
    private void saveNodeStatesToDisk() {
        try {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFilePath))) {
                oos.writeObject(nodeStates);
                System.out.println("Saved node states to disk: " + nodeStates.size() + " levels");
            }

            // Save the changes detected map
            try (ObjectOutputStream changesOos = new ObjectOutputStream(new FileOutputStream(saveFilePath + ".changes"))) {
                changesOos.writeObject(levelChangesDetected);
                System.out.println("Saved changes detected map to disk: " + levelChangesDetected.size() + " entries");
            }
        } catch (IOException e) {
            System.err.println("Error saving node states to disk: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads all node states from disk.
     */
    @SuppressWarnings("unchecked")
    private void loadNodeStates() {
        File file = new File(saveFilePath);
        if (!file.exists()) {
            System.out.println("No saved node states file found");
            return;
        }

        try {
            // Load the node states
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFilePath))) {
                Map<String, Map<String, NodeState>> loadedStates =
                        (Map<String, Map<String, NodeState>>) ois.readObject();
                nodeStates.clear();
                nodeStates.putAll(loadedStates);
                System.out.println("Loaded node states from disk: " + nodeStates.size() + " levels");
            }

            // Load the changes detected map
            File changesFile = new File(saveFilePath + ".changes");
            if (changesFile.exists()) {
                try (ObjectInputStream changesOis = new ObjectInputStream(new FileInputStream(changesFile))) {
                    Map<String, Boolean> loadedChanges = (Map<String, Boolean>) changesOis.readObject();
                    levelChangesDetected.clear();
                    levelChangesDetected.putAll(loadedChanges);
                    System.out.println("Loaded changes detected map from disk: " + levelChangesDetected.size() + " entries");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading node states from disk: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Inner class to store the state of a node.
     * This class must be serializable.
     */
    private static class NodeState implements Serializable {
        private static final long serialVersionUID = 1L;

        int[] position; // [row, col]
        int rotationCount;
        List<Integer> connectors; // Side ordinals
    }
}
