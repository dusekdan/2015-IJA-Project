package labyrinth;

/**
 * Created by Filip on 14. 5. 2015.
 */

import labyrinth.board.MazeBoard;

import java.io.*;
import java.util.ArrayList;

/**
 * This is a helper class for undo, it works in a similar way that InputOutput class does, but it does not use files for storage. ByteArray is used instead.
 */
public class UndoHelper {

    /** Bytearray arraylist used for storing undo steps */
    public static ArrayList<byte[]> undoArray = new ArrayList<>(1000);

    /**
     * Saves the game for undo in the future
     * @param object MazeField
     */
    public static void saveTurn(Object object) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out;

        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(object);
            byte[] bytes = bos.toByteArray();
            if(undoArray.size() < 999) {
                undoArray.add(0, bytes);
            }
            else {
                undoArray.remove(999);
                undoArray.add(0, bytes);
            }

            out.close();
            bos.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Provides the undo for the actual turn.
     * @return MazeBoard after undo.
     */
    public static MazeBoard undoTurn() {

        MazeBoard tempBoard = null;

        if(undoArray.size() != 0) {

            ByteArrayInputStream bis = new ByteArrayInputStream(undoArray.remove(0));
            ObjectInput in = null;

            try {
                in = new ObjectInputStream(bis);
                tempBoard = (MazeBoard) in.readObject();

            } catch (Exception e){
                try {
                    bis.close();
                } catch (IOException ex) {
                    // ignore close exception
                }
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException ex) {
                    // ignore close exception
                }
            }
        }
        return tempBoard;
    }
}
