/**
 * Soubor: src/main/java/visualization/EnvPresenter.java
 *
 * Popis:
 *
 * Třída prezentující (vizualizující) model prostředí v GUI (Swing).
 *
 * @Author: Yaroslav Hryn (xhryny00),Oleksandr Musiichuk (xmusii00)
 *
 */

package visualization;

import ija.ijaProject.game.Game;
import visualization.common.ToolEnvironment;
import visualization.common.ToolField;
import visualization.view.FieldView;

import java.awt.*;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import javax.swing.Timer;

/**
 * Třída zajišťuje zobrazení herního prostředí pomocí Swing komponent.
 * Vytváří vizuální mřížku podle prostředí ToolEnvironment a zajišťuje správu kliknutí, režimu přehrávání
 * a kontrolu dokončení úrovně.
 */
public class EnvPresenter {
    public static final int CELL_SIZE = 40;
    private final ToolEnvironment env;
    private List<FieldView> fields;
    private JFrame frame;
    private JPanel mainPanel;
    private Runnable levelCompletedCallback;
    private boolean levelCompletionDetected = false;
    private final Timer levelCheckTimer;
    private ToolEnvironment environment;
    private boolean clicksEnabled = true;
    private boolean inReplayMode = false;


    /**
     * Konstruktor inicializuje prezentér pro dané herní prostředí.
     *
     * @param env herní prostředí, které má být vizualizováno
     */
    public EnvPresenter(ToolEnvironment env) {
        this.env = env;

        this.fields = new ArrayList<>();
        levelCheckTimer = new Timer(200, e -> checkLevelCompletion());
    }

    public void setEnvironment(ToolEnvironment env) {
        this.environment = env;
        getGamePanel().removeAll();
        SwingUtilities.invokeLater(() -> {
            initialize();
            levelCheckTimer.start();
        });
    }

    /**
     * Nastaví, zda je aktivní režim přehrávání (replay).
     *
     * @param replay {@code true}, pokud má být aktivní režim přehrávání
     */
    public void setReplayMode(boolean replay) {
        this.inReplayMode = replay;
    }

    /**
     * Vrací hlavní panel s herní mřížkou.
     *
     * @return hlavní JPanel s obsahem hry
     */
    public JPanel getGamePanel() {
        return mainPanel;
    }

    /**
     * Opens the game in a standalone window.
     */
    public void open() {
        try {
            SwingUtilities.invokeAndWait(() -> {
                this.initialize();
                this.frame.setVisible(true);
                levelCheckTimer.start();
            });
        } catch (InvocationTargetException | InterruptedException var2) {
            Logger.getLogger(EnvPresenter.class.getName()).log(Level.SEVERE, "Error opening game window", var2);
        }
    }

    /**
     * Inicializuje zobrazení a spustí časovač pro kontrolu dokončení úrovně.
     */
    public void init() {
        SwingUtilities.invokeLater(() -> {
            this.initialize();
            levelCheckTimer.start();
        });
    }

    /**
     * Zakáže uživatelská kliknutí ve všech polích mřížky.
     */
    public void disableUserClicks() {
        System.out.println("[EnvPresenter] disableUserClicks() called; fieldsCount = " + fields.size());
        clicksEnabled = false;
        for (Component comp : mainPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel maybeGrid = (JPanel) comp;
                for (Component inner : maybeGrid.getComponents()) {
                    if (inner instanceof FieldView) {
                        ((FieldView) inner).disableClicks();
                    }
                }
            }
        }
    }

    /**
     * Povolení uživatelských kliknutí ve všech polích mřížky.
     */
    public void enableUserClicks() {
        this.clicksEnabled = true;
        for (Component comp : mainPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel maybeGrid = (JPanel) comp;
                for (Component inner : maybeGrid.getComponents()) {
                    if (inner instanceof FieldView) {
                        ((FieldView) inner).enableClicks();
                    }
                }
            }
        }
    }

    /**
     * Obnoví zobrazení všech polí v mřížce.
     */
    public void refreshViews() {
        for (Component comp : mainPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel maybeGrid = (JPanel) comp;
                for (Component inner : maybeGrid.getComponents()) {
                    if (inner instanceof FieldView) {
                        ((FieldView) inner).repaint();
                    }
                }
            }
        }
    }
    /**
     * Sets a callback to be called when the level is completed.
     *
     * @param callback The callback to run when level is completed
     */
    public void setLevelCompletedCallback(Runnable callback) {
        this.levelCompletedCallback = callback;
    }

    /**
     * Zkontroluje, zda je úroveň dokončena.
     *
     * @return {@code true}, pokud jsou všechny žárovky rozsvíceny
     */
    private boolean isLevelCompleted() {
        boolean hasBulbs = false;
        boolean allBulbsLit = true;

        for (int row = 1; row <= env.rows(); row++) {
            for (int col = 1; col <= env.cols(); col++) {
                ToolField field = env.fieldAt(row, col);
                if (field.isBulb()) {
                    hasBulbs = true;
                    if (!field.light()) {
                        allBulbsLit = false;
                        break;
                    }
                }
            }
            if (!allBulbsLit) {
                break;
            }
        }
        return hasBulbs && allBulbsLit;
    }

    /**
     * Spouští pravidelnou kontrolu dokončení úrovně.
     * Pokud je úroveň dokončena, zastaví se časovač a spustí se příslušné callback.
     */
    private void checkLevelCompletion() {
        if (!levelCompletionDetected && isLevelCompleted()) {
            levelCompletionDetected = true;
            levelCheckTimer.stop();
            if (levelCompletedCallback != null) {
                SwingUtilities.invokeLater(levelCompletedCallback);
            }
        }
    }

    /**
     * Inicializuje a vykreslí herní mřížku a komponenty v novém okně.
     * Pokud je aktivní replay mód, zakáže kliknutí.
     */
    private void initialize() {
        System.out.println("[EnvPresenter] initialize() START; fields was: " + fields.size());
        fields.clear();
        this.frame = new JFrame("VoltMaze");
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setSize(350, 400);
        this.frame.setPreferredSize(new Dimension(350, 400));
        this.frame.setResizable(false);

        int rows = this.env.rows();
        int cols = this.env.cols();

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(15, 23, 42));

        JPanel gridPanel = new JPanel(new GridLayout(rows, cols, 2, 2));
        gridPanel.setBackground(new Color(15, 23, 42)); // Dark blue background
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for(int row = 1; row <= rows; ++row) {
            for(int col = 1; col <= cols; ++col) {
                ToolField field = this.env.fieldAt(row, col);
                FieldView fieldView = new FieldView(field);
                gridPanel.add(fieldView);
                this.fields.add(fieldView);
            }
        }
        System.out.println("[EnvPresenter] initialize() END; fields now: " + fields.size());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.add(gridPanel, BorderLayout.CENTER);
        this.frame.getContentPane().add(mainPanel);
        this.frame.pack();

        if (inReplayMode) {
            SwingUtilities.invokeLater(() -> {
                System.out.println("[EnvPresenter] (inside initialize) disableUserClicks(); fieldsCount="
                        + fields.size());
                disableUserClicks();
            });
        }

        levelCompletionDetected = false;
    }

    /**
     * Gets the list of field views.
     *
     * @return A copy of the field views list
     */
    protected List<FieldView> fields() {
        return new ArrayList<>(this.fields);
    }


    /**
     * Resets the level completion detection state.
     * Call this when starting a new level.
     */
    public void resetLevelCompletionState() {
        levelCompletionDetected = false;
        if (!levelCheckTimer.isRunning()) {
            levelCheckTimer.start();
        }
    }

    /**
     * Cleans up resources when the presenter is no longer needed.
     */
    public void cleanup() {
        levelCheckTimer.stop();

        if (fields != null) {
            fields.clear();
        }
        if (frame != null) {
            frame.dispose();
        }
    }

    /**
     * Vrací referenci na aktuální herní prostředí.
     *
     * @return instance {@code ToolEnvironment}
     */
    public ToolEnvironment getEnvironment() {
        return env;
    }

    /**
     * Vrací, zda je aktivní režim přehrávání.
     *
     * @return {@code true}, pokud je aktivní replay mód
     */
    public boolean inReplayMode() {
        return inReplayMode;
    }

    /**
     * Pomocná metoda pro výpis aktuálního stavu instance do konzole (pro ladění).
     */
    public void debugPrintState() {
        System.out.println("[EnvPresenter] this=" + this
                + ", inReplayMode=" + inReplayMode
                + ", panel=" + mainPanel
                + ", fieldsCount=" + fields.size());
    }
}