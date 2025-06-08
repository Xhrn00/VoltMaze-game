package ija.ijaProject.game.simulation;

import java.io.Serializable;

/**
 * Třída {@code NodeState} reprezentuje stav jednoho uzlu v herním poli.
 * Ukládá jeho pozici a počet provedených rotací.
 *
 * Třída implementuje rozhraní {@code Serializable}, což umožňuje její ukládání
 * a přenos v rámci záznamu nebo přehrávání hry.
 */
public class NodeState implements Serializable {
    public final int x, y;
    public final int rotationCount;

    /**
     * Vytvoří novou instanci {@code NodeState} se zadanými souřadnicemi a počtem rotací.
     *
     * @param x souřadnice X uzlu
     * @param y souřadnice Y uzlu
     * @param rotationCount počet rotací, které byly na uzlu provedeny
     */
    public NodeState(int x, int y, int rotationCount) {
        this.x = x;
        this.y = y;
        this.rotationCount = rotationCount;
    }
}

