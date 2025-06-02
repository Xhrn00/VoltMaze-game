/**
 * Soubor: src/main/java/ija.ijaProject/common/Posotion.java
 *
 * Popis:
 * Třída reprezentující pozici v prostředí.
 * Pozice je reprezentována indexem do mřížky prostředí
 * (řádek, sloupec).
 * Objekt je neměnný (nemodifikovatelný).
 * Dva objekty typu Position jsou shodné,
 * pokud reprezentují stejnou pozici
 * (mají stejné souřadnice).
 *
 * @Author: Yaroslav Hryn (xhryny00),Oleksandr Musiichuk (xmusii00)
 *
 */

package ija.ijaProject.common;

import java.util.Objects;

/** Třída reprezentující pozici v prostředí.
 * Pozice je reprezentována indexem do mřížky prostředí
 * (řádek, sloupec).
 * Objekt je neměnný (nemodifikovatelný).
 * Dva objekty typu Position jsou shodné,
 * pokud reprezentují stejnou pozici
 * (mají stejné souřadnice).*/
public class Position {
    public final int row;
    public final int col;

    /**
     * Vytvoří novou instanci Position se zadaným řádkem a sloupcem.
     *
     * @param row  index řádku (>=1)
     * @param col  index sloupce (>=1)
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }


    /** Vrací informaci o řádku pozice.*/
    public int row() {
        return row;
    }

    /**Vrací informaci o sloupci pozice.*/
    public int col() {
        return col;
    }


    /**
     * Porovná, zda je objekt {@code obj} roven této instanci.
     * Dva objekty Position jsou shodné, pokud mají stejné souřadnice row a col.
     *
     * @param obj objekt k porovnání
     * @return {@code true}, pokud je {@code obj} typu Position se stejnými souřadnicemi
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Position)) return false;
        Position other = (Position) obj;
        return this.row == other.row && this.col == other.col;
    }

    /**
     * Vrátí hash kód této pozice, založený na obou souřadnicích.
     *
     * @return hash kód
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
