package labyrinth;

import labyrinth.board.MazeBoard;
import java.io.*;

/**
 * Class responsible for saving and loading the game. Main principle used is object serialization
 * @author xdusek21 & xkalou03
 */
public class InputOutput {

    /**
     * Function saving the actual game state
     * @param object Object to be saved
     * @param save Name of the file in which it gets saved
     */
    public static void saveGame(Object object, String save) {

        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(save));
            out.writeObject(object);
            out.close();
        }
        catch (Exception e) {}
    }

    /**
     * Function loading the game back up
     * @param load Save file name, used to be loaded
     * @return Since we save the whole MazeBoard, it is what this function returns.
     */
    public static MazeBoard loadGame(String load) {

        MazeBoard temp = null;

        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(load));
            temp = (MazeBoard) in.readObject();
            in.close();
        }
        catch (Exception e) {}

        return temp;
    }
}
