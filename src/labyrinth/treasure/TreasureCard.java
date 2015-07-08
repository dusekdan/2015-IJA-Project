
package labyrinth.treasure;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class representing a one TreasureCard
 * @author xdusek21 & xkalou03
 */
public class TreasureCard implements Serializable{

    /** internal value that we set to a value taken from the constructor */
    public Treasure treasure;

    /**
     * Constructor sets internal variable to value from param.
     * @param tr TreasureCard Treasure value
     */
    public TreasureCard(Treasure tr) {
            this.treasure = tr;
    }

    /**
     * Override method hashCode() that NetBeans created for me
     * @return integer
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.treasure);
        return hash;
    }

    /**
     * Override method equals() that NetBeans created for me
     * @param obj Object reference
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TreasureCard other = (TreasureCard) obj;
        if (!Objects.equals(this.treasure, other.treasure)) {
            return false;
        }
        return true;
    }
}
