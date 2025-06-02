package ija.ijaProject.game.levels;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameMove implements Serializable {
    private static final long serialVersionUID = 1L;
    public final int x, y;          // Координаты клетки
    public final int rotation;      // Угол поворота (90, 180, 270)
    public final long timestamp;    // Временная метка (опционально)
    private final List<GameMove> currentGameMoves = new ArrayList<>();
    private String currentGameLogFile = null;

    public GameMove(int x, int y, int rotation) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.timestamp = System.currentTimeMillis();
    }
}
