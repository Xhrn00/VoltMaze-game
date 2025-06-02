/**
 * Soubor: src/main/java/ija.ijaProject/game/levels/NodeStateManager.java
 *
 * Popis:
 *
 * Manages the saving and loading of node states for game levels.
 * This allows the game to remember the state of nodes (position, rotation)
 *  when a player exits a level and returns to it later.
 *
 * @Author: Yaroslav Hryn (xhryny00),Oleksandr Musiichuk (xmusii00)
 *
 */

package ija.ijaProject.game.levels;

import ija.ijaProject.common.GameNode;
import ija.ijaProject.common.Position;
import ija.ijaProject.common.Side;
import ija.ijaProject.game.Game;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Manages the saving and loading of node states for game levels.
 * This allows the game to remember the state of nodes (position, rotation)
 * when a player exits a level and returns to it later.
 */
public class NodeStateManager {
    private static NodeStateManager instance;
    private final String saveDir = "game_states";
    private List<GameMove> currentGameMoves;
    private String currentGameLogPath;
    private boolean isReplayMode = false;

    public boolean isReplayMode() {
        return isReplayMode;
    }


    public void setReplayMode(boolean replayMode) {
        this.isReplayMode = replayMode;
    }

    private NodeStateManager() {
        new File(saveDir).mkdirs();
    }

    public static synchronized NodeStateManager getInstance() {
        if (instance == null) {
            instance = new NodeStateManager();
        }
        return instance;
    }



    // Сохраняет начальное состояние (после рандомизации)
    public void saveInitialState(int level, int difficulty, Game game) {
        saveState("initial_", level, difficulty, game);
    }

    // Сохраняет прогресс игрока
    public void savePlayerProgress(int level, int difficulty, Game game) {
        saveState("progress_", level, difficulty, game);
    }

    // Загружает состояние для симуляции
    public void loadSimulationState(int level, int difficulty, Game game) {
        // 1. Загружаем начальное состояние
        loadState("initial_", level, difficulty, game);

        // 2. Поверх него загружаем прогресс игрока
        //loadState("progress_", level, difficulty, game);
    }

    private void saveState(String prefix, int level, int difficulty, Game game) {
        String filename = prefix + level + "_" + difficulty + ".dat";
        List<NodeState> states = new ArrayList<>();

        for (GameNode node : game.getNodes()) {
            Position pos = node.getPosition();
            states.add(new NodeState(pos.row(), pos.col(), node.getRotationCount()));
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(saveDir + "/" + filename))) {
            oos.writeObject(states);
        } catch (IOException e) {
            System.err.println("Error saving " + prefix + " state: " + e.getMessage());
        }
    }

    private void loadState(String prefix, int level, int difficulty, Game game) {
        String filename = saveDir + "/" + prefix + level + "_" + difficulty + ".dat";
        File file = new File(filename);

        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<NodeState> states = (List<NodeState>) ois.readObject();
            Map<Position, GameNode> nodeMap = game.getNodes().stream()
                    .collect(Collectors.toMap(GameNode::getPosition, Function.identity()));

            for (NodeState state : states) {
                GameNode node = nodeMap.get(new Position(state.x, state.y));
                if (node != null) {
                    node.resetRotationCount();
                    for (int i = 0; i < state.rotationCount; i++) {
                        node.turn();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading " + prefix + " state: " + e.getMessage());
        }
    }

    /**
     * Начинает запись новой игры
     */
    public void startNewGameLog(int levelNumber, int difficulty) {
        String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        currentGameLogPath = saveDir + "/moves_" + levelNumber + "_" + difficulty + "_" + timestamp + ".dat";
        currentGameMoves = new ArrayList<>();
    }

    /**
     * Логирует ход игрока
     */
    public void logMove(int x, int y, int rotation) {
        if (isReplayMode) {
            return; // Don't log anything during replay or state load
        }

        System.out.println("Move " + x + ", " + y + ", " + rotation);
        if (isReplayMode || currentGameMoves == null) return;

        currentGameMoves.add(new GameMove(x, y, rotation));
        saveMovesToFile();
    }

    /**
     * Загружает лог ходов для воспроизведения
     */
    public List<GameMove> loadGameMoves(int levelNumber, int difficulty) {
        File latestLog = findLatestLogFile(levelNumber, difficulty);
        if (latestLog == null) return Collections.emptyList();
        System.out.println("[LOAD] Loaded replay moves from file: " + latestLog.getName());



        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(latestLog))) {
            return (List<GameMove>) ois.readObject();
        } catch (Exception e) {
            System.err.println("Error loading game moves: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private File findLatestLogFile(int levelNumber, int difficulty) {
        File dir = new File(saveDir);
        String prefix = "moves_" + levelNumber + "_" + difficulty + "_";

        return Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .filter(f -> f.getName().startsWith(prefix))
                .max(Comparator.comparingLong(File::lastModified))
                .orElse(null);
    }

    private void saveMovesToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(currentGameLogPath))) {
            oos.writeObject(currentGameMoves);
        } catch (IOException e) {
            System.err.println("Error saving moves: " + e.getMessage());
        }
    }

}


