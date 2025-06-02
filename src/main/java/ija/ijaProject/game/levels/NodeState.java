package ija.ijaProject.game.levels;

import java.io.Serializable;

public class NodeState implements Serializable {
    public final int x, y;
    public final int rotationCount;

    public NodeState(int x, int y, int rotationCount) {
        this.x = x;
        this.y = y;
        this.rotationCount = rotationCount;
    }
}

