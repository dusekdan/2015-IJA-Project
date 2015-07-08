package labyrinth.players;

import labyrinth.board.MazeBoard;
import labyrinth.treasure.Treasure;
import labyrinth.treasure.TreasureCard;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class represent player with all its possible states and properties
 * @author xdusek21 & xkalou03
 */
public class Player implements Serializable {

    /** Information about the number of players that are currently playing */
    public int nOfPlayers;
    /** Unique ID that identifies the player */
    public int ID;
    /** ArrayList of TreasureCards that are supposed to be found by player */
    public ArrayList<TreasureCard> trToFind;
    /** Starting and final destination X coordinate */
    private int posX, destX;
    /** Starting and final destination Y coordinate */
    private int posY, destY;

    /** Logical value telling us if the player was shifted or not */
    public boolean shifted = false;

    /** Logical value telling us if the player played in this turn already */
    public boolean played = false;
    /** Logical value telling us if the player shifted the board in this turn already */
    public boolean shiftClick = false;

    /** If player found all the cards, this property is set to true */
    public boolean goHome = false;


    /**
     * Constructor for player class. Fills the object with all the information required for proper work
     * @param ID Unique identifier of a player
     * @param trToFind Cards that player needs to find in order to win the  game
     * @param posX Starting position X coordinate, also used as a final (home) destination
     * @param posY Starting position Y coordinate, also used as a final (home) destination
     */
    public Player(int ID, ArrayList<TreasureCard> trToFind, int posX, int posY) {

        this.ID = ID;
        this.trToFind = trToFind;
        this.posX = posX;
        this.posY = posY;
        this.destX = posX;
        this.destY = posY;
    }

    /**
     * Getter for position's actual X
     * @return Integer position X coordinate
     */
    public int getPosX()
    {
        return posX;
    }

    /**
     * Getter for position's actual Y
     * @return Integer position Y coordinate
     */
    public int getPosY()
    {
        return posY;
    }

    /**
     * Getter for final destination's X coordinate
     * @return Integer destination's X coordinate
     */
    public int getDestX(){return destX;}
    /**
     * Getter for final destination's Y coordinate
     * @return Integer destination's Y coordinate
     */
    public int getDestY(){return destY;}

    /**
     * Returns score of the player
     * @return integer Player's score
     */
    public int getScore() {
        return trToFind.size();
    }

    /**
     * Setter for posX and posY
     * @param x Integer desired X position coordinate
     * @param y Integer desired Y position coordinate
     */
    public void changePosition(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    /**
     * Gets the player the card he is supposed to find in the moment
     * @return TreasureCard to find or null (if there's none to find, a.k.a. player is on his way home)
     */
    public TreasureCard getTreasureCardToFind() {
        if(getScore() != 0) {
            return trToFind.get(0);
        }
        else return null;
    }

    /**
     * Procedure called when the card is found. Removes the card from the players pack to find.
     */
    public void cardFound() {
        TreasureCard temp = trToFind.remove(0);
        MazeBoard.cardPack.popCard(temp);
        if(getScore() == 0)
        {
            this.goHome = true;
        }
    }

    /**
     * Checks what player is standing on at the moment. Called upon every step.
     * @param actualCard Card that player should find in order to increase respectively decrease his score
     * @return Boolean true or boolean false
     */
    public boolean checkTreasureFound(Treasure actualCard)
    {
        TreasureCard tmp = getTreasureCardToFind();
        if(tmp.treasure.getCode() == actualCard.getCode())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}

