package labyrinth;


import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

//import javafx.stage.FileChooser;
import labyrinth.board.*;
import labyrinth.players.Player;
import labyrinth.treasure.TreasureCard;

/**
 * This is the class responsible for Graphical User Interface drawing, redrawing, design, redesing, well everything.
 * @author xdusek21 & xkalou03
 */
public class GUILoader implements Serializable {

    /** GUI Element */
    private JButton newGame;
    /** GUI Element */
    private JButton saveGame;
    /** GUI Element */
    private JButton endTurn;

    /** GUI Element */
    private JFrame frame;
    /** GUI Element */
    private Map<String, Component> componentsAdded = new HashMap<String, Component>();

    /** GUI Element */
    private MazeBoard hraciPlocha;
    /** GUI Element */
    private JButton[][] board;
    /** GUI Element */
    private JLabel[][] plIcons;
    /** GUI Element */
    private JButton freeStoneImage;
    /** GUI Element */
    private JLabel freeStoneIcon;
    /** GUI Element */
    private JPanel actualScore;
    /** GUI Element */
    private JLabel imageToFind;
    /** GUI Element */
    private JLabel desToFind;

    /** GUI Element */
    public JLabel[] players;

    private JButton undo;


    /** ImagesLoader is stored here, so it can be used in the whole file */
    private ImagesLoader ImLoader;

    /** Boolean switches used for controlling elements of the GUI */
    private boolean buttonNewGame = false;
    /** Number of rotation applied on the corner icon */
    private int cornIter = 0;
    /** Boolean switches used for controlling elements of the GUI */
    private boolean endOfGame = false;
    /** Boolean switches used for controlling elements of the GUI */
    private boolean startOfGame = true;

    /**
     * Inits the whole GUI
     */
    public void initGUI() {
        frame = new JFrame("Labyrinth by Daniel Dusek (xdusek21) and Filip Kalous (xkalou03)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // Hook something that asks if we're sure. Typical MS junk.
        ImLoader = new ImagesLoader();
        // Load newGameMenu into the main frame, this is hardcoded and it is not going to be moved anywhere
        //createComponent(newGameMenu());
    }

    /**
     * Draws/redraws the GUI
     */
    public void drawGUI() {
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    /**
     * Creates a component from what it is given to it
     * @param component Component that should be created
     * @param layout Layout that will be used for that given component
     * @param name Name that identifies the component, so we can DESTROY IT later
     */
    private void createComponent(Component component, String layout, String name) {
        frame.getContentPane().add(component, layout);
        componentsAdded.put(name, component);
        frame.revalidate();
    }

    /**
     * Destroy the component by given name
     * @param name Name of the component to be destroyed
     */
    private void destroyComponent(String name) {

        if(componentExists(name)) {
            frame.getContentPane().remove(componentsAdded.get(name));
            componentsAdded.remove(name);
            frame.revalidate();
        }
    }

    /**
     * Functions checks if the component exists, by given name
     * @param name Name of the component which existence is to be checked
     * @return
     */
    private boolean componentExists(String name) {
        Component value = componentsAdded.get(name);
        if (value == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Main component for right side of the screen
     * @param playersNumber Number of players that are in the game
     * @return Component comStatsPanel
     */
    private Component comStatsPanel(int playersNumber) {

        int numberOfPlayers = playersNumber;
        String[] names = {"Player #1", "Player #2", "Player #3", "Player #4"};

        JPanel rightCol = new JPanel();
        rightCol.setLayout(new BoxLayout(rightCol, BoxLayout.PAGE_AXIS));

        JPanel trImageToFind = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 50));
        actualScore = new JPanel(new GridLayout(numberOfPlayers + 1, 1));
        final JPanel freeStone = new JPanel(new GridLayout(2, 1));
        JPanel temp = new JPanel();

        JLabel freeStoneDescript = new JLabel("<html><span style='font-size: 13px'>Rotate Stone</span></html>", SwingConstants.CENTER);


        freeStoneDescript.setSize(freeStoneDescript.getPreferredSize());

        freeStoneImage = new JButton();

        ImageIcon freeIcon = new ImageIcon(addStoneIcon(hraciPlocha.getFreeCard().cardPaths(hraciPlocha.getFreeCard()))
                .getScaledInstance(96, 96, Image.SCALE_SMOOTH));

        imageToFind = new JLabel(); // na vypsani hledaneho kamenu
        desToFind = new JLabel();

        Player activePlayer = hraciPlocha.getActivePlayer();
        TreasureCard toFind = activePlayer.getTreasureCardToFind();

        if (toFind == null) {
            //imageToFind.setText("GO HOME");
            desToFind.setText("<html><span style='font-size: 13px'>GO HOME!</span></html>");
            desToFind.setIcon(new ImageIcon(ImLoader.players.get(hraciPlocha.getActivePlayer().ID)));
        } else {
            String turnText = updatePlayerTurn();
            desToFind.setText(turnText);
            desToFind.setIcon(new ImageIcon(ImLoader.players.get(hraciPlocha.getActivePlayer().ID)));
            imageToFind.setIcon(ImLoader.getTreasureIcon(toFind.treasure.getCode()));
        }


        trImageToFind.add(imageToFind, JLabel.CENTER);
        trImageToFind.add(desToFind, JLabel.CENTER);

        freeStoneImage.setIcon(freeIcon);
        freeStoneImage.setOpaque(false);
        freeStoneImage.setContentAreaFilled(false);
        freeStoneImage.setBorderPainted(false);


        freeStoneIcon = new JLabel();

        freeStoneImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                freeStoneImage.setIcon(null);   // deleting the icon

                hraciPlocha.getFreeCard().turnRight();

                freeStoneImage.setIcon(ImLoader.addTrToCard(hraciPlocha.getFreeCard()));
            }
        });

        temp.add(freeStoneImage);

        freeStone.add(freeStoneDescript);
        freeStone.add(temp);

        rightCol.add(trImageToFind);
        rightCol.add(actualScore);
        rightCol.add(freeStone);

        trImageToFind.setPreferredSize(new Dimension(200, 180));
        trImageToFind.setBorder(BorderFactory.createLineBorder(Color.black, 2));

        actualScore.setPreferredSize(new Dimension(200, 180));
        actualScore.setBorder(BorderFactory.createLineBorder(Color.black, 2));

        freeStone.setPreferredSize(new Dimension(200, 180));
        freeStone.setBorder(BorderFactory.createLineBorder(Color.black, 2));

        JLabel title = new JLabel("<html><span style='font-size: 13px'>Actual score</span></html>", SwingConstants.CENTER);

        actualScore.add(title);

        players = new JLabel[numberOfPlayers];

        for (int i = 0; i < numberOfPlayers; i++) {

            players[i] = new JLabel("<html><span style='font-size: 11px'>" + names[i] + ":  " + hraciPlocha.players.get(i).getScore() + "</span><br><br></html>", SwingConstants.CENTER);
            actualScore.add(players[i]);
        }

        return rightCol;
    }


    /**
     * Component that appears when the player is about to start the game
     * @return Component NewGameDialog
     */
    private Component comNewGameDialog() {

        String[] players = {"2", "3", "4"};
        String[] board = {"5x5", "7x7", "9x9", "11x11"};
        String[] treasures = {"12", "24"};

        final int[] values = new int[3];

        JPanel pane = new JPanel();
        //pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
        //pane.setLayout(new GridLayout(4, 2));
        pane.setPreferredSize(new Dimension(200, 200));

        JPanel inPane = new JPanel();
        inPane.setLayout(new GridLayout(4, 2));

        JLabel numberPlayers = new JLabel("Number of players: ");
        final JComboBox chooseNumber = new JComboBox(players);

        inPane.add(numberPlayers);
        inPane.add(chooseNumber);

        JLabel boardSize = new JLabel("Size of board: ");
        final JComboBox chooseSize = new JComboBox(board);

        inPane.add(boardSize);
        inPane.add(chooseSize);

        JLabel treasureCount = new JLabel("Treasure count: ");
        final JComboBox chooseTreasure = new JComboBox(treasures);

        inPane.add(treasureCount);
        inPane.add(chooseTreasure);

        JButton cancel = new JButton();
        JButton start = new JButton("Start");
        cancel.setOpaque(false);
        cancel.setContentAreaFilled(false);
        cancel.setBorderPainted(false);


        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                startOfGame = false;

                values[0] = Integer.parseInt(chooseNumber.getSelectedItem().toString());
                values[1] = Character.getNumericValue(chooseSize.getSelectedItem().toString().charAt(0));
                values[2] = Integer.parseInt(chooseTreasure.getSelectedItem().toString());

                if (values[1] == 1)
                    values[1] = 11;

                if (values[1] == 5 && values[2] == 24) {
                    JOptionPane.showMessageDialog(frame, "For gamefield sized 5x5 is allowed the maximum number of treasures 12. \n You selected 24 and that amout was automatically reduced.");
                    values[2] = 12;
                }

                destroyComponent("newgamediag");

                hraciPlocha = MazeBoard.createMazeBoard(values[1]);  // safety at its best
                hraciPlocha.newGame(values[2], values[0]);
                hraciPlocha.numberOfTreasure = values[2];

                UndoHelper.saveTurn(hraciPlocha);

                ImLoader.loadAllImages(values[2], values[1], values[0]);

                createComponent(comStatsPanel(values[0]), BorderLayout.EAST, "statspanel");

                // vytvoreni comGameBoard, tedy herniho pole, vcetne posuvniku
                createComponent(comGameBoard(values[1]), BorderLayout.CENTER, "gameboard");


                buttonNewGame = true;
                newGame.setEnabled(true);
                saveGame.setEnabled(true);
                endTurn.setEnabled(true);

                String turnText = updatePlayerTurn();
                desToFind.setIcon(new ImageIcon(ImLoader.players.get(hraciPlocha.getActivePlayer().ID)));
                desToFind.setText(turnText);
            }
        });

        inPane.add(cancel);
        inPane.add(start);

        pane.add(inPane);

        return pane;
    }

    /**
     * Function responsible for creating newGameMenu
     */
    public void newGameMenu() {
        createComponent(comNewGameMenu(), BorderLayout.WEST, "newgamemenu");
    }

    /**
     * Function responsible for creating newGameDialog
     */
    public void newGameDialog() {
        createComponent(comNewGameDialog(), BorderLayout.CENTER, "newgamediag");
    }


    /**
     * Setter for tmpI and tmpJ that helps program to determine which shifter is to be disabled (prevention of contra-turning)
     * @param tmpI Integer Coordinate X of the shifter used
     * @param tmpJ Integer Coordinate Y of the shifter used
     */
    private void setDeactivateShifter(int tmpI, int tmpJ) {
        hraciPlocha.deX = tmpI;
        hraciPlocha.deY = tmpJ;
        if (tmpI == 0 || tmpI == hraciPlocha.n + 1) {
            if (tmpI == 0) {
                hraciPlocha.deX = hraciPlocha.n + 1;
            } else if (tmpI == hraciPlocha.n + 1) {
                hraciPlocha.deX = 0;
            }
        }

        if (tmpJ == 0 || tmpJ == hraciPlocha.n + 1) {
            if (tmpJ == 0) {
                hraciPlocha.deY = hraciPlocha.n + 1;
            } else if (tmpJ == hraciPlocha.n + 1) {
                hraciPlocha.deY = 0;
            }
        }

        // deX, deY jsou souřadnice buttonu ktery potřebuju pro příští tah deaktivovat
    }

    /**
     * This function handles all the button clicks, dependent on the context in which it is called
     * @param i X coordinate of the button pressed
     * @param j Y coordinate of the button pressed
     */
    private void buttonCoords(int i, int j) {
        MazeCard oldEmpty;

        int scaled = scaledConst(hraciPlocha.n);
        int n = (hraciPlocha.n + 2);


        freeStoneImage.setIcon(null);
        freeStoneIcon.setIcon(null);

        // First control line horizontal
        if (i == 0 && j != 0 && j != n - 1) {
            if (j % 2 == 0) {
                oldEmpty = hraciPlocha.shift(hraciPlocha.get(i + 1, j));
                setDeactivateShifter(i, j);
                destroyComponent("gameboard");

                createComponent(comGameBoard(hraciPlocha.n), BorderLayout.CENTER, "gameboard");
            }
        }
        // Second vertical control line
        else if (j == n - 1 && i != 0 && i != n - 1) {
            if (i % 2 == 0) {
                oldEmpty = hraciPlocha.shift(hraciPlocha.get(i, j - 1));
                setDeactivateShifter(i, j);
                destroyComponent("gameboard");

                createComponent(comGameBoard(hraciPlocha.n), BorderLayout.CENTER, "gameboard");
            }
        }
        // Second horizontal control line
        else if (i == n - 1 && j != 0 && j != n - 1) {
            if (j % 2 == 0) {
                oldEmpty = hraciPlocha.shift(hraciPlocha.get(i - 1, j));
                setDeactivateShifter(i, j);
                destroyComponent("gameboard");

                createComponent(comGameBoard(hraciPlocha.n), BorderLayout.CENTER, "gameboard");
            }
        }
        // First vertical control line
        else if (j == 0 && i != 0 && i != n - 1) {
            if (i % 2 == 0) {
                oldEmpty = hraciPlocha.shift(hraciPlocha.get(i, j + 1));

                setDeactivateShifter(i, j);
                destroyComponent("gameboard");

                createComponent(comGameBoard(hraciPlocha.n), BorderLayout.CENTER, "gameboard");
            }
        }

        freeStoneImage.setPreferredSize(new Dimension(96, 96));
    }


    /**
     * Deactivates all the shifting coms, this happens after player shift within his turn
     * @param size Integer size of the gameboard
     */
    public void deactivateShiftingComs(int size) {

        hraciPlocha.turnShifted = true;
        int n = size + 2;
        for (int i = 0; i < n; i++) {

            for (int j = 0; j < n; j++) {

                // First control line horizontal
                if (i == 0 && j != 0 && j != n - 1) {
                    if (j % 2 == 0) {
                        board[i][j].setEnabled(false);
                    }
                }
                // Second vertical control line
                else if (j == n - 1 && i != 0 && i != n - 1) {
                    if (i % 2 == 0) {
                        board[i][j].setEnabled(false);
                    }
                }
                // Second horizontal control line
                else if (i == n - 1 && j != 0 && j != n - 1) {
                    if (j % 2 == 0) {
                        board[i][j].setEnabled(false);
                    }
                }
                // First vertical control line
                else if (j == 0 && i != 0 && i != n - 1) {
                    if (i % 2 == 0) {
                        board[i][j].setEnabled(false);
                    }
                }
            }
        }
    }

    /**
     * Contra-function to deactivateShiftingComs()
     * @param size Integer size of the gameboard
     */
    private void activateShiftingComs(int size) {

        hraciPlocha.turnShifted = false;
        int n = size + 2;
        for (int i = 0; i < n; i++) {

            for (int j = 0; j < n; j++) {

                if (i == hraciPlocha.deX && j == hraciPlocha.deY) {
                    continue;
                }
                // First control line horizontal
                else if (i == 0 && j != 0 && j != n - 1) {
                    if (j % 2 == 0) {
                        board[i][j].setEnabled(true);
                    }
                }
                // Second vertical control line
                else if (j == n - 1 && i != 0 && i != n - 1) {
                    if (i % 2 == 0) {
                        board[i][j].setEnabled(true);
                    }
                }
                // Second horizontal control line
                else if (i == n - 1 && j != 0 && j != n - 1) {
                    if (j % 2 == 0) {
                        board[i][j].setEnabled(true);
                    }
                }
                // First vertical control line
                else if (j == 0 && i != 0 && i != n - 1) {
                    if (i % 2 == 0) {
                        board[i][j].setEnabled(true);
                    }
                }
            }
        }
    }

    /**
     * Component creating method, responsible for everything that happens withing comGameBoard
     * @param size Integer size of the game board
     * @return Component GameBoard
     */
    private Component comGameBoard(int size) {
        int n = size + 2;   // +2 for extending the gamefield (arrows)

        // here comes the fucking constant for fucking stretching the fucking pieces of fucking field. fuckers.

        GridBagLayout gridbag = new GridBagLayout();

        JPanel rightCol = new JPanel();
        rightCol.setLayout(gridbag);

        GridBagConstraints c = new GridBagConstraints();

        board = new JButton[n][n];
        plIcons = new JLabel[n][n];

        ImageIcon noShiftIcon = new ImageIcon(ImLoader.shifts.get(0));


        int xdimension = scaledConst(size);
        int ydimension = scaledConst(size);

        Dimension buttonSize = new Dimension(xdimension, ydimension);
        boolean shiftButton = false;

        for (int i = 0; i < n; i++) {

            for (int j = 0; j < n; j++) {

                if (i == hraciPlocha.deX && j == hraciPlocha.deY) {
                    board[i][j] = new JButton(noShiftIcon);
                    board[i][j].setDisabledIcon(noShiftIcon);
                    board[i][j].setEnabled(false);
                }
                // First control line horizontal
                else if (i == 0 && j != 0 && j != n - 1) {
                    if (j % 2 == 0) {
                        board[i][j] = new JButton();
                        board[i][j].setIcon(new ImageIcon(ImLoader.shifts.get(1)));
                        shiftButton = true;
                    } else {
                        board[i][j] = new JButton(noShiftIcon);
                        board[i][j].setDisabledIcon(noShiftIcon);
                        board[i][j].setEnabled(false);
                    }
                }
                // Second vertical control line
                else if (j == n - 1 && i != 0 && i != n - 1) {
                    if (i % 2 == 0) {
                        board[i][j] = new JButton(new ImageIcon(ImLoader.shifts.get(2)));
                        shiftButton = true;
                    } else {
                        board[i][j] = new JButton(noShiftIcon);
                        board[i][j].setDisabledIcon(noShiftIcon);
                        board[i][j].setEnabled(false);
                    }
                }
                // Second horizontal control line
                else if (i == n - 1 && j != 0 && j != n - 1) {
                    if (j % 2 == 0) {
                        board[i][j] = new JButton();
                        board[i][j].setIcon(new ImageIcon(ImLoader.shifts.get(3)));
                        shiftButton = true;
                    } else {
                        board[i][j] = new JButton(noShiftIcon);
                        board[i][j].setDisabledIcon(noShiftIcon);
                        board[i][j].setEnabled(false);
                    }
                }
                // First vertical control line
                else if (j == 0 && i != 0 && i != n - 1) {
                    if (i % 2 == 0) {
                        board[i][j] = new JButton();
                        board[i][j].setIcon(new ImageIcon(ImLoader.shifts.get(4)));
                        shiftButton = true;
                    } else {
                        board[i][j] = new JButton(noShiftIcon);
                        board[i][j].setDisabledIcon(noShiftIcon);
                        board[i][j].setEnabled(false);
                    }
                }
                // zarazka zpracovani buttonCoords(i,j)
                // corners [E]
                else if ((i == 0 && j == 0) || (i == 0 && j == n - 1) || (i == n - 1 && j == 0) || (i == n - 1 && j == n - 1)) {
                    board[i][j] = new JButton(noShiftIcon);
                    board[i][j].setDisabledIcon(noShiftIcon);
                    board[i][j].setEnabled(false);
                } else {

                    if((i == 1 && j == 1) || (i == 1 && j == size) || (i == size && j == 1) || (i == size && j == size)) {

                        // rohy
                        board[i][j] = new JButton();
                        plIcons[i][j] = new JLabel();
                        board[i][j].add(plIcons[i][j]);
                        board[i][j].setIcon(new ImageIcon(ImLoader.corners.get(cornIter)));
                        //board[i][j].setBorder(BorderFactory.createLineBorder(Color.GRAY));
                        cornIter++;
                        if(i == size && j == size)
                            cornIter = 0;
                    } else {

                    // tady se vytváří všechny ty fancy klikací Jbuttony, takže by se tu měly vytvářet i Jlabely pro hráče
                        board[i][j] = new JButton();
                        plIcons[i][j] = new JLabel();
                        board[i][j].add(plIcons[i][j]);
                        //board[i][j].setBorder(BorderFactory.createLineBorder(Color.GRAY));
                        board[i][j].setIcon(ImLoader.addTrToCard(hraciPlocha.field[i][j].stone));
                    }

                    // přidelovani hracu na pozice (ikonky)

                    for (int k = 0; k < hraciPlocha.numberOfPlayers; k++) {
                        if (hraciPlocha.players.get(k).getPosX() == i && hraciPlocha.players.get(k).getPosY() == j) {
                            plIcons[i][j].setIcon(new ImageIcon(ImLoader.players.get(k)));
                        }
                    }
                }

                board[i][j].setPreferredSize(buttonSize);
                //board[i][j].revalidate();

                final int tmpI = i;
                final int tmpJ = j;

                if (shiftButton) {
                    final int tmpSize = size;
                    board[i][j].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            if (!hraciPlocha.getActivePlayer().shiftClick) {
                                buttonCoords(tmpI, tmpJ);

                                deactivateShiftingComs(tmpSize);
                                hraciPlocha.getActivePlayer().shiftClick = true;
                            }
                        }
                    });
                } else {

                    final int tmpCoordX = i;
                    final int tmpCoordY = j;

                    board[i][j].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {

                            if (!(hraciPlocha.getActivePlayer().played) && hraciPlocha.getActivePlayer().shiftClick) {

                                int tmpPreScore = hraciPlocha.getActivePlayer().getScore();
                                if (hraciPlocha.findWay(tmpCoordX, tmpCoordY)) {


                                    hraciPlocha.getActivePlayer().changePosition(tmpCoordX, tmpCoordY);

                                    int tmpPostScore = hraciPlocha.getActivePlayer().getScore();

                                    if (tmpPreScore > tmpPostScore) {
                                        players[hraciPlocha.getActivePlayer().ID].setText("<html><span style='font-size: 11px'>Player #" + ((hraciPlocha.getActivePlayer().ID) + 1) + ":  " + hraciPlocha.getActivePlayer().getScore() + "</span><br><br></html>");
                                    }

                                    if ((hraciPlocha.getActivePlayer().goHome) &&
                                            (hraciPlocha.getActivePlayer().getPosX() == hraciPlocha.getActivePlayer().getDestX()
                                                    && hraciPlocha.getActivePlayer().getPosY() == hraciPlocha.getActivePlayer().getDestY())) {
                                        createComponent(comEndGame(), BorderLayout.CENTER, "endgame");
                                        frame.revalidate();

                                    } else {

                                        hraciPlocha.getActivePlayer().played = true;

                                        // vykresleni noveho gameboardu, je to fakt dobry
                                        destroyComponent("gameboard");
                                        createComponent(comGameBoard(hraciPlocha.n), BorderLayout.CENTER, "gameboard");

                                        if (hraciPlocha.getActivePlayer().goHome) {
                                            destroyComponent("statspanel");
                                            createComponent(comStatsPanel(hraciPlocha.numberOfPlayers), BorderLayout.EAST, "statspanel");
                                        }
                                    }

                                    //addPlayerIcon(tmpCoordX, tmpCoordY, hraciPlocha.n);
                                }
                            } else if (!hraciPlocha.getActivePlayer().shiftClick) { // questionable
                                JOptionPane.showMessageDialog(null, "You have to shift before playing!");
                            } else {
                                JOptionPane.showMessageDialog(null, "You have moved your player in this turn, please finish the turn.");
                            }
                        }
                    });

                }

                shiftButton = false;
                c.gridx = j;
                c.gridy = i;
                c.gridwidth = c.gridheight = 1;
                c.fill = GridBagConstraints.BOTH;
                c.anchor = GridBagConstraints.NORTHWEST;

                // připnutí labelů k tlačítkům


                rightCol.add(board[i][j], c);
            }
        }

        freeStoneImage.setIcon(ImLoader.addTrToCard(hraciPlocha.getFreeCard()));

        if (hraciPlocha.getActivePlayer().shiftClick) {
            deactivateShiftingComs(size);
        }
        
        return rightCol;
    }

    /**
     * Component that appears after the game ends
     * @return Component endGame
     */
    private Component comEndGame() {

        destroyComponent("gameboard");
        endOfGame = true;

        //JPanel finalPanel = new JPanel(new GridLayout(2, 1));
        JPanel finalPanel = new JPanel();

        JLabel winPanel = new JLabel("Player " + ((hraciPlocha.getActivePlayer().ID) + 1) + " won the game!");
        finalPanel.add(winPanel);

        JPanel scorePanel = new JPanel(new GridLayout(0, 2));
        JLabel playerLabels[] = new JLabel[hraciPlocha.numberOfPlayers * 2];

        for (int i = 0, j = 0; i < hraciPlocha.numberOfPlayers; i++, j += 2) {

            playerLabels[j] = new JLabel();
            playerLabels[j + 1] = new JLabel();
            playerLabels[j].setText("Player " + ((hraciPlocha.players.get(i).ID) + 1) + ":    ");
            playerLabels[j + 1].setText(hraciPlocha.players.get(i).getScore() + " card(s) were remaining.");
            scorePanel.add(playerLabels[j]);
            scorePanel.add(playerLabels[j + 1]);
        }

        finalPanel.add(scorePanel);

        saveGame.setEnabled(false);
        endTurn.setEnabled(false);
        return finalPanel;
    }


    /**
     * Component that is shown in the left column
     * @return Component newGameMenu
     */
    private Component comNewGameMenu() {
        // Left column panel
        JPanel leftCol = new JPanel(new GridLayout(2, 1));


        // Panels nested in left column
        JPanel gameMenu = new JPanel(); // Game menu panel
        JPanel gameTurns = new JPanel();

        // Add game menu to left columns
        leftCol.add(gameMenu);
        leftCol.add(gameTurns);


        leftCol.setBorder(BorderFactory.createLineBorder(Color.black, 2));

        // gameMenu settings and button

        gameMenu.setPreferredSize(new Dimension(200, 250));
        gameMenu.setBorder(BorderFactory.createLineBorder(Color.black, 2));

        gameTurns.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        gameTurns.setPreferredSize(new Dimension(200, 250));

        newGame = new JButton("New game");
        newGame.setEnabled(false);


        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (buttonNewGame) {
                    int n = JOptionPane.showConfirmDialog(frame, "The game is in progress, do you really want to start a new one?", "Start new game", JOptionPane.YES_NO_OPTION);
                    if (n == JOptionPane.YES_OPTION) {
                        if (!endOfGame) {
                            destroyComponent("gameboard");
                        } else
                            destroyComponent("endgame");

                        destroyComponent("statspanel");
                        createComponent(comNewGameDialog(), BorderLayout.CENTER, "newgamediag");
                        saveGame.setEnabled(false);
                        newGame.setEnabled(false);
                        buttonNewGame = false;
                        endTurn.setEnabled(false);
                    }
                }
            }
        });

        saveGame = new JButton("Save game");
        saveGame.setEnabled(false);
        JButton loadGame = new JButton("Load game");
        JButton exitGame = new JButton("Exit game");

        endTurn = new JButton("End Turn");
        endTurn.setEnabled(false);

        exitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                int n = JOptionPane.showConfirmDialog(frame, "Do you really want to close the game?", "Exit game", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    frame.dispose();
                }
            }
        });

        saveGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                JFileChooser c = new JFileChooser();
                String save = "";
                // Demonstrate "Save" dialog:
                int rVal = c.showSaveDialog(c);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    //c.getSelectedFile().getName();
                    //c.getCurrentDirectory().toString();
                    save = save.concat(c.getCurrentDirectory().toString());
                    save = save.concat("/");
                    String temp = c.getSelectedFile().getName();
                    if(temp.contains(".dat"))
                        save = save.concat(temp);
                    else
                        save = save.concat(temp + ".dat");

                    InputOutput.saveGame(hraciPlocha, save);
                }
            }
        });

        loadGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String load = "";
                JFileChooser c = new JFileChooser();
                // Demonstrate "Save" dialog:
                int rVal = c.showOpenDialog(c);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    //c.getSelectedFile().getName();
                    //c.getCurrentDirectory().toString();
                    load = load.concat(c.getCurrentDirectory().toString());
                    load = load.concat("/");
                    load = load.concat(c.getSelectedFile().getName());
                    if(!startOfGame) {
                        destroyComponent("gameboard");
                        destroyComponent("statspanel");
                    }
                    else {
                        destroyComponent("newgamediag");
                    }

                    saveGame.setEnabled(true);
                    newGame.setEnabled(true);
                    endTurn.setEnabled(true);
                    buttonNewGame = true;
                    hraciPlocha = InputOutput.loadGame(load);

                    System.out.println(hraciPlocha.numberOfTreasure +" " + hraciPlocha.n + " " + hraciPlocha.numberOfPlayers);
                    ImLoader.reload(hraciPlocha.numberOfTreasure, hraciPlocha.n, hraciPlocha.numberOfPlayers);

                    createComponent(comStatsPanel(hraciPlocha.numberOfPlayers), BorderLayout.EAST, "statspanel");
                    createComponent(comGameBoard(hraciPlocha.n), BorderLayout.CENTER, "gameboard");
                    frame.revalidate();
                }
            }
        });

        endTurn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                int n = JOptionPane.showConfirmDialog(frame, "Next Player. Let's do this.", "Next Player", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    hraciPlocha.changeActivePlayer();

                    activateShiftingComs(hraciPlocha.n);

                    String turnText = updatePlayerTurn();
                    desToFind.setText(turnText);
                    desToFind.setIcon(new ImageIcon(ImLoader.players.get(hraciPlocha.getActivePlayer().ID)));

                    TreasureCard toFind = hraciPlocha.getActivePlayer().getTreasureCardToFind();
                    if (toFind != null)
                        imageToFind.setIcon(ImLoader.getTreasureIcon(toFind.treasure.getCode()));

                    else {
                        //imageToFind.setText("GO HOME");
                        desToFind.setText("<html><span style='font-size: 13px'>GO HOME!</span></html>");
                        desToFind.setIcon(new ImageIcon(ImLoader.players.get(hraciPlocha.getActivePlayer().ID)));
                        imageToFind.setIcon(null);
                    }

                    hraciPlocha.getActivePlayer().played = false;
                    hraciPlocha.getActivePlayer().shiftClick = false;

                    UndoHelper.saveTurn(hraciPlocha);

                }
            }
        });

        JButton horizontalSeparatorB = new JButton();
        horizontalSeparatorB.setOpaque(false);
        horizontalSeparatorB.setContentAreaFilled(false);
        horizontalSeparatorB.setBorderPainted(false);

        Dimension buttonDimension = new Dimension(130, 35);
        endTurn.setPreferredSize(buttonDimension);
        newGame.setPreferredSize(buttonDimension);
        saveGame.setPreferredSize(buttonDimension);
        loadGame.setPreferredSize(buttonDimension);
        exitGame.setPreferredSize(buttonDimension);
        horizontalSeparatorB.setPreferredSize(buttonDimension);

        gameMenu.add(newGame);
        gameMenu.add(saveGame);
        gameMenu.add(loadGame);
        gameMenu.add(exitGame);
        gameMenu.add(horizontalSeparatorB);

        gameTurns.add(endTurn);


        //JButton redo = new JButton(">>");
        undo = new JButton("UNDO");

        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                MazeBoard temp = UndoHelper.undoTurn();
                if(temp != null) {
                    destroyComponent("gameboard");
                    destroyComponent("statspanel");

                    hraciPlocha = temp;

                    createComponent(comStatsPanel(hraciPlocha.numberOfPlayers), BorderLayout.EAST, "statspanel");
                    createComponent(comGameBoard(hraciPlocha.n), BorderLayout.CENTER, "gameboard");
                }
            }
        });

        buttonDimension = new Dimension(120, 35);
       // redo.setPreferredSize(buttonDimension);
        undo.setPreferredSize(buttonDimension);

        gameMenu.add(undo);
        //gameMenu.add(redo);

        return leftCol;
    }

    /**
     * Updates player turn and tells who is playing now.
     * @return String message
     */
    public String updatePlayerTurn()
    {
        return "Player's "+(hraciPlocha.activePlayerN + 1)+" turn!\n";
    }

    /**
     * Adds an icon for the stone
     * @param type String type of the icon
     * @return Image of the stone
     */
    private Image addStoneIcon(String type) {
        Image iconType = null;
        String imageToUse = "/resources/" + type + ".png";

        try {

            iconType = ImageIO.read(getClass().getResource(imageToUse));

        } catch (IOException ex) {
        }

        return iconType;
    }

    /**
     * Scaling function for the icon images
     * @param size Size of the board
     * @return Return scaled constant
     */
    private int scaledConst(int size) {

        double constant = 1.0;
        int scaled = 0;

        switch (size) {
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

        scaled = (int) Math.round(64 * constant);
        return scaled;
    }
}