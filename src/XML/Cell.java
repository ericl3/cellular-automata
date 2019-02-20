package XML;

/**
 *
 * @author kunalupadya
 */
public class Cell {

    private String cellState;
    private int row;
    private int column;

    public Cell(int row, int col, String state){
        this.row = row;
        this.column = col;
        cellState = state;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public String getCellState() {
        return cellState;
    }
}
