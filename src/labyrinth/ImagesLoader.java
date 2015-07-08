package labyrinth;

import labyrinth.board.MazeBoard;
import labyrinth.board.MazeCard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class loads all the image files before they are used, therefore it doesn't overload the application
 * @author xdusek21 & xkalou03
 */
public class ImagesLoader {

    /** ArrayList of Buffered Images for treasures */
    public ArrayList<BufferedImage> treasures;
    /** ArrayList of Buffered Images for player icons */
    public ArrayList<Image> players;
    /** ArrayList of BufferedImages for corner icons */
    public ArrayList<BufferedImage> corners;

    public ArrayList<BufferedImage> shifts;

    /** Variable used for scale-calculation */
    private int dim;

    /**
     * Load all images (except for pathing icons) into class property ArrayLists
     * @param countTreasure Number of treasures
     * @param size The size of the board
     * @param countPlayers Number of players
     * @return Image
     */
    public Image loadAllImages(int countTreasure, int size, int countPlayers) {

        treasures = new ArrayList<>(countTreasure);
        players = new ArrayList<>(countPlayers);
        corners = new ArrayList<>(4);
        shifts = new ArrayList<>(5);
        dim = scaledConst(size, false);
        int dimPlay = scaledConst(size, true);

        BufferedImage tmpImage = null;
        try {

            for(int i = 1; i <= countTreasure; i++) {

                BufferedImage inT = ImageIO.read(getClass().getResource("/resources/treasures/"+ i +".png"));
                BufferedImage tmp = new BufferedImage(dim, dim, BufferedImage.TYPE_INT_ARGB);
                Graphics g = tmp.createGraphics();
                g.drawImage(inT, 0, 0, dim, dim, null);
                treasures.add(i - 1, tmp);
            }

            for(int i = 0; i < countPlayers; i++) {

                Image inPlay = ImageIO.read(getClass().getResource("/resources/player-"+ i +".png"));
                BufferedImage tmp = new BufferedImage(dimPlay, dimPlay, BufferedImage.TYPE_INT_ARGB);
                Graphics g = tmp.createGraphics();
                g.drawImage(inPlay, 0, 0, dimPlay, dimPlay, null);
                players.add(i, tmp);
            }

            for(int i = 0; i < 4; i++) {

                BufferedImage inCor = ImageIO.read(getClass().getResource("/resources/corner-"+ i +".png"));
                BufferedImage tmp = new BufferedImage(dim, dim, BufferedImage.TYPE_INT_ARGB);
                Graphics g = tmp.createGraphics();
                g.drawImage(inCor, 0, 0, dim, dim, null);
                corners.add(i, tmp);
            }

            for(int i = 0; i < 5; i++) {

                BufferedImage inCor = ImageIO.read(getClass().getResource("/resources/shift"+ i +".png"));
                BufferedImage tmp = new BufferedImage(dim, dim, BufferedImage.TYPE_INT_ARGB);
                Graphics g = tmp.createGraphics();
                g.drawImage(inCor, 0, 0, dim, dim, null);
                shifts.add(i, tmp);
            }

        } catch (IOException ex) {}

        return tmpImage;

    }

    /**
     * Adds icon of treasure to the icon of path
     * @param card From this variable it gathers the information about the treasure (there may be even no treasure at all)
     * @return ImageIcon forged from two Icons
     */
    public ImageIcon addTrToCard(MazeCard card) {

        BufferedImage tmp = null;
        ImageIcon ret;
        Graphics g = null;

        try {

            BufferedImage in = ImageIO.read(getClass().getResource("/resources/" + card.cardPaths(card) + ".png"));
            tmp = new BufferedImage(dim, dim, BufferedImage.TYPE_INT_ARGB);
            g = tmp.createGraphics();
            g.drawImage(in, 0, 0, dim, dim, null);
        }
        catch (IOException e) {}

        if(card.treasure != null)
            g.drawImage(treasures.get(card.treasure.getCode()), dim/3, dim/3, dim/3, dim/3, null);

        ret = new ImageIcon(tmp);
        return ret;
    }

    /**
     * Honestly a loads of magic is happening here, constants were selected as the best for each gameboard size stretch
     * @param size Integer size of the gameboard
     * @param play Boolean by it is determining if it scales the player or the icon
     * @return Integer scale number
     */
    private int scaledConst(int size, boolean play) {

        double constant = 1.0;
        int scaled = 0;

        switch(size) {
            case 5:
                constant = 1.28575;
                break;
            case 7:
                constant = 1.0;
                break;
            case 9:
                constant = 0.81825;
                break;
            case 11:
                constant = 0.69237;
                break;
        }
        if(play)
            scaled = (int) Math.round(22*constant);
        else
            scaled = (int) Math.round(64*constant);

        return scaled;
    }

    /**
     * Gets icon of the treasure specified by code
     * @param code Specifies the treasure we are looking for
     * @return ImageIcon of the treasure
     */
    public ImageIcon getTreasureIcon(int code) {

        return (new ImageIcon(treasures.get(code)));
    }

    /**
     * Used for reloading the pictures after the game was load with different size (simply reloads already lodead images since their size should change)
     * @param treasures Number of treasures
     * @param size Size of the board
     * @param players Number of players
     */
    public void reload(int treasures, int size, int players) {

        loadAllImages(treasures, size, players);
    }
}
