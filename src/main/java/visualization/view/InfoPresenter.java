/**
 * Soubor: src/main/java/visualization/view/InfoPresenter.java
 *
 * Popis:
 * Zobrazí nové oko, které obsahuje informace o tom,
 * které políčko je třeba kolikrát otočit,
 * aby se dostalo do správné polohy.
 *
 * @Author: Yaroslav Hryn (xhryny00), Oleksandr Musiichuk (xmusii00)
 */

package visualization.view;

import ija.ijaProject.common.GameNode;
import ija.ijaProject.common.Position;
import ija.ijaProject.common.Side;
import ija.ijaProject.game.Game;
import visualization.common.Observable;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/** Zobrazení informačního okna,
 * ve kterém se pro každou herní buňku zobrazí, kolik
 * otočení je třeba provést, aby dosáhla správné  pozice. */
public class InfoPresenter implements Observable.Observer {
    public static final int TILE_SIZE = 40;
    private static final Color BG        = Color.decode("#0f1e2e");
    private static final Color CELL_BG   = Color.decode("#1f2f3f");
    private static final Color LINE      = Color.decode("#2f3f4f");
    private static final Color BG_CELL     = new Color(0x1A1F34);
    private static final Color GRID_COLOR  = new Color(0x3A4154);
    private static final Font  FONT_NUMBER = new Font("Arial", Font.BOLD, 18);
    private final Game currentGame;
    private final Game solvedGame;
    private final JPanel panel;
    private final Map<Position, JLabel> labels = new HashMap<>();
    private final Map<Position,JButton> buttons = new HashMap<>();
    static{
        ToolTipManager.sharedInstance().setInitialDelay(2000); // 2 секунды перед показом
    }

    /**
     * Vytvoří panel s tlačítky( reprezentuje jedno políčko.)
     */
    public InfoPresenter(Game currentGame, Game solvedGame) {
        this.currentGame = currentGame;
        this.solvedGame = solvedGame;

        int rows = currentGame.rows();
        int cols = currentGame.cols();
        this.panel = new JPanel(new GridLayout(rows, cols, 0, 0));
        panel.setBackground(BG_CELL);


        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                Position pos = new Position(r, c);
                GameNode node = currentGame.getGameNode(r, c);
                JButton btn = new JButton();
                btn.setEnabled(false);

                btn.setBorder(BorderFactory.createLineBorder(GRID_COLOR));
                btn.setBackground(BG_CELL);
                btn.setFont(FONT_NUMBER);
                btn.setForeground(Color.WHITE);
                btn.setMargin(new Insets(0,0,0,0));
                btn.setOpaque(true);

                if (currentGame.isPlayebleNode(node)) {
                    node.addObserver(this);
                    buttons.put(pos, btn);
                    updateTooltip(pos, btn);
                    ToolTipManager.sharedInstance().registerComponent(btn);
                    btn.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            updateTooltip(pos, btn);
                        }
                    });
                } else {
                    btn.setText("?");
                    btn.setBackground(Color.DARK_GRAY);
                }
                panel.add(btn);
            }
        }

        updateAll();
    }

    /** Volá se, když se nějaký uzel otočí  */
    @Override
    public void update(Observable source) {
        SwingUtilities.invokeLater(this::updateAll);
    }

    /** Aktualizuje tlačítka podle aktuálního stavu  */
    private void updateAll() {
        for (Map.Entry<Position, JButton> e : buttons.entrySet()) {
            Position pos = e.getKey();
            JButton btn    = e.getValue();

            GameNode cur = currentGame.getGameNode(pos.row(), pos.col());
            GameNode sol = solvedGame .getGameNode(pos.row(), pos.col());

            int need = rotationsNeeded(
                    cur.getConnectors(),
                    sol.getConnectors()
            );
            btn.setText(need >= 0 ? String.valueOf(need) : "?");
            updateTooltip(pos, btn);
            if (need == 0) {
                btn.setBackground(Color.decode("#39D353"));
            } else {
                btn.setBackground(BG_CELL);
            }
        }
    }
    /** */
     private void updateTooltip(Position pos, JButton btn) {
        GameNode cur = currentGame.getGameNode(pos.row(), pos.col());
        int actual = cur.getRotationCount();

        btn.setToolTipText(
                "<html> Actual: " + actual + "</html>"
        );
    }

    /** Vrátí počet otočení,do spravneho řešení  */
    private int rotationsNeeded(Set<Side> cur, Set<Side> tgt) {
        for (int k = 0; k < 4; k++) {
            final int kk = k;
            Set<Side> rot = cur.stream()
                    .map(s -> rotateCW(s, kk))
                    .collect(Collectors.toSet());
            if (rot.equals(tgt)) return kk;
        }
        return 0;
    }

    /** Otočí jednu stranu */
    private Side rotateCW(Side s, int times) {
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

    /** Vrátí JPanel, který pak vloží do JavaFX SwingNode */
    public JPanel getPanel() {
        return panel;
    }
}