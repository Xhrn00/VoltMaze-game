/**
 * Soubor: src/main/java/ija.ijaProject/common/GameNode.java
 *
 * Popis:
 *
 *   Třída reprezentující uzel (políčko) v prostředí hry (Game). Každé políčko má čtyři strany označené pomocí Side. Každá strana může být prázdná nebo obsahovat konektor (vstupní/výstupní vodič), pomocí kterého se lze napojit na sousední políčko. Pro účely hry existují 4 typy uzlů (políček):
 * Prázdné (nic neobsahují).
 * Vodič spojující strany políčka (konektory). Vodič může spojovat sousední strany (L), protější strany (I), protější strany a jednu boční (T), nebo všechny strany (X).
 * Zdroj elektrické energie společně s vodiči (stejné podmínky jako u vodičů). Zdroj je v celé hře právě jeden.
 * Žárovky. Žárovka má pouze jeden přívod. Žárovek může být více, alespoň jedna.
 *
 * @Author: Yaroslav Hryn (xhryny00),Oleksandr Musiichuk (xmusii00)
 *
 */

package ija.ijaProject.common;

import ija.ijaProject.game.Game;
import ija.ijaProject.game.levels.NodeStateManager;
import visualization.common.AbstractObservableField;
import visualization.common.ToolField;
import java.util.HashSet;
import java.util.Set;

/** Třída reprezentující uzel (políčko) v prostředí hry (Game). Každé políčko má čtyři strany označené pomocí Side. Každá strana může být prázdná nebo obsahovat konektor (vstupní/výstupní vodič), pomocí kterého se lze napojit na sousední políčko. Pro účely hry existují 4 typy uzlů (políček):
 * Prázdné (nic neobsahují).
 * Vodič spojující strany políčka (konektory). Vodič může spojovat sousední strany (L), protější strany (I), protější strany a jednu boční (T), nebo všechny strany (X).
 * Zdroj elektrické energie společně s vodiči (stejné podmínky jako u vodičů). Zdroj je v celé hře právě jeden.
 * Žárovky. Žárovka má pouze jeden přívod. Žárovek může být více, alespoň jedna.*/
public class GameNode extends AbstractObservableField implements ToolField {
    private Game game;
    private final Position position;
    private boolean isBulb;
    private boolean isPower;
    private boolean isLink;
    private boolean isConnectedToPower = false;
    private final Set<Side> connectors = new HashSet<>();
    private int rotationCount = 0;

    /**
     * Přiřadí odkaz na instanci hry, umožňuje redraw po každém otočení.
     * @param game instance třídy Game
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Vytvoří uzel na dané pozici.
     * @param position pozice ve formátu (řádek, sloupec)
     */
    public GameNode(Position position) {
        this.position = position;
    }

    /**
     * Vrací kopii množiny aktuálních konektorů.
     * @return množina směrů konektorů
     */
    public Set<Side> getConnectors() {
        return new HashSet<>(connectors);
    }

    /**
     * Nastaví tento uzel jako žárovku a přidá konektor.
     * @param side směr, ve kterém je konektor žárovky
     */
    public void setBulb(Side side) {
        isBulb = true;
        connectors.add(side);
        notifyObservers();
    }

    /**
     * Nastaví tento uzel jako zdroj a přidá konektory.
     * @param sides pole směrů, které zdroj připojuje
     */
    public void setPower(Side... sides) {
        isPower = true;
        for (Side s : sides) {
            connectors.add(s);
        }
        notifyObservers();
    }

    /**
     * Nastaví tento uzel jako spojovací (link) a přidá konektory pro všechny zadané směry.
     * @param sides směry, ve kterých tento uzel propojuje sousední uzly
     */
    public void setLink(Side... sides) {
        isLink = true;
        for (Side s : sides) {
            connectors.add(s);
        }
        notifyObservers();
    }

    /**
     * Otočí tento uzel o 90° po směru hodinových ručiček.
     * Po otočení aktualizuje množinu konektorů, notifikaci pozorovatelům
     * a, pokud je přiřazena hra, znovu ji inicializuje.
     */
    @Override
    public void turn() {
        Set<Side> newConnectors = new HashSet<>();
        for (Side s : connectors) {
            newConnectors.add(rotateClockwise(s));
        }
        connectors.clear();
        connectors.addAll(newConnectors);
        //notifyObservers();
        rotationCount++;
        Position pos = this.getPosition();
        if (!NodeStateManager.getInstance().isReplayMode()) {
            NodeStateManager.getInstance().logMove(pos.row(), pos.col(), 1);
        }
        notifyObservers();

        if (game != null) {
            game.init();
        }

        //NodeStateManager.getInstance().logMove(position.col(), position.row(), rotationCount % 4);
    }

    /**
     * Vrátí, kolikrát byl tento uzel za svou životnost otočen uživatelem.
     * @return počet otočení
     */
    public int getRotationCount() {
        return rotationCount;
    }

    /**
     * Resetuje vnitřní počítadlo otočení tohoto uzlu na nulu.
     */
    public void resetRotationCount() {
        this.rotationCount = 0;
    }

    /**
     * Pomocná metoda otáčející jeden směr o 90° CW.
     * @param side původní směr
     * @return nový směr po otočení
     */
    private Side rotateClockwise(Side side) {
        return switch (side) {
            case NORTH -> Side.EAST;
            case EAST -> Side.SOUTH;
            case SOUTH -> Side.WEST;
            case WEST -> Side.NORTH;
        };
    }

    /**
     * Zjistí, zda tento uzel obsahuje konektor na severní straně.
     * @return true pokud ano, jinak false
     */
    @Override
    public boolean north() {
        return connectors.contains(Side.NORTH);
    }

    /** @return true pokud tento uzel obsahuje konektor na východní straně */
    @Override
    public boolean east() {
        return connectors.contains(Side.EAST);
    }

    /** @return true pokud tento uzel obsahuje konektor na jižní straně */
    @Override
    public boolean south() {
        return connectors.contains(Side.SOUTH);
    }

    /** @return true pokud tento uzel obsahuje konektor na západní straně */
    @Override
    public boolean west() {
        return connectors.contains(Side.WEST);
    }

    /**
     * Indikuje, zda je tento uzel právě napájen (a tedy rozsvícen).
     * @return true pokud napájen, jinak false
     */
    @Override
    public boolean light() {
        return isConnectedToPower;
    }

    /**
     * Nastaví příznak, zda je tento uzel spojen s napájením.
     * Pozorovatelé jsou automaticky notifikováni, pokud se stav změní.
     * @param connected nové napájecí spojení (true = napájeno)
     */
    public void setConnectedToPower(boolean connected) {
        if (this.isConnectedToPower != connected) {
            this.isConnectedToPower = connected;
            notifyObservers();
        }
    }


    /** Ověřuje typ políčka (vodič).*/
    @Override
    public boolean isLink() {
        return isLink;
    }

    /** Ověřuje typ políčka (žárovka).*/
    @Override
    public boolean isBulb() {
        return isBulb;
    }

    /** Ověřuje typ políčka (zdroj).*/
    @Override
    public boolean isPower() {
        return isPower;
    }

    /** Vrací pozici políčka.*/
    public Position getPosition() {
        return position;
    }

    /** Ověří, zda na zadané straně políčka existuje konektor. */
    public boolean containsConnector(Side side) {
        return connectors.contains(side);
    }


    /**
     * Vrátí textovou reprezentaci uzlu pro debuggování.
     * Zahrnuje typ uzlu, pozici a směry konektorů.
     * @return řetězec popisující tento uzel
     */
    @Override
    public String toString() {
        char type = 'E';
        if (isBulb) type = 'B';
        else if (isPower) type = 'P';
        else if (isLink) type = 'L';

        StringBuilder sb = new StringBuilder()
                .append('{')
                .append(type)
                .append('[')
                .append(position.row())
                .append('@')
                .append(position.col())
                .append("][");

        if (north()) sb.append("NORTH,");
        if (east()) sb.append("EAST,");
        if (south()) sb.append("SOUTH,");
        if (west()) sb.append("WEST,");

        if (sb.charAt(sb.length()-1) == '[') {
            sb.append(']');
        } else {
            sb.setCharAt(sb.length()-1, ']');
        }

        return sb.append('}').toString();
    }


    /**
     * Vrátí pole všech směrů, ve kterých má tento uzel konektory.
     * @return pole hodnot Side
     */
    public Side[] getSides() {
        return connectors.toArray(new Side[0]);
    }
}