package ija.ijaProject.game.simulation;

import ija.ijaProject.common.Position;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Třída {@code GameMove} představuje jeden tah hráče ve hře.
 * Obsahuje informace o pozici tahu (x, y), rotaci prvku a časovém razítku,
 * kdy byl tah proveden.
 *
 * Třída implementuje rozhraní {@code Serializable}, což umožňuje
 * ukládání a přenos objektů mezi různými částmi aplikace nebo jejich serializaci do souboru.
 */
public class GameMove implements Serializable {
    private static final long serialVersionUID = 1L;
    public final int x, y;
    public final int rotation;
    public final long timestamp;
    private final List<GameMove> currentGameMoves = new ArrayList<>();
    private String currentGameLogFile = null;

    /**
     * Vytvoří nový tah se zadanou pozicí a rotací.
     * Časové razítko se nastaví automaticky na aktuální čas.
     *
     * @param x souřadnice X tahu
     * @param y souřadnice Y tahu
     * @param rotation rotace prvku
     */
    public GameMove(int x, int y, int rotation) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.timestamp = System.currentTimeMillis();
    }
    public Position getPosition() {
        return new Position(x, y);
    }
}
