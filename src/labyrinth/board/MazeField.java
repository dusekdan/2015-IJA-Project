package labyrinth.board;

import java.io.Serializable;

/**
 * Class represents one field (stone) on the game board
 * @author xdusek21 & xkalou03
 */
public class MazeField implements Serializable {

    /** Row index used to access the field, starting at the value 1 */
    public int row = 1;
    /** Column index used to access the field, starting at the value 1 */
    public int col = 1;
    /** MazeCard variable representing one the stone, with all it's MazeCard properties and methods */
    public MazeCard stone;


    /**
     * Constructor initializing values for rows and columns
     * @param r Integer count of rows
     * @param c Integer count of columns
     */
    public MazeField(int r, int c) {

        this.stone = null;
        this.row = r;
        this.col = c;
    }

    /**
     * Getter for rows
     * @return Integer row on which is the accessed field located
     */
    public int row() {

        return this.row;
    }

    /**
     * Getter for columns
     * @return Integer column on which is the accessed field located
     */
    public int col() {

        return this.col;
    }

    /**
     * Returns a card that is located on the accessed field
     * @return MazeCard stone located on the currently accessed field
     */
    public MazeCard getCard() {
        return this.stone;
    }

    /**
     * Puts a card on the accessed field
     * @param c MazeCard card that is to be put on the field
     */
    public void putCard(MazeCard c) {

        this.stone = c;
    }

}
