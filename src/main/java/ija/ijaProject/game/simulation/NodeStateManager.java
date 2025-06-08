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

package ija.ijaProject.game.simulation;

import ija.ijaProject.common.GameNode;
import ija.ijaProject.common.Position;
import ija.ijaProject.game.Game;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Třída {@code NodeStateManager} spravuje ukládání a načítání stavů uzlů pro jednotlivé úrovně hry.
 * Umožňuje uchování pokroku hráče, přehrávání tahů a obnovení stavu hry při návratu do úrovně.
 */
public class NodeStateManager {
    private static NodeStateManager instance;
    private final String saveDir = "game_states";
    private List<GameMove> currentGameMoves;
    private String currentGameLogPath;
    private boolean isReplayMode = false;

    /**
     * Vrací instanci {@code NodeStateManager} (singleton).
     *
     * @return instance správce stavu uzlů
     */
    public static synchronized NodeStateManager getInstance() {
        if (instance == null) {
            instance = new NodeStateManager();
        }
        return instance;
    }

    /**
     * Vrací, zda je aktivní režim přehrávání.
     *
     * @return {@code true}, pokud je aktivní přehrávání; jinak {@code false}
     */
    public boolean isReplayMode() {
        return isReplayMode;
    }

    /**
     * Nastaví, zda má být aktivní režim přehrávání.
     *
     * @param replayMode {@code true} pro aktivaci přehrávání; {@code false} pro běžný režim
     */
    public void setReplayMode(boolean replayMode) {
        this.isReplayMode = replayMode;
    }

    /**
     * Ověří, zda existuje uložený počáteční stav hry pro danou úroveň a obtížnost.
     *
     * @param levelNumber číslo úrovně
     * @param difficulty obtížnost hry
     * @return {@code true}, pokud existuje uložený počáteční stav; jinak {@code false}
     */
    public boolean hasSavedProgress(int levelNumber, int difficulty) {
        return initialStateExists(levelNumber, difficulty);
    }

    /**
     * Uloží počáteční stav uzlů pro danou úroveň a obtížnost.
     *
     * @param level číslo úrovně
     * @param difficulty obtížnost hry
     * @param game instance hry
     */
    public void saveInitialState(int level, int difficulty, Game game) {
        saveState("initial_", level, difficulty, game);
    }

    /**
     * Uloží aktuální pokrok hráče ve hře.
     *
     * @param level číslo úrovně
     * @param difficulty obtížnost hry
     * @param game instance hry
     */
    public void savePlayerProgress(int level, int difficulty, Game game) {
        saveState("progress_", level, difficulty, game);
    }

    /**
     * Načte simulovaný (počáteční) stav hry ze souboru.
     *
     * @param level číslo úrovně
     * @param difficulty obtížnost hry
     * @param game instance hry
     */
    public void loadSimulationState(int level, int difficulty, Game game) {
        loadState("initial_", level, difficulty, game);
    }

    /**
     * Spustí nový záznam tahů pro danou úroveň a obtížnost.
     *
     * @param levelNumber číslo úrovně
     * @param difficulty obtížnost hry
     */
    public void startNewGameLog(int levelNumber, int difficulty) {
        String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        currentGameLogPath = saveDir + "/moves_" + levelNumber + "_" + difficulty + "_" + timestamp + ".dat";
        currentGameMoves = new ArrayList<>();
    }

    /**
     * Zaloguje nový tah hráče. Pokud je aktivní režim přehrávání, operace je ignorována.
     *
     * @param x souřadnice X
     * @param y souřadnice Y
     * @param rotation počet rotací provedených na uzlu
     */
    public void logMove(int x, int y, int rotation) {
        if (isReplayMode) {
            return;
        }
        System.out.println("Move " + x + ", " + y + ", " + rotation);
        if (isReplayMode || currentGameMoves == null) return;

        currentGameMoves.add(new GameMove(x, y, rotation));
        saveMovesToFile();
    }

    /**
     * Načte seznam tahů ze souboru pro přehrávání hry.
     *
     * @param levelNumber číslo úrovně
     * @param difficulty obtížnost hry
     * @return seznam zaznamenaných tahů; pokud nejsou k dispozici, vrací prázdný seznam
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

    /**
     * Načte historii tahů a nastaví přehrávací režim.
     *
     * @param levelNumber číslo úrovně
     * @param difficulty obtížnost hry
     */
    public void loadExistingHistory(int levelNumber, int difficulty) {
        List<GameMove> savedMoves = loadGameMoves(levelNumber, difficulty);
        if (savedMoves == null) {
            currentGameMoves = new ArrayList<>();
        } else {
            currentGameMoves = new ArrayList<>(savedMoves);
        }
        setReplayMode(true);

        File latestLog = findLatestLogFile(levelNumber, difficulty);
        if (latestLog != null) {
            currentGameLogPath = latestLog.getAbsolutePath();
        }
    }

    /**
     * Nahradí záznam tahů zkrácenou verzí a uloží ji.
     *
     * @param trimmedMoves seznam tahů, které mají zůstat zachovány
     */
    public void startFromPartialLog(List<GameMove> trimmedMoves) {
        if (currentGameLogPath == null) {
            System.err.println("No existing game log path. Cannot trim.");
            return;
        }

        currentGameMoves = new ArrayList<>(trimmedMoves);
        saveMovesToFile();
    }


    /**
     * Privátní konstruktor, který vytvoří adresář pro ukládání, pokud ještě neexistuje.
     */
    private NodeStateManager() {
        new File(saveDir).mkdirs();
    }

    /**
     * Kontroluje, zda existuje uložený počáteční stav pro danou úroveň a obtížnost.
     *
     * @param levelNumber číslo úrovně
     * @param difficulty obtížnost
     * @return {@code true}, pokud existuje odpovídající uložený soubor
     */
    private boolean initialStateExists(int levelNumber, int difficulty) {
        File file = new File(saveDir + "/initial_" + levelNumber + "_" + difficulty + ".dat");
        return file.exists();
    }

    /**
     * Uloží stav všech uzlů ve hře do souboru s daným prefixem, úrovní a obtížností.
     *
     * @param prefix předpona názvu souboru (např. "initial" nebo "final")
     * @param level číslo úrovně
     * @param difficulty obtížnost
     * @param game instance hry, jejíž stav se ukládá
     */
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

    /**
     * Načte stav uzlů ze souboru a aplikuje jej na aktuální instanci hry.
     *
     * @param prefix předpona názvu souboru
     * @param level číslo úrovně
     * @param difficulty obtížnost
     * @param game instance hry, do které se stav načte
     */
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
     * Vyhledá nejnovější soubor se záznamem tahů pro danou úroveň a obtížnost.
     *
     * @param levelNumber číslo úrovně
     * @param difficulty obtížnost
     * @return nejnovější nalezený soubor nebo {@code null}, pokud žádný neexistuje
     */
    private File findLatestLogFile(int levelNumber, int difficulty) {
        File dir = new File(saveDir);
        String prefix = "moves_" + levelNumber + "_" + difficulty + "_";

        return Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .filter(f -> f.getName().startsWith(prefix))
                .max(Comparator.comparingLong(File::lastModified))
                .orElse(null);
    }

    /**
     * Uloží aktuální seznam tahů do souboru na předem určené cestě.
     */
    private void saveMovesToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(currentGameLogPath))) {
            oos.writeObject(currentGameMoves);
        } catch (IOException e) {
            System.err.println("Error saving moves: " + e.getMessage());
        }
    }


}


