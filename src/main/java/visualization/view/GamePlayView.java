package visualization.view;

import ija.ijaProject.common.GameNode;
import ija.ijaProject.common.Side;
import ija.ijaProject.game.levels.LevelManager;
import ija.ijaProject.game.Game;
import ija.ijaProject.game.levels.GameLevels;
//import ija.ijaProject.game.levels.NodeStateManager;
import ija.ijaProject.game.levels.NodeStateManager;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import visualization.EnvPresenter;
import visualization.common.ToolEnvironment;


import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * GamePlayView provides the visualization and UI for the game screen.
 * It includes a back button, level information, and wraps the game SwingNode.
 */
public class GamePlayView {

    private static final int CELL_SIZE =40 ;
    private static final String BG_COLOR_HEX = "#0F172A";
    private final StackPane root;
    private final BorderPane layout;
    private final Stage stage;
    private Button backButton;
    private Text levelText;
    private final int levelNumber;
    private final int difficulty;
    private SwingNode gameNode;
    private EventHandler<ActionEvent> backHandler;
    private EventHandler<ActionEvent> nextLevelHandler;
    private VBox levelCompleteOverlay;
    private GameBridge gameBridge;
    private Runnable onLevelCompleted;
    private boolean levelAlreadyCompleted = false;
    private Timeline gameNodeCheckTimeline;
    //private final SwingNode swingNode;
    private Game solvedGame;
    private Button infoButton;
    private Stage infoStage;
    /**
     * Creates a new GamePlayView for the specified level and difficulty.
     *
     * @param stage The primary stage
     * @param levelNumber The level number
     * @param difficulty The difficulty level (0=Beginner, 1=Intermediate, 2=Advanced)
     */

    public GamePlayView(Stage stage, int levelNumber, int difficulty) {
        this.stage = stage;
        this.levelNumber = levelNumber;
        this.difficulty = difficulty;
        //this.swingNode = GameLevels.createGameLevel(levelNumber, difficulty, /* callback */ null);
        //this.swingNode = GameLevels.createGameLevel(levelNumber, difficulty, this::handleLevelCompleted);
        //this.presenter = (EnvPresenter) swingNode.getUserData();

        // Check if this level was already completed before
        levelAlreadyCompleted = LevelManager.getInstance().isLevelCompleted(levelNumber, difficulty);

        // Create the main layout
        root = new StackPane();
        layout = new BorderPane();

        // Create and add the background
        Pane backgroundPane = createBackground();
        root.getChildren().add(backgroundPane);

        // Create the header with back button and level info
        HBox header = createHeader();
        layout.setTop(header);

        // Create the game content
        System.out.println("Starting game with level: " + levelNumber + ", difficulty: " + difficulty);
        gameNode = GameLevels.createGameLevel(levelNumber, difficulty, this::handleLevelCompleted);
        System.out.println("gameNode=" + gameNode + ", class=" + (gameNode != null ? gameNode.getClass() : "null") + ", id=" + (gameNode != null ? gameNode.getId() : "null"));


        // Create a container for the game to add padding
        StackPane gameContainer = new StackPane(gameNode);
        gameContainer.setPadding(new Insets(10));
        gameContainer.setStyle("-fx-background-color: rgba(15, 23, 42, 0.7); -fx-background-radius: 10px;");

        // Add a border to the game container
        gameContainer.setBorder(new Border(new BorderStroke(
                Color.web("#0EA5E9", 0.6),
                BorderStrokeStyle.SOLID,
                new CornerRadii(10),
                new BorderWidths(2)
        )));

        // Add drop shadow to the game container
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#0EA5E9", 0.5));
        shadow.setRadius(15);
        shadow.setSpread(0.1);
        gameContainer.setEffect(shadow);

        layout.setCenter(gameContainer);


        // Create the level complete overlay (initially hidden)
        levelCompleteOverlay = createLevelCompleteOverlay();
        levelCompleteOverlay.setVisible(false);
        levelCompleteOverlay.setOpacity(0);

        // Add layout and overlay to root
        root.getChildren().addAll(layout, levelCompleteOverlay);

        // Set up game completion listener with a delay to ensure proper initialization
        Platform.runLater(this::setupGameCompletionListener);

        // If the level is already completed, show the completion overlay after a short delay
        if (levelAlreadyCompleted) {
            Platform.runLater(() -> {
                // Show the completion overlay immediately
                showLevelCompleteOverlay();
            });
        }
    }

    private EnvPresenter getPlayPresenter() {
        return (EnvPresenter) gameNode.getUserData();
    }


    /**
     * Creates the background for the game screen.
     *
     * @return A pane containing the background
     */
    private Pane createBackground() {
        Pane backgroundPane = new Pane();

        // Create a dark background
        Rectangle background = new Rectangle();
        background.widthProperty().bind(backgroundPane.widthProperty());
        background.heightProperty().bind(backgroundPane.heightProperty());
        background.setFill(Color.web("#0F172A"));

        // Add electric grid lines
        Group gridLines = createGridLines();

        backgroundPane.getChildren().addAll(background, gridLines);

        // Make sure the background pane fills the entire window
        backgroundPane.prefWidthProperty().bind(stage.widthProperty());
        backgroundPane.prefHeightProperty().bind(stage.heightProperty());

        return backgroundPane;
    }

    /**
     * Creates the grid lines for the background.
     *
     * @return A group containing the grid lines
     */
    private Group createGridLines() {
        Group gridLines = new Group();

        // Create horizontal and vertical grid lines that will scale with the window
        for (int i = 0; i < 40; i++) {
            // Horizontal lines
            Line hLine = new Line();
            hLine.startXProperty().bind(stage.widthProperty().multiply(0));
            hLine.endXProperty().bind(stage.widthProperty());
            hLine.startYProperty().set(i * 40);
            hLine.endYProperty().set(i * 40);
            hLine.setStroke(Color.web("#0EA5E9", 0.1));
            hLine.setStrokeWidth(1);

            // Vertical lines
            Line vLine = new Line();
            vLine.startXProperty().set(i * 40);
            vLine.endXProperty().set(i * 40);
            vLine.startYProperty().bind(stage.heightProperty().multiply(0));
            vLine.endYProperty().bind(stage.heightProperty());
            vLine.setStroke(Color.web("#0EA5E9", 0.1));
            vLine.setStrokeWidth(1);

            gridLines.getChildren().addAll(hLine, vLine);
        }

        return gridLines;
    }

    /**
     * Creates the header with back button and level info.
     *
     * @return An HBox containing the header elements
     */
    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setSpacing(20);

        // Add semi-transparent background to header
        header.setStyle("-fx-background-color: rgba(15, 23, 42, 0.7);");

        // Create back button
        backButton = new Button("← Back to Levels");
        backButton.setStyle(
                "-fx-background-color: rgba(14, 165, 233, 0.2);" +
                        "-fx-background-radius: 30;" +
                        "-fx-border-color: #0EA5E9;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 30;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 15;"
        );

        // Add drop shadow effect
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#0EA5E9"));
        shadow.setRadius(10);
        shadow.setSpread(0);
        backButton.setEffect(shadow);

        // Add hover effects
        addButtonHoverEffect(backButton);

        // Create level text
        String difficultyName = switch (difficulty) {
            case 0 -> "Beginner";
            case 1 -> "Intermediate";
            case 2 -> "Advanced";
            default -> "Unknown";
        };

        levelText = new Text(difficultyName + " - Level " + levelNumber);
        levelText.setFont(Font.font("System", FontWeight.BOLD, 20));
        levelText.setFill(Color.web("#7DD3FC"));

        // Add glow effect to text
        Glow glow = new Glow(0.5);
        levelText.setEffect(glow);

        // Add to header
        header.getChildren().addAll(backButton, levelText);

        Button infoButton = new Button("Hints");
        infoButton.setStyle(
                "-fx-background-color: rgba(14, 165, 233, 0.2);" +
                        "-fx-background-radius: 30;" +
                        "-fx-border-color: #0EA5E9;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 30;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 15;"
        );
        addButtonHoverEffect(infoButton);
        infoButton.setOnAction(e -> openInfoWindow());
        header.getChildren().add(infoButton);



        return header;
    }

    private void openInfoWindow() {
        EnvPresenter p = getPlayPresenter();
        if (p==null) {
            return;
        }
        Game current = (Game) p.getEnvironment();
        Game solved = (Game) gameNode.getProperties().get("solvedGame");
        if (solved == null) {
            solved = current.deepCopy();
            solved.init();
        }

        InfoPresenter info = new InfoPresenter(current, solved);
        for (GameNode node : current.getNodes()) {
            node.addObserver(info);
        }

        SwingNode infoSwing = new SwingNode();
        SwingUtilities.invokeLater(() -> infoSwing.setContent(info.getPanel()));

        infoStage = new Stage();
        infoStage.setTitle("Hint: rotations to solve");
        StackPane root = new StackPane(infoSwing);
        root.setStyle("-fx-background-color: " + BG_COLOR_HEX + ";"); // ваш фон
        Scene scene = new Scene(root,
                current.cols()*InfoPresenter.TILE_SIZE,
                current.rows()*InfoPresenter.TILE_SIZE
        );
        infoStage.setScene(scene);
        infoStage.show();
    }

    /**
     * Creates the level complete overlay.
     *
     * @return A VBox containing the level complete message and buttons
     */
    private VBox createLevelCompleteOverlay() {
        VBox overlay = new VBox(20);
        overlay.setAlignment(Pos.CENTER);
        overlay.setPadding(new Insets(30));
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");

        // Create container for content
        VBox content = new VBox(25);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));
        content.setMaxWidth(500);
        content.setMaxHeight(400);
        content.setStyle(
                "-fx-background-color: rgba(15, 23, 42, 0.95);" +
                        "-fx-background-radius: 20px;" +
                        "-fx-border-color: #0EA5E9;" +
                        "-fx-border-width: 3px;" +
                        "-fx-border-radius: 20px;"
        );

        // Add drop shadow
        DropShadow contentShadow = new DropShadow();
        contentShadow.setColor(Color.web("#0EA5E9", 0.7));
        contentShadow.setRadius(30);
        contentShadow.setSpread(0.2);
        content.setEffect(contentShadow);

        // Create completion message
        Text completionTitle = new Text("Level Complete!");
        completionTitle.setFont(Font.font("System", FontWeight.BOLD, 36));
        completionTitle.setFill(Color.web("#0EA5E9"));

        // Add glow to title
        Glow titleGlow = new Glow(0.8);
        completionTitle.setEffect(titleGlow);

        // Create congratulation message
        Text congratsText = new Text("Congratulations! You've successfully completed this level.");
        congratsText.setFont(Font.font("System", FontWeight.NORMAL, 18));
        congratsText.setFill(Color.WHITE);
        congratsText.setWrappingWidth(400);
        congratsText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // Create buttons container
        HBox buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(20, 0, 0, 0));

        // Create retry button
        Button retryButton = new Button("Retry Level");
        styleButton(retryButton);

        // Create next level button
        Button nextLevelButton = new Button("Next Level →");
        styleButton(nextLevelButton);
        nextLevelButton.setStyle(
                "-fx-background-color: rgba(14, 165, 233, 0.4);" +
                        "-fx-background-radius: 30;" +
                        "-fx-border-color: #0EA5E9;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 30;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12 25;"
        );

        // Add hover effects
        addButtonHoverEffect(retryButton);
        addButtonHoverEffect(nextLevelButton);

        // Set button actions
        retryButton.setOnAction(e -> {
            if (infoStage != null) {
                infoStage.close();
                infoStage = null;
            }
            hideLevelCompleteOverlay();
            retryCurrentLevel();
        });

        nextLevelButton.setOnAction(e -> {
            // Notify the MainApp that the level is completed
            hideLevelCompleteOverlay();
            if (infoStage != null) {
                infoStage.close();
                infoStage = null;
            }

            if (onLevelCompleted != null) {
                onLevelCompleted.run();
            }

            if (nextLevelHandler != null) {
                nextLevelHandler.handle(e);
            }
        });

        // Add buttons to container
        buttons.getChildren().addAll(retryButton, nextLevelButton);

        // Add all elements to content
        content.getChildren().addAll(completionTitle, congratsText, buttons);

        // Add content to overlay
        overlay.getChildren().add(content);

        return overlay;
    }

    /**
     * Applies standard styling to a button.
     *
     * @param button The button to style
     */
    private void styleButton(Button button) {
        button.setStyle(
                "-fx-background-color: rgba(14, 165, 233, 0.2);" +
                        "-fx-background-radius: 30;" +
                        "-fx-border-color: #0EA5E9;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 30;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12 25;"
        );

        // Add drop shadow
        DropShadow buttonShadow = new DropShadow();
        buttonShadow.setColor(Color.web("#0EA5E9"));
        buttonShadow.setRadius(10);
        buttonShadow.setSpread(0);
        button.setEffect(buttonShadow);
    }

    /**
     * Adds hover effects to a button.
     *
     * @param button The button to add hover effects to
     */
    private void addButtonHoverEffect(Button button) {
        button.setOnMouseEntered(e -> {
            // Change style on hover
            String style = button.getStyle();
            style = style.replace("rgba(14, 165, 233, 0.2)", "rgba(14, 165, 233, 0.4)");
            button.setStyle(style);

            // Enhance glow effect
            DropShadow hoverShadow = new DropShadow();
            hoverShadow.setColor(Color.web("#0EA5E9"));
            hoverShadow.setRadius(15);
            hoverShadow.setSpread(0.2);
            button.setEffect(hoverShadow);
        });

        button.setOnMouseExited(e -> {
            // Restore original style
            String style = button.getStyle();
            style = style.replace("rgba(14, 165, 233, 0.4)", "rgba(14, 165, 233, 0.2)");
            button.setStyle(style);

            // Restore original shadow
            DropShadow originalShadow = new DropShadow();
            originalShadow.setColor(Color.web("#0EA5E9"));
            originalShadow.setRadius(10);
            originalShadow.setSpread(0);
            button.setEffect(originalShadow);
        });
    }

    /**
     * Sets up a listener to detect when the game is completed.
     * Uses the GameBridge to monitor the game state.
     */
    private void setupGameCompletionListener() {
        System.out.println("Настройка слушателя завершения игры...");
        System.out.println("gameNode=" + gameNode + ", userData=" + (gameNode != null ? gameNode.getUserData() : "null"));

        // Создаем JavaFX таймер для периодической проверки
        if (gameNodeCheckTimeline != null) {
            gameNodeCheckTimeline.stop();
        }

        gameNodeCheckTimeline = new Timeline(
                new KeyFrame(
                        Duration.millis(200), // Проверяем каждые 500 мс
                        event -> {
                            try {
                                // Проверяем, инициализирован ли gameNode и его userData
                                if (gameNode != null && gameNode.getUserData() instanceof EnvPresenter) {
                                    // Останавливаем таймер
                                    gameNodeCheckTimeline.stop();

                                    // Настраиваем слушателя
                                    EnvPresenter presenter = (EnvPresenter) gameNode.getUserData();
                                    setupGameCompletionListenerWithPresenter(presenter);
                                } else {
                                    System.out.println("Ожидание инициализации gameNode: userData=" +
                                            (gameNode != null ? gameNode.getUserData() : "null"));
                                }
                            } catch (Exception e) {
                                System.err.println("Ошибка при проверке gameNode: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                )
        );

        // Запускаем таймер с повторением
        gameNodeCheckTimeline.setCycleCount(Timeline.INDEFINITE);
        gameNodeCheckTimeline.play();

        // For testing purposes, keep the key press handler
        root.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case F5 -> handleLevelCompleted(); // Press F5 to simulate level completion
            }
        });
    }

    /**
     * Sets up the game completion listener using the provided EnvPresenter.
     *
     * @param presenter The EnvPresenter containing the game
     */
    private void setupGameCompletionListenerWithPresenter(EnvPresenter presenter) {
        try {
            // Получаем Game через метод getEnvironment
            ToolEnvironment environment = presenter.getEnvironment();
            System.out.println("ToolEnv " + environment);

            if (environment instanceof Game) {
                Game game = (Game) environment;

                // Создаем GameBridge для мониторинга игры
                gameBridge = new GameBridge(game);

                // Добавляем слушатель завершения
                gameBridge.addCompletionListener(() -> Platform.runLater(this::handleLevelCompleted));
                System.out.println("Слушатель завершения уровня успешно настроен");
            } else {
                System.out.println("env не является экземпляром Game: " +
                        (environment != null ? environment.getClass().getName() : "null"));

                // Если не удалось получить Game, выводим сообщение о F5
                System.out.println("Автоматическое определение завершения уровня не работает.");
                System.out.println("Используйте клавишу F5 для завершения уровня.");
            }
        } catch (Exception e) {
            System.err.println("Ошибка при настройке слушателя: " + e.getMessage());
            e.printStackTrace();

            // В случае ошибки, выводим сообщение о F5
            System.out.println("Автоматическое определение завершения уровня не работает из-за ошибки.");
            System.out.println("Используйте клавишу F5 для завершения уровня.");
        }
    }

    /**
     * Handles level completion logic.
     * This is called when the game is completed.
     */
    public void handleLevelCompleted() {
        // Проверяем, не вызывается ли метод повторно
        if (levelAlreadyCompleted) {
            System.out.println("Уровень " + levelNumber + " уже был завершен ранее, игнорируем повторное завершение");
            return;
        }

        System.out.println("Level " + levelNumber + " at difficulty " + difficulty + " completed!");

        // Mark the level as completed in LevelManager
        LevelManager.getInstance().markLevelCompleted(levelNumber, difficulty);
        levelAlreadyCompleted = true;
        // Clear any saved state for this level since it's completed
        NodeStateManager.getInstance().clearNodeStates(levelNumber, difficulty);

        // Show the completion overlay
        showLevelCompleteOverlay();
    }

    /**
     * Helper method to get the Game instance from the panel.
     * This method is no longer used as we get the Game from EnvPresenter.
     *
     * @param panel The JPanel containing the game
     * @return The Game instance, or null if not found
     */
    private Game getGameFromPanel(JPanel panel) {
        System.out.println("getGameFromPanel вызван с panel=" + panel);

        if (panel == null) {
            System.out.println("getGameFromPanel: panel равен null");
            return null;
        }

        try {
            // Выводим информацию о компонентах панели
            Component[] components = panel.getComponents();
            System.out.println("Компонентов в панели: " + components.length);

            for (int i = 0; i < components.length; i++) {
                Component component = components[i];
                System.out.println("Компонент " + i + ": " + component.getClass().getName());
            }
        } catch (Exception e) {
            System.err.println("Ошибка при анализе компонентов панели: " + e.getMessage());
            e.printStackTrace();
        }

        // Этот метод больше не используется, так как мы получаем Game из EnvPresenter
        return null;
    }

    /**
     * Shows the level complete overlay with animation.
     */
    public void showLevelCompleteOverlay() {
        try {
            levelCompleteOverlay.setVisible(true);

            // Fade in animation
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), levelCompleteOverlay);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        } catch (Exception e) {
            System.err.println("Ошибка при показе оверлея завершения уровня: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Hides the level complete overlay with animation.
     */
    private void hideLevelCompleteOverlay() {
        try {
            // Fade out animation
            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), levelCompleteOverlay);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> levelCompleteOverlay.setVisible(false));
            fadeOut.play();
        } catch (Exception e) {
            System.err.println("Ошибка при скрытии оверлея завершения уровня: " + e.getMessage());
            e.printStackTrace();

            // В случае ошибки, просто скрываем оверлей
            levelCompleteOverlay.setVisible(false);
        }
    }

    /**
     * Restarts the current level.
     */
    private void restartLevel() {
        // Stop monitoring the current game
        if (gameBridge != null) {
            gameBridge.stopMonitoring();
            gameBridge = null;
        }

        // Останавливаем таймер, если он запущен
        if (gameNodeCheckTimeline != null) {
            gameNodeCheckTimeline.stop();
            gameNodeCheckTimeline = null;
        }

        // Replace the game node with a new one for the same level
        SwingNode newGameNode = GameLevels.createGameLevel(levelNumber, difficulty, this::handleLevelCompleted);

        // Get the parent of the current game node
        StackPane gameContainer = (StackPane) gameNode.getParent();

        // Replace the game node
        gameContainer.getChildren().clear();
        gameContainer.getChildren().add(newGameNode);

        // Update the reference
        gameNode = newGameNode;

        // Сбрасываем флаг завершения уровня
        levelAlreadyCompleted = false;

        // Set up game completion listener again
        Platform.runLater(this::setupGameCompletionListener);
    }

    /**
     * Resets the current level's progress while keeping other levels intact.
     */
    private void retryCurrentLevel() {
        // Close info window if open
        if (infoStage != null) {
            infoStage.close();
            infoStage = null;
        }

        // Hide level complete overlay if visible
        if (levelCompleteOverlay.isVisible()) {
            hideLevelCompleteOverlay();
        }

        // Reset the progress for this specific level
        LevelManager.getInstance().resetLevelCompletion(levelNumber, difficulty);
        NodeStateManager.getInstance().clearNodeStates(levelNumber, difficulty);

        // Reset the level already completed flag
        levelAlreadyCompleted = false;

        // Restart the level
        restartLevel();

        // Show confirmation message
        showTemporaryMessage("Level reset successfully!");
    }

    /**
     * Shows a temporary message in the UI.
     *
     * @param message The message to show
     */
    private void showTemporaryMessage(String message) {
        // Create a text node for the message
        Text messageText = new Text(message);
        messageText.setFont(Font.font("System", FontWeight.BOLD, 18));
        messageText.setFill(Color.WHITE);

        // Create a container with a background
        StackPane messageContainer = new StackPane(messageText);
        messageContainer.setStyle(
                "-fx-background-color: rgba(14, 165, 233, 0.7);" +
                        "-fx-background-radius: 10px;" +
                        "-fx-padding: 15px 25px;"
        );
        messageContainer.setOpacity(0);

        // Position at the top center of the screen
        StackPane.setAlignment(messageContainer, Pos.TOP_CENTER);
        StackPane.setMargin(messageContainer, new Insets(100, 0, 0, 0));

        // Add to the root
        root.getChildren().add(messageContainer);

        // Fade in
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), messageContainer);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        // Pause
        PauseTransition pause = new PauseTransition(Duration.seconds(2));

        // Fade out
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), messageContainer);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> root.getChildren().remove(messageContainer));

        // Play the sequence
        fadeIn.setOnFinished(e -> pause.play());
        pause.setOnFinished(e -> fadeOut.play());
        fadeIn.play();
    }

    /**
     * Sets the handler for the back button.
     *
     * @param handler The event handler
     */
    public void setOnBackAction(EventHandler<ActionEvent> handler) {
        this.backHandler = handler;

        // Create a new handler that first saves state, then calls the original handler
        EventHandler<ActionEvent> wrappedHandler = e -> {
            // Add explicit logging to verify this is being called
            System.out.println("Back button clicked - saving state before exit");

            // Save the state before exiting
            saveStateOnExit();

            // Then call the original handler
            if (handler != null) {
                handler.handle(e);
            }
        };

        // Set the wrapped handler on the back button
        backButton.setOnAction(wrappedHandler);
    }

    /**
     * Sets the handler for the next level button.
     *
     * @param handler The event handler
     */
    public void setOnNextLevelAction(EventHandler<ActionEvent> handler) {
        this.nextLevelHandler = handler;
    }

    /**
     * Sets a handler to be called when the level is completed.
     *
     * @param handler The runnable to execute when level is completed
     */
    public void setOnLevelCompleted(Runnable handler) {
        this.onLevelCompleted = handler;
    }

    /**
     * Gets the root pane of this view.
     *
     * @return The root pane
     */
    public StackPane getRoot() {
        return root;
    }

    public void saveStateOnExit() {
        try {
            System.out.println("saveStateOnExit called for level " + levelNumber + " at difficulty " + difficulty);

            if (gameNode != null) {
                EnvPresenter presenter = getPlayPresenter();
                if (presenter != null) {
                    // Get the game from the presenter
                    Object environment = presenter.getEnvironment();
                    if (environment instanceof Game) {
                        Game game = (Game) environment;

                        // Don't save if the game is already completed
                        if (levelAlreadyCompleted) {
                            System.out.println("Level " + levelNumber + " at difficulty " + difficulty +
                                    " is already completed, not saving state");
                            return;
                        }

                        // Save the node states
                        boolean saved = NodeStateManager.getInstance().saveNodeStates(levelNumber, difficulty, game);
                        System.out.println("Saved game state for level " + levelNumber + " at difficulty " + difficulty + ": " + saved);
                    } else {
                        System.err.println("Environment is not a Game instance: " +
                                (environment != null ? environment.getClass().getName() : "null"));
                    }
                } else {
                    System.err.println("Could not get presenter from gameNode");
                }
            } else {
                System.err.println("gameNode is null, cannot save state");
            }
        } catch (Exception e) {
            System.err.println("Error saving game state: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cleans up resources used by this view.
     * This should be called when the view is no longer needed.
     */
    public void cleanup() {
        System.out.println("GamePlayView cleanup called");
        saveStateOnExit();

        // Stop the game bridge if it exists
        if (gameBridge != null) {
            gameBridge.stopMonitoring();
            gameBridge = null;
        }

        // Останавливаем таймер, если он запущен
        if (gameNodeCheckTimeline != null) {
            gameNodeCheckTimeline.stop();
            gameNodeCheckTimeline = null;
        }

        // Remove any listeners
        if (backButton != null) {
            backButton.setOnAction(null);
        }

        // Clear references
        backHandler = null;
        nextLevelHandler = null;
        onLevelCompleted = null;

        System.out.println("GamePlayView cleanup completed");
    }

    /**
     * Shows this game screen.
     */
    public void show() {
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("VoltMaze - Level " + levelNumber);
    }
}
