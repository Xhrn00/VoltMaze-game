/**
 * Soubor: src/main/java/visualization/view/InfoView.java
 *
 * Popis:
 *  Třída InfoView zajišťuje zobrazení informací o hře.
 *   Vytváří animované pozadí s elektrickými částicemi a obvody,
 *   flexibilní rozložení reagující na změnu velikosti okna a posuvný panel pro textový obsah.
 *
 *
 * @Author: Yaroslav Hryn (xhryny00), Oleksandr Musiichuk (xmusii00)
 */

package visualization.view;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

/** Třída InfoView zajišťuje zobrazení informací o hře.*/
public class InfoView {

    private final StackPane root;
    private final Stage stage;
    private Button backButton;


    /**
     * Konstruktor třídy <code>InfoView</code>.
     * Inicializuje hlavní rozložení, animace a obsah.
     *
     * @param stage primární JavaFX Stage, na které se InfoView zobrazí
     */
    public InfoView(Stage stage) {
        this.stage = stage;

        root = new StackPane();
        root.setAlignment(Pos.CENTER);

        Pane backgroundPane = createAnimatedBackground();
        root.getChildren().add(backgroundPane);

        Pane circuitDecoration = createCircuitDecoration();
        root.getChildren().add(circuitDecoration);

        StackPane centeringContainer = new StackPane();
        centeringContainer.setAlignment(Pos.CENTER);
        centeringContainer.prefWidthProperty().bind(stage.widthProperty());
        centeringContainer.prefHeightProperty().bind(stage.heightProperty());

        VBox contentContainer = new VBox(30);
        contentContainer.setAlignment(Pos.TOP_CENTER);
        contentContainer.setPadding(new Insets(40, 20, 40, 20));

        contentContainer.maxWidthProperty().bind(
                stage.widthProperty().multiply(
                        stage.widthProperty().lessThan(600).get() ? 0.95 :
                                stage.widthProperty().lessThan(1000).get() ? 0.85 : 0.7
                )
        );

        HBox headerBox = createHeader();

        VBox infoContent = createInfoContent();

        ScrollPane scrollPane = new ScrollPane(infoContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPannable(true); // Enable panning with mouse drag

        scrollPane.setStyle(
                "-fx-background: transparent;" +
                        "-fx-background-color: transparent;" +
                        "-fx-padding: 0;" +
                        "-fx-border-width: 0;" +
                        "-fx-control-inner-background: transparent;"
        );

        contentContainer.getChildren().addAll(headerBox, scrollPane);

        centeringContainer.getChildren().add(contentContainer);

        root.getChildren().add(centeringContainer);

        startAnimations(circuitDecoration);

        configurePlatformSpecificSettings();
    }



    /**
     * Konfiguruje platformně specifická nastavení
     */
    private void configurePlatformSpecificSettings() {
        boolean isMobile = detectMobileDevice();

        if (isMobile) {
            adjustFontSizesForMobile();
            backButton.setPrefHeight(50);
            backButton.setPrefWidth(120);
            backButton.setStyle(backButton.getStyle() + "-fx-font-size: 18px;");
        }
    }

    /**
     * Detekuje, zda aplikace běží na mobilním zařízení.
     *
     * @return <code>true</code>, pokud je mobilní zařízení, jinak <code>false</code>
     */
    private boolean detectMobileDevice() {
        boolean smallScreen = stage.getWidth() < 800;

        String osName = System.getProperty("os.name").toLowerCase();
        boolean touchOS = osName.contains("android") || osName.contains("ios");

        return smallScreen || touchOS;
    }

    private void adjustFontSizesForMobile() {
    }

    /**
     * Vytvoří animované pozadí s přechodem a efekty částic.
     *
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
     * Vytvoří síť mřížkových čar na pozadí.
     *
     * @return Group s grafickými čárami
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
     * Vytvoří animované částice energie rozptýlené po ploše.
     *
     * @return Group s částicemi
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
     * Vytvoří hlavičku s návratovým tlačítkem a názvem sekce.
     *
     * @return HBox s tlačítkem a textem
     */
    private HBox createHeader() {
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(0, 0, 20, 0));

        headerBox.prefWidthProperty().bind(
                stage.widthProperty().multiply(
                        stage.widthProperty().lessThan(600).get() ? 0.95 :
                                stage.widthProperty().lessThan(1000).get() ? 0.85 : 0.7
                )
        );
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

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#0EA5E9"));
        shadow.setRadius(10);
        shadow.setSpread(0);
        backButton.setEffect(shadow);

        backButton.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), backButton);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();

            backButton.setStyle(
                    "-fx-background-color: rgba(14, 165, 233, 0.4);" +
                            "-fx-background-radius: 30;" +
                            "-fx-border-color: #0EA5E9;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 30;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 16px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-padding: 8 20;"
            );

            DropShadow hoverShadow = new DropShadow();
            hoverShadow.setColor(Color.web("#0EA5E9"));
            hoverShadow.setRadius(15);
            hoverShadow.setSpread(0.2);
            backButton.setEffect(hoverShadow);
        });

        backButton.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), backButton);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();

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

            DropShadow originalShadow = new DropShadow();
            originalShadow.setColor(Color.web("#0EA5E9"));
            originalShadow.setRadius(10);
            originalShadow.setSpread(0);
            backButton.setEffect(originalShadow);
        });

        Text infoTitle = new Text("Game Information");
        infoTitle.setFont(Font.font("System", FontWeight.BOLD, 36));
        infoTitle.setFill(Color.web("#7DD3FC"));

        DropShadow titleGlow = new DropShadow();
        titleGlow.setColor(Color.web("#0EA5E9"));
        titleGlow.setRadius(10);
        titleGlow.setSpread(0.2);
        infoTitle.setEffect(titleGlow);

        StackPane stackPane = new StackPane();
        stackPane.prefWidthProperty().bind(headerBox.widthProperty());

        stackPane.getChildren().add(infoTitle);

        StackPane.setAlignment(backButton, Pos.CENTER_LEFT);
        stackPane.getChildren().add(backButton);

        headerBox.getChildren().add(stackPane);

        return headerBox;
    }

    /**
     * Vytvoří kontejner s textovým obsahem sekcí a popisy.
     *
     * @return VBox s oddíly informací
     */
    private VBox createInfoContent() {
        VBox infoBox = new VBox(25);
        infoBox.setAlignment(Pos.CENTER);

        VBox contentContainer = new VBox(25);
        contentContainer.setAlignment(Pos.TOP_LEFT);
        contentContainer.setPadding(new Insets(30));
        contentContainer.setStyle(
                "-fx-background-color: rgba(15, 23, 42, 0.85);" +
                        "-fx-background-radius: 15px;" +
                        "-fx-border-color: rgba(14, 165, 233, 0.3);" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 15px;"
        );

        contentContainer.prefWidthProperty().bind(
                stage.widthProperty().multiply(
                        stage.widthProperty().lessThan(600).get() ? 0.9 :
                                stage.widthProperty().lessThan(1000).get() ? 0.8 : 0.7
                )
        );
        contentContainer.maxWidthProperty().bind(contentContainer.prefWidthProperty());

        Text aboutTitle = createSectionTitle("About VoltMaze");
        Text aboutContent = createContentText(
                "VoltMaze is an electrifying puzzle game that challenges your logical thinking and problem-solving skills. " +
                        "Inspired by the classic LightBulb puzzle game, VoltMaze adds an electric twist to the gameplay, " +
                        "creating a unique and engaging experience."
        );

        Text howToPlayTitle = createSectionTitle("How to Play");
        Text howToPlayContent = createContentText(
                "The goal of VoltMaze is to light up all the electric cells on the grid according to specific rules:\n\n" +
                        "1. Each cell can be in one of two states: powered (lit) or unpowered (dark).\n\n" +
                        "2. Clicking on a cell toggles its state between powered and unpowered.\n\n" +
                        "3. Electric cells must follow these rules:\n" +
                        "   • A powered cell illuminates its entire row and column.\n" +
                        "   • No two powered cells can illuminate each other.\n" +
                        "   • All dark cells must be illuminated by at least one powered cell.\n\n" +
                        "4. Some cells may be fixed and cannot be toggled."
        );

        Text strategiesTitle = createSectionTitle("Strategies");
        Text strategiesContent = createContentText(
                "Here are some tips to help you solve VoltMaze puzzles:\n\n" +
                        "• Start with the obvious moves: If a cell can only be illuminated by one possible powered cell, that cell must be powered.\n\n" +
                        "• Look for cells that cannot be powered: If powering a cell would cause a conflict with the rules, it must remain dark.\n\n" +
                        "• Use the process of elimination: If you've determined all the cells that must be powered in a row or column, the rest must be dark.\n\n" +
                        "• Sometimes it helps to work backward: Assume a cell is powered and see if it leads to a contradiction."
        );

        Text difficultyTitle = createSectionTitle("Difficulty Levels");
        Text difficultyContent = createContentText(
                "VoltMaze offers multiple difficulty levels to challenge players of all skill levels:\n\n" +
                        "• Beginner: 5×5 grid with few obstacles\n" +
                        "• Intermediate: 7×7 grid with more obstacles\n" +
                        "• Advanced: 10×10 grid with complex obstacle patterns\n" +
                        "• Expert: 12×12 grid with minimal hints and maximum challenge"
        );

        Text creditsTitle = createSectionTitle("Credits");
        Text creditsContent = createContentText(
                "VoltMaze was developed as a project for IJA (Java Programming Language) course.\n\n" +
                        "The game was inspired by the classic LightBulb puzzle game, with an electric theme added to create a unique experience.\n\n" +
                        "© 2024 VoltMaze Team"
        );

        contentContainer.getChildren().addAll(
                aboutTitle, aboutContent,
                howToPlayTitle, howToPlayContent,
                strategiesTitle, strategiesContent,
                difficultyTitle, difficultyContent,
                creditsTitle, creditsContent
        );

        infoBox.getChildren().add(contentContainer);

        return infoBox;
    }

    /**
     * Vytvoří nadpis sekce s efektem záře.
     *
     * @param title text nadpisu
     * @return Text jako sekční nadpis
     */
    private Text createSectionTitle(String title) {
        Text sectionTitle = new Text(title);
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 24));
        sectionTitle.setFill(Color.web("#0EA5E9"));

        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#0EA5E9"));
        glow.setRadius(5);
        glow.setSpread(0.1);
        sectionTitle.setEffect(glow);

        return sectionTitle;
    }

    /**
     * Vytvoří blok textu s automatickým zalomením.
     *
     * @param content text obsahu
     * @return Text s obsahem
     */
    private Text createContentText(String content) {
        Text text = new Text(content);
        text.setFont(Font.font("System", FontWeight.NORMAL, 16));
        text.setFill(Color.WHITE);
        text.setTextAlignment(TextAlignment.LEFT);

        text.wrappingWidthProperty().bind(
                stage.widthProperty().multiply(
                        stage.widthProperty().lessThan(600).get() ? 0.85 :
                                stage.widthProperty().lessThan(1000).get() ? 0.75 : 0.65
                )
        );

        return text;
    }


    /**
     * Vytvoří dekorativní obvodovou cestu.
     *
     * @return Pane s obvody
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
     * Vytvoří cestu obvodu na základě procentuální polohy.
     *
     * @param startXPercent počáteční X jako procento
     * @param startYPercent počáteční Y jako procento
     * @param widthPercent šířka jako procento
     * @param heightPercent výška jako procento
     * @return Path definující obvod
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
     * Spustí animace obvodu (pohyb čar a efekty) v nekonečné smyčce.
     *
     * @param circuitDecoration Pane s obvody, které se mají animovat
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
     * Nastaví obsluhu akce pro tlačítko "Back".
     *
     * @param handler běhajíca logika při stisku tlačítka
     */
    public void setOnBackAction(EventHandler<ActionEvent> handler) {
        backButton.setOnAction(handler);
    }

    /**
     * Vrátí hlavní kořenový uzel pro vložení do scény.
     *
     * @return StackPane root
     */
    public StackPane getRoot() {
        return root;
    }
}