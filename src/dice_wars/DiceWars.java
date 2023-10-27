package dice_wars;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.lang.Math;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * Main class for the application: creates the JFrame and most of the Swing components.
 * It contains the main method.
 */
public class DiceWars {

    static JFrame frame;
    static ImageIcon icon;
    static JButton reroll;
    static DicePanel gameArea;
    static DiceMenu menu;
    static JButton end;
    static JLabel playerScore;
    static JLabel enemyScore;
    static JLabel turn;
    static Random randomizer;
    static boolean attackingState;
    static boolean gameStarted;
    static int attackingRow;
    static int attackingCol;
    static CellPanel[][] cells;
    static boolean playerState;
    static Clip diceMenu;
    static Clip diceBG;

    /**
     * Constructor for the DiceWars class.
     */
    public DiceWars() {
        SwingUtilities.invokeLater(() -> {
            initializeGUI();
        });
    }

    private void initializeGUI() {
        icon = new ImageIcon("Assets/dice_icon.png");
        reroll = new JButton("Reroll map");
        gameArea = new DicePanel();
        menu = new DiceMenu();
        end = new JButton("Play");
        playerScore = new JLabel("0", JLabel.CENTER);
        enemyScore = new JLabel("0", JLabel.CENTER);
        turn = new JLabel("Player's Turn", JLabel.CENTER);
        frame = new JFrame("Dice Wars");

        randomizer = new Random();
        attackingState = false;
        gameStarted = false;
        attackingRow = 0;
        attackingCol = 0;
        playerState = true;

        frame.setIconImage(icon.getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(menu);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        diceMenu = playSound(new File("Assets/dice_menu.wav"), true);

        gameArea.setBorder(new MatteBorder(6, 6, 6, 6, Color.BLACK));

        playerScore.setFont(new Font("Roboto", Font.BOLD, 64));
        playerScore.setBorder(new EmptyBorder(7, 7, 7, 7));
        playerScore.setForeground(DicePanel.PLAYER_COLOR);
        playerScore.setText(String.valueOf(DicePanel.getPlayerTerritories()));

        enemyScore.setFont(new Font("Roboto", Font.BOLD, 64));
        enemyScore.setBorder(new EmptyBorder(7, 7, 7, 7));
        enemyScore.setForeground(DicePanel.ENEMY_COLOR);
        enemyScore.setText(String.valueOf(DicePanel.getEnemyTerritories()));

        turn.setFont(new Font("Roboto", Font.BOLD, 42));
        turn.setBorder(new EmptyBorder(7, 7, 7, 7));
        turn.setForeground(DicePanel.PLAYER_COLOR);
        turn.setOpaque(true);

        end.setFont(new Font("Georgia", Font.BOLD, 36));
        end.setBorder(new EmptyBorder(5, 5, 5, 5));
        end.setBackground(Color.WHITE);
        end.setForeground(Color.BLACK);
        end.setFocusPainted(false);
        end.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameStarted) {
                    endTurn();
                    playSound(new File("Assets/ai_turn.wav"), false);
                } else {
                    gameStarted = true;
                    end.setText("END TURN");
                    frame.remove(reroll);
                    frame.add(turn, BorderLayout.NORTH);
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    if (randomizer.nextInt(2) == 0) {
                        aiTurn();
                    }
                    diceBG = playSound(new File("Assets/dice_bg.wav"), true);
                    FloatControl gainControl = 
                        (FloatControl) diceBG.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(-10.0f);
                    diceMenu.stop();
                }
            }
        });
        end.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (end.isEnabled()) {
                    end.setBackground(Color.BLACK);
                    end.setForeground(Color.WHITE);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                end.setBackground(Color.WHITE);
                end.setForeground(Color.BLACK);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                end.setBackground(Color.WHITE);
                end.setForeground(Color.BLACK);
            }
        });

        reroll.setFont(new Font("Georgia", Font.BOLD, 40));
        reroll.setBackground(Color.WHITE);
        reroll.setForeground(Color.BLACK);
        reroll.setFocusPainted(false);
    }

    /**
     * Shows the main game screen of the game.
     */
    static void gameScreen() {
        frame.remove(menu);
        frame.add(gameArea);
        frame.add(reroll, BorderLayout.NORTH);
        frame.add(end, BorderLayout.SOUTH);
        frame.add(playerScore, BorderLayout.WEST);
        frame.add(enemyScore, BorderLayout.EAST);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        //diceMenu.stop();

        cells = gameArea.getCellMatrix();
    }

    public static boolean getGameStarted() {
        return gameStarted;
    }

    public static boolean getPlayerState() {
        return playerState;
    }

    /**
     * Changes the variables attackingState, attackingRow and attackingCol
     * depending on the given parameters.
     * 
     * @param state  state (attack or not)
     * @param row    attacking row
     * @param col    attacking column
     */
    public static void setAttackState(boolean state, int row, int col) {
        attackingState = state;
        attackingCol = col;
        attackingRow = row;
    }

    public static void setAttackState(boolean state) {
        attackingState = state;
    }

    public static boolean getAttackState() {
        return attackingState;
    }

    /**
     * Checks if the cell that is hovered over can be attacked,
     * depending on which cell the player is attacking with.
     * 
     * @param enemyRow  row of the cell that is trying to be attacked
     * @param enemyCol  column of the cell that is trying to be attacked
     * @return          true if it can be attacked, false if not
     */
    public static boolean canAttack(int enemyRow, int enemyCol) {
        if (Math.abs(attackingCol - enemyCol) <= 1 && Math.abs(attackingRow - enemyRow) <= 1) {
            return true;
        }
        return false;
    }

    /**
     * Attacks a cell, using the coordinates of the cell that entered the attacking state.
     * This is a method for the player's attack.
     * 
     * @param enemyRow  row of the cell that is being attacked
     * @param enemyCol  column of the cell that is being attacked
     */
    public static void attack(int enemyRow, int enemyCol) {

        int enemyRolls = 0;
        int playerRolls = 0;
        int enemyDice = cells[enemyRow][enemyCol].getDiceNumber();
        int playerDice = cells[attackingRow][attackingCol].getDiceNumber();
        for (int i = 0; i < enemyDice; i++) {
            enemyRolls += randomizer.nextInt(6) + 1;
        }
        for (int i = 0; i < playerDice; i++) {
            playerRolls += randomizer.nextInt(6) + 1;
        }

        if (enemyRolls < playerRolls) {
            cells[enemyRow][enemyCol].setDiceNumber(playerDice - 1);
            cells[enemyRow][enemyCol].setIsPlayer(true);
            DicePanel.setEnemyTerritories(DicePanel.getEnemyTerritories() - 1);
            playerScore.setText(String.valueOf(DicePanel.getPlayerTerritories()));
            enemyScore.setText(String.valueOf(DicePanel.getEnemyTerritories()));
        }
        cells[attackingRow][attackingCol].setDiceNumber(1);
        cells[attackingRow][attackingCol].number.setForeground(Color.WHITE);
        attackingState = false;
        frame.pack();
        frame.setLocationRelativeTo(null);
        if (DicePanel.getEnemyTerritories() == 0) {
            playSound(new File("Assets/dice_win.wav"), false);
            diceBG.stop();
            new DiceEnd(true);
        }
    }

    /**
     * Attacks a cell, using the coordinates given through the parameters.
     * This is a method for the AI's attack.
     * 
     * @param attackingRow  row of attacking cell
     * @param attackingCol  column of attacking cell
     * @param attackedRow   row of attacked cell
     * @param attackedCol   column of attacked cell
     */
    public static void attackAI(int attackingRow, int attackingCol,
        int attackedRow, int attackedCol) {

        int enemyRolls = 0;
        int playerRolls = 0;
        int playerDice = cells[attackedRow][attackedCol].getDiceNumber();
        int enemyDice = cells[attackingRow][attackingCol].getDiceNumber();
        for (int i = 0; i < enemyDice; i++) {
            enemyRolls += randomizer.nextInt(6) + 1;
        }
        for (int i = 0; i < playerDice; i++) {
            playerRolls += randomizer.nextInt(6) + 1;
        }

        if (enemyRolls > playerRolls) {
            cells[attackedRow][attackedCol].setDiceNumber(enemyDice - 1);
            cells[attackedRow][attackedCol].setIsPlayer(false);
            DicePanel.setPlayerTerritories(DicePanel.getPlayerTerritories() - 1);
            playerScore.setText(String.valueOf(DicePanel.getPlayerTerritories()));
            enemyScore.setText(String.valueOf(DicePanel.getEnemyTerritories()));
        }
        cells[attackingRow][attackingCol].setDiceNumber(1);
        frame.pack();
        frame.setLocationRelativeTo(null);

        if (DicePanel.getPlayerTerritories() == 0) {
            playSound(new File("Assets/dice_lose.wav"), false);
            cells[attackingRow][attackingCol].setBackground(DicePanel.ENEMY_COLOR);
            diceBG.stop();
            new DiceEnd(false);
        }
    }

    /**
     * End the player's turn, randomly assigning new dice throughout their territories.
     * Number of dice is based on player-controlled territories.
     */
    static void endTurn() {
        int newDice = DicePanel.getPlayerTerritories();
        boolean full = false;

        while (newDice > 0 && !full) {
            full = true;
            for (int i = 0; i < DicePanel.ROWS; i++) {
                for (int j = 0; j < DicePanel.COLUMNS; j++) {
                    if (cells[i][j].getIsPlayer() && cells[i][j].getDiceNumber() < 8) {
                        int randDice = randomizer.nextInt(4);
                        full = false;
                        if (randomizer.nextInt(3) != 0) {
                            continue;
                        }

                        if (randDice > newDice) {
                            cells[i][j].setDiceNumber(cells[i][j].getDiceNumber() + newDice);
                            if (cells[i][j].getDiceNumber() > 8) {
                                cells[i][j].setDiceNumber(8);
                            }
                            newDice = 0;
                        } else {
                            if (cells[i][j].getDiceNumber() + randDice > 8) {
                                cells[i][j].setDiceNumber(8);
                                newDice -= 8 - cells[i][j].getDiceNumber();
                            } else {
                                cells[i][j].setDiceNumber(cells[i][j].getDiceNumber() + randDice);
                                newDice -= randDice;
                            }
                        }
                    }
                }
            }
        }

        // Starts the AI's turn
        aiTurn();
    }

    /**
     * End the AI's turn, randomly assigning new dice throughout their territories.
     * Number of dice is based on AI-controlled territories.
     */
    static void endTurnAI() {
        int newDice = DicePanel.getEnemyTerritories();
        boolean full = false;

        end.setEnabled(true);
        playerState = true;
        turn.setText("Player's Turn");
        turn.setForeground(DicePanel.PLAYER_COLOR);

        while (newDice > 0 && !full) {
            full = true;
            for (int i = 0; i < DicePanel.ROWS; i++) {
                for (int j = 0; j < DicePanel.COLUMNS; j++) {
                    if (!cells[i][j].getIsPlayer() && cells[i][j].getDiceNumber() < 8) {
                        int randDice = randomizer.nextInt(4);
                        full = false;
                        if (randomizer.nextInt(3) != 0) {
                            continue;
                        }

                        if (randDice > newDice) {
                            cells[i][j].setDiceNumber(cells[i][j].getDiceNumber() + newDice);
                            if (cells[i][j].getDiceNumber() > 8) {
                                cells[i][j].setDiceNumber(8);
                            }
                            newDice = 0;
                        } else {
                            if (cells[i][j].getDiceNumber() + randDice > 8) {
                                cells[i][j].setDiceNumber(8);
                                newDice -= 8 - cells[i][j].getDiceNumber();
                            } else {
                                cells[i][j].setDiceNumber(cells[i][j].getDiceNumber() + randDice);
                                newDice -= randDice;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * The AI's logic system: how the AI decides which territory to attack.
     * 
     * Current implementation: checks player-controlled neighbours, attacks lowest
     * dice number that is smaller or equal to the attacking cell's dice number.
     */
    static void aiTurn() {

        end.setEnabled(false);
        playerState = false;
        turn.setText("Computer's Turn");
        turn.setForeground(DicePanel.ENEMY_COLOR);

        SwingWorker<Object, String> worker = new SwingWorker<Object, String>() {

            @Override
            protected Object doInBackground() throws Exception {
                boolean attackableNeighbour = true;

                while (attackableNeighbour) {
                    attackableNeighbour = false;
                    for (int i = 0; i < DicePanel.ROWS; i++) {
                        for (int j = 0; j < DicePanel.COLUMNS; j++) {
                            if (!cells[i][j].getIsPlayer() && cells[i][j].getDiceNumber() > 1) {
                                int min = 9;
                                int minI = 0;
                                int minJ = 0;

                                for (int neighborI = Math.max(0, i - 1); 
                                    neighborI <= Math.min(i + 1, cells.length - 1); neighborI++) {
                                    for (int neighborJ = Math.max(0, j - 1); neighborJ
                                        <= Math.min(j + 1, cells[0].length - 1); neighborJ++) {
                                        if (!(neighborI == i && neighborJ == j)
                                            && cells[neighborI][neighborJ].getIsPlayer() 
                                            && cells[neighborI][neighborJ].getDiceNumber() < min) {
                                            min = cells[neighborI][neighborJ].getDiceNumber();
                                            minI = neighborI;
                                            minJ = neighborJ;
                                        }
                                    }
                                }

                                if (playerState) {
                                    return null;
                                }

                                if (cells[i][j].getDiceNumber() >= min) {
                                    attackableNeighbour = true;

                                    Thread.sleep(700);

                                    cells[minI][minJ].setBackground(DicePanel.LIGHT_PLAYER_COLOR);
                                    cells[i][j].setBackground(DicePanel.LIGHT_ENEMY_COLOR);

                                    Thread.sleep(1000);

                                    attackAI(i, j, minI, minJ);

                                    cells[i][j].setBackground(DicePanel.ENEMY_COLOR);
                                    if (cells[minI][minJ].getIsPlayer()) {
                                        cells[minI][minJ].setBackground(DicePanel.PLAYER_COLOR);
                                    }

                                    Thread.sleep(300);
                                }
                            }
                        }
                    }
                }

                endTurnAI();
                return null;
            }
        };

        worker.execute();
    }

    /**
     * Plays the .wav sound file given as parameter.
     * 
     * @param file  sound file (.wav)
     * @param loop  true if the sound loops, false otherwise
     * @return      clip of played sound
     */
    static Clip playSound(File file, boolean loop) {
        try {
            AudioInputStream audioInputStream = 
                AudioSystem.getAudioInputStream(file.getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start();
            }
            return clip;
        } catch (Exception ex) {
            throw new UnsupportedOperationException("Can't play audio ;-;");
        }
    }

    public static void main(String[] args) {
        new DiceWars();
    }
}