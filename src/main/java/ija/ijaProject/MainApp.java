package ija.ijaProject;

import ija.ijaProject.game.Game;
import ija.ijaProject.game.levels.LevelManager;
import ija.ijaProject.game.levels.NodeStateManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import visualization.EnvPresenter;
import visualization.view.*;

public class MainApp extends Application {
    private static final int CELL_SIZE = 40;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        // Set this to true to make the JavaFX toolkit exit when the last window is closed
        Platform.setImplicitExit(true);

        this.primaryStage = primaryStage;
        primaryStage.setTitle("VoltMaze");

        // Set minimum window size
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);

        // Add proper window close handling
        primaryStage.setOnCloseRequest(this::handleWindowClose);

        // Show the main menu first
        showMainMenu();

        primaryStage.show();
    }

    /**
     * Handles the window close event to ensure proper application termination.
     *
     * @param event The window event
     */
    private void handleWindowClose(WindowEvent event) {
        System.out.println("Window close requested");

        // Save game state if we're in a game
        if (primaryStage != null && primaryStage.getScene() != null &&
                primaryStage.getScene().getRoot() != null) {
            Object userData = primaryStage.getScene().getRoot().getUserData();
            if (userData instanceof GamePlayView) {
                GamePlayView gameView = (GamePlayView) userData;
                // Save the game state before closing
                System.out.println("Saving game state before window close");
                gameView.saveStateOnExit();
            }
        }

        // Properly exit the application
        Platform.exit();
    }

    /**
     * Shows the main menu screen.
     */
    private void showMainMenu() {
        // First, save state if we're coming from a game
        saveCurrentGameState();

        // Create a main menu view
        GameMenuView menuView = new GameMenuView(primaryStage);

        // Set up button actions
        menuView.setOnPlayAction(e -> showLevels());
        menuView.setOnInfoAction(e -> showInfo());

        // Set the scene
        Scene scene = new Scene(menuView.getRoot(), 800, 600);
        primaryStage.setScene(scene);
    }

    /**
     * Shows the levels selection screen.
     */
    private void showLevels() {
        // First, save state if we're coming from a game
        saveCurrentGameState();

        // Create a levels view
        LevelsView levelsView = new LevelsView(primaryStage);

        // Use the correct method name: setOnBackAction instead of setBackHandler
        levelsView.setOnBackAction(e -> showMainMenu());

        // Use the correct method name: setOnLevelSelectAction instead of setLevelSelectedHandler
        levelsView.setOnLevelSelectAction(e -> {
            // Get the level number and difficulty from the button's properties
            Button button = (Button) e.getSource();
            int levelNumber = (int) button.getProperties().get("levelNumber");
            int difficulty = (int) button.getProperties().get("difficulty");

            startGame(levelNumber, difficulty);
        });

        // Set the scene
        Scene scene = new Scene(levelsView.getRoot(), 800, 600);
        primaryStage.setScene(scene);
    }

    /**
     * Shows the information screen.
     */
    private void showInfo() {
        // First, save state if we're coming from a game
        saveCurrentGameState();

        // Create an info view
        InfoView infoView = new InfoView(primaryStage);

        // Set up back button action to return to main menu
        infoView.setOnBackAction(e -> showMainMenu());

        // Set the scene
        Scene scene = new Scene(infoView.getRoot(), 800, 600);
        primaryStage.setScene(scene);
    }

    /**
     * Helper method to save the current game state if we're in a game view
     */
    private void saveCurrentGameState() {
        if (primaryStage != null && primaryStage.getScene() != null &&
                primaryStage.getScene().getRoot() != null) {
            Object userData = primaryStage.getScene().getRoot().getUserData();
            if (userData instanceof GamePlayView) {
                GamePlayView gameView = (GamePlayView) userData;
                // Save the game state before navigating away
                System.out.println("Saving game state before navigation");
                gameView.saveStateOnExit();
            }
        }
    }

    private void handleLevelCompleted(int levelNumber, int difficulty) {
        System.out.println("MainApp: Level " + levelNumber + " at difficulty " + difficulty + " completed!");

        // Mark the level as completed (this is already done in GamePlayView, but we do it here as well for safety)
        LevelManager.getInstance().markLevelCompleted(levelNumber, difficulty);

        // Clear any saved state for this level since it's completed
        NodeStateManager.getInstance().clearNodeStates(levelNumber, difficulty);

        // Calculate the next level
        int nextLevel = levelNumber + 1;

        // If we've reached the end of levels for this difficulty, go to the next difficulty
        if (nextLevel > 10) {
            int nextDifficulty = difficulty + 1;

            // If we've completed all difficulties, go back to levels menu
            if (nextDifficulty > 2) {
                showLevels();
            } else {
                startGame(1, nextDifficulty);
            }
        } else {
            startGame(nextLevel, difficulty);
        }
    }

    @Override
    public void init() {
        // Add a shutdown hook to save settings when the app closes
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Saving settings before shutdown...");
            // This is a good place to ensure any final saves happen
        }));
    }

    /**
     * Starts a game with the specified level and difficulty.
     *
     * @param levelNumber The level number
     * @param difficulty The difficulty level (0=Beginner, 1=Intermediate, 2=Advanced)
     */
    private void startGame(int levelNumber, int difficulty) {
        System.out.println("Starting game with level: " + levelNumber + ", difficulty: " + difficulty);

        try {
            // Create the game play view
            GamePlayView gamePlayView = new GamePlayView(primaryStage, levelNumber, difficulty);

            // Set up back button action
            gamePlayView.setOnBackAction(e -> {
                System.out.println("Back button clicked in game view");
                showLevels();
            });

            // Set up next level action
            gamePlayView.setOnNextLevelAction(e -> handleLevelCompleted(levelNumber, difficulty));

            // Set up level completion handler
            gamePlayView.setOnLevelCompleted(() -> handleLevelCompleted(levelNumber, difficulty));

            // Show the game play view
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
     * Returns to the levels selection screen.
     */
    private void returnToLevels() {
        // Save state before returning to levels
        saveCurrentGameState();
        showLevels();
    }

    @Override
    public void stop() {
        // Try to clean up the current view if it exists
        if (primaryStage != null && primaryStage.getScene() != null &&
                primaryStage.getScene().getRoot() != null) {

            // Try to get the current view
            Object userData = primaryStage.getScene().getRoot().getUserData();
            if (userData instanceof GamePlayView) {
                System.out.println("Cleaning up GamePlayView...");
                GamePlayView gameView = (GamePlayView) userData;

                // Save the game state before cleanup
                System.out.println("Final save before application exit");
                gameView.saveStateOnExit();

                // Call a cleanup method on the game view
                gameView.cleanup();
            }
        }

        // Make sure all JavaFX threads are terminated
        Platform.exit();

        // Add a small delay before forcing exit
        new Thread(() -> {
            try {
                // Wait a short time to allow Platform.exit() to work
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
