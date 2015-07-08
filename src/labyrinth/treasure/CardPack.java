
package labyrinth.treasure;

import java.util.Random;
import java.util.ArrayList;

/**
 * Class represent a pack of card, with number of card specified by its constructor parameters
 * @author xdusek21 & xkalou03
 */

public class CardPack {

    /** Maximal size of pack */
    private int maxSize;

    /** Size of pack */
    private int initSize;

    /** Actual size of the pack */
    private int actualSize;

    /** Represents a card pack */
    private ArrayList cardSet;

    /**
     * Constructor that creates a card pack
     * @param maxSize Specifies a maximum number of cards that may be contained in a pack, in context of the task it is a bit excessive
     * @param initSize Specifies how much cards will the pack start with
     */
    public CardPack(int maxSize, int initSize) {
        
        if(maxSize >= initSize) {
            
            this.maxSize = maxSize;
            this.initSize = initSize;
            this.actualSize = initSize;
        }
        else {
            System.exit(0); // chyba
        }
        
        this.cardSet = new ArrayList();
        
        for(int i = 0; i < initSize; i++) {
        
            Treasure treasure = Treasure.getTreasure(i);
            TreasureCard card = new TreasureCard(treasure);
            this.cardSet.add(card);
        }
    }

    /**
     * Pops card from the top of the deck
     * @param delete The TreasureCard type variable determining what will be popped
     */
    public void popCard(TreasureCard delete) {
        
        //TreasureCard pop = (TreasureCard) cardSet.get(0);
        cardSet.remove(delete);
        actualSize--;        
        return;
    }


    /**
     * Non-invasive popCard() method returning the treasure
     * @param code Identifies the treasure by its code
     * @return Treasure treasure
     */
    public Treasure getTreasure(int code)
    {
        TreasureCard pop = (TreasureCard) cardSet.get(code);   // pocitame od nuly
        return pop.treasure;
    }

    /**
     * Non-invasive popCard() method
     * @param code Identifies the treasure by its code
     * @return TreasureCard temp
     */
    public TreasureCard getTreasureCard(int code)
    {
        TreasureCard temp = (TreasureCard) cardSet.get(code);   // pocitame od nuly
        return temp;
    }

    /**
     * Simply returns a size of a pack
     * @return Integer Size of a pack
     */
    public int size() {
        
        return actualSize;
    }

    /**
     * Shuffles the deck. Using a java.util.random and commonly used shuffling algorithm.
     */
    public void shuffle() {
        
        Random random = new Random();
        int indexRand;
        TreasureCard temp;
        
        for(int i = actualSize - 1; i > 0; i--) {
        
            indexRand = random.nextInt(i + 1);
            if(indexRand != i) {
            
                temp = (TreasureCard) cardSet.get(indexRand);
                cardSet.set(indexRand, cardSet.get(i));
                cardSet.set(i, temp);
            }
        }
    }
}
