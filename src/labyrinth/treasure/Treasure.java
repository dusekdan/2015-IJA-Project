
package labyrinth.treasure;

import java.io.Serializable;

/**
 * Class Treasure representing set of achievable treasures
 * @author xdusek21 & xkalou03
 */

public class Treasure implements Serializable {

    /** Treasure identifier */
    private int code;

    /** regulation variable*/
    private static int count;

    /** internal property representing set of treasures */
    public static Treasure treasureSet[];

    /** logical value telling us that card was added to the pack already (or not) */
    public boolean assigned = false;

    /**
     * Constructor for class Treasure, sets internal property code
     * @param code Identifies the treasure by its code
     */
    private Treasure(int code) {
    
        this.code = code;
    }

    /**
     * Fills the internal property treasureSet with values
     * @param c Size of a set
     */
    public static void createSet(int c) {

        count = c;
        treasureSet = new Treasure[count];

        for(int i = 0; i < count; i++) {

            Treasure treasure = new Treasure(i);
            treasureSet[i] = treasure;
        }
    }

    /**
     * Getter for a specific treasure
     * @param code Identifies the treasure by its code
     * @return Treasure or null
     */
    public static Treasure getTreasure(int code) {
        
        if(code < 0 || code >= count) {
            return null;
        }
        else {
            return treasureSet[code];
        }
    }

    /**
     * Getter for code of the treasure
     * @return integer
     */
    public int getCode() {

        return this.code;
    }
}
