/**
 * Soubor: src/main/java/visualization/view/GameBridge.java
 *
 * Popis:
 * Třída GameBridge slouží jako most mezi herní logikou (Game) a JavaFX UI.
 *  Pravidelně kontroluje stav hry a při zjištění, že jsou všechny žárovky rozsvíceny,
 * vyvolá zaregistrované posluchače dokončení hry.
 *
 *
 * @Author: Yaroslav Hryn (xhryny00)
 * @Author: Oleksandr Musiichuk (xmusii00)
 *
 */


package visualization.view;

import ija.ijaProject.game.Game;
import visualization.common.ToolField;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * GameBridge connects the Game logic with the JavaFX UI.
 * It monitors the game state and notifies listeners when the game is completed.
 */
public class GameBridge {
    private final Game game;
    private final List<Runnable> completionListeners = new ArrayList<>();
    private boolean isCompleted = false;
    private Timer completionTimer;

    /**
     * Creates a new GameBridge for the specified game.
     *
     * @param game The game to monitor
     */
    public GameBridge(Game game) {
        this.game = game;
        startMonitoring();
    }

    /**
     * Adds a listener that will be called when the game is completed.
     *
     * @param listener The listener to add
     */
    public void addCompletionListener(Runnable listener) {
        completionListeners.add(listener);

        if (isCompleted) {
            listener.run();
        }
    }

    /**
     * Starts monitoring the game state for completion.
     */
    private void startMonitoring() {
        completionTimer = new Timer(true);
        completionTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isGameCompleted() && !isCompleted) {
                    isCompleted = true;
                    notifyCompletionListeners();
                    completionTimer.cancel();
                }
            }
        }, 1000, 1000);
    }

    /**
     * Notifies all completion listeners that the game is completed.
     */
    private void notifyCompletionListeners() {
        for (Runnable listener : completionListeners) {
            try {
                listener.run();
            } catch (Exception e) {
                System.err.println("Error notifying completion listener: " + e.getMessage());
            }
        }
    }

    /**
     * Checks if the game is completed by examining if all bulbs are lit.
     *
     * @return true if all bulbs are lit, false otherwise
     */
    private boolean isGameCompleted() {
        for (int row = 1; row <= game.rows(); row++) {
            for (int col = 1; col <= game.cols(); col++) {
                ToolField field = game.fieldAt(row, col);

                if (field != null && field.isBulb() && !field.light()) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Stops monitoring the game state.
     */
    public void stopMonitoring() {
        if (completionTimer != null) {
            completionTimer.cancel();
            completionTimer = null;
        }
    }
}