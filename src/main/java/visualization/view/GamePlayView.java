/**
 * Soubor: src/main/java/visualization/view/GamePlayView.java
 *
 * Popis:
 *  Spravuje zobrazení herního panelu JavaFX a
 *  ovládacích prvků, hlavičky,tlačítka zpět
 *
 *
 * @Author: Yaroslav Hryn (xhryny00), Oleksandr Musiichuk (xmusii00)
 */

package visualization.view;

import ija.ijaProject.common.GameNode;
import ija.ijaProject.game.levels.*;
import ija.ijaProject.game.Game;
import ija.ijaProject.game.simulation.GameReplay;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import javafx.scene.control.Label;


import javax.swing.*;
import java.awt.*;


/**
 * Visuallizace herniho okna
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
    private Button stepBackButton;
    private Button stepForwardButton;
    private Button playModeButton;

    private Button simulationButton;
    private boolean simulationMode;
    private GameReplay replay;

    private EnvPresenter playPresenter;

    /**
     * Vytvoří GamePlayView pro hru s level a difficulty..
     *
     * @param stage The primary stage
     * @param levelNumber The level number
     * @param difficulty The difficulty level (0=Beginner, 1=Intermediate, 2=Advanced)
     */
    public GamePlayView(Stage stage, int levelNumber, int difficulty, boolean simulationMode) {
        this.stage = stage;
        this.levelNumber = levelNumber;
        this.difficulty = difficulty;
        this.simulationMode = simulationMode;
        levelAlreadyCompleted = LevelManager.getInstance().isLevelCompleted(levelNumber, difficulty);

        root = new StackPane();
        layout = new BorderPane();
        Pane backgroundPane = createBackground();
        root.getChildren().add(backgroundPane);

        HBox header = createHeader();
        layout.setTop(header);

        System.out.println("Starting game with level: " + levelNumber + ", difficulty: " + difficulty);
        gameNode = GameLevels.createGameLevel(levelNumber, difficulty, this::handleLevelCompleted, simulationMode);
        gameNode.setMouseTransparent(simulationMode); // blocks mouse events only
        System.out.println("GameNode: " + gameNode);
        StackPane gameContainer = new StackPane(gameNode);
        gameContainer.setPadding(new Insets(10));
        gameContainer.setStyle("-fx-background-color: rgba(15, 23, 42, 0.7); -fx-background-radius: 10px;");

        gameContainer.setBorder(new Border(new BorderStroke(
                Color.web("#0EA5E9", 0.6),
                BorderStrokeStyle.SOLID,
                new CornerRadii(10),
                new BorderWidths(2)
        )));

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#0EA5E9", 0.5));
        shadow.setRadius(15);
        shadow.setSpread(0.1);
        gameContainer.setEffect(shadow);
        layout.setCenter(gameContainer);

        levelCompleteOverlay = createLevelCompleteOverlay();
        levelCompleteOverlay.setVisible(false);
        levelCompleteOverlay.setOpacity(0);

        root.getChildren().addAll(layout, levelCompleteOverlay);

        if (simulationMode) {
            Label simBanner = new Label("SIMULATION MODE");
            simBanner.setStyle("-fx-background-color: #0EA5E9; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 8 20;");
            StackPane.setAlignment(simBanner, Pos.TOP_CENTER);
            root.getChildren().add(simBanner);
        }


        Platform.runLater(this::setupGameCompletionListener);
        if (levelAlreadyCompleted) {
            Platform.runLater(() -> {
                showLevelCompleteOverlay();
            });
        }
    }
    private EnvPresenter getPlayPresenter() {
        return (EnvPresenter) gameNode.getUserData();
    }


    /**
     * Background
     *
     * @return  panel s background
     */
    private Pane createBackground() {
        Pane backgroundPane = new Pane();

        Rectangle background = new Rectangle();
        background.widthProperty().bind(backgroundPane.widthProperty());
        background.heightProperty().bind(backgroundPane.heightProperty());
        background.setFill(Color.web("#0F172A"));
        Group gridLines = createGridLines();

        backgroundPane.getChildren().addAll(background, gridLines);

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
        for (int i = 0; i < 40; i++) {
            Line hLine = new Line();
            hLine.startXProperty().bind(stage.widthProperty().multiply(0));
            hLine.endXProperty().bind(stage.widthProperty());
            hLine.startYProperty().set(i * 40);
            hLine.endYProperty().set(i * 40);
            hLine.setStroke(Color.web("#0EA5E9", 0.1));
            hLine.setStrokeWidth(1);

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
        header.setStyle("-fx-background-color: rgba(15, 23, 42, 0.7);");

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
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#0EA5E9"));
        shadow.setRadius(10);
        backButton.setEffect(shadow);
        addButtonHoverEffect(backButton);

        String difficultyName = switch (difficulty) {
            case 0 -> "Beginner";
            case 1 -> "Intermediate";
            case 2 -> "Advanced";
            default -> "Unknown";
        };

        levelText = new Text(difficultyName + " - Level " + levelNumber);
        levelText.setFont(Font.font("System", FontWeight.BOLD, 20));
        levelText.setFill(Color.web("#7DD3FC"));
        levelText.setEffect(new Glow(0.5));

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
        stepBackButton = new Button("<-");
        stepForwardButton = new Button("->");
        playModeButton = new Button("▶");
        stepBackButton.setStyle(
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

        stepForwardButton.setStyle(
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
        playModeButton.setStyle(
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

        stepBackButton.setDisable(false);
        stepForwardButton.setDisable(false);
        playModeButton.setDisable(false);

        header.getChildren().addAll(backButton, levelText, infoButton, stepBackButton, stepForwardButton, playModeButton);

        return header;
    }
//    private void setupButtonsOnStart() {
//        Platform.runLater(() -> {Object rep = gameNode.getProperties().get("replay");
//            boolean hasReplay = (rep instanceof GameReplay);
//
//            stepBackButton.setVisible(hasReplay);
//            stepForwardButton.setVisible(hasReplay);
//            stepBackButton.setDisable(!hasReplay);
//            stepForwardButton.setDisable(!hasReplay);
//
//            playModeButton.setVisible(hasReplay);
//            playModeButton.setDisable(!hasReplay);});
//
//    }
//    public void updateSimulationButtons() {
//        Object rep = gameNode.getProperties().get("replay");
//        boolean hasReplay = (rep instanceof GameReplay);
//
//        stepBackButton.setVisible(hasReplay);
//        stepForwardButton.setVisible(hasReplay);
//        playModeButton.setVisible(hasReplay);
//
//        stepBackButton.setDisable(!hasReplay);
//        stepForwardButton.setDisable(!hasReplay);
//        playModeButton.setDisable(!hasReplay);
//    }

    /**
     * Inicializuje ovládací prvky pro simulaci (kroky zpět, vpřed, přepnutí do režimu hry).
     *
     * Tato metoda kontroluje, zda je ve vlastnostech uzlu k dispozici objekt {@code GameReplay},
     * a podle toho zobrazuje a aktivuje příslušná tlačítka.
     * Nastavuje také akce pro tlačítka krokování a návrat do režimu hry.
     */
    private void setupSimulationControls() {
        Platform.runLater(() -> {Object rep = gameNode.getProperties().get("replay");
            boolean hasReplay = (rep instanceof GameReplay);
    System.out.println("hasReplay: " + hasReplay);
            stepBackButton.setVisible(hasReplay);
            stepForwardButton.setVisible(hasReplay);
            stepBackButton.setDisable(!hasReplay);
            stepForwardButton.setDisable(!hasReplay);

            playModeButton.setVisible(hasReplay);
            playModeButton.setDisable(!hasReplay);});

        stepBackButton.setOnAction(e -> {
            System.out.println("[UI] Step Back button clicked");
            GameReplay replay = (GameReplay) gameNode.getProperties().get("replay");
            EnvPresenter playPr = (EnvPresenter) gameNode.getUserData();
            if (replay != null) {
                if (playPr != null) {
                    System.out.println("  → Before stepBack: ");
                    playPr.debugPrintState();
                }
                replay.stepBackward();
                if (playPr != null) {
                    System.out.println("  → After stepBack: ");
                    playPr.debugPrintState();
                }
            } else {
                System.out.println("[UI] No replay found in gameNode properties");
            }
        });

        stepForwardButton.setOnAction(e -> {
            System.out.println("[UI] Step Forward button clicked");
            GameReplay replay = (GameReplay) gameNode.getProperties().get("replay");
            if (replay != null) {
                EnvPresenter playPr = (EnvPresenter) gameNode.getUserData();
                if (playPr != null) {
                    System.out.println("  → Before stepForward: ");
                    playPr.debugPrintState();
                }
                replay.stepForward();
                if (playPr != null) {
                    System.out.println("  → After stepForward: ");
                    playPr.debugPrintState();
                }
            } else {
                System.out.println("[UI] No replay found in gameNode properties");
            }
        });

        playModeButton.setOnAction(e -> {
            System.out.println("[UI] Play Mode button clicked");
            GameReplay replay = (GameReplay) gameNode.getProperties().get("replay");
            EnvPresenter playPr = (EnvPresenter) gameNode.getUserData();
            if (replay != null) {
                System.out.println("  → Before switchToPlayMode: ");
                playPr.debugPrintState();
                replay.switchToPlayMode();
            }


            if (playPr != null) {
                playPr.setReplayMode(false);
                playPr.enableUserClicks();
            }
            System.out.println("  → After switchToPlayMode: ");
            playPr.debugPrintState();
            stepBackButton.setVisible(false);
            stepForwardButton.setVisible(false);
            playModeButton.setVisible(false);

        });
    }

    private Game getCurrentGame() {
        EnvPresenter presenter = getPlayPresenter();
        if (presenter == null) return null;
        ToolEnvironment env = presenter.getEnvironment();
        if (env instanceof Game) {
            return (Game) env;
        }
        return null;
    }

    /**
     * Otevře nové okno nápovědy s InfoPresenter.
     */
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


        DropShadow contentShadow = new DropShadow();
        contentShadow.setColor(Color.web("#0EA5E9", 0.7));
        contentShadow.setRadius(30);
        contentShadow.setSpread(0.2);
        content.setEffect(contentShadow);

        Text completionTitle = new Text("Level Complete!");
        completionTitle.setFont(Font.font("System", FontWeight.BOLD, 36));
        completionTitle.setFill(Color.web("#0EA5E9"));

        Glow titleGlow = new Glow(0.8);
        completionTitle.setEffect(titleGlow);

        Text congratsText = new Text("Congratulations! You've successfully completed this level.");
        congratsText.setFont(Font.font("System", FontWeight.NORMAL, 18));
        congratsText.setFill(Color.WHITE);
        congratsText.setWrappingWidth(400);
        congratsText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        HBox buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(20, 0, 0, 0));

        Button retryButton = new Button("Retry Level");
        styleButton(retryButton);

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

        addButtonHoverEffect(retryButton);
        addButtonHoverEffect(nextLevelButton);

        retryButton.setOnAction(e -> {

            if (infoStage != null) {
                infoStage.close();
                infoStage = null;
            }
            hideLevelCompleteOverlay();
            retryCurrentLevel();
        });

        nextLevelButton.setOnAction(e -> {
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

        buttons.getChildren().addAll(retryButton, nextLevelButton);

        content.getChildren().addAll(completionTitle, congratsText, buttons);

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
            String style = button.getStyle();
            style = style.replace("rgba(14, 165, 233, 0.2)", "rgba(14, 165, 233, 0.4)");
            button.setStyle(style);

            DropShadow hoverShadow = new DropShadow();
            hoverShadow.setColor(Color.web("#0EA5E9"));
            hoverShadow.setRadius(15);
            hoverShadow.setSpread(0.2);
            button.setEffect(hoverShadow);
        });

        button.setOnMouseExited(e -> {
            String style = button.getStyle();
            style = style.replace("rgba(14, 165, 233, 0.4)", "rgba(14, 165, 233, 0.2)");
            button.setStyle(style);

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
        if (gameNodeCheckTimeline != null) {
            gameNodeCheckTimeline.stop();
        }

        gameNodeCheckTimeline = new Timeline(
                new KeyFrame(
                        Duration.millis(150),
                        event -> {
                            try {
                                if (gameNode != null && gameNode.getUserData() instanceof EnvPresenter) {
                                    gameNodeCheckTimeline.stop();
                                    EnvPresenter presenter = (EnvPresenter) gameNode.getUserData();
                                    setupGameCompletionListenerWithPresenter(presenter);
                                } else {
                                    System.out.println("Waiting for gameNpde " +
                                            (gameNode != null ? gameNode.getUserData() : "null"));
                                }
                            } catch (Exception e) {
                                System.err.println("Error unvalid GameNode " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                )
        );
        gameNodeCheckTimeline.setCycleCount(Timeline.INDEFINITE);
        gameNodeCheckTimeline.play();
        root.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case F5 -> handleLevelCompleted();
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
            ToolEnvironment environment = presenter.getEnvironment();
            System.out.println("ToolEnv " + environment);

            if (environment instanceof Game) {
                Game game = (Game) environment;
                gameBridge = new GameBridge(game);
                gameBridge.addCompletionListener(() -> Platform.runLater(this::handleLevelCompleted));
            } else {
                System.out.println("Env Problem: " +
                        (environment != null ? environment.getClass().getName() : "null"));
            }
            setupSimulationControls();
        } catch (Exception e) {
            System.err.println("Error listener " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles level completion logic.
     * This is called when the game is completed.
     */
    public void handleLevelCompleted() {
        if (levelAlreadyCompleted) {
            System.out.println("Level " + levelNumber + " was completed");
            return;
        }

        System.out.println("Level " + levelNumber + " at difficulty " + difficulty + " completed!");

        LevelManager.getInstance().markLevelCompleted(levelNumber, difficulty);
        levelAlreadyCompleted = true;
        //NodeStateManager.getInstance().clearNodeStates(levelNumber, difficulty);

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

        if (panel == null) {
            System.out.println("ERROR: panel is null");
            return null;
        }

        try {
            Component[] components = panel.getComponents();

            for (int i = 0; i < components.length; i++) {
                Component component = components[i];
            }
        } catch (Exception e) {
            System.err.println("Error: component panel " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Shows the level complete overlay with animation.
     */
    public void showLevelCompleteOverlay() {
        try {
            levelCompleteOverlay.setVisible(true);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), levelCompleteOverlay);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        } catch (Exception e) {
            System.err.println("Error: Complete Level overlay " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Hides the level complete overlay with animation.
     */
    private void hideLevelCompleteOverlay() {
        try {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), levelCompleteOverlay);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> levelCompleteOverlay.setVisible(false));
            fadeOut.play();
        } catch (Exception e) {
            System.err.println("Error: hide Level Complete Overlay " + e.getMessage());
            e.printStackTrace();
            levelCompleteOverlay.setVisible(false);
        }
    }

    /**
     * Restarts the current level.
     */
    private void restartLevel() {
        if (gameBridge != null) {
            gameBridge.stopMonitoring();
            gameBridge = null;
        }
        if (gameNodeCheckTimeline != null) {
            gameNodeCheckTimeline.stop();
            gameNodeCheckTimeline = null;
        }
        SwingNode newGameNode = GameLevels.createGameLevel(levelNumber, difficulty, this::handleLevelCompleted, simulationMode);
        StackPane gameContainer = (StackPane) gameNode.getParent();
        gameContainer.getChildren().clear();
        gameContainer.getChildren().add(newGameNode);
        gameNode = newGameNode;
        levelAlreadyCompleted = false;
        Platform.runLater(this::setupGameCompletionListener);
    }

    /**
     * Resets the current level's progress while keeping other levels intact.
     */
    private void retryCurrentLevel() {
        if (infoStage != null) {
            infoStage.close();
            infoStage = null;
        }
        if (levelCompleteOverlay.isVisible()) {
            hideLevelCompleteOverlay();
        }
        LevelManager.getInstance().resetLevelCompletion(levelNumber, difficulty);
        //NodeStateManager.getInstance().clearNodeStates(levelNumber, difficulty);
        levelAlreadyCompleted = false;
        restartLevel();
        showTemporaryMessage("Level reset successfully!");
    }

    /**
     * Shows a temporary message in the UI.
     *
     * @param message The message to show
     */
    private void showTemporaryMessage(String message) {
        Text messageText = new Text(message);
        messageText.setFont(Font.font("System", FontWeight.BOLD, 18));
        messageText.setFill(Color.WHITE);

        StackPane messageContainer = new StackPane(messageText);
        messageContainer.setStyle(
                "-fx-background-color: rgba(14, 165, 233, 0.7);" +
                        "-fx-background-radius: 10px;" +
                        "-fx-padding: 15px 25px;"
        );
        messageContainer.setOpacity(0);

        StackPane.setAlignment(messageContainer, Pos.TOP_CENTER);
        StackPane.setMargin(messageContainer, new Insets(100, 0, 0, 0));

        root.getChildren().add(messageContainer);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), messageContainer);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        PauseTransition pause = new PauseTransition(Duration.seconds(2));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), messageContainer);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> root.getChildren().remove(messageContainer));

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
        EventHandler<ActionEvent> wrappedHandler = e -> {
            System.out.println("Back button clicked - saving state before exit");
            //saveStateOnExit();
            if (infoStage != null) {
                infoStage.close();
                infoStage = null;
            }
            if (handler != null) {
                handler.handle(e);
            }
        };
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

    /**
     * Cleans up resources used by this view.
     * This should be called when the view is no longer needed.
     */
    public void cleanup() {
        System.out.println("GamePlayView cleanup called");
        //saveStateOnExit();

        if (gameBridge != null) {
            gameBridge.stopMonitoring();
            gameBridge = null;
        }

        if (gameNodeCheckTimeline != null) {
            gameNodeCheckTimeline.stop();
            gameNodeCheckTimeline = null;
        }

        if (backButton != null) {
            backButton.setOnAction(null);
        }

        backHandler = null;
        nextLevelHandler = null;
        onLevelCompleted = null;

        System.out.println("GamePlayView cleanup completed");
    }


}