package ija.ijaProject.game.levels;

import ija.ijaProject.common.GameNode;
import ija.ijaProject.common.Position;
import ija.ijaProject.common.Side;
import ija.ijaProject.game.Game;
import javafx.animation.PauseTransition;
import javafx.embed.swing.SwingNode;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import visualization.EnvPresenter;
import visualization.view.InfoPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Map;
import java.util.Random;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
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


    // ==================== INTERMEDIATE LEVELS (11-20) ====================

    // Level 11: Introduction to intermediate difficulty
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

    // Level 12: Complex grid with multiple paths
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

    // Level 13: Introducing complex interdependence
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

    // Level 14: Complex grid with multiple power sources
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

    // Level 15: Complex grid with multiple paths and bulbs
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

    // Level 16: Complex grid with multiple paths and power sources
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

    // Level 17: Complex grid with multiple paths and bulbs
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

    // Level 18: Complex grid with multiple paths and power sources
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

    // Level 19: Complex grid with multiple paths and bulbs
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

    // Level 20: Complex grid with multiple paths and power sources
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

    // Level 21: Introduction to advanced difficulty
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

    // Level 22: Complex grid with multiple paths
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

    // Level 23: Complex grid with multiple power sources
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

    // Level 24: Complex grid with multiple paths and bulbs
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

    // Level 25: Complex grid with multiple paths and power sources
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

    // Level 26: Complex grid with multiple paths and bulbs
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

    // Level 27: Complex grid with multiple paths and power sources
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

    // Level 28: Complex grid with multiple paths and bulbs
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

    // Level 29: Complex grid with multiple paths and power sources
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

    // Level 30: Most complex level with multiple paths, power sources, and bulbs
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
     * Creates a SwingNode containing the game for the specified level and difficulty.
     *
     * @param levelNumber The level number (1-10 for each difficulty)
     * @param difficulty The difficulty level (0=Beginner, 1=Intermediate, 2=Advanced)
     * @return A SwingNode containing the game
     */
    public static SwingNode createGameLevel(int levelNumber, int difficulty, Runnable levelCompletedCallback) {
        SwingNode swingNode = new SwingNode();

        // Get the appropriate level definition
        Object[][] levelDef = getLevelDefinition(levelNumber, difficulty);

        SwingUtilities.invokeLater(() -> {
            try {
                // Create game with appropriate grid size based on difficulty
                int gridSize = getGridSizeForDifficulty(difficulty);
                Game game = Game.create(gridSize, gridSize + 2);

                // Add nodes from level definition
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

                // Create a solved version of the game for reference
                Game solvedGame = game.deepCopy();

                // Check if we have saved states for this level and if the level is not completed
                boolean hasSavedStates = false;
                boolean isLevelCompleted = LevelManager.getInstance().isLevelCompleted(levelNumber, difficulty);
                System.out.println("Trueeeeeee1 = ");
                if (!isLevelCompleted) {
                    NodeStateManager nodeStateManager = NodeStateManager.getInstance();
                    hasSavedStates = nodeStateManager.hasSavedStates(levelNumber, difficulty);
                }
                // Store the final value for use in the Platform.runLater
                final boolean finalHasSavedStates = hasSavedStates;
                final boolean finalIsLevelCompleted = isLevelCompleted;
                if (finalHasSavedStates) {System.out.println("Trueeeeeee1 = ");}
                if (!finalIsLevelCompleted) {System.out.println("Trueeeeeee2 = ");}

                EnvPresenter solvedPr = new EnvPresenter(game);
                solvedPr.init();
                JPanel solvedPanel = solvedPr.getGamePanel();
                Platform.runLater(() -> swingNode.setContent(solvedPanel));
                Platform.runLater(() -> swingNode.getProperties().put("solvedGame", solvedGame));

                Platform.runLater(() -> {
                    PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    pause.setOnFinished(evt -> {
                        SwingUtilities.invokeLater(() -> {
                            // If we have saved states and the level is not completed, load them
                            if (finalHasSavedStates && !finalIsLevelCompleted) {
                                System.out.println("Loading saved state for level " + levelNumber + " at difficulty " + difficulty);
                                NodeStateManager.getInstance().loadNodeStates(levelNumber, difficulty, game);

                                // Re-initialize to update connections based on loaded state
                                //game.init();
                            } else {
                                // Otherwise, randomize the level as usual
                                System.out.println("No saved state found or level completed, randomizing level " + levelNumber + " at difficulty " + difficulty);
                                List<Position> posList = game.getNodes().stream()
                                        .map(GameNode::getPosition).toList();
                                Random rnd = new Random();
                                int moves = 12 + difficulty * 5;
                                do {
                                    for (int i = 0; i < moves; i++) {
                                        game.rotateNode(posList.get(rnd.nextInt(posList.size())));
                                    }
                                } while (game.anyBulbLit());

                                for (GameNode node : game.getNodes()) {
                                    node.resetRotationCount();
                                }
                            }

                            // Set up node change listeners to detect when the player makes changes
                            for (GameNode node : game.getNodes()) {
                                node.addObserver(observable -> {
                                    // Mark that changes have been made to this level
                                    NodeStateManager.getInstance().markLevelChanged(levelNumber, difficulty);
                                    System.out.println("Node changed in level " + levelNumber + " at difficulty " + difficulty);
                                });
                            }

                            EnvPresenter playPr = new EnvPresenter(game);
                            if (levelCompletedCallback != null) {
                                // Create a wrapper that prevents immediate completion after loading
                                final boolean[] initialCheckDone = {false};

                                playPr.setLevelCompletedCallback(() -> {
                                    // Skip the first completion check to avoid false positives during initialization
                                    if (!initialCheckDone[0]) {
                                        initialCheckDone[0] = true;
                                        System.out.println("Skipping initial completion check");
                                        return;
                                    }

                                    // Normal completion - call the callback
                                    levelCompletedCallback.run();
                                });
                            }
                            playPr.init();
                            JPanel playPanel = playPr.getGamePanel();

                            Platform.runLater(() -> {
                                swingNode.setUserData(playPr);
                                swingNode.setContent(playPanel);

                                // Store the game and level info for saving state when exiting
                                swingNode.getProperties().put("game", game);
                                swingNode.getProperties().put("levelNumber", levelNumber);
                                swingNode.getProperties().put("difficulty", difficulty);

                                Tooltip tip = new Tooltip();
                                tip.setShowDelay(Duration.seconds(1));
                                Tooltip.install(swingNode, tip);
                                final int cols = game.cols();
                                final int rows = game.rows();

                                swingNode.setOnMouseMoved(ev -> {
                                    Bounds b = swingNode.getLayoutBounds();
                                    double w = b.getWidth(), h = b.getHeight();
                                    double cellW = w / cols, cellH = h / rows;

                                    int col = Math.min(cols, Math.max(1, (int)(ev.getX()/cellW) + 1));
                                    int row = Math.min(rows, Math.max(1, (int)(ev.getY()/cellH) + 1));

                                    Set<Side> cur = game.getGameNode(row, col).getConnectors();
                                    Set<Side> tgt = solvedGame.getGameNode(row, col).getConnectors();

                                    int remaining = rotationsNeeded(cur, tgt);
                                    int actual= game.getGameNode(row, col).getRotationCount();

                                    tip.setText("GameNode " + row + "," + col +
                                            "\n Need Rotations: " + remaining +
                                            "\n Actual Rotations: " + actual);
                                });
                                swingNode.setOnMouseExited(ev -> tip.hide());
                            });
                        });
                    });
                    pause.play();
                });

            } catch (Exception e) {
                System.err.println("ERROR during initialization: " + e.getMessage());
                e.printStackTrace();
            }
        });

        return swingNode;
    }
    /*private static void onCellHover(MouseEvent evt, Tooltip tip, Game game, Game solvedGame) {
        // вычисляем над какой клеткой курсор
        int cols = game.cols();
        int rows = game.rows();

        Bounds b = swingNode.getLayoutBounds();
        double w = b.getWidth(), h = b.getHeight();

        double cellW = w  / cols;
        double cellH = h  / rows;

        int col = Math.min(cols, Math.max(1, (int)(evt.getX() / cellW) + 1));
        int row = Math.min(rows, Math.max(1, (int)(evt.getY() / cellH) + 1));

        // получаем коннекторы
        Set<Side> cur = game.getGameNode(row, col).getConnectors();
        Set<Side> tgt = solvedGame.getGameNode(row, col).getConnectors();

        int remaining = rotationsNeeded(cur, tgt);
        int done      = game.getGameNode(row, col).getRotationCount();

        // обновляем текст подсказки
        tip.setText(
                String.format("Ячейка [%d,@%d]\nОсталось вращений: %d\nСделано вращений: %d",
                        row, col, remaining, done)
        );
    }*/

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
     * Gets the level definition for the specified level and difficulty.
     *
     * @param levelNumber The level number (1-10 for each difficulty)
     * @param difficulty The difficulty level (0=Beginner, 1=Intermediate, 2=Advanced)
     * @return The level definition array
     */
    private static Object[][] getLevelDefinition(int levelNumber, int difficulty) {
        // Calculate the actual level number (1-30)
        int actualLevel = difficulty * 10 + levelNumber;

        return switch (actualLevel) {
            // Beginner levels (1-10)
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

            // Intermediate levels (11-20)
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

            // Advanced levels (21-30)
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

            default -> LEVEL_1_BEGINNER; // Default to level 1 if not found
        };
    }

    /**
     * Gets the grid size for the specified difficulty level.
     *
     * @param difficulty The difficulty level (0=Beginner, 1=Intermediate, 2=Advanced)
     * @return The grid size for the difficulty
     */
    private static int getGridSizeForDifficulty(int difficulty) {
        return switch (difficulty) {
            case 0 -> 9; // Beginner
            case 1 -> 11; // Intermediate
            case 2 -> 9; // Advanced
            default -> 10;
        };
    }
}