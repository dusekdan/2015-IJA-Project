package labyrinth.board;



import labyrinth.treasure.Treasure;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Class represents one field (stone) at the game
 * @author xdusek21 & xkalou03
 */
public class MazeCard implements Serializable {

    /** Direction in which is possible for current stone to go */
    private CANGO[] direction = new CANGO[3];

    /** Treasure assigned to the card, may be (and on default is) null if there is no treasure assigned*/
    public Treasure treasure = null;

    /** Enum for directions in which is possible for player to go from the given card */
    public static enum CANGO {

        LEFT, RIGHT, UP, DOWN;
    }

    /**
     * Pseudo-constructor for the MazeCard, based on type generates a card
     * @param type String type of the card. C, L, F possible, each representing a different card.
     * @return MazeCard itself or throws an exception at you if you give it a wrong type.
     */
    public static MazeCard create(String type) {

        MazeCard stone = new MazeCard();

        if(type.equals("C")) {

            stone.direction[0] = CANGO.LEFT;
            stone.direction[1] = CANGO.UP;
            stone.direction[2] = null;
        }
        else if(type.equals("L")) {

            stone.direction[0] = CANGO.LEFT;
            stone.direction[1] = CANGO.RIGHT;
            stone.direction[2] = null;
        }
        else if(type.equals("F")) {

            stone.direction[0] = CANGO.LEFT;
            stone.direction[1] = CANGO.UP;
            stone.direction[2] = CANGO.RIGHT;
        }
        else
            throw new IllegalArgumentException("Bad type of string");

        return stone;
    }

    /**
     * Similar as create() pseudo-constructor, but it adds an extra treasure to the card.
     * @param type String type used to determine what will be the type of the card created
     * @param treasure Treasure variable determining what treasure will be generated on the card
     * @return MazeCard created card
     */
    public static MazeCard createWithTreasure(String type, Treasure treasure)
    {
        MazeCard stone = new MazeCard();

        if(type.equals("C")) {

            stone.direction[0] = CANGO.LEFT;
            stone.direction[1] = CANGO.UP;
            stone.direction[2] = null;
        }
        else if(type.equals("L")) {

            stone.direction[0] = CANGO.LEFT;
            stone.direction[1] = CANGO.RIGHT;
            stone.direction[2] = null;
        }
        else if(type.equals("F")) {

            stone.direction[0] = CANGO.LEFT;
            stone.direction[1] = CANGO.UP;
            stone.direction[2] = CANGO.RIGHT;
        }
        else
            throw new IllegalArgumentException("Bad type of string");

        stone.treasure = treasure;
        return stone;
    }

    /**
     * Returns boolen value based on the fact if the player can go in the given direction on actual card
     * @param dir Direction in which we are interested
     * @return True or False
     */
    public boolean canGo(MazeCard.CANGO dir) {

        for(int i = 0; i < length(); i++) {


            if(direction[i].equals(dir)){
                return true;
            }
        }

        return false;
    }

    /**
     * Based on the type and rotation, the method determines what is the work-name assigned for the current card/path
     * @param pStone Card in which work-name we are interested in.
     * @return String work-name
     */
    public String cardPaths(MazeCard pStone)
    {

        String type = "";

        if(pStone.canGo(CANGO.LEFT) && pStone.canGo(CANGO.RIGHT) && !pStone.canGo(CANGO.UP) && !pStone.canGo(CANGO.DOWN))
        {
            type = "L0";
        }

        if(!pStone.canGo(CANGO.LEFT) && !pStone.canGo(CANGO.RIGHT) && pStone.canGo(CANGO.UP) && pStone.canGo(CANGO.DOWN))
        {
            type = "L1";
        }

        if(pStone.canGo(CANGO.LEFT) && !pStone.canGo(CANGO.RIGHT) && pStone.canGo(CANGO.UP) && !pStone.canGo(CANGO.DOWN))
        {
            type = "C0";
        }

        if(!pStone.canGo(CANGO.LEFT) && pStone.canGo(CANGO.RIGHT) && pStone.canGo(CANGO.UP) && !pStone.canGo(CANGO.DOWN))
        {
            type = "C1";
        }

        if(!pStone.canGo(CANGO.LEFT) && pStone.canGo(CANGO.RIGHT) && !pStone.canGo(CANGO.UP) && pStone.canGo(CANGO.DOWN))
        {
            type = "C2";
        }

        if(pStone.canGo(CANGO.LEFT) && !pStone.canGo(CANGO.RIGHT) && !pStone.canGo(CANGO.UP) && pStone.canGo(CANGO.DOWN))
        {
            type = "C3";
        }

        if(pStone.canGo(CANGO.LEFT) && pStone.canGo(CANGO.RIGHT) && pStone.canGo(CANGO.UP) && !pStone.canGo(CANGO.DOWN))
        {
            type = "F0";
        }

        if(!pStone.canGo(CANGO.LEFT) && pStone.canGo(CANGO.RIGHT) && pStone.canGo(CANGO.UP) && pStone.canGo(CANGO.DOWN))
        {
            type = "F1";
        }

        if(pStone.canGo(CANGO.LEFT) && pStone.canGo(CANGO.RIGHT) && !pStone.canGo(CANGO.UP) && pStone.canGo(CANGO.DOWN))
        {
            type = "F2";
        }

        if(pStone.canGo(CANGO.LEFT) && !pStone.canGo(CANGO.RIGHT) && pStone.canGo(CANGO.UP) && pStone.canGo(CANGO.DOWN))
        {
            type = "F3";
        }


        return type;
    }

    /**
     * Rotates the current card to the right, by 90 degrees
     */
    public void turnRight() {

        for(int i = 0; i < length(); i++) {

            if(direction[i].equals(CANGO.LEFT))
                direction[i] = CANGO.UP;
            else if(direction[i].equals(CANGO.UP))
                direction[i] = CANGO.RIGHT;
            else if(direction[i].equals(CANGO.RIGHT))
                direction[i] = CANGO.DOWN;
            else if(direction[i].equals(CANGO.DOWN))
                direction[i] = CANGO.LEFT;
        }
    }

    /**
     * Helper variable for rotation-dependent methods
     * @return Integer length
     */
    private int length() {

        int len = this.direction.length;
        if(this.direction[2] == null)
            len -= 1;

        return len;
    }
}