package labyrinth;

import labyrinth.players.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author xdusek21 & xkalou03
 * Runnable main class
 */
public class Labyrinth implements Serializable {

    /**
     * Main method which runs the program
     * @param args
     */
    public static void main(String[] args)
    {
        GUILoader game = new GUILoader();
        game.initGUI();
        game.newGameMenu();
        game.newGameDialog();
        game.drawGUI();
    }
}