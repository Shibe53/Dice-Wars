import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.MatteBorder;

/**
 * Class for the main panel of the application: the game area.
 */
public class DicePanel extends JPanel {

    static final int ROWS = 6;
    static final int COLUMNS = 6;
    
    static int playerTerritories = 0;
    static int enemyTerritories = 0;
    static CellPanel[][] cells = new CellPanel[ROWS][COLUMNS];
    Random randomizer = new Random();
    GridBagConstraints gbc = new GridBagConstraints();

    /**
     * Constructor for the DicePanel class.
     */
    public DicePanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.GRAY);
        generateMap();

        DiceWars.reroll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateMap();
                DiceWars.playSound(new File("Assets/dice_roll.wav"), false);
                DiceWars.playerScore.setText(String.valueOf(DicePanel.getPlayerTerritories()));
                DiceWars.enemyScore.setText(String.valueOf(DicePanel.getEnemyTerritories()));
            }
        });
    }

    CellPanel[][] getCellMatrix() {
        return cells;
    }

    static void setPlayerTerritories(int value) {
        playerTerritories = value;
    }
    
    static int getPlayerTerritories() {
        return playerTerritories;
    }

    static void setEnemyTerritories(int value) {
        enemyTerritories = value;
    }

    static int getEnemyTerritories() {
        return enemyTerritories;
    }

    /**
     * Generates a grid made of CellPanels the represent territories.
     * 
     * The territories are randomly (and fairly) assigned to either the player or the opponent.
     * Each territory has a random number of dice, from 1 to 4.
     */
    public void generateMap() {
        playerTerritories = 0;
        enemyTerritories = 0;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (cells[row][col] != null) {
                    this.remove(cells[row][col]);
                }
                cells[row][col] = new CellPanel(row, col);
                cells[row][col].setBorder(new MatteBorder(2, 2, 2, 2, Color.BLACK));
                gbc.gridx = col;
                gbc.gridy = row;
                this.add(cells[row][col], gbc);

                int randTerritory = randomizer.nextInt(2);
                if (playerTerritories - enemyTerritories > 3
                    || (enemyTerritories - playerTerritories <= 3 && randTerritory == 0)) {
                    cells[row][col].setIsPlayer(false);
                } else if (enemyTerritories - playerTerritories > 3
                    || (playerTerritories - enemyTerritories <= 3 && randTerritory == 1)) {
                    cells[row][col].setIsPlayer(true);
                }

                int diceNr = randomizer.nextInt(4) + 1;
                cells[row][col].setDiceNumber(diceNr);
            }
        }
    }
}

/**
 * Panel for every cell in the grid.
 */
class CellPanel extends JPanel {

    private Color defaultBackground;
    private boolean isPlayer;
    private int dice = 0;
    private int row;
    private int col;

    MyListener mouse = new MyListener();
    JLabel number = new JLabel(String.valueOf(dice), JLabel.CENTER);

    /**
     * Constructor for CellPanel.
     * 
     * @param r  row of the cell in the grid
     * @param c  column of the cell in the grid
     */
    public CellPanel(int r, int c) {
        row = r;
        col = c;
        setLayout(new BorderLayout());

        number.setFont(new Font("Serif", Font.BOLD, 34));
        this.add(number, BorderLayout.CENTER);

        addMouseListener(mouse);
    }

    /**
     * Changes the dice value of the current CellPanel.
     * 
     * @param value  new dice number
     */
    public void setDiceNumber(int value) {
        dice = value;
        number.setText(String.valueOf(dice));
    }

    public int getDiceNumber() {
        return dice;
    }

    /**
     * Changes if the territory belongs to the player or the opponent.
     * 
     * @param is  true if it belongs to the player, false if to the opponent
     */
    public void setIsPlayer(boolean is) {
        isPlayer = is;
        if (isPlayer) {
            number.setForeground(Color.WHITE);
            setBackground(new Color(100, 50, 168));
            defaultBackground = new Color(100, 50, 168);
            DicePanel.setPlayerTerritories(DicePanel.getPlayerTerritories() + 1);

        } else {
            number.setForeground(Color.BLACK);
            setBackground(new Color(61, 153, 49));
            defaultBackground = new Color(61, 153, 49);
            DicePanel.setEnemyTerritories(DicePanel.getEnemyTerritories() + 1);
        }
    }

    public boolean getIsPlayer() {
        return isPlayer;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 100);
    }

    class MyListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (DiceWars.getGameStarted()) {
                if (isPlayer && !DiceWars.getAttackState() && dice > 1) {
                    DiceWars.setAttackState(true, row, col);
                    number.setForeground(Color.PINK);
                    DiceWars.playSound(new File("Assets/cell_click.wav"), false);
                } else if (!isPlayer && DiceWars.getAttackState() && DiceWars.canAttack(row, col)) {
                    DiceWars.attack(row, col);
                    DiceWars.playSound(new File("Assets/cell_click.wav"), false);
                } else if (DiceWars.getAttackState() && row == DiceWars.attackingRow 
                    && col == DiceWars.attackingCol) {
                    DiceWars.setAttackState(false);
                    number.setForeground(Color.WHITE);
                    DiceWars.playSound(new File("Assets/cell_click.wav"), false);
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (DiceWars.getGameStarted()) {
                defaultBackground = getBackground();
                if ((isPlayer && !DiceWars.getAttackState() && dice > 1) 
                    || (DiceWars.getAttackState() && row == DiceWars.attackingRow 
                    && col == DiceWars.attackingCol)) {
                    setBackground(new Color(119, 82, 168));
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else if (!isPlayer && DiceWars.getAttackState() && DiceWars.canAttack(row, col)) {
                    setBackground(new Color(121, 204, 88));
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (DiceWars.getGameStarted()) {
                setBackground(defaultBackground);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }
}
