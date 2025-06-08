/**
 * Soubor: src/main/java/visualization/view/GamePlayView.java
 *
 * Popis:
 *
 *  Zobrazuje výběr úrovní hry rozdělený dle obtížností.
 *  Nabízí přepínání mezi třemi obtížnostmi (Beginner, Intermediate, Advanced),
 *  vykresluje mřížku tlačítek s čísly úrovní, umožňuje návrat zpět
 *  a reset ukládaného postupu.
 *
 *
 * @Author: Yaroslav Hryn (xhryny00), Oleksandr Musiichuk (xmusii00)
 */

package visualization.view;

import ija.ijaProject.game.levels.LevelManager;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import ija.ijaProject.game.simulation.SimulationState;

/** Zobrazuje výběr úrovní hry rozdělený dle obtížností.*/
    public class LevelsView {

        private final StackPane root;
        private final Stage stage;
        private Button backButton;
        private Button easyButton;
        private Button mediumButton;
        private Button hardButton;
        private final VBox contentContainer;
        private final VBox levelsContainer;
        private int currentDifficulty = 0; // 0 = easy, 1 = medium, 2 = hard
        private EventHandler<ActionEvent> levelSelectHandler;
        private final LevelManager levelManager = LevelManager.getInstance();
        private ToggleButton simulationToggle;
        private static boolean isSimulationModeActive = false;


    /**
     * Vytvoří  LevelsView pro zadané JavaFX
     * @param stage primární okno aplikace
     */
    public LevelsView(Stage stage) {
        this.stage = stage;

        root = new StackPane();

        Pane backgroundPane = createAnimatedBackground();
        root.getChildren().add(backgroundPane);

        contentContainer = new VBox(30);
        contentContainer.setAlignment(Pos.TOP_CENTER);
        contentContainer.maxWidthProperty().bind(stage.widthProperty().multiply(0.9));
        contentContainer.setPadding(new Insets(40, 20, 40, 20));

        Pane headerBox = createHeader();

        HBox difficultySelector = createDifficultySelector();

        levelsContainer = new VBox();
        levelsContainer.setAlignment(Pos.CENTER);
        levelsContainer.setPadding(new Insets(20, 0, 0, 0));

        showLevels(0);

        Pane circuitDecoration = createCircuitDecoration();

        contentContainer.getChildren().addAll(headerBox, difficultySelector, levelsContainer);

        root.getChildren().addAll(circuitDecoration, contentContainer);

        startAnimations(circuitDecoration);
    }

    /**
     * Vytvoří Pane obsahující pozadí:
     * @return Pane s animovaným pozadím
     */
    private Pane createAnimatedBackground() {
        Pane backgroundPane = new Pane();

        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#0F172A")),
                new Stop(0.5, Color.web("#1E293B")),
                new Stop(1, Color.web("#0F172A"))
        );

        Rectangle background = new Rectangle();
        background.widthProperty().bind(backgroundPane.widthProperty());
        background.heightProperty().bind(backgroundPane.heightProperty());
        background.setFill(gradient);

        Group gridLines = createGridLines();

        Group particles = createEnergyParticles();

        backgroundPane.getChildren().addAll(background, gridLines, particles);

        backgroundPane.prefWidthProperty().bind(stage.widthProperty());
        backgroundPane.prefHeightProperty().bind(stage.heightProperty());

        return backgroundPane;
    }

    /**
     * Vytvoří skupinu vodorovných a svislých čar tvořících
     * elektrickou mřížku. Čáry se dynamicky škálují podle velikosti okna.
     *
     * @return Group obsahující mřížkové čáry
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
     * Vytvoří skupinu kruhových částic energie, které se náhodně rozmístí po celé ploše
     * a animují se pulzujícím efektem září a zvětšování.
     *
     * @return Group obsahující animované energetické částice
     */
    private Group createEnergyParticles() {
        Group particles = new Group();

        for (int i = 0; i < 50; i++) {
            double xPercent = Math.random();
            double yPercent = Math.random();
            double size = 2 + Math.random() * 4;

            Circle particle = new Circle(size);

            particle.centerXProperty().bind(stage.widthProperty().multiply(xPercent));
            particle.centerYProperty().bind(stage.heightProperty().multiply(yPercent));

            particle.setFill(Color.web("#22D3EE", 0.6));

            Glow glow = new Glow(0.8);
            particle.setEffect(glow);

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(particle.opacityProperty(), 0.2 + Math.random() * 0.6),
                            new KeyValue(particle.radiusProperty(), size)
                    ),
                    new KeyFrame(Duration.seconds(1 + Math.random() * 2),
                            new KeyValue(particle.opacityProperty(), 0.8 + Math.random() * 0.2),
                            new KeyValue(particle.radiusProperty(), size * 1.5)
                    )
            );
            timeline.setAutoReverse(true);
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();

            particles.getChildren().add(particle);
        }

        return particles;
    }

    /**
     * Vytvoří hlavičku obrazovky s názvem "Select Level" a tlačítkem zpět.
     * Hlavička je umístěna do StackPane pro snadné zarovnání prvků.
     *
     * @return Pane obsahující nadpis a tlačítko zpět
     */
        private Pane createHeader() {
            StackPane headerContainer = new StackPane();
            headerContainer.setPadding(new Insets(0, 0, 20, 0));

            Text levelsTitle = new Text("Select Level");
            levelsTitle.setFont(Font.font("System", FontWeight.BOLD, 36));
            levelsTitle.setFill(Color.web("#7DD3FC"));

            DropShadow titleGlow = new DropShadow();
            titleGlow.setColor(Color.web("#0EA5E9"));
            titleGlow.setRadius(10);
            titleGlow.setSpread(0.2);
            levelsTitle.setEffect(titleGlow);

            backButton = new Button("←  Back");
            backButton.setStyle(
                    "-fx-background-color: rgba(14, 165, 233, 0.2);" +
                            "-fx-background-radius: 30;" +
                            "-fx-border-color: #0EA5E9;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 30;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 16px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-padding: 8 20;"
            );
/*
            simulationToggle = new ToggleButton();
            updateSimulationToggleText();

            simulationToggle.setStyle(
                    "-fx-background-color: rgba(14, 165, 233, 0.2);" +
                            "-fx-background-radius: 20;" +
                            "-fx-border-color: #0EA5E9;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 20;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 12px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-padding: 6 12;"
            );

            simulationToggle.setSelected(SimulationState.isSimulationMode()); // Восстанавливаем состояние
            simulationToggle.setPickOnBounds(true);
            simulationToggle.setFocusTraversable(false);

            simulationToggle.setOnAction(e -> {
                SimulationState.setSimulationMode(simulationToggle.isSelected());
                updateSimulationToggleText();
            });

            headerContainer.getChildren().add(simulationToggle);*/

           // StackPane.setAlignment(simulationToggle, Pos.CENTER_RIGHT);

            DropShadow shadow = new DropShadow();
            shadow.setColor(Color.web("#0EA5E9"));
            shadow.setRadius(10);
            shadow.setSpread(0);
            backButton.setEffect(shadow);

            addButtonHoverEffect(backButton);

            headerContainer.getChildren().add(levelsTitle);
            StackPane.setAlignment(levelsTitle, Pos.CENTER);

            headerContainer.getChildren().add(backButton);
            StackPane.setAlignment(backButton, Pos.CENTER_LEFT);

            return headerContainer;
        }

        public boolean isSimulationModeEnabled() {
            return simulationToggle != null && simulationToggle.isSelected();
        }

        private void updateSimulationToggleText() {
            simulationToggle.setText(
                    SimulationState.isSimulationMode()
                            ? "Exit Simulation Mode"
                            : "Simulation Mode"
            );
        }


    /**
     * Vytvoří komponentu pro výběr obtížnosti hry s tlačítky Beginner, Intermediate a Advanced.
     * Každé tlačítko změní aktuální úroveň obtížnosti a obnoví seznam úrovní.
     *
     * @return HBox obsahující kontejner s tlačítky obtížnosti
     */
    private HBox createDifficultySelector() {
        HBox difficultyBox = new HBox(15);
        difficultyBox.setAlignment(Pos.CENTER);
        difficultyBox.setPadding(new Insets(10, 0, 10, 0));

        HBox container = new HBox(15);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(15, 30, 15, 30));
        container.setStyle(
                "-fx-background-color: rgba(15, 23, 42, 0.85);" +
                        "-fx-background-radius: 15px;" +
                        "-fx-border-color: rgba(14, 165, 233, 0.3);" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 15px;"
        );

        easyButton = createDifficultyButton("Beginner", 0);
        mediumButton = createDifficultyButton("Intermediate", 1);
        hardButton = createDifficultyButton("Advanced", 2);

        updateDifficultyButtonStyles();

        container.getChildren().addAll(easyButton, mediumButton, hardButton);

        difficultyBox.getChildren().add(container);

        return difficultyBox;
    }

    /**
     * Vytvoří tlačítko pro zvolenou obtížnost, které při stisknutí
     * změní aktuální obtížnost a znovu vykreslí úrovně.
     *
     * @param text text tlačítka (např. "Beginner")
     * @param difficulty kód obtížnosti (0=Beginner, 1=Intermediate, 2=Advanced)
     * @return nově vytvořené tlačítko
     */
    private Button createDifficultyButton(String text, int difficulty) {
        Button button = new Button(text);

        button.setOnAction(e -> {
            currentDifficulty = difficulty;
            updateDifficultyButtonStyles();
            showLevels(difficulty);
        });

        return button;
    }

    /**
     * Aktualizuje styly tlačítek obtížnosti podle toho,
     * která obtížnost je právě vybraná.
     * Vybrané tlačítko se zobrazí intenzivněji a s efektem záře.
     */
    private void updateDifficultyButtonStyles() {
        String unselectedStyle =
                "-fx-background-color: rgba(14, 165, 233, 0.2);" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-color: #0EA5E9;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 20;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 15;";

        String selectedStyle =
                "-fx-background-color: rgba(14, 165, 233, 0.5);" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-color: #0EA5E9;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 20;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 15;";

        easyButton.setStyle(currentDifficulty == 0 ? selectedStyle : unselectedStyle);
        mediumButton.setStyle(currentDifficulty == 1 ? selectedStyle : unselectedStyle);
        hardButton.setStyle(currentDifficulty == 2 ? selectedStyle : unselectedStyle);

        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#0EA5E9"));
        glow.setRadius(15);
        glow.setSpread(0.3);

        easyButton.setEffect(currentDifficulty == 0 ? glow : null);
        mediumButton.setEffect(currentDifficulty == 1 ? glow : null);
        hardButton.setEffect(currentDifficulty == 2 ? glow : null);
    }


    /**
     * Zobrazí seznam úrovní pro zvolenou obtížnost.
     * Vymaže předchozí obsah, vytvoří obalovací VBox s poloprůhledným pozadím,
     * přidá nadpis obtížnosti, mřížku tlačítek úrovní a tlačítko pro resetování postupu.
     *
     * @param difficulty kód obtížnosti (0=Beginner, 1=Intermediate, 2=Advanced)
     */
    private void showLevels(int difficulty) {
        levelsContainer.getChildren().clear();

        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(25));
        container.setStyle(
                "-fx-background-color: rgba(15, 23, 42, 0.85);" +
                        "-fx-background-radius: 15px;" +
                        "-fx-border-color: rgba(14, 165, 233, 0.3);" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 15px;"
        );

        String difficultyName = difficulty == 0 ? "Beginner" : difficulty == 1 ? "Intermediate" : "Advanced";
        Text difficultyTitle = new Text(difficultyName + " Levels");
        difficultyTitle.setFont(Font.font("System", FontWeight.BOLD, 24));
        difficultyTitle.setFill(Color.web("#0EA5E9"));

        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#0EA5E9"));
        glow.setRadius(5);
        glow.setSpread(0.1);
        difficultyTitle.setEffect(glow);

        GridPane levelsGrid = createLevelsGrid(difficulty);

        Button resetButton = createResetProgressButton();

        Region spacer = new Region();
        spacer.setPrefHeight(10);

        container.getChildren().addAll(difficultyTitle, levelsGrid, spacer, resetButton);

        levelsContainer.getChildren().add(container);
    }

    /**
     * Vytvoří a vrátí GridPane 5×2 s tlačítky pro jednotlivé úrovně
     * podle dané obtížnosti. Každé tlačítko spouští metodu pro načtení úrovně.
     *
     * @param difficulty kód obtížnosti úrovní (0=Beginner, 1=Intermediate, 2=Advanced)
     * @return GridPane obsahující 10 tlačítek úrovní
     */
    private GridPane createLevelsGrid(int difficulty) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15);
        grid.setVgap(15);

        int highestCompleted = levelManager.getHighestCompletedLevel(difficulty);

        //  5x2 grid, 10 levels
        for (int i = 0; i < 10; i++) {
            int levelNumber = i + 1;
            int row = i / 5;
            int col = i % 5;

            boolean isCompleted = LevelManager.getInstance().isLevelCompleted(levelNumber, difficulty);

            boolean isUnlocked = true; // This is the key change

            Button levelButton = createLevelButton(levelNumber, difficulty, highestCompleted);
            grid.add(levelButton, col, row);
        }

        return grid;
    }

    /**
     * Vytvoří tlačítko pro danou úroveň s odpovídajícím vzhledem a efekty.
     * Označí tlačítko jako dokončené (zelené) nebo nedokončené (modré),
     * přidá záři a animaci při najetí myší a nastaví akci volající
     * levelSelectHandler při kliknutí.
     *
     * @param levelNumber      číslo úrovně (1–10)
     * @param difficulty       úroveň obtížnosti (0=Beginner, 1=Intermediate, 2=Advanced)
     * @param highestCompleted nejvyšší dokončená úroveň pro danou obtížnost
     * @return tlačítko reprezentující danou úroveň
     */
    private Button createLevelButton(int levelNumber, int difficulty, int highestCompleted) {
        Button button = new Button();

        button.getProperties().put("levelNumber", levelNumber);
        button.getProperties().put("difficulty", difficulty);

        boolean isCompleted = levelManager.isLevelCompleted(levelNumber, difficulty);

        boolean isUnlocked = true;

        if (isCompleted) {
            button.setText("✓");
        } else {
            button.setText(String.valueOf(levelNumber));
        }

        String baseStyle =
                "-fx-background-radius: 10;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-min-width: 60px;" +
                        "-fx-min-height: 60px;" +
                        "-fx-max-width: 60px;" +
                        "-fx-max-height: 60px;";

        if (isCompleted) {
            button.setStyle(
                    "-fx-background-color: rgba(34, 197, 94, 0.6);" +
                            "-fx-border-color: #22C55E;" +
                            "-fx-text-fill: white;" +
                            baseStyle
            );
        } else {
            button.setStyle(
                    "-fx-background-color: rgba(14, 165, 233, 0.3);" +
                            "-fx-border-color: #0EA5E9;" +
                            "-fx-text-fill: white;" +
                            baseStyle
            );
        }

        DropShadow glow = new DropShadow();
        if (isCompleted) {
            glow.setColor(Color.web("#22C55E"));
        } else {
            glow.setColor(Color.web("#0EA5E9"));
        }
        glow.setRadius(10);
        glow.setSpread(0.1);
        button.setEffect(glow);

        button.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), button);
            st.setToX(1.1);
            st.setToY(1.1);
            st.play();
            String hoverStyle = isCompleted ?
                    "-fx-background-color: rgba(34, 197, 94, 0.8);" +
                            "-fx-border-color: #22C55E;" :
                    "-fx-background-color: rgba(14, 165, 233, 0.5);" +
                            "-fx-border-color: #0EA5E9;";

            button.setStyle(
                    hoverStyle +
                            "-fx-text-fill: white;" +
                            baseStyle
            );

            DropShadow hoverGlow = new DropShadow();
            hoverGlow.setColor(isCompleted ? Color.web("#22C55E") : Color.web("#0EA5E9"));
            hoverGlow.setRadius(15);
            hoverGlow.setSpread(0.3);
            button.setEffect(hoverGlow);
        });

        button.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), button);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();

            String normalStyle = isCompleted ?
                    "-fx-background-color: rgba(34, 197, 94, 0.6);" +
                            "-fx-border-color: #22C55E;" :
                    "-fx-background-color: rgba(14, 165, 233, 0.3);" +
                            "-fx-border-color: #0EA5E9;";

            button.setStyle(
                    normalStyle +
                            "-fx-text-fill: white;" +
                            baseStyle
            );

            DropShadow originalGlow = new DropShadow();
            originalGlow.setColor(isCompleted ? Color.web("#22C55E") : Color.web("#0EA5E9"));
            originalGlow.setRadius(10);
            originalGlow.setSpread(0.1);
            button.setEffect(originalGlow);
        });

        button.setOnAction(e -> {
            if (levelSelectHandler != null) {
                levelSelectHandler.handle(e);
            }
        });

        return button;
    }

    /**
     * Přidá tlačítku efekt při najetí myší – mírné zvětšení, změnu průhlednosti pozadí
     * a zesílenou záři. Po opuštění ta změnu vrátí zpět.
     *
     * @param button tlačítko, na které se má efekt aplikovat
     */
    private void addButtonHoverEffect(Button button) {
        button.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), button);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();

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
            ScaleTransition st = new ScaleTransition(Duration.millis(150), button);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();

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
     * Vytvoří dekorativní obvody (Path) ve dvou rozích okna.
     * Obvody se mění podle velikosti okna díky vázání vlastností.
     *
     * @return Pane obsahující dvě cesty obvodu
     */
    private Pane createCircuitDecoration() {
        Pane circuitPane = new Pane();

        circuitPane.prefWidthProperty().bind(stage.widthProperty());
        circuitPane.prefHeightProperty().bind(stage.heightProperty());

        Path topLeftCircuit = createCircuitPath(0.05, 0.05, 0.25, 0.25);
        Path bottomRightCircuit = createCircuitPath(0.7, 0.7, 0.25, 0.25);

        circuitPane.getChildren().addAll(topLeftCircuit, bottomRightCircuit);
        return circuitPane;
    }

    /**
     * Sestaví cestu ("path"), která připomíná elektrický obvod,
     * zakotvenou procentuálně vzhledem k velikosti okna.
     *
     * @param startXPercent  výchozí X pozice jako procento šířky okna
     * @param startYPercent  výchozí Y pozice jako procento výšky okna
     * @param widthPercent   šířka obvodu jako procento šířky okna
     * @param heightPercent  výška obvodu jako procento výšky okna
     * @return Path s několika segmenty, stylizovaný jako obvod
     */
    private Path createCircuitPath(double startXPercent, double startYPercent, double widthPercent, double heightPercent) {
        Path path = new Path();

        MoveTo moveTo = new MoveTo();
        moveTo.xProperty().bind(stage.widthProperty().multiply(startXPercent));
        moveTo.yProperty().bind(stage.heightProperty().multiply(startYPercent));

        HLineTo hLine1 = new HLineTo();
        hLine1.xProperty().bind(stage.widthProperty().multiply(startXPercent + widthPercent * 0.3));

        VLineTo vLine1 = new VLineTo();
        vLine1.yProperty().bind(stage.heightProperty().multiply(startYPercent + heightPercent * 0.4));

        HLineTo hLine2 = new HLineTo();
        hLine2.xProperty().bind(stage.widthProperty().multiply(startXPercent + widthPercent * 0.7));

        VLineTo vLine2 = new VLineTo();
        vLine2.yProperty().bind(stage.heightProperty().multiply(startYPercent + heightPercent * 0.7));

        HLineTo hLine3 = new HLineTo();
        hLine3.xProperty().bind(stage.widthProperty().multiply(startXPercent + widthPercent));

        path.getElements().addAll(moveTo, hLine1, vLine1, hLine2, vLine2, hLine3);

        path.setStroke(Color.web("#0EA5E9", 0.6));
        path.setStrokeWidth(2);
        path.setStrokeLineCap(StrokeLineCap.ROUND);
        path.getStrokeDashArray().addAll(15.0, 5.0, 5.0, 5.0);

        return path;
    }

    /**
     * Spustí animace všech obvodových cest v zadaném Pane.
     * Každá cesta dostane plynulý pohyb přerušované čáry a periodické
     * prolínání (fading) mezi hodnotami průhlednosti.
     *
     * @param circuitDecoration Pane obsahující cesty (Path), které mají být animovány
     */
    private void startAnimations(Pane circuitDecoration) {
        for (int i = 0; i < circuitDecoration.getChildren().size(); i++) {
            Path path = (Path) circuitDecoration.getChildren().get(i);

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(path.strokeDashOffsetProperty(), 0)
                    ),
                    new KeyFrame(Duration.seconds(10),
                            new KeyValue(path.strokeDashOffsetProperty(), 100)
                    )
            );
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();

            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), path);
            fadeTransition.setFromValue(0.4);
            fadeTransition.setToValue(0.8);
            fadeTransition.setCycleCount(Animation.INDEFINITE);
            fadeTransition.setAutoReverse(true);
            fadeTransition.play();
        }
    }

    /**
     * Nastaví obsluhu události pro tlačítko „Back“.
     *
     * @param handler EventHandler, který bude volán při kliknutí na tlačítko zpět
     */
    public void setOnBackAction(EventHandler<ActionEvent> handler) {
        backButton.setOnAction(handler);
    }


    /**
     * Nastaví globální obsluhu pro výběr úrovně.
     * Tato funkce se zavolá, když uživatel klikne na tlačítko s číslem úrovně.
     *
     * @param handler EventHandler, který bude volán při výběru úrovně
     */
    public void setOnLevelSelectAction(EventHandler<ActionEvent> handler) {
        this.levelSelectHandler = handler;
    }

    /**
     * Vrátí hlavní kontejner (root) tohoto pohledu.
     *
     * @return StackPane představující kořenovou vrstvu UI
     */
    public StackPane getRoot() {
        return root;
    }

    /**
     * Vytvoří tlačítko pro resetování postupu ve hře.
     * Při kliknutí otevře dialog pro potvrzení resetu.
     *
     * @return Button označený „Reset Progress“
     */
    private Button createResetProgressButton() {
        Button resetButton = new Button("Reset Progress");
        resetButton.setStyle(
                "-fx-background-color: rgba(239, 68, 68, 0.2);" + // Red background
                        "-fx-background-radius: 30;" +
                        "-fx-border-color: #EF4444;" + // Red border
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 30;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 20;"
        );

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#EF4444"));
        shadow.setRadius(10);
        shadow.setSpread(0);
        resetButton.setEffect(shadow);

        resetButton.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), resetButton);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();

            String style = resetButton.getStyle();
            style = style.replace("rgba(239, 68, 68, 0.2)", "rgba(239, 68, 68, 0.4)");
            resetButton.setStyle(style);

            DropShadow hoverShadow = new DropShadow();
            hoverShadow.setColor(Color.web("#EF4444"));
            hoverShadow.setRadius(15);
            hoverShadow.setSpread(0.2);
            resetButton.setEffect(hoverShadow);
        });

        resetButton.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), resetButton);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();

            String style = resetButton.getStyle();
            style = style.replace("rgba(239, 68, 68, 0.4)", "rgba(239, 68, 68, 0.2)");
            resetButton.setStyle(style);

            DropShadow originalShadow = new DropShadow();
            originalShadow.setColor(Color.web("#EF4444"));
            originalShadow.setRadius(10);
            originalShadow.setSpread(0);
            resetButton.setEffect(originalShadow);
        });

        resetButton.setOnAction(e -> {
            showResetConfirmationDialog();
        });

        return resetButton;
    }


    /**
     * Zobrazí modální potvrzovací dialog pro vymazání postupu.
     * V dialogu se objeví varovná ikona, nadpis, vysvětlující text
     * a tlačítka „Cancel“ a „Reset All Progress“. Po potvrzení se
     * smaže postup všech úrovní, zavře dialog a obnoví zobrazení.
     */
    private void showResetConfirmationDialog() {
        StackPane dialogRoot = new StackPane();
        dialogRoot.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7)");

        VBox dialogContent = new VBox(20);
        dialogContent.setAlignment(Pos.CENTER);
        dialogContent.setPadding(new Insets(30));
        dialogContent.setMaxWidth(400);
        dialogContent.setMaxHeight(250);
        dialogContent.setStyle(
                "-fx-background-color: #1E293B;" +
                        "-fx-background-radius: 15px;" +
                        "-fx-border-color: #0EA5E9;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 15px;"
        );

        Text warningIcon = new Text("⚠️");
        warningIcon.setFont(Font.font("System", FontWeight.BOLD, 40));
        warningIcon.setFill(Color.web("#FBBF24"));

        Text title = new Text("Reset Progress");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        title.setFill(Color.web("#FBBF24"));

        Text message = new Text("Are you sure you want to reset all level progress? This action cannot be undone.");
        message.setFont(Font.font("System", FontWeight.NORMAL, 16));
        message.setFill(Color.WHITE);
        message.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        message.setWrappingWidth(340);

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle(
                "-fx-background-color: rgba(100, 116, 139, 0.3);" +
                        "-fx-background-radius: 30;" +
                        "-fx-border-color: #64748B;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 30;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 20;"
        );

        Button confirmButton = new Button("Reset All Progress");
        confirmButton.setStyle(
                "-fx-background-color: rgba(239, 68, 68, 0.3);" +
                        "-fx-background-radius: 30;" +
                        "-fx-border-color: #EF4444;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 30;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 20;"
        );

        addButtonHoverEffect(cancelButton);

        confirmButton.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), confirmButton);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();

            String style = confirmButton.getStyle();
            style = style.replace("rgba(239, 68, 68, 0.3)", "rgba(239, 68, 68, 0.5)");
            confirmButton.setStyle(style);

            DropShadow hoverShadow = new DropShadow();
            hoverShadow.setColor(Color.web("#EF4444"));
            hoverShadow.setRadius(15);
            hoverShadow.setSpread(0.2);
            confirmButton.setEffect(hoverShadow);
        });

        confirmButton.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), confirmButton);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();

            String style = confirmButton.getStyle();
            style = style.replace("rgba(239, 68, 68, 0.5)", "rgba(239, 68, 68, 0.3)");
            confirmButton.setStyle(style);

            DropShadow originalShadow = new DropShadow();
            originalShadow.setColor(Color.web("#EF4444"));
            originalShadow.setRadius(10);
            originalShadow.setSpread(0);
            confirmButton.setEffect(originalShadow);
        });

        buttonBox.getChildren().addAll(cancelButton, confirmButton);

        dialogContent.getChildren().addAll(warningIcon, title, message, buttonBox);

        dialogRoot.getChildren().add(dialogContent);


        Scene dialogScene = new Scene(dialogRoot, stage.getWidth(), stage.getHeight());
        dialogScene.setFill(Color.TRANSPARENT);

        Stage dialogStage = new Stage();
        dialogStage.initOwner(stage);
        dialogStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
        dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        dialogStage.setScene(dialogScene);

        cancelButton.setOnAction(e -> dialogStage.close());

        confirmButton.setOnAction(e -> {
            levelManager.resetProgress();
            dialogStage.close();
            showLevels(currentDifficulty);
            showResetConfirmationToast();
        });

        dialogStage.show();
    }

    /**
     * Zobrazí krátké oznamovací „toast“ okno informující o úspěšném resetu.
     * Toast se vyblikuje na dně okna a po 3 sekundách automaticky zmizí.
     */
    private void showResetConfirmationToast() {
        StackPane toastContainer = new StackPane();
        toastContainer.setStyle(
                "-fx-background-color: rgba(34, 197, 94, 0.9);" +
                        "-fx-background-radius: 30px;" +
                        "-fx-padding: 15px 25px;"
        );

        HBox toastContent = new HBox(10);
        toastContent.setAlignment(Pos.CENTER);

        Text checkIcon = new Text("✓");
        checkIcon.setFont(Font.font("System", FontWeight.BOLD, 18));
        checkIcon.setFill(Color.WHITE);

        Text message = new Text("Progress reset successfully!");
        message.setFont(Font.font("System", FontWeight.BOLD, 16));
        message.setFill(Color.WHITE);

        toastContent.getChildren().addAll(checkIcon, message);
        toastContainer.getChildren().add(toastContent);

        root.getChildren().add(toastContainer);
        StackPane.setAlignment(toastContainer, Pos.BOTTOM_CENTER);
        StackPane.setMargin(toastContainer, new Insets(0, 0, 50, 0));

        toastContainer.setOpacity(0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), toastContainer);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();


        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), toastContainer);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(event -> root.getChildren().remove(toastContainer));
            fadeOut.play();
        });
        pause.play();
    }
}