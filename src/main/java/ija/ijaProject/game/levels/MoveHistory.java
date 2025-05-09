package ija.ijaProject.game.levels;

import ija.ijaProject.common.GameNode;
import ija.ijaProject.common.Position;
import ija.ijaProject.game.Game;

import java.io.*;
import java.util.*;

/**
 * Tracks the history of moves in a game level for simulation and playback.
 */
public class MoveHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private static MoveHistory instance;
    private final Map<String, List<Move>> levelMoves;
    private final Map<String, Integer> currentMoveIndices;
    private boolean recordingEnabled = true;

    /**
     * Represents a single move in the game.
     */
    public static class Move implements Serializable {
        private static final long serialVersionUID = 1L;

        private final int row;
        private final int col;
        private final long timestamp;

        public Move(int row, int col) {
            this.row = row;
            this.col = col;
            this.timestamp = System.currentTimeMillis();
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public long getTimestamp() {
            return timestamp;
        }

        @Override
        public String toString() {
            return "Move{" +
                    "row=" + row +
                    ", col=" + col +
                    ", time=" + new Date(timestamp) +
                    '}';
        }
    }

    private MoveHistory() {
        levelMoves = new HashMap<>();
        currentMoveIndices = new HashMap<>();
        loadHistory();
    }

    /**
     * Gets the singleton instance of MoveHistory.
     *
     * @return The MoveHistory instance
     */
    public static synchronized MoveHistory getInstance() {
        if (instance == null) {
            instance = new MoveHistory();
        }
        return instance;
    }

    /**
     * Enables or disables recording of moves.
     * This is useful when in simulation mode to prevent recording simulated moves.
     *
     * @param enabled Whether recording is enabled
     */
    public void setRecordingEnabled(boolean enabled) {
        this.recordingEnabled = enabled;
    }

    /**
     * Gets whether recording is enabled.
     *
     * @return Whether recording is enabled
     */
    public boolean isRecordingEnabled() {
        return recordingEnabled;
    }

    /**
     * Gets the key for a level.
     *
     * @param levelNumber The level number
     * @param difficulty The difficulty level
     * @return The level key
     */
    private String getLevelKey(int levelNumber, int difficulty) {
        return difficulty + "-" + levelNumber;
    }

    /**
     * Records a move in the history.
     *
     * @param levelNumber The level number
     * @param difficulty The difficulty level
     * @param row The row of the node that was rotated
     * @param col The column of the node that was rotated
     */
    public void recordMove(int levelNumber, int difficulty, int row, int col) {
        if (!recordingEnabled) {
            System.out.println("Recording is disabled, not recording move");
            return;
        }

        String levelKey = getLevelKey(levelNumber, difficulty);

        // Get or create the move list for this level
        List<Move> moves = levelMoves.computeIfAbsent(levelKey, k -> new ArrayList<>());

        // Get the current index for this level
        int currentIndex = currentMoveIndices.getOrDefault(levelKey, 0);

        // If we're not at the end of the history, truncate the history
        if (currentIndex < moves.size()) {
            moves = new ArrayList<>(moves.subList(0, currentIndex));
            levelMoves.put(levelKey, moves);
        }

        // Add the new move
        Move move = new Move(row, col);
        moves.add(move);

        // Update the current index
        currentMoveIndices.put(levelKey, moves.size());

        // Save the history
        saveHistory();

        System.out.println("Recorded move: " + move + " for level " + levelNumber + " at difficulty " + difficulty);
        System.out.println("History size: " + moves.size() + ", current index: " + currentMoveIndices.get(levelKey));
    }

    /**
     * Clears the move history for a level.
     *
     * @param levelNumber The level number
     * @param difficulty The difficulty level
     */
    public void clearHistory(int levelNumber, int difficulty) {
        String levelKey = getLevelKey(levelNumber, difficulty);
        levelMoves.remove(levelKey);
        currentMoveIndices.remove(levelKey);
        saveHistory();
    }

    /**
     * Gets whether there are previous moves available.
     *
     * @param levelNumber The level number
     * @param difficulty The difficulty level
     * @return true if there are previous moves, false otherwise
     */
    public boolean hasPreviousMove(int levelNumber, int difficulty) {
        String levelKey = getLevelKey(levelNumber, difficulty);
        int currentIndex = currentMoveIndices.getOrDefault(levelKey, 0);
        return currentIndex > 0;
    }

    /**
     * Gets whether there are next moves available.
     *
     * @param levelNumber The level number
     * @param difficulty The difficulty level
     * @return true if there are next moves, false otherwise
     */
    public boolean hasNextMove(int levelNumber, int difficulty) {
        String levelKey = getLevelKey(levelNumber, difficulty);
        List<Move> moves = levelMoves.getOrDefault(levelKey, Collections.emptyList());
        int currentIndex = currentMoveIndices.getOrDefault(levelKey, 0);
        return currentIndex < moves.size();
    }

    /**
     * Gets the previous move and decrements the current index.
     *
     * @param levelNumber The level number
     * @param difficulty The difficulty level
     * @return The previous move, or null if there are no previous moves
     */
    public Move getPreviousMove(int levelNumber, int difficulty) {
        String levelKey = getLevelKey(levelNumber, difficulty);
        List<Move> moves = levelMoves.getOrDefault(levelKey, Collections.emptyList());
        int currentIndex = currentMoveIndices.getOrDefault(levelKey, 0);

        if (currentIndex > 0) {
            currentIndex--;
            currentMoveIndices.put(levelKey, currentIndex);
            saveHistory(); // Save the updated index
            return moves.get(currentIndex);
        }

        return null;
    }

    /**
     * Gets the next move and increments the current index.
     *
     * @param levelNumber The level number
     * @param difficulty The difficulty level
     * @return The next move, or null if there are no next moves
     */
    public Move getNextMove(int levelNumber, int difficulty) {
        String levelKey = getLevelKey(levelNumber, difficulty);
        List<Move> moves = levelMoves.getOrDefault(levelKey, Collections.emptyList());
        int currentIndex = currentMoveIndices.getOrDefault(levelKey, 0);

        if (currentIndex < moves.size()) {
            Move move = moves.get(currentIndex);
            currentIndex++;
            currentMoveIndices.put(levelKey, currentIndex);
            saveHistory(); // Save the updated index
            return move;
        }

        return null;
    }

    /**
     * Gets the total number of moves for a level.
     *
     * @param levelNumber The level number
     * @param difficulty The difficulty level
     * @return The total number of moves
     */
    public int getTotalMoves(int levelNumber, int difficulty) {
        String levelKey = getLevelKey(levelNumber, difficulty);
        List<Move> moves = levelMoves.getOrDefault(levelKey, Collections.emptyList());
        return moves.size();
    }

    /**
     * Gets the current move index for a level.
     *
     * @param levelNumber The level number
     * @param difficulty The difficulty level
     * @return The current move index
     */
    public int getCurrentMoveIndex(int levelNumber, int difficulty) {
        String levelKey = getLevelKey(levelNumber, difficulty);
        return currentMoveIndices.getOrDefault(levelKey, 0);
    }

    /**
     * Saves the move history to disk.
     */
    private void saveHistory() {
        try {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("move_history.ser"))) {
                oos.writeObject(levelMoves);
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("move_indices.ser"))) {
                oos.writeObject(currentMoveIndices);
            }
        } catch (IOException e) {
            System.err.println("Error saving move history: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads the move history from disk.
     */
    @SuppressWarnings("unchecked")
    private void loadHistory() {
        try {
            File movesFile = new File("move_history.ser");
            if (movesFile.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(movesFile))) {
                    Map<String, List<Move>> loadedMoves = (Map<String, List<Move>>) ois.readObject();
                    levelMoves.clear();
                    levelMoves.putAll(loadedMoves);
                }
            }

            File indicesFile = new File("move_indices.ser");
            if (indicesFile.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(indicesFile))) {
                    Map<String, Integer> loadedIndices = (Map<String, Integer>) ois.readObject();
                    currentMoveIndices.clear();
                    currentMoveIndices.putAll(loadedIndices);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading move history: " + e.getMessage());
            e.printStackTrace();
        }
    }
}