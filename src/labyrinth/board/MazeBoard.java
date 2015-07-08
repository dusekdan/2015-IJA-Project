package labyrinth.board;

import labyrinth.players.Player;
import labyrinth.treasure.CardPack;
import labyrinth.treasure.Treasure;
import labyrinth.treasure.TreasureCard;

import java.io.Serializable;
import java.util.Random;
import java.lang.Math;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Class represent a gameBoard from the technical point of view. All dependencies and work on the background is done in here.
 * @author xdusek21 & xkalou03
 */
public class MazeBoard implements Serializable {

    /** Size of the MazeBoard, crucial property */
    public int n;

    /** Variable representing the gameboard field, two dimensional array */
    public MazeField[][] field;

    /** Extra stone is stored here, it is set to null at the beginning */
    public MazeCard emptyStone = null;

    /** Arraylist of String used for random generation of C/L/F card in the 1:1:1 ratio */
    ArrayList<String> tmp = new ArrayList<String>();

    /** Hashmap helper for the 1:1:1 ratio generation */
    Map<String, Integer> map = new HashMap<String, Integer>();

    /** CardPack generated at the begining of the every new game*/
    public static CardPack cardPack;

    /** Identifier of treasures */
    public int numberOfTreasure;



    /** The number of players stored in here */
    public int numberOfPlayers;

    /** Active player ID stored in here */
    public int activePlayerN;

    /** Active player object is stored in here */
    private Player activePlayer;
    /** Information whether turn was shifted  or not*/
    public boolean turnShifted = false;
    /** Number of turns that have been played since the beginning of the game */
    public int turnCounter = 0;

    /** Arraylist of the all objects containing the player reference */
    public ArrayList<Player> players;


    /** Property used for mazeSolving */
    public boolean[][] wasHere;
    /** Property used for mazeSolving */
    public boolean[][] correctPath;


    /** Layout helpers group (for different numbers of cards) */
    private int[] autoF = {5, 12, 21, 32};   // počet T karet které jsou vygenerovány pro 5x5, 7x7, 9x9 a 11x11
    /** Layout helpers group (for different numbers of cards) */
    private int generatedTreasures = 0;


    /** Initialization of deX */
    public int deX = -1;
    /** Initialization of deY */
    public int deY = -1;


    /**
     * Construct all the players based on values from the create game form
     * @param nofplayers Integer number of players to be created
     * @param size Integer size of the board that is also important when creating the players
     */
    public void createPlayers(int nofplayers, int size)
    {
        numberOfPlayers = nofplayers;
        players = new ArrayList<Player>(numberOfPlayers);
        int posArray[] = {
                        1, 1,       // Player #1
                        1, size,    // Player #2
                        size, 1,    // Player #3
                        size, size  // Player #4
                        };
        int count = 0;

        for (int i = 0; i < numberOfPlayers; i++) {

            players.add(new Player(i, divideCards(numberOfPlayers), posArray[count], posArray[count + 1]));
            count += 2;
        }

    }

    /**
     * Functions returns boolean true or boolean false based on the possibility of the player to reach selected destination
     * @param x Integer desired X position coordinate
     * @param y Integer desired Y position coordinate
     * @return boolean true or boolean false
     */
    public boolean findWay(int x, int y)
    {
        int sX = players.get(activePlayerN).getPosX();
        int sY = players.get(activePlayerN).getPosY();

            for(int i = 1; i <= n; i++)
            {
                for(int j = 1; j <= n; j++)
                {
                    wasHere[i][j] = false;
                    correctPath[i][j] = false;
                }
            }

        boolean pathFound = recSolve(sX, sY, x, y);

        if(pathFound) {

            // checknu jestli nahodou nenajdu treasure
            if(field[x][y].stone.treasure != null) {

                if(players.get(activePlayerN).checkTreasureFound(field[x][y].stone.treasure))
                {
                    players.get(activePlayerN).cardFound();
                }
            }

        }
        return pathFound;

    }

    /**
     * Recursive algorhitm solving the maze by the left hand rule
     * @param sX Integer starting position X
     * @param sY Integer starting position Y
     * @param dX Integer extraposition position X
     * @param dY Integer extraposition position Y
     * @return True on successfull dive-in, false on the exact opposite
     */
    private boolean recSolve(int sX, int sY, int dX, int dY)
    {
        // we are already in the final destination
        if(sX == dX && sY == dY)
        {

            return true;
        }

        if(wasHere[sX][sY]) // tady ma byt jeste false pokud je vedlejsi policko nepristupne
        {

            return false;
        }

        // musíme zjistit, jestli se dostaneme tam i zpět
        // taky musíme zjistit, jestli pokusem o přístup na vedlejší políčko neporušujeme hranice desky

            // otestujeme všechny 4 směry


        wasHere[sX][sY] = true;

        //  DOWN
        if((sX+1) <= n) // muzeme se zanorit smerem dolu, coz take provedeme
        {
            if(field[sX][sY].stone.canGo(MazeCard.CANGO.DOWN) && field[sX+1][sY].stone.canGo(MazeCard.CANGO.UP))
            {
                if(recSolve(sX+1, sY, dX, dY)) {
                    correctPath[sX][sY] = true;
                    return true;
                }
            }
        }

        // UP
        if((sX-1) > 0)
        {
            if(field[sX][sY].stone.canGo(MazeCard.CANGO.UP) && field[sX-1][sY].stone.canGo(MazeCard.CANGO.DOWN))
            {
                if(recSolve(sX-1, sY, dX, dY)) {
                    correctPath[sX][sY] = true;
                    return true;
                }
            }
        }

        // LEFT
        if((sY-1) > 0)
        {
            if(field[sX][sY].stone.canGo(MazeCard.CANGO.LEFT) && field[sX][sY-1].stone.canGo(MazeCard.CANGO.RIGHT))
            {
                if(recSolve(sX, sY-1, dX, dY)) {
                    correctPath[sX][sY] = true;
                    return true;
                }
            }
        }

        // RIGHT
        if((sY+1) <= n)
        {
            if(field[sX][sY].stone.canGo(MazeCard.CANGO.RIGHT) && field[sX][sY+1].stone.canGo(MazeCard.CANGO.LEFT))
            {
                if(recSolve(sX, sY + 1, dX, dY)) {
                    correctPath[sX][sY] = true;
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * Divides the card to the players (based on the number of them)
     * @param players Number of players card should be divided to
     * @return ArrayList of TreasureCards (for each player, better call this in a loop)
     */
    private ArrayList<TreasureCard> divideCards(int players)
    {
        Random random = new Random();
        int index;
        int sizeOfCardpack = MazeBoard.cardPack.size();
        int numbersOfCard = sizeOfCardpack / players;
        ArrayList<TreasureCard> trToFind = new ArrayList();

        for (int i = 0; i < numbersOfCard; i++) {


            index = random.nextInt(sizeOfCardpack);
            if(MazeBoard.cardPack.getTreasureCard(index).treasure.assigned)
            {
                // super hardcore pro beast mode activated
                --i;
                continue;
            }
            TreasureCard temp = MazeBoard.cardPack.getTreasureCard(index);
            temp.treasure.assigned = true;
            trToFind.add(temp);
        }

        return trToFind;
    }

    /**
     * Changes the focus from the player #1 -> #2, then from player #2 -> #3 and similarly
     */
    public void changeActivePlayer()
    {
        // zvednuti ukazatele na hrace
        if(activePlayerN+1 == numberOfPlayers)
        {
            activePlayerN = 0;
            activePlayer = players.get(activePlayerN);
        }
        else
        {
            activePlayerN++;
            activePlayer = players.get(activePlayerN);
        }
        turnCounter++;
        turnShifted = false;
    }

    /**
     * Creates the MazeBoard with all its dependencies, initializes its fields etc.
     * @param n Integer size of the MazeBoard
     * @return MazeBoard
     */
    public static MazeBoard createMazeBoard(int n) {

        MazeBoard board = new MazeBoard();
        int ratio = Math.round((n * n) / 3);

        board.tmp.add("C");
        board.tmp.add("L");
        board.tmp.add("F");

        board.map.put("C", ratio - 4);
        board.map.put("L", ratio);
        if(n == 5)
            board.map.put("F", ratio - 5);
        else if(n == 7)
            board.map.put("F", ratio - 12);
        else if(n == 9)
            board.map.put("F", ratio - 21);
        else if(n == 11)
            board.map.put("F", ratio - 32);

        board.n = n;
        board.wasHere = new boolean[n+1][n+1];
        board.correctPath = new boolean[n+1][n+1];
        board.field = new MazeField[n + 1][n + 1];
        int i, j;
        for(i = 1; i <= n; i++) {
            for(j = 1; j <= n; j++) {

                board.field[i][j] = new MazeField(i, j);
            }
        }

        return board;
    }


    /**
     * Generates treasures randomly in ratio 1:1:1
     * @param treasures Number of treasures
     * @return ArrayList of treasures
     */
    private ArrayList randomGenerate(int treasures) {

        int size = n * n;
        int cnt = 0;
        ArrayList<Integer> treaArray = new ArrayList<Integer>();
        Random random = new Random();
        int index;

        for(int i = 1; i <= n; i++) {
            for(int j = 1; j <= n; j++) {

                if (((i % 2 == 1) && (j % 2 == 1))) {
                    if(!((i == 1 && j == 1) || (i == n && j == 1) || (i == n && j == n) || (i == 1 && j == n))) {
                        if((i != 1 && j == 1) || (i == 1 && j != 1) || (i != 1 && j == n) || (i == n && j != 1)) {
                            if(cnt < treasures) {
                                treaArray.add((i - 1) * n + j);
                                cnt++;
                            }
                        }
                    }
                }
            }
        }

        while(cnt != treasures) {

            index = random.nextInt((size - 3) + 1) + 2;
            if(!(treaArray.contains(index)) && index != (n) && index != (size - n + 1)) {
                treaArray.add(index);
                cnt++;
            }
        }
        return treaArray;
    }

    /**
     * Creates a new game (initialization included)
     * @param treasures Number of treasures
     * @param players Number of players
     */
    public void newGame(int treasures, int players) {

        // Vytvoří unikátní cardPack pro danou hru

        Treasure.createSet(treasures);
        cardPack = new CardPack(treasures, treasures);
        cardPack.shuffle();

        // !!!!
        ArrayList<Integer> indexGen = randomGenerate(treasures);

        Random random = new Random();
        int i, j;
        int index, rindex;
        String rand = "";

        int check = Math.round((n * n) / 3) * 3;

        while (check != (n * n)) {        // zatim jen do L, pak doresit
            map.put("L", map.get("L") + 1);
            check++;
        }

        for(i = 1; i <= n; i++) {
            for(j = 1; j <= n; j++) {

                if((i == 1 && j == 1) || (i == 1 && j == n) || (i == n && j == 1) || (i == n && j == n)) {
                    rand = "C";
                    field[i][j].stone = MazeCard.create(rand);
                }
                else if((i % 2 == 1) && (j % 2 == 1)) {
                    rand = "F";
                }
                else {

                    index = random.nextInt(tmp.size());
                    rand = tmp.get(index);
                    decrementIndex(rand);
                }
                if(!(indexGen.contains((n) * (i - 1) + (j))))
                    field[i][j].stone = MazeCard.create(rand);
                else {
                    if(generatedTreasures < treasures) {
                        field[i][j].stone = MazeCard.createWithTreasure(rand, cardPack.getTreasure(generatedTreasures));
                        generatedTreasures++;
                    }
                }



                rindex = random.nextInt(4);

                // only lesser than because we want unturned stones too
                if((i % 2 == 1) && (j % 2 == 1) && !((i == 1 && j == 1) || (i == 1 && j == n) || (i == n && j == 1) || (i == n && j == n))) {

                    if (i == 1) {
                        field[i][j].stone.turnRight();
                        field[i][j].stone.turnRight();
                    }
                    else if(j == 1) {
                        field[i][j].stone.turnRight();
                    }
                    else if(i == n)
                        continue;
                    else if(j == n) {
                        field[i][j].stone.turnRight();
                        field[i][j].stone.turnRight();
                        field[i][j].stone.turnRight();
                    }
                }
                else if((i == 1 && j == 1) || (i == 1 && j == n) || (i == n && j == 1) || (i == n && j == n)) {

                    if (i == 1 && j == 1)  {
                        field[i][j].stone.turnRight();
                        field[i][j].stone.turnRight();
                    }
                    else if (i == 1 && j == n) {
                        field[i][j].stone.turnRight();
                        field[i][j].stone.turnRight();
                        field[i][j].stone.turnRight();
                    }
                    else if (i == n && j == 1)
                        field[i][j].stone.turnRight();
                    else if (i == n && j == n)
                        continue;
                }
                else {

                    for(int ri = 0; ri < rindex; ri++)
                    {
                        field[i][j].stone.turnRight();
                    }
                }
            }
        }

        emptyStone = MazeCard.create(rand);    // vytvoreni volneho kamene


        createPlayers(players, n);    // vytvoreni vsech hracu
        activePlayer = this.players.get(0);
        activePlayerN = 0;


    }

    /**
     * Returns the active players, based on ID
     * @return Object of the active player
     */
    public Player getActivePlayer() {

        return this.activePlayer;
    }

    /**
     * Helper function for decrementing indexes in the process of random-ratio generation
     * @param rand String type of card
     */
    private void decrementIndex(String rand) {

        if (rand == "C") {
            helpDecrement(rand);
        }
        if (rand == "L") {
            helpDecrement(rand);
        }
        if (rand == "F") {
            helpDecrement(rand);
        }
    }

    /**
     * Another variation of helper for decrementing indexes in the process of random-ratio generation
     * @param type String type of the of card
     */
    private void helpDecrement(String type) {

        map.put(type, map.get(type) - 1);
        if (map.get(type) == 0 && tmp.size() > 1) {
            tmp.remove(type);
        }
    }

    /**
     * Get the field from mazeboard on the indexes given by row and column
     * @param r Integer number of row
     * @param c Integer number of column
     * @return MazeField field
     */
    public MazeField get(int r, int c) {

        if(r < 1 || r > n || c < 1 || c > n)
            return null;

        return field[r][c];
    }

    /**
     * Gives you the current emptyStone
     * @return MazeCard emptyStone
     */
    public MazeCard getFreeCard() {

        return emptyStone;
    }

    /**
     * Functions that takes care of player standing on the edge of the field when it gets shifted
     * @param x Integer player position X
     * @param y Integer player position Y
     * @param type Type in which direction the shift calling this function was called
     */
    private void checkPlayerPos(int x, int y, int type) {
        for(int k = 0; k < numberOfPlayers; k++) {

            if(players.get(k).getPosX() == x && players.get(k).getPosY() == y && !(players.get(k).shifted)) {

                if(type == 1) {
                    if(x == n)
                        players.get(k).changePosition(1, y);
                    else
                        players.get(k).changePosition(x + 1, y);
                }
                if(type == 2) {
                    if(x == 1)
                        players.get(k).changePosition(n, y);
                    else
                        players.get(k).changePosition(x - 1, y);
                }
                if(type == 3) {
                    if(y == n)
                        players.get(k).changePosition(x, 1);
                    else
                        players.get(k).changePosition(x, y + 1);
                }
                if(type == 4) {
                    if(y == 1)
                        players.get(k).changePosition(x, n);
                    else
                        players.get(k).changePosition(x, y - 1);
                }
            players.get(k).shifted = true;
            }
        }
    }

    /**
     * Holy grail of shifting methods. Shift in the desired direction
     * @param mf Whole MazeField
     * @return Empty MazeCard
     */
    public MazeCard shift(MazeField mf) {

        int row = mf.row;
        int col = mf.col;
        MazeCard temp;
        MazeCard temp1;
        MazeCard oldEmpty = emptyStone;

        if(row == 1 && (col % 2) == 0) {

            temp1 = emptyStone;
            for(int i = 1; i <= n; i++) {

                temp = field[i][col].stone;
                field[i][col].stone = temp1;
                temp1 = temp;

                checkPlayerPos(i, col, 1);

            }
            emptyStone = temp1;
        }

        else if(row == n && (col % 2) == 0) {
            temp1 = emptyStone;

            for(int i = n; i > 0; i--) {

                temp = field[i][col].stone;
                field[i][col].stone = temp1;
                temp1 = temp;

                checkPlayerPos(i, col, 2);
            }
            emptyStone = temp1;
        }

        else if(col == 1 && (row % 2) == 0) {
            temp1 = emptyStone;
            for(int i = 1; i <= n; i++) {

                temp = field[row][i].stone;
                field[row][i].stone = temp1;
                temp1 = temp;

                checkPlayerPos(row, i, 3);
            }
            emptyStone = temp1;
        }

        else if(col == n && (row % 2) == 0) {

            temp1 = emptyStone;

            for(int i = n; i > 0; i--) {

                temp = field[row][i].stone;
                field[row][i].stone = temp1;
                temp1 = temp;

                checkPlayerPos(row, i, 4);

            }
            emptyStone = temp1;
        }


        // reset shifted na false u všech hráčů
        for(int k = 0; k < numberOfPlayers; k++) {
            players.get(k).shifted = false; // hope this works
        }

        return oldEmpty;
    }
}
