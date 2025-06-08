/**
 * Soubor: src/main/java/ija.ijaProject/game/levels/GameLevels.java
 *
 * Popis:
 *  obsahuje statické definice jednotlivých úrovní hry 10*3
 * a vytvoři Hru
 *
 *
 * @Author: Yaroslav Hryn (xhryny00),Oleksandr Musiichuk (xmusii00)
 *
 */

package ija.ijaProject.game.levels;

import ija.ijaProject.common.GameNode;
import ija.ijaProject.common.Position;
import ija.ijaProject.common.Side;
import ija.ijaProject.game.Game;
import ija.ijaProject.game.simulation.GameMove;
import ija.ijaProject.game.simulation.GameReplay;
import ija.ijaProject.game.simulation.NodeStateManager;
import javafx.animation.PauseTransition;
import javafx.embed.swing.SwingNode;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
import visualization.EnvPresenter;


import javax.swing.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GameLevels class handles the creation and management of different game levels.
 * It provides methods to create and load specific levels based on level number and difficulty.
 */
public class GameLevels {

    // ==================== BEGINNER LEVELS (1-10) ====================

    private static final Object[][] LEVEL_1_BEGINNER = {
            {"P", 2, 2, Side.SOUTH},
            {"L", 3, 2, Side.EAST, Side.NORTH},
            {"L", 3, 3, Side.WEST, Side.SOUTH},
            {"L", 4, 3, Side.NORTH, Side.SOUTH},
            {"B", 5, 3, Side.NORTH}
    };



    private static final Object[][] LEVEL_2_BEGINNER = {
            {"P", 2, 2, Side.SOUTH},
            {"L", 3, 2, Side.NORTH, Side.SOUTH},
            {"L", 4, 2, Side.NORTH, Side.SOUTH},
            {"L", 5, 2, Side.NORTH, Side.EAST},
            {"L", 5, 3, Side.WEST, Side.EAST},
            {"L", 5, 4, Side.WEST, Side.SOUTH},
            {"B", 6, 4, Side.NORTH}
    };


    private static final Object[][] LEVEL_3_BEGINNER = {
            {"P", 2, 3, Side.SOUTH},
            {"L", 3, 3, Side.NORTH, Side.SOUTH, Side.EAST},
            {"L", 4, 3, Side.NORTH, Side.SOUTH},
            {"B", 5, 3, Side.NORTH},
            {"L", 3, 4, Side.WEST, Side.EAST},
            {"L", 3, 5, Side.WEST, Side.SOUTH},
            {"L", 4, 5, Side.NORTH, Side.SOUTH},
            {"B", 5, 5, Side.NORTH}
    };


    private static final Object[][] LEVEL_4_BEGINNER = {
            {"P", 2, 4, Side.SOUTH},
            {"L", 3, 4, Side.NORTH, Side.SOUTH, Side.WEST, Side.EAST},
            {"L", 4, 4, Side.NORTH, Side.SOUTH},
            {"L", 5, 4, Side.NORTH, Side.SOUTH},
            {"B", 6, 4, Side.NORTH},
            {"L", 3, 3, Side.EAST, Side.SOUTH},
            {"B", 4, 3, Side.NORTH},
            {"L", 3, 5, Side.WEST, Side.SOUTH},
            {"L", 4, 5, Side.NORTH, Side.EAST},
            {"B", 4, 6, Side.WEST}
    };



    private static final Object[][] LEVEL_5_BEGINNER = {
            {"P", 2, 5, Side.SOUTH},
            {"L", 3, 5, Side.NORTH, Side.SOUTH, Side.WEST, Side.EAST},
            {"L", 4, 5, Side.NORTH, Side.SOUTH,Side.WEST},
            {"B", 5, 5, Side.NORTH},
            {"L", 3, 4, Side.EAST, Side.WEST},
            {"L", 3, 3, Side.EAST, Side.SOUTH},
            {"B", 4, 3, Side.NORTH},
            {"L", 3, 6, Side.WEST, Side.EAST,Side.SOUTH},
            {"L", 3, 7, Side.WEST, Side.SOUTH},
            {"B", 4, 7, Side.NORTH},
            {"L", 4, 4, Side.EAST, Side.SOUTH},
            {"B", 5, 4, Side.NORTH},
            {"L", 4, 6, Side.NORTH, Side.SOUTH},
            {"B", 5, 6, Side.NORTH}
    };


    private static final Object[][] LEVEL_6_BEGINNER = {
            {"P", 2, 4, Side.SOUTH},
            {"L", 3, 4, Side.NORTH, Side.SOUTH, Side.EAST, Side.WEST},
            {"L", 4, 4, Side.NORTH, Side.SOUTH,Side.EAST},
            {"L", 5, 4, Side.NORTH, Side.SOUTH},
            {"L", 6, 4, Side.NORTH, Side.SOUTH, Side.EAST, Side.WEST},
            {"L", 7, 4, Side.NORTH, Side.SOUTH,Side.EAST},
            {"B", 8, 4, Side.NORTH},
            {"L", 3, 3, Side.EAST, Side.SOUTH},
            {"B", 4, 3, Side.NORTH},
            {"L", 3, 5, Side.WEST, Side.SOUTH},
            {"B", 4, 5, Side.NORTH},
            {"L", 6, 3, Side.SOUTH, Side.EAST},
            {"B", 7, 3, Side.NORTH},
            {"L", 6, 5, Side.WEST, Side.SOUTH},
            {"B", 7, 5, Side.NORTH}
    };


    private static final Object[][] LEVEL_7_BEGINNER = {
            {"P", 2, 6, Side.SOUTH},
            {"L", 3, 6, Side.NORTH, Side.SOUTH, Side.EAST, Side.WEST},
            {"L", 4, 6, Side.NORTH, Side.SOUTH},
            {"L", 5, 6, Side.NORTH, Side.SOUTH, Side.EAST, Side.WEST},
            {"L", 6, 6, Side.NORTH, Side.SOUTH},
            {"L", 7, 6, Side.NORTH, Side.SOUTH, Side.EAST, Side.WEST},
            {"L", 8, 6, Side.NORTH, Side.SOUTH,Side.EAST},
            {"B", 9, 6, Side.NORTH},
            {"L", 3, 5, Side.EAST, Side.WEST},
            {"B", 4, 5, Side.SOUTH},
            {"L", 3, 7, Side.WEST, Side.SOUTH},
            {"B", 4, 7, Side.NORTH},
            {"L", 5, 5, Side.EAST, Side.NORTH, Side.SOUTH},
            {"B", 6, 5, Side.NORTH},
            {"L", 7, 7, Side.WEST, Side.EAST},
            {"B", 8, 7, Side.WEST}
    };

    private static final Object[][] LEVEL_8_BEGINNER = {
            {"P", 2, 4, Side.SOUTH},
            {"L", 3, 4, Side.NORTH, Side.SOUTH, Side.EAST, Side.WEST},
            {"L", 4, 4, Side.NORTH, Side.SOUTH,Side.EAST},
            {"L", 5, 4, Side.NORTH, Side.SOUTH},
            {"L", 6, 4, Side.NORTH, Side.SOUTH, Side.EAST, Side.WEST},
            {"L", 7, 4, Side.NORTH, Side.SOUTH,Side.EAST},
            {"B", 8, 4, Side.NORTH},
            {"L", 3, 3, Side.EAST, Side.SOUTH},
            {"B", 4, 3, Side.NORTH},
            {"L", 3, 5, Side.WEST, Side.SOUTH},
            {"B", 4, 5, Side.NORTH},
            {"L", 6, 3, Side.SOUTH, Side.EAST},
            {"B", 7, 3, Side.NORTH},
            {"L", 6, 5, Side.WEST, Side.SOUTH},
            {"B", 7, 5, Side.NORTH}
    };


    private static final Object[][] LEVEL_9_BEGINNER = {
            {"P", 2, 4, Side.SOUTH},
            {"L", 3, 4, Side.NORTH, Side.SOUTH, Side.EAST, Side.WEST},
            {"L", 4, 4, Side.NORTH, Side.SOUTH,Side.EAST},
            {"L", 5, 4, Side.NORTH, Side.SOUTH},
            {"L", 6, 4, Side.NORTH, Side.SOUTH, Side.EAST, Side.WEST},
            {"L", 7, 4, Side.NORTH, Side.SOUTH,Side.EAST},
            {"B", 8, 4, Side.NORTH},
            {"L", 3, 3, Side.EAST, Side.SOUTH},
            {"B", 4, 3, Side.NORTH},
            {"L", 3, 5, Side.WEST, Side.SOUTH},
            {"B", 4, 5, Side.NORTH},
            {"L", 6, 3, Side.SOUTH, Side.EAST},
            {"B", 7, 3, Side.NORTH},
            {"L", 6, 5, Side.WEST, Side.SOUTH},
            {"B", 7, 5, Side.NORTH}
    };


    private static final Object[][] LEVEL_10_BEGINNER = {
            {"P", 2, 4, Side.SOUTH},
            {"L", 3, 4, Side.NORTH, Side.SOUTH, Side.EAST, Side.WEST},
            {"L", 4, 4, Side.NORTH, Side.SOUTH,Side.EAST},
            {"L", 5, 4, Side.NORTH, Side.SOUTH},
            {"L", 6, 4, Side.NORTH, Side.SOUTH, Side.EAST, Side.WEST},
            {"L", 7, 4, Side.NORTH, Side.SOUTH,Side.EAST},
            {"B", 8, 4, Side.NORTH},
            {"L", 3, 3, Side.EAST, Side.SOUTH},
            {"B", 4, 3, Side.NORTH},
            {"L", 3, 5, Side.WEST, Side.SOUTH},
            {"B", 4, 5, Side.NORTH},
            {"L", 6, 3, Side.SOUTH, Side.EAST},
            {"B", 7, 3, Side.NORTH},
            {"L", 6, 5, Side.WEST, Side.SOUTH},
            {"B", 7, 5, Side.NORTH}
    };



    private static final Object[][] LEVEL_11_INTERMEDIATE = {
            {"P", 2, 2, Side.SOUTH},
            {"L", 3, 2, Side.NORTH, Side.SOUTH},
            {"L", 4, 2, Side.NORTH, Side.EAST,Side.SOUTH},
            {"L", 4, 3, Side.WEST, Side.SOUTH,Side.NORTH},
            {"L", 5, 3, Side.NORTH, Side.EAST},
            {"L", 5, 4, Side.WEST, Side.SOUTH},
            {"L", 6, 4, Side.NORTH, Side.SOUTH},
            {"L", 7, 4, Side.NORTH, Side.EAST, Side.SOUTH},
            {"L", 8, 4, Side.NORTH, Side.EAST},
            {"L", 8, 5, Side.WEST, Side.SOUTH},
            {"L", 9, 5, Side.NORTH, Side.SOUTH},
            {"L",10, 5, Side.WEST, Side.NORTH},
            {"B", 3, 3, Side.SOUTH},
            {"B", 5, 2, Side.NORTH},
            {"B", 7, 5, Side.WEST},
            {"B",10, 4, Side.EAST}
    };

    private static final Object[][] LEVEL_12_INTERMEDIATE = {
            {"P", 5, 6, Side.SOUTH, Side.EAST,Side.WEST},

            {"L", 4, 3, Side.SOUTH, Side.EAST},
            {"L", 5, 3, Side.NORTH, Side.SOUTH},

            {"L", 3, 4, Side.WEST,  Side.EAST},
            {"L", 4, 4, Side.EAST,Side.WEST, Side.SOUTH},
            {"L", 5, 4, Side.NORTH, Side.SOUTH},
            {"L",  7, 4, Side.EAST, Side.WEST},

            {"L",  3, 5, Side.WEST,  Side.EAST,Side.SOUTH},
            {"L",  4, 5, Side.WEST,  Side.SOUTH,Side.NORTH},
            {"L",  5, 5, Side.NORTH, Side.EAST},
            {"L",  6, 5, Side.SOUTH,  Side.EAST},
            {"L",  7, 5, Side.WEST,  Side.NORTH},

            {"L",  3, 6, Side.WEST, Side.EAST},
            {"L",  6, 6, Side.NORTH,  Side.WEST},

            {"L",  4, 7, Side.WEST,  Side.SOUTH},
            {"L",  5, 7, Side.NORTH, Side.WEST,Side.SOUTH},
            {"L",  6, 7, Side.NORTH,  Side.SOUTH},


            {"B", 3, 3, Side.EAST},
            {"B", 6, 3, Side.NORTH},
            {"B", 7, 3, Side.EAST},
            {"B", 6 , 4, Side.NORTH},
            {"B",  4, 6, Side.EAST},
            {"B",  7, 7, Side.NORTH},
            {"B",  3, 7, Side.WEST}

    };

    private static final Object[][] LEVEL_13_INTERMEDIATE = {
            {"P", 4, 4, Side.SOUTH,Side.EAST},

            {"L", 4, 3, Side.SOUTH, Side.NORTH},
            {"L", 5, 3, Side.NORTH, Side.EAST},
            {"L", 6, 3, Side.SOUTH, Side.EAST},
            {"L", 7, 3, Side.NORTH, Side.EAST},

            {"L", 5, 4, Side.NORTH, Side.EAST,Side.WEST},
            {"L", 6, 4, Side.EAST,Side.WEST},

            {"L",  3, 5, Side.WEST, Side.SOUTH},
            {"L",  4, 5, Side.WEST, Side.NORTH},
            {"L",  5, 5, Side.WEST, Side.SOUTH},
            {"L",  6, 5, Side.SOUTH,  Side.WEST,Side.NORTH},
            {"L",  7, 5, Side.EAST,  Side.NORTH},

            {"L",  4, 6, Side.SOUTH, Side.EAST},
            {"L",  5, 6, Side.NORTH,  Side.SOUTH},
            {"L",  6, 6, Side.SOUTH,Side.NORTH, Side.EAST},
            {"L",  7, 6, Side.NORTH,  Side.WEST},

            {"L",  3, 7, Side.WEST,  Side.SOUTH},
            {"L",  4, 7, Side.NORTH, Side.WEST},
            {"L",  6, 7, Side.NORTH,  Side.SOUTH,Side.WEST},


            {"B", 3, 3, Side.SOUTH},
            {"B", 3, 4, Side.EAST},
            {"B", 7, 4, Side.WEST},
            {"B", 3 , 6, Side.EAST},
            {"B",  7, 7, Side.NORTH},
            {"B",  5, 7, Side.SOUTH}
    };

    private static final Object[][] LEVEL_14_INTERMEDIATE = {
            {"P", 6, 4, Side.NORTH,Side.EAST},

            {"L", 4, 3, Side.EAST, Side.NORTH},
            {"L", 5, 3, Side.EAST, Side.SOUTH},
            {"L", 6, 3, Side.SOUTH, Side.NORTH},
            {"L", 7, 3, Side.NORTH, Side.EAST},

            {"L", 4, 4, Side.SOUTH,Side.WEST},
            {"L", 5, 4, Side.EAST,Side.SOUTH,Side.NORTH},
            {"L", 7, 4, Side.NORTH,Side.WEST},

            {"L",  3, 5, Side.WEST, Side.SOUTH,Side.EAST},
            {"L",  4, 5, Side.EAST,Side.SOUTH, Side.NORTH},
            {"L",  5, 5, Side.WEST, Side.NORTH},
            {"L",  6, 5, Side.SOUTH,  Side.EAST,Side.WEST},

            {"L",  3, 6, Side.WEST, Side.EAST},
            {"L",  5, 6, Side.EAST,  Side.SOUTH},
            {"L",  6, 6, Side.SOUTH,Side.NORTH, Side.WEST},

            {"L",  4, 7, Side.WEST,  Side.SOUTH},
            {"L",  5, 7, Side.NORTH, Side.WEST},
            {"L",  6, 7, Side.NORTH, Side.WEST},
            {"L",  7, 7, Side.NORTH, Side.WEST},


            {"B", 3, 3, Side.SOUTH},
            {"B", 3, 4, Side.EAST},
            {"B", 7, 5, Side.NORTH},
            {"B", 4 , 6, Side.WEST},
            {"B",  7, 6, Side.NORTH},
            {"B",  3, 7, Side.WEST}
    };

    private static final Object[][] LEVEL_15_INTERMEDIATE = {
            {"P", 5, 4, Side.NORTH,Side.SOUTH},

            {"L", 3, 3, Side.SOUTH, Side.EAST},
            {"L", 4, 3, Side.SOUTH, Side.NORTH},
            {"L", 8, 3, Side.EAST, Side.NORTH},

            {"L", 3, 4, Side.SOUTH,Side.WEST},
            {"L", 4, 4, Side.EAST,Side.SOUTH,Side.NORTH},
            {"L", 6, 4, Side.NORTH,Side.WEST,Side.SOUTH},
            {"L", 7, 4, Side.NORTH,Side.SOUTH},
            {"L", 8, 4, Side.NORTH,Side.WEST},

            {"L",  4, 5, Side.SOUTH,Side.WEST},
            {"L",  5, 5, Side.EAST, Side.NORTH},

            {"L",  3, 6, Side.SOUTH, Side.WEST},
            {"L",  4, 6, Side.NORTH,  Side.SOUTH},
            {"L",  5, 6, Side.SOUTH,Side.NORTH, Side.WEST},
            {"L",  6, 6, Side.SOUTH,Side.NORTH, Side.WEST},
            {"L",  7, 6, Side.SOUTH,Side.NORTH, Side.WEST},
            {"L",  8, 6, Side.EAST,Side.NORTH, Side.WEST},

            {"L",  3, 7, Side.EAST,  Side.SOUTH},
            {"L",  8, 7, Side.WEST, Side.EAST},

            {"L",  3, 8, Side.SOUTH, Side.WEST},
            {"L",  4, 8, Side.NORTH,  Side.SOUTH},
            {"L",  5, 8, Side.SOUTH,Side.NORTH, Side.WEST},
            {"L",  6, 8, Side.SOUTH,Side.NORTH, Side.WEST},
            {"L",  7, 8, Side.SOUTH,Side.NORTH, Side.WEST},
            {"L",  8, 8, Side.NORTH, Side.WEST},


            {"B", 5, 3, Side.NORTH},
            {"B", 6, 3, Side.EAST},
            {"B", 7, 3, Side.SOUTH},
            {"B", 3 , 5, Side.EAST},
            {"B",  6, 5, Side.EAST},
            {"B",  7, 5, Side.EAST},
            {"B",  8, 5, Side.EAST},
            {"B",  4, 7, Side.NORTH},
            {"B",  5, 7, Side.EAST},
            {"B",  6, 7, Side.EAST},
            {"B",  7, 7, Side.EAST}
    };

    private static final Object[][] LEVEL_16_INTERMEDIATE = {
            {"P", 4, 6, Side.NORTH},

            {"L", 3, 3, Side.EAST, Side.SOUTH},
            {"L", 4, 3, Side.NORTH, Side.SOUTH},
            {"L", 5, 3, Side.SOUTH, Side.NORTH,Side.EAST},
            {"L", 6, 3, Side.NORTH, Side.SOUTH},
            {"L", 7, 3, Side.NORTH, Side.EAST},

            {"L", 3, 4, Side.SOUTH,Side.WEST,Side.EAST},
            {"L", 4, 4, Side.EAST,Side.NORTH},
            {"L", 6, 4, Side.SOUTH,Side.EAST},
            {"L", 7, 4, Side.NORTH,Side.EAST,Side.WEST},

            {"L",  3, 5, Side.WEST,Side.EAST},
            {"L",  4, 5, Side.WEST,Side.SOUTH},
            {"L",  7, 5, Side.WEST, Side.EAST},


            {"L",  3, 6, Side.WEST, Side.SOUTH},
            {"L",  7, 6, Side.EAST,  Side.NORTH,Side.WEST},


            {"L",  4, 7, Side.NORTH,  Side.SOUTH},
            {"L",  5, 7, Side.NORTH, Side.WEST,Side.SOUTH},
            {"L",  6, 7, Side.NORTH, Side.SOUTH},
            {"L",  7, 7, Side.NORTH, Side.WEST},


            {"B", 5, 4, Side.WEST},
            {"B", 5, 5, Side.NORTH},
            {"B", 6, 5, Side.WEST},
            {"B", 5 , 6, Side.EAST},
            {"B",  6, 6, Side.SOUTH},
            {"B",  3, 7, Side.SOUTH}
    };

    private static final Object[][] LEVEL_17_INTERMEDIATE = {
            {"P", 2, 2, Side.SOUTH},
            {"L", 3, 2, Side.NORTH, Side.SOUTH},
            {"L", 4, 2, Side.NORTH, Side.EAST,Side.SOUTH},
            {"L", 4, 3, Side.WEST, Side.SOUTH,Side.NORTH},
            {"L", 5, 3, Side.NORTH, Side.EAST},
            {"L", 5, 4, Side.WEST, Side.SOUTH},
            {"L", 6, 4, Side.NORTH, Side.SOUTH},
            {"L", 7, 4, Side.NORTH, Side.EAST, Side.SOUTH},
            {"L", 8, 4, Side.NORTH, Side.EAST},
            {"L", 8, 5, Side.WEST, Side.SOUTH},
            {"L", 9, 5, Side.NORTH, Side.SOUTH},
            {"L",10, 5, Side.WEST, Side.NORTH},
            {"B", 3, 3, Side.SOUTH},
            {"B", 5, 2, Side.NORTH},
            {"B", 7, 5, Side.WEST},
            {"B",10, 4, Side.EAST}

    };

    private static final Object[][] LEVEL_18_INTERMEDIATE = {
            {"P", 6, 4, Side.NORTH,Side.EAST},

            {"L", 4, 3, Side.EAST, Side.NORTH},
            {"L", 5, 3, Side.EAST, Side.SOUTH},
            {"L", 6, 3, Side.SOUTH, Side.NORTH},
            {"L", 7, 3, Side.NORTH, Side.EAST},

            {"L", 4, 4, Side.SOUTH,Side.WEST},
            {"L", 5, 4, Side.EAST,Side.SOUTH,Side.NORTH},
            {"L", 7, 4, Side.NORTH,Side.WEST},

            {"L",  3, 5, Side.WEST, Side.SOUTH,Side.EAST},
            {"L",  4, 5, Side.EAST,Side.SOUTH, Side.NORTH},
            {"L",  5, 5, Side.WEST, Side.NORTH},
            {"L",  6, 5, Side.SOUTH,  Side.EAST,Side.WEST},

            {"L",  3, 6, Side.WEST, Side.EAST},
            {"L",  5, 6, Side.EAST,  Side.SOUTH},
            {"L",  6, 6, Side.SOUTH,Side.NORTH, Side.WEST},

            {"L",  4, 7, Side.WEST,  Side.SOUTH},
            {"L",  5, 7, Side.NORTH, Side.WEST},
            {"L",  6, 7, Side.NORTH, Side.WEST},
            {"L",  7, 7, Side.NORTH, Side.WEST},


            {"B", 3, 3, Side.SOUTH},
            {"B", 3, 4, Side.EAST},
            {"B", 7, 5, Side.NORTH},
            {"B", 4 , 6, Side.WEST},
            {"B",  7, 6, Side.NORTH},
            {"B",  3, 7, Side.WEST}
    };

    private static final Object[][] LEVEL_19_INTERMEDIATE = {
            {"P", 4, 4, Side.SOUTH,Side.EAST},

            {"L", 4, 3, Side.SOUTH, Side.NORTH},
            {"L", 5, 3, Side.NORTH, Side.EAST},
            {"L", 6, 3, Side.SOUTH, Side.EAST},
            {"L", 7, 3, Side.NORTH, Side.EAST},

            {"L", 5, 4, Side.NORTH, Side.EAST,Side.WEST},
            {"L", 6, 4, Side.EAST,Side.WEST},

            {"L",  3, 5, Side.WEST, Side.SOUTH},
            {"L",  4, 5, Side.WEST, Side.NORTH},
            {"L",  5, 5, Side.WEST, Side.SOUTH},
            {"L",  6, 5, Side.SOUTH,  Side.WEST,Side.NORTH},
            {"L",  7, 5, Side.EAST,  Side.NORTH},

            {"L",  4, 6, Side.SOUTH, Side.EAST},
            {"L",  5, 6, Side.NORTH,  Side.SOUTH},
            {"L",  6, 6, Side.SOUTH,Side.NORTH, Side.EAST},
            {"L",  7, 6, Side.NORTH,  Side.WEST},

            {"L",  3, 7, Side.WEST,  Side.SOUTH},
            {"L",  4, 7, Side.NORTH, Side.WEST},
            {"L",  6, 7, Side.NORTH,  Side.SOUTH,Side.WEST},


            {"B", 3, 3, Side.SOUTH},
            {"B", 3, 4, Side.EAST},
            {"B", 7, 4, Side.WEST},
            {"B", 3 , 6, Side.EAST},
            {"B",  7, 7, Side.NORTH},
            {"B",  5, 7, Side.SOUTH}
    };

    private static final Object[][] LEVEL_20_INTERMEDIATE = {
            {"P", 5, 4, Side.NORTH,Side.SOUTH},

            {"L", 3, 3, Side.SOUTH, Side.EAST},
            {"L", 4, 3, Side.SOUTH, Side.NORTH},
            {"L", 8, 3, Side.EAST, Side.NORTH},

            {"L", 3, 4, Side.SOUTH,Side.WEST},
            {"L", 4, 4, Side.EAST,Side.SOUTH,Side.NORTH},
            {"L", 6, 4, Side.NORTH,Side.WEST,Side.SOUTH},
            {"L", 7, 4, Side.NORTH,Side.SOUTH},
            {"L", 8, 4, Side.NORTH,Side.WEST},

            {"L",  4, 5, Side.SOUTH,Side.WEST},
            {"L",  5, 5, Side.EAST, Side.NORTH},

            {"L",  3, 6, Side.SOUTH, Side.WEST},
            {"L",  4, 6, Side.NORTH,  Side.SOUTH},
            {"L",  5, 6, Side.SOUTH,Side.NORTH, Side.WEST},
            {"L",  6, 6, Side.SOUTH,Side.NORTH, Side.WEST},
            {"L",  7, 6, Side.SOUTH,Side.NORTH, Side.WEST},
            {"L",  8, 6, Side.EAST,Side.NORTH, Side.WEST},

            {"L",  3, 7, Side.EAST,  Side.SOUTH},
            {"L",  8, 7, Side.WEST, Side.EAST},

            {"L",  3, 8, Side.SOUTH, Side.WEST},
            {"L",  4, 8, Side.NORTH,  Side.SOUTH},
            {"L",  5, 8, Side.SOUTH,Side.NORTH, Side.WEST},
            {"L",  6, 8, Side.SOUTH,Side.NORTH, Side.WEST},
            {"L",  7, 8, Side.SOUTH,Side.NORTH, Side.WEST},
            {"L",  8, 8, Side.NORTH, Side.WEST},


            {"B", 5, 3, Side.NORTH},
            {"B", 6, 3, Side.EAST},
            {"B", 7, 3, Side.SOUTH},
            {"B", 3 , 5, Side.EAST},
            {"B",  6, 5, Side.EAST},
            {"B",  7, 5, Side.EAST},
            {"B",  8, 5, Side.EAST},
            {"B",  4, 7, Side.NORTH},
            {"B",  5, 7, Side.EAST},
            {"B",  6, 7, Side.EAST},
            {"B",  7, 7, Side.EAST}
    };

    // ==================== ADVANCED LEVELS (21-30) ====================

    private static final Object[][] LEVEL_21_ADVANCED = {
            {"P", 7, 7, Side.WEST,Side.SOUTH},

            {"L", 4, 3, Side.SOUTH, Side.EAST},
            {"L", 5, 3, Side.SOUTH, Side.NORTH,Side.EAST},
            {"L", 6, 3, Side.SOUTH, Side.NORTH},
            {"L", 7, 3, Side.EAST, Side.NORTH},

            {"L", 3, 4, Side.SOUTH,Side.WEST,Side.EAST},
            {"L", 4, 4, Side.WEST,Side.NORTH},
            {"L", 5, 4, Side.WEST,Side.SOUTH},
            {"L", 6, 4, Side.NORTH,Side.EAST},
            {"L", 7, 4, Side.WEST,Side.SOUTH},
            {"L", 8, 4, Side.NORTH,Side.WEST,Side.EAST},

            {"L",  3, 5, Side.EAST,Side.WEST},
            {"L",  5, 5, Side.SOUTH, Side.NORTH},
            {"L",  6, 5, Side.WEST, Side.NORTH,Side.SOUTH},

            {"L",  3, 6, Side.EAST, Side.WEST},
            {"L",  4, 6, Side.EAST,  Side.SOUTH},
            {"L",  5, 6, Side.NORTH, Side.EAST},
            {"L",  6, 6, Side.SOUTH,Side.EAST},
            {"L",  7, 6, Side.NORTH, Side.EAST},

            {"L",  3, 7, Side.EAST,  Side.SOUTH,Side.WEST},
            {"L",  4, 7, Side.WEST, Side.NORTH},
            {"L",  5, 7, Side.EAST,  Side.WEST},
            {"L",  6, 7, Side.WEST, Side.EAST},
            {"L",  8, 7, Side.WEST, Side.EAST,Side.NORTH},

            {"L",  3, 8, Side.SOUTH, Side.WEST},
            {"L",  5, 8, Side.WEST,  Side.SOUTH},
            {"L",  6, 8, Side.SOUTH,Side.NORTH, Side.WEST},

            {"B", 3, 3, Side.EAST},
            {"B", 8, 3, Side.EAST},

            {"B", 4, 5, Side.SOUTH},
            {"B", 7 , 5, Side.NORTH},
            {"B",  8, 5, Side.WEST},

            {"B",  8, 6, Side.EAST},

            {"B",  4, 8, Side.NORTH},
            {"B",  7, 8, Side.NORTH},
            {"B",  8, 8, Side.WEST}
    };

    private static final Object[][] LEVEL_22_ADVANCED = {
            {"P", 5, 5, Side.EAST,Side.NORTH},

            {"L", 3, 3, Side.SOUTH, Side.EAST},
            {"L", 4, 3, Side.SOUTH, Side.NORTH},
            {"L", 5, 3, Side.NORTH,Side.EAST},
            {"L", 7, 3, Side.EAST, Side.NORTH},

            {"L", 3, 4, Side.SOUTH,Side.WEST},
            {"L", 5, 4, Side.WEST,Side.SOUTH},
            {"L", 6, 4, Side.EAST,Side.NORTH},
            {"L", 7, 4, Side.WEST,Side.EAST},
            {"L", 8, 4, Side.WEST,Side.EAST},


            {"L",  6, 5, Side.EAST,Side.WEST},
            {"L",  7, 5, Side.EAST, Side.WEST},
            {"L",  8, 5, Side.WEST, Side.EAST},

            {"L",  3, 6, Side.EAST, Side.WEST,Side.SOUTH},
            {"L",  5, 6, Side.WEST,  Side.SOUTH,Side.EAST},
            {"L",  6, 6, Side.NORTH, Side.WEST},
            {"L",  7, 6, Side.SOUTH,Side.EAST,Side.WEST},
            {"L",  8, 6, Side.NORTH, Side.WEST},


            {"L",  3, 7, Side.EAST,  Side.SOUTH,Side.WEST},
            {"L",  4, 7, Side.SOUTH, Side.NORTH},
            {"L",  5, 7, Side.NORTH,Side.SOUTH,  Side.WEST},
            {"L",  6, 7, Side.SOUTH, Side.NORTH},
            {"L",  7, 7, Side.WEST, Side.EAST,Side.NORTH},

            {"L",  3, 8, Side.SOUTH, Side.WEST},
            {"L",  5, 8, Side.WEST,  Side.SOUTH},
            {"L",  7, 8, Side.SOUTH,Side.NORTH, Side.WEST},
            {"L",  8, 8, Side.WEST,  Side.NORTH},



            {"B", 6, 3, Side.SOUTH},
            {"B", 8, 3, Side.EAST},

            {"B", 4, 4, Side.NORTH},

            {"B", 3, 5, Side.EAST},
            {"B", 4 , 5, Side.SOUTH},

            {"B",  4, 6, Side.NORTH},

            {"B",  8, 7, Side.EAST},

            {"B",  4, 8, Side.NORTH},
            {"B",  6, 8, Side.SOUTH}
    };


    private static final Object[][] LEVEL_23_ADVANCED = {
            {"P", 6, 5, Side.SOUTH,Side.NORTH},

            {"L", 3, 3, Side.EAST, Side.SOUTH},
            {"L", 4, 3, Side.NORTH, Side.EAST},
            {"L", 5, 3, Side.EAST, Side.SOUTH},
            {"L", 6, 3, Side.NORTH, Side.SOUTH},

            {"L", 3, 4, Side.WEST, Side.EAST},
            {"L", 4, 4, Side.WEST, Side.SOUTH},
            {"L", 5, 4, Side.NORTH, Side.WEST},
            {"L", 7, 4, Side.NORTH, Side.EAST},

            {"L", 3, 5, Side.WEST, Side.SOUTH},
            {"L", 4, 5, Side.NORTH, Side.EAST},
            {"L",5, 5, Side.EAST, Side.SOUTH},
            {"L",7, 5, Side.WEST, Side.NORTH,Side.EAST},

            {"L", 4, 6, Side.WEST, Side.SOUTH,Side.EAST},
            {"L", 5, 6, Side.WEST, Side.NORTH,Side.EAST},
            {"L",6, 6, Side.EAST, Side.SOUTH},
            {"L",7, 6, Side.WEST, Side.NORTH},

            {"L", 3, 7, Side.WEST, Side.SOUTH},
            {"L",4, 7, Side.WEST, Side.NORTH},
            {"L",6, 7, Side.WEST, Side.SOUTH},

            {"B", 7, 3, Side.NORTH},
            {"B", 6, 4, Side.SOUTH},
            {"B", 3, 6, Side.EAST},
            {"B", 5, 7, Side.WEST},
            {"B", 7, 7, Side.NORTH}
    };

    private static final Object[][] LEVEL_24_ADVANCED = {
            {"P", 6, 8, Side.WEST,Side.SOUTH},

            {"L", 3, 3, Side.SOUTH, Side.EAST},
            {"L", 5, 3, Side.SOUTH, Side.EAST},
            {"L", 6, 3, Side.NORTH,Side.EAST,Side.SOUTH},
            {"L", 7, 3, Side.SOUTH, Side.NORTH},
            {"L", 8, 3, Side.EAST, Side.NORTH},

            {"L", 3, 4, Side.SOUTH,Side.WEST,Side.EAST},
            {"L", 6, 4, Side.WEST,Side.SOUTH,Side.EAST},
            {"L", 8, 4, Side.EAST,Side.WEST},

            {"L",  3, 5, Side.EAST,Side.WEST},
            {"L",  5, 5, Side.SOUTH, Side.NORTH},
            {"L",  6, 5, Side.WEST, Side.EAST,Side.NORTH},
            {"L",  8, 5, Side.WEST, Side.EAST},

            {"L",  3, 6, Side.WEST,Side.SOUTH},
            {"L",  4, 6, Side.SOUTH,Side.NORTH},
            {"L",  5, 6, Side.NORTH, Side.EAST},
            {"L",  6, 6, Side.SOUTH,Side.WEST},
            {"L",  7, 6, Side.NORTH, Side.WEST,Side.EAST},
            {"L",  8, 6, Side.WEST,Side.EAST},

            {"L",  3, 7, Side.EAST, Side.SOUTH},
            {"L",  5, 7, Side.SOUTH, Side.EAST,Side.WEST},
            {"L",  6, 7, Side.NORTH,Side.SOUTH,  Side.EAST},
            {"L",  7, 7, Side.WEST, Side.NORTH},
            {"L",  8, 7, Side.WEST, Side.EAST},

            {"L",  3, 8, Side.SOUTH, Side.WEST},
            {"L",  4, 8, Side.NORTH,  Side.SOUTH},
            {"L",  5, 8, Side.NORTH, Side.WEST},

            {"B", 4, 3, Side.NORTH},

            {"B", 4, 4, Side.NORTH},
            {"B", 5, 4, Side.WEST},
            {"B", 7, 4, Side.NORTH},

            {"B", 4, 5, Side.SOUTH},
            {"B", 7 , 5, Side.EAST},

            {"B",  4, 7, Side.NORTH},

            {"B",  7, 8, Side.NORTH},
            {"B",  8, 8, Side.WEST}

    };

    private static final Object[][] LEVEL_25_ADVANCED = {
            {"P", 7, 7, Side.WEST,Side.NORTH,Side.EAST},

            {"L", 3, 3, Side.SOUTH, Side.EAST},
            {"L", 5, 3, Side.SOUTH, Side.EAST},
            {"L", 6, 3, Side.NORTH,Side.SOUTH},
            {"L", 7, 3, Side.EAST, Side.NORTH},

            {"L", 3, 4, Side.SOUTH,Side.WEST,Side.EAST},
            {"L", 5, 4, Side.WEST,Side.EAST},
            {"L", 7, 4, Side.WEST,Side.SOUTH},
            {"L", 8, 4, Side.NORTH,Side.WEST,Side.EAST},

            {"L",  3, 5, Side.EAST,Side.WEST},
            {"L",  5, 5, Side.SOUTH, Side.WEST},
            {"L",  6, 5, Side.WEST, Side.EAST,Side.NORTH},
            {"L",  8, 5, Side.WEST, Side.EAST},

            {"L",  3, 6, Side.WEST,Side.EAST},
            {"L",  4, 6, Side.WEST,Side.EAST},
            {"L",  6, 6, Side.NORTH, Side.EAST,Side.WEST},
            {"L",  7, 6, Side.EAST,Side.WEST},
            {"L",  8, 6, Side.WEST,Side.EAST},

            {"L",  3, 7, Side.EAST, Side.SOUTH,Side.WEST},
            {"L",  4, 7, Side.SOUTH, Side.NORTH,Side.WEST},
            {"L",  5, 7, Side.NORTH,Side.SOUTH},
            {"L",  6, 7, Side.WEST, Side.NORTH,Side.SOUTH},

            {"L",  3, 8, Side.SOUTH, Side.WEST},
            {"L",  6, 8, Side.NORTH,  Side.SOUTH},
            {"L",  7, 8, Side.NORTH, Side.WEST,Side.SOUTH},

            {"B", 4, 3, Side.NORTH},
            {"B", 8, 3, Side.EAST},

            {"B", 4, 4, Side.NORTH},
            {"B", 6, 4, Side.EAST},

            {"B", 4, 5, Side.EAST},
            {"B", 7 , 5, Side.EAST},

            {"B",  5, 6, Side.SOUTH},

            {"B",  8, 7, Side.WEST},

            {"B",  4, 8, Side.NORTH},
            {"B",  5, 8, Side.SOUTH},
            {"B",  8, 8, Side.NORTH}
    };


    private static final Object[][] LEVEL_26_ADVANCED = {
            {"P", 5, 5, Side.EAST,Side.NORTH},

            {"L", 3, 3, Side.SOUTH, Side.EAST},
            {"L", 4, 3, Side.SOUTH, Side.NORTH},
            {"L", 5, 3, Side.NORTH,Side.EAST},
            {"L", 7, 3, Side.EAST, Side.NORTH},

            {"L", 3, 4, Side.SOUTH,Side.WEST},
            {"L", 5, 4, Side.WEST,Side.SOUTH},
            {"L", 6, 4, Side.EAST,Side.NORTH},
            {"L", 7, 4, Side.WEST,Side.EAST},
            {"L", 8, 4, Side.WEST,Side.EAST},

            {"L",  6, 5, Side.EAST,Side.WEST},
            {"L",  7, 5, Side.EAST, Side.WEST},
            {"L",  8, 5, Side.WEST, Side.EAST},

            {"L",  3, 6, Side.EAST, Side.WEST,Side.SOUTH},
            {"L",  5, 6, Side.WEST,  Side.SOUTH,Side.EAST},
            {"L",  6, 6, Side.NORTH, Side.WEST},
            {"L",  7, 6, Side.SOUTH,Side.EAST,Side.WEST},
            {"L",  8, 6, Side.NORTH, Side.WEST},

            {"L",  3, 7, Side.EAST,  Side.SOUTH,Side.WEST},
            {"L",  4, 7, Side.SOUTH, Side.NORTH},
            {"L",  5, 7, Side.NORTH,Side.SOUTH,  Side.WEST},
            {"L",  6, 7, Side.SOUTH, Side.NORTH},
            {"L",  7, 7, Side.WEST, Side.EAST,Side.NORTH},

            {"L",  3, 8, Side.SOUTH, Side.WEST},
            {"L",  5, 8, Side.WEST,  Side.SOUTH},
            {"L",  7, 8, Side.SOUTH,Side.NORTH, Side.WEST},
            {"L",  8, 8, Side.WEST,  Side.NORTH},

            {"B", 6, 3, Side.SOUTH},
            {"B", 8, 3, Side.EAST},

            {"B", 4, 4, Side.NORTH},

            {"B", 3, 5, Side.EAST},
            {"B", 4 , 5, Side.SOUTH},

            {"B",  4, 6, Side.NORTH},

            {"B",  8, 7, Side.EAST},

            {"B",  4, 8, Side.NORTH},
            {"B",  6, 8, Side.SOUTH}
    };

    private static final Object[][] LEVEL_27_ADVANCED = {
            {"P", 6, 5, Side.SOUTH,Side.NORTH},

            {"L", 3, 3, Side.EAST, Side.SOUTH},
            {"L", 4, 3, Side.NORTH, Side.EAST},
            {"L", 5, 3, Side.EAST, Side.SOUTH},
            {"L", 6, 3, Side.NORTH, Side.SOUTH},

            {"L", 3, 4, Side.WEST, Side.EAST},
            {"L", 4, 4, Side.WEST, Side.SOUTH},
            {"L", 5, 4, Side.NORTH, Side.WEST},
            {"L", 7, 4, Side.NORTH, Side.EAST},

            {"L", 3, 5, Side.WEST, Side.SOUTH},
            {"L", 4, 5, Side.NORTH, Side.EAST},
            {"L",5, 5, Side.EAST, Side.SOUTH},
            {"L",7, 5, Side.WEST, Side.NORTH,Side.EAST},

            {"L", 4, 6, Side.WEST, Side.SOUTH,Side.EAST},
            {"L", 5, 6, Side.WEST, Side.NORTH,Side.EAST},
            {"L",6, 6, Side.EAST, Side.SOUTH},
            {"L",7, 6, Side.WEST, Side.NORTH},

            {"L", 3, 7, Side.WEST, Side.SOUTH},
            {"L",4, 7, Side.WEST, Side.NORTH},
            {"L",6, 7, Side.WEST, Side.SOUTH},

            {"B", 7, 3, Side.NORTH},
            {"B", 6, 4, Side.SOUTH},
            {"B", 3, 6, Side.EAST},
            {"B", 5, 7, Side.WEST},
            {"B", 7, 7, Side.NORTH}
    };

    private static final Object[][] LEVEL_28_ADVANCED = {
            {"P", 7, 7, Side.WEST,Side.SOUTH},

            {"L", 4, 3, Side.SOUTH, Side.EAST},
            {"L", 5, 3, Side.SOUTH, Side.NORTH,Side.EAST},
            {"L", 6, 3, Side.SOUTH, Side.NORTH},
            {"L", 7, 3, Side.EAST, Side.NORTH},

            {"L", 3, 4, Side.SOUTH,Side.WEST,Side.EAST},
            {"L", 4, 4, Side.WEST,Side.NORTH},
            {"L", 5, 4, Side.WEST,Side.SOUTH},
            {"L", 6, 4, Side.NORTH,Side.EAST},
            {"L", 7, 4, Side.WEST,Side.SOUTH},
            {"L", 8, 4, Side.NORTH,Side.WEST,Side.EAST},

            {"L",  3, 5, Side.EAST,Side.WEST},
            {"L",  5, 5, Side.SOUTH, Side.NORTH},
            {"L",  6, 5, Side.WEST, Side.NORTH,Side.SOUTH},

            {"L",  3, 6, Side.EAST, Side.WEST},
            {"L",  4, 6, Side.EAST,  Side.SOUTH},
            {"L",  5, 6, Side.NORTH, Side.EAST},
            {"L",  6, 6, Side.SOUTH,Side.EAST},
            {"L",  7, 6, Side.NORTH, Side.EAST},

            {"L",  3, 7, Side.EAST,  Side.SOUTH,Side.WEST},
            {"L",  4, 7, Side.WEST, Side.NORTH},
            {"L",  5, 7, Side.EAST,  Side.WEST},
            {"L",  6, 7, Side.WEST, Side.EAST},
            {"L",  8, 7, Side.WEST, Side.EAST,Side.NORTH},

            {"L",  3, 8, Side.SOUTH, Side.WEST},
            {"L",  5, 8, Side.WEST,  Side.SOUTH},
            {"L",  6, 8, Side.SOUTH,Side.NORTH, Side.WEST},

            {"B", 3, 3, Side.EAST},
            {"B", 8, 3, Side.EAST},

            {"B", 4, 5, Side.SOUTH},
            {"B", 7 , 5, Side.NORTH},
            {"B",  8, 5, Side.WEST},

            {"B",  8, 6, Side.EAST},

            {"B",  4, 8, Side.NORTH},
            {"B",  7, 8, Side.NORTH},
            {"B",  8, 8, Side.WEST}
    };

    private static final Object[][] LEVEL_29_ADVANCED = {
            {"P", 7, 7, Side.WEST,Side.NORTH,Side.EAST},

            {"L", 3, 3, Side.SOUTH, Side.EAST},
            {"L", 5, 3, Side.SOUTH, Side.EAST},
            {"L", 6, 3, Side.NORTH,Side.SOUTH},
            {"L", 7, 3, Side.EAST, Side.NORTH},

            {"L", 3, 4, Side.SOUTH,Side.WEST,Side.EAST},
            {"L", 5, 4, Side.WEST,Side.EAST},
            {"L", 7, 4, Side.WEST,Side.SOUTH},
            {"L", 8, 4, Side.NORTH,Side.WEST,Side.EAST},

            {"L",  3, 5, Side.EAST,Side.WEST},
            {"L",  5, 5, Side.SOUTH, Side.WEST},
            {"L",  6, 5, Side.WEST, Side.EAST,Side.NORTH},
            {"L",  8, 5, Side.WEST, Side.EAST},

            {"L",  3, 6, Side.WEST,Side.EAST},
            {"L",  4, 6, Side.WEST,Side.EAST},
            {"L",  6, 6, Side.NORTH, Side.EAST,Side.WEST},
            {"L",  7, 6, Side.EAST,Side.WEST},
            {"L",  8, 6, Side.WEST,Side.EAST},

            {"L",  3, 7, Side.EAST, Side.SOUTH,Side.WEST},
            {"L",  4, 7, Side.SOUTH, Side.NORTH,Side.WEST},
            {"L",  5, 7, Side.NORTH,Side.SOUTH},
            {"L",  6, 7, Side.WEST, Side.NORTH,Side.SOUTH},

            {"L",  3, 8, Side.SOUTH, Side.WEST},
            {"L",  6, 8, Side.NORTH,  Side.SOUTH},
            {"L",  7, 8, Side.NORTH, Side.WEST,Side.SOUTH},

            {"B", 4, 3, Side.NORTH},
            {"B", 8, 3, Side.EAST},

            {"B", 4, 4, Side.NORTH},
            {"B", 6, 4, Side.EAST},

            {"B", 4, 5, Side.EAST},
            {"B", 7 , 5, Side.EAST},

            {"B",  5, 6, Side.SOUTH},

            {"B",  8, 7, Side.WEST},

            {"B",  4, 8, Side.NORTH},
            {"B",  5, 8, Side.SOUTH},
            {"B",  8, 8, Side.NORTH}
    };


    private static final Object[][] LEVEL_30_ADVANCED = {
            {"P", 6, 8, Side.WEST,Side.SOUTH},

            {"L", 3, 3, Side.SOUTH, Side.EAST},
            {"L", 5, 3, Side.SOUTH, Side.EAST},
            {"L", 6, 3, Side.NORTH,Side.EAST,Side.SOUTH},
            {"L", 7, 3, Side.SOUTH, Side.NORTH},
            {"L", 8, 3, Side.EAST, Side.NORTH},

            {"L", 3, 4, Side.SOUTH,Side.WEST,Side.EAST},
            {"L", 6, 4, Side.WEST,Side.SOUTH,Side.EAST},
            {"L", 8, 4, Side.EAST,Side.WEST},

            {"L",  3, 5, Side.EAST,Side.WEST},
            {"L",  5, 5, Side.SOUTH, Side.NORTH},
            {"L",  6, 5, Side.WEST, Side.EAST,Side.NORTH},
            {"L",  8, 5, Side.WEST, Side.EAST},

            {"L",  3, 6, Side.WEST,Side.SOUTH},
            {"L",  4, 6, Side.SOUTH,Side.NORTH},
            {"L",  5, 6, Side.NORTH, Side.EAST},
            {"L",  6, 6, Side.SOUTH,Side.WEST},
            {"L",  7, 6, Side.NORTH, Side.WEST,Side.EAST},
            {"L",  8, 6, Side.WEST,Side.EAST},

            {"L",  3, 7, Side.EAST, Side.SOUTH},
            {"L",  5, 7, Side.SOUTH, Side.EAST,Side.WEST},
            {"L",  6, 7, Side.NORTH,Side.SOUTH,  Side.EAST},
            {"L",  7, 7, Side.WEST, Side.NORTH},
            {"L",  8, 7, Side.WEST, Side.EAST},

            {"L",  3, 8, Side.SOUTH, Side.WEST},
            {"L",  4, 8, Side.NORTH,  Side.SOUTH},
            {"L",  5, 8, Side.NORTH, Side.WEST},

            {"B", 4, 3, Side.NORTH},

            {"B", 4, 4, Side.NORTH},
            {"B", 5, 4, Side.WEST},
            {"B", 7, 4, Side.NORTH},

            {"B", 4, 5, Side.SOUTH},
            {"B", 7 , 5, Side.EAST},

            {"B",  4, 7, Side.NORTH},

            {"B",  7, 8, Side.NORTH},
            {"B",  8, 8, Side.WEST}
    };


    /**
     * Vytvoří JavaFX SwingNode obsahující herní úroveň.
     *
     * zobrazi spravné řešení pak nahodně otočí Gamenode,
     * a zobrazi ho.
     * Taky pomovci ToolTip po držení kurzoru nad jednym políčkem
     * zobrazi aktualní a potřebný pro spravné řešení počet otočení
     *
     *
     * @param levelNumber           číslo úrovně
     * @param difficulty            obtížnost
     * @param levelCompletedCallback  Oznamení, ktere se vyvolá při dokončení úrovně
     * @return SwingNode,            Swingový panel s hrou
     */
    public static SwingNode createGameLevel(int levelNumber, int difficulty, Runnable levelCompletedCallback, boolean isSimulation) {
        SwingNode swingNode = new SwingNode();
        boolean hasSaved = NodeStateManager.getInstance().hasSavedProgress(levelNumber, difficulty);

        if(hasSaved) {
            System.out.println("Saves exists");
        }
        else System.out.println("doesnt Exitst");

        Object[][] levelDef = getLevelDefinition(levelNumber, difficulty);

        SwingUtilities.invokeLater(() -> {
            try {
                int gridSize = getGridSizeForDifficulty(difficulty);
                Game game = Game.create(gridSize, gridSize + 2);

                for (Object[] n : levelDef) {
                    String type = (String) n[0];
                    int row = (Integer) n[1];
                    int col = (Integer) n[2];
                    Position p = new Position(row, col);
                    Side[] sides = new Side[n.length - 3];
                    for (int i = 3; i < n.length; i++) {
                        sides[i - 3] = (Side) n[i];
                    }
                    switch (type) {
                        case "L" -> game.createLinkNode(p, sides);
                        case "B" -> game.createBulbNode(p, sides[0]);
                        case "P" -> game.createPowerNode(p, sides);
                    }
                }

                game.init();
                Game solvedGame = game.deepCopy();
                Platform.runLater(() -> swingNode.getProperties().put("solvedGame", solvedGame));

                if (hasSaved) {

                    System.out.println("Simulation mode: Loading saved scrambled state directly");

                    NodeStateManager.getInstance().setReplayMode(true);
                    NodeStateManager.getInstance().loadSimulationState(levelNumber, difficulty, game);
                    NodeStateManager.getInstance().loadExistingHistory(levelNumber, difficulty);

                    List<GameMove> savedMoves = NodeStateManager.getInstance().loadGameMoves(levelNumber, difficulty);


                    EnvPresenter playPr = new EnvPresenter(game);
                    playPr.setReplayMode(true);
                    playPr.init();


                    Runnable refreshCallback = () -> {
                        Platform.runLater(() -> {
                            playPr.refreshViews();
                            if (playPr.inReplayMode()) {
                                playPr.disableUserClicks();
                            }
                        });
                    };

                    GameReplay replay = new GameReplay(savedMoves, game, refreshCallback);
                    swingNode.getProperties().put("replay", replay);
                    JPanel playPanel = playPr.getGamePanel();
                    SwingUtilities.invokeLater(() -> {
                        Platform.runLater(() -> {
                            swingNode.setUserData(playPr);
                            swingNode.setContent(playPr.getGamePanel());

                            swingNode.getProperties().put("game", game);
                            swingNode.getProperties().put("levelNumber", levelNumber);
                            swingNode.getProperties().put("difficulty", difficulty);


                            setupTooltipWithRotationInfo(swingNode, game, solvedGame);
                        });
                    });

                } else {
                    NodeStateManager.getInstance().setReplayMode(false);

                    EnvPresenter solvedPr = new EnvPresenter(game);
                    solvedPr.init();

                    SwingUtilities.invokeLater(() -> {
                        JPanel solvedPanel = solvedPr.getGamePanel();
                        System.out.println("SolvedPanel = " + solvedPanel);
                        swingNode.setContent(solvedPanel);
                    });

                    Platform.runLater(() -> {
                        PauseTransition pause = new PauseTransition(Duration.seconds(1));
                        pause.setOnFinished(evt -> SwingUtilities.invokeLater(() -> {

                            List<Position> posList = game.getNodes().stream()
                                    .map(GameNode::getPosition).toList();
                            Random rnd = new Random();
                            int moves = 10 + difficulty * 5;
                            do {
                                for (int i = 0; i < moves; i++) {
                                    game.rotateNode(posList.get(rnd.nextInt(posList.size())));
                                }
                            } while (game.anyBulbLit());

                            NodeStateManager.getInstance().saveInitialState(levelNumber, difficulty, game);

                            for (GameNode node : game.getNodes()) {
                                node.resetRotationCount();
                            }

                            NodeStateManager.getInstance().startNewGameLog(levelNumber, difficulty);


                            EnvPresenter playPr = new EnvPresenter(game);
                            if (levelCompletedCallback != null) {
                                final boolean[] initialCheckDone = {false};
                                playPr.setLevelCompletedCallback(() -> {
                                    if (!initialCheckDone[0]) {
                                        initialCheckDone[0] = true;
                                        System.out.println("Skipping initial completion check");
                                        return;
                                    }
                                    levelCompletedCallback.run();
                                });
                            }
                            for (GameNode node : game.getNodes()) {
                                node.addObserver(observable -> {

                                    NodeStateManager.getInstance().savePlayerProgress(levelNumber, difficulty, game);
                                });
                            }
                            playPr.init();

                            SwingUtilities.invokeLater(() -> {
                                Platform.runLater(() -> {
                                    System.out.println("PlayPrPanel = " + playPr.getGamePanel());
                                    swingNode.setUserData(playPr);
                                    swingNode.setContent(playPr.getGamePanel());

                                    swingNode.getProperties().put("game", game);
                                    swingNode.getProperties().put("levelNumber", levelNumber);
                                    swingNode.getProperties().put("difficulty", difficulty);

                                    setupTooltipWithRotationInfo(swingNode, game, solvedGame);
                                });
                            });
                        }));
                        pause.play();
                    });
                }

            } catch (Exception e) {
                System.err.println("ERROR during initialization: " + e.getMessage());
                e.printStackTrace();
            }
        });

        return swingNode;
    }




    private static void setupTooltipWithRotationInfo(SwingNode swingNode, Game game, Game solvedGame) {
        Tooltip tip = new Tooltip();
        tip.setShowDelay(Duration.seconds(1));
        Tooltip.install(swingNode, tip);

        final int cols = game.cols();
        final int rows = game.rows();

        swingNode.setOnMouseMoved(ev -> {
            Bounds b = swingNode.getLayoutBounds();
            double w = b.getWidth(), h = b.getHeight();
            double cellW = w / cols, cellH = h / rows;

            int col = Math.min(cols, Math.max(1, (int) (ev.getX() / cellW) + 1));
            int row = Math.min(rows, Math.max(1, (int) (ev.getY() / cellH) + 1));

            Set<Side> cur = game.getGameNode(row, col).getConnectors();
            Set<Side> tgt = solvedGame.getGameNode(row, col).getConnectors();

            int remaining = rotationsNeeded(cur, tgt);
            int actual = game.getGameNode(row, col).getRotationCount();

            tip.setText("GameNode " + row + "," + col +
                    "\n Need Rotations: " + remaining +
                    "\n Actual Rotations: " + actual);
        });

        swingNode.setOnMouseExited(ev -> tip.hide());
    }



    /**
     * Vrátí Side otočeny o 90°
     *
     * @param cur aktuální množina connectorů (ke kterým Side ma connektery)
     * @param tgt cílová množina connectorů (ke kterým Side ma connektery)
     * @return počet otočení
     */
    private static int rotationsNeeded(Set<Side> cur, Set<Side> tgt) {
        for (int k = 0; k < 4; k++) {
            final int kk = k;
            Set<Side> rot = cur.stream()
                    .map(s -> rotateCW(s, kk))
                    .collect(Collectors.toSet());
            if (rot.equals(tgt)) return kk;
        }
        return 0;
    }

    /**
     * Otočí stranu o 90°
     *
     * @param s     vstupní strana
     * @param times počet otočení
     * @return  strana po otočení
     */
    private static Side rotateCW(Side s, int times) {
        Side r = s;
        for (int i = 0; i < times; i++) {
            r = switch(r) {
                case NORTH -> Side.EAST;
                case EAST  -> Side.SOUTH;
                case SOUTH -> Side.WEST;
                case WEST  -> Side.NORTH;
            };
        }
        return r;
    }


    /**
     * Vrati Level defintion.
     *
     * @param levelNumber level
     * @param difficulty  difficulty of the level
     * @return The level level definition.
     */
    private static Object[][] getLevelDefinition(int levelNumber, int difficulty) {
        int actualLevel = difficulty * 10 + levelNumber;

        return switch (actualLevel) {

            case 1 -> LEVEL_1_BEGINNER;
            case 2 -> LEVEL_2_BEGINNER;
            case 3 -> LEVEL_3_BEGINNER;
            case 4 -> LEVEL_4_BEGINNER;
            case 5 -> LEVEL_5_BEGINNER;
            case 6 -> LEVEL_6_BEGINNER;
            case 7 -> LEVEL_7_BEGINNER;
            case 8 -> LEVEL_8_BEGINNER;
            case 9 -> LEVEL_9_BEGINNER;
            case 10 -> LEVEL_10_BEGINNER;


            case 11 -> LEVEL_11_INTERMEDIATE;
            case 12 -> LEVEL_12_INTERMEDIATE;
            case 13 -> LEVEL_13_INTERMEDIATE;
            case 14 -> LEVEL_14_INTERMEDIATE;
            case 15 -> LEVEL_15_INTERMEDIATE;
            case 16 -> LEVEL_16_INTERMEDIATE;
            case 17 -> LEVEL_17_INTERMEDIATE;
            case 18 -> LEVEL_18_INTERMEDIATE;
            case 19 -> LEVEL_19_INTERMEDIATE;
            case 20 -> LEVEL_20_INTERMEDIATE;


            case 21 -> LEVEL_21_ADVANCED;
            case 22 -> LEVEL_22_ADVANCED;
            case 23 -> LEVEL_23_ADVANCED;
            case 24 -> LEVEL_24_ADVANCED;
            case 25 -> LEVEL_25_ADVANCED;
            case 26 -> LEVEL_26_ADVANCED;
            case 27 -> LEVEL_27_ADVANCED;
            case 28 -> LEVEL_28_ADVANCED;
            case 29 -> LEVEL_29_ADVANCED;
            case 30 -> LEVEL_30_ADVANCED;

            default -> LEVEL_1_BEGINNER;
        };
    }

    /**
     *  Vrati jak velke ma byt herni pole
     *
     * @param difficulty The difficulty level
     * @return rozměr herniho pole
     */
    private static int getGridSizeForDifficulty(int difficulty) {
        return switch (difficulty) {
            case 0 -> 9;
            case 1 -> 11;
            case 2 -> 9;
            default -> 10;
        };
    }
}