/**
 * Soubor: src/main/java/ija.ijaProject/MainApp.java
 *
 * Popis:
 * Hlavní třída aplikace VoltMaze.
 * Zajišťuje celý cyklus životnosti JavaFX aplikace: zobrazení menu, výběr úrovní,
 * herní obrazovku, informace a nastavení. Spravuje uložení nastavení při ukončení.
 *
 *
 * @Author: Yaroslav Hryn (xhryny00),Oleksandr Musiichuk (xmusii00)
 *
 */

package ija.ijaProject;

import ija.ijaProject.game.levels.LevelManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import visualization.view.*;

/** Hlavní třída aplikace VoltMaze.
 * Zajišťuje celý cyklus životnosti JavaFX aplikace: zobrazení menu, výběr úrovní,
 * herní obrazovku, informace a nastavení. Spravuje uložení nastavení při ukončení. */
public class MainApp extends Application {
    private static final int CELL_SIZE =40 ;
    private Stage primaryStage;
    private boolean simulationMode = false;


    /**
     * Spustí JavaFX aplikaci.
     *
     * @param primaryStage primární okno aplikace
     */
    @Override
    public void start(Stage primaryStage) {
        Platform.setImplicitExit(true);
        this.primaryStage = primaryStage;
        primaryStage.setTitle("VoltMaze");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.setOnCloseRequest(this::handleWindowClose);
        showMainMenu();
        primaryStage.show();
    }

    /**
     * Obslouží požadavek na zavření okna a zajistí korektní ukončení aplikace.
     *
     * @param event událost zavření okna
     */
    private void handleWindowClose(WindowEvent event) {
        if (primaryStage != null && primaryStage.getScene() != null &&
                primaryStage.getScene().getRoot() != null) {
            Object userData = primaryStage.getScene().getRoot().getUserData();
            if (userData instanceof GamePlayView) {
                GamePlayView gameView = (GamePlayView) userData;

                System.out.println("Saving game state before window close");
            }
        }
        Platform.exit();
    }

    /**
     * Zobrazí úvodní hlavní menu s tlačítky Play, Info a Settings.
     */
    private void showMainMenu() {
        GameMenuView menuView = new GameMenuView(primaryStage);

        menuView.setOnPlayAction(e -> showLevels());
        menuView.setOnInfoAction(e -> showInfo());

        Scene scene = new Scene(menuView.getRoot(), 800, 600);
        primaryStage.setScene(scene);
    }

    /**
     * Zobrazí obrazovku výběru úrovní.
     */
    private void showLevels() {
        LevelsView levelsView = new LevelsView(primaryStage);
        levelsView.setOnBackAction(e -> showMainMenu());
        levelsView.setOnLevelSelectAction(e -> {
            Button button = (Button) e.getSource();
            int levelNumber = (int) button.getProperties().get("levelNumber");
            int difficulty = (int) button.getProperties().get("difficulty");

            boolean simulationMode = levelsView.isSimulationModeEnabled();
            startGame(levelNumber, difficulty, simulationMode);
        });


        Scene scene = new Scene(levelsView.getRoot(), 800, 600);
        primaryStage.setScene(scene);
    }

    /**
     * Zobrazí informační stránku o hře.
     */
    private void showInfo() {
        InfoView infoView = new InfoView(primaryStage);
        infoView.setOnBackAction(e -> showMainMenu());
        Scene scene = new Scene(infoView.getRoot(), 800, 600);
        primaryStage.setScene(scene);
    }

    /**
     * Zpracuje dokončení úrovně a rozhodne, zda spustit další úroveň nebo vrátit se k výběru.
     *
     * @param levelNumber číslo dokončené úrovně
     * @param difficulty  úroveň obtížnosti (0=Beginner,1=Intermediate,2=Advanced)
     */
    private void handleLevelCompleted(int levelNumber, int difficulty) {
        System.out.println("MainApp: Level " + levelNumber + " at difficulty " + difficulty + " completed!");
        LevelManager.getInstance().markLevelCompleted(levelNumber, difficulty);

        int nextLevel = levelNumber + 1;

        if (nextLevel > 10) {
            int nextDifficulty = difficulty + 1;
            if (nextDifficulty > 2) {
                showLevels();
            } else {
                startGame(1, nextDifficulty);
            }
        } else {
            startGame(nextLevel, difficulty);
        }
    }


    /**
     * Inicializuje manažery nastavení a jazyka před startem UI.
     */
    @Override
    public void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Saving settings before shutdown...");

        }));
    }



    /**
     * Spustí herní obrazovku pro zvolenou úroveň a obtížnost.
     *
     * @param levelNumber číslo úrovně
     * @param difficulty  úroveň obtížnosti
     */
    private void startGame(int levelNumber, int difficulty, boolean simulationMode) {
        System.out.println("Starting game with level: " + levelNumber + ", difficulty: " + difficulty + ", sim: " + simulationMode);

        try {
            GamePlayView gamePlayView = new GamePlayView(primaryStage, levelNumber, difficulty, simulationMode);

            gamePlayView.setOnBackAction(e -> {
                System.out.println("Back button clicked in game view");
                showLevels();
            });

            gamePlayView.setOnNextLevelAction(e -> handleLevelCompleted(levelNumber, difficulty));
            gamePlayView.setOnLevelCompleted(() -> handleLevelCompleted(levelNumber, difficulty));

            Scene scene = new Scene(gamePlayView.getRoot());
            scene.getRoot().setUserData(gamePlayView);
            primaryStage.setScene(scene);

        } catch (Exception e) {
            System.err.println("Error starting game: " + e.getMessage());
            e.printStackTrace();
            showLevels();
        }
    }

    /**
     * Spustí hru na zadané úrovni a obtížnosti.
     * Tato metoda volá rozšířenou verzi metody {@code startGame}, kde je přednastaveno,
     * že se nejedná o opakované spuštění hry.
     *
     * @param levelNumber číslo úrovně, která se má spustit
     * @param difficulty obtížnost hry (např. 1 = snadná, 2 = střední, 3 = těžká)
     */
    private void startGame(int levelNumber, int difficulty) {
        startGame(levelNumber, difficulty, false);
    }



    @Override
    public void stop() {
        if (primaryStage != null && primaryStage.getScene() != null &&
                primaryStage.getScene().getRoot() != null) {

            Object userData = primaryStage.getScene().getRoot().getUserData();
            if (userData instanceof GamePlayView) {
                System.out.println("Cleaning up GamePlayView...");
                GamePlayView gameView = (GamePlayView) userData;
                gameView.cleanup();
            }
        }

        Platform.exit();


        new Thread(() -> {
            try {
                Thread.sleep(200);
                System.out.println("Forcing application exit...");
                System.exit(0);
            } catch (InterruptedException e) {
                System.exit(0);
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}