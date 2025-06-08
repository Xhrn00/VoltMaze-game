package ija.ijaProject.game.simulation;

import ija.ijaProject.common.GameNode;
import ija.ijaProject.game.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Třída {@code GameReplay} slouží k přehrávání záznamu tahů ve hře.
 * Umožňuje krokování vpřed a zpět v rámci záznamu hry a přepnutí do režimu "přehrávání".
 *
 * Uchovává seznam tahů, aktuální pozici v záznamu a zajišťuje aktualizaci zobrazení hry
 * pomocí poskytnutého callbacku.
 */
public class GameReplay {
    private final List<GameMove> moves;
    private int currentMoveIndex = -1;
    private boolean isPlayMode = false;
    private final Runnable refreshCallback;
    private final Game game;

    /**
     * Vytvoří novou instanci {@code GameReplay} s daným seznamem tahů, hrou a callbackem.
     *
     * @param moves seznam tahů k přehrání
     * @param game instance hry, ve které se přehrávání vykonává
     * @param refreshCallback funkce, která se zavolá po každé změně (např. pro překreslení UI)
     */
    public GameReplay(List<GameMove> moves, Game game, Runnable refreshCallback) {
        this.moves = moves;
        this.game = game;
        this.refreshCallback = refreshCallback;
        System.out.println("[Replay] Created with " + moves.size() + " moves.");
    }

    /**
     * Posune přehrávání o jeden tah dopředu a aplikuje příslušný tah na herní stav.
     * Pokud je přehrávač v režimu "play" nebo je na konci záznamu, akce se neprovede.
     */
    public void stepForward() {
        System.out.println("[Replay] stepForward called. isPlayMode=" + isPlayMode + ", currentMoveIndex=" + currentMoveIndex);
        if (isPlayMode || currentMoveIndex >= moves.size() - 1) {
            System.out.println("[Replay] Skipping stepForward: either in play mode or at end.");
            return;
        }
        currentMoveIndex++;
        System.out.println("[Replay] Applying move at index: " + currentMoveIndex);
        applyMove(moves.get(currentMoveIndex));
        refreshCallback.run();
    }

    /**
     * Vrátí se o jeden tah zpět a zruší účinek příslušného tahu ve hře.
     * Pokud je přehrávač v režimu "play" nebo je na začátku záznamu, akce se neprovede.
     */
    public void stepBackward() {
        System.out.println("[Replay] stepBackward called. isPlayMode=" + isPlayMode + ", currentMoveIndex=" + currentMoveIndex);
        if (isPlayMode || currentMoveIndex < 0) {
            System.out.println("[Replay] Skipping stepBackward: either in play mode or at beginning.");
            return;
        }
        System.out.println("[Replay] Reverting move at index: " + currentMoveIndex);
        revertMove(moves.get(currentMoveIndex));
        currentMoveIndex--;
        refreshCallback.run();
    }

    /**
     * Přepne režim z přehrávání zpět na normální herní režim.
     * Používá zkrácený záznam tahů až po aktuální krok a inicializuje hru do tohoto stavu.
     */
    public void switchToPlayMode() {
        System.out.println("[Replay] Switching to play mode.");
        System.out.println("!!!! NODE: " + NodeStateManager.getInstance().isReplayMode());
        NodeStateManager.getInstance().setReplayMode(false);
        System.out.println("!!!! NODE: " + NodeStateManager.getInstance().isReplayMode());
        this.isPlayMode = true;

        List<GameMove> trimmed = new ArrayList<>(moves.subList(0, currentMoveIndex + 1));
        System.out.println("[Replay] Trimmed moves to size: " + trimmed.size());

        NodeStateManager.getInstance().startFromPartialLog(trimmed);

    }

    /**
     * Aplikuje daný tah na aktuální stav hry — otáčí příslušným uzlem podle rotace.
     *
     * @param move tah, který má být aplikován
     */
    private void applyMove(GameMove move) {
        System.out.println("[Replay] Applying move: (" + move.x + ", " + move.y + ")");
        GameNode node = game.getGameNode(move.x, move.y);
        if (node != null) {
            for (int i = 0; i < move.rotation; i++) {
                node.turn();
            }
        } else {
            System.out.println("[Replay] Warning: Node is null in applyMove");
        }
    }

    /**
     * Vrátí zpět daný tah v herním stavu — simuluje zpětnou rotaci.
     *
     * @param move tah, který má být vrácen zpět
     */
    private void revertMove(GameMove move) {
        System.out.println("[Replay] Reverting move: (" + move.x + ", " + move.y + ")");
        GameNode node = game.getGameNode(move.x, move.y);
        if (node != null) {
            for (int i = 0; i < 3; i++) node.turn();
        } else {
            System.out.println("[Replay] Warning: Node is null in revertMove");
        }
    }
}