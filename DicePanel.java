import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.MatteBorder;

public class DicePanel extends JPanel {

    static final int ROWS = 6;
    static final int COLUMNS = 6;
    
    static int playerTerritories = 0;
    static int enemyTerritories = 0;
    static CellPanel[][] cells = new CellPanel[ROWS][COLUMNS];
    Random randomizer = new Random();
    GridBagConstraints gbc = new GridBagConstraints();

    public DicePanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.GRAY);
        generateMap();

        DiceWars.reroll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateMap();
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

class CellPanel extends JPanel {

    private Color defaultBackground;
    private boolean isPlayer;
    private int dice = 0;
    private int row;
    private int col;

    MyListener mouse = new MyListener();
    JLabel number = new JLabel(String.valueOf(dice), JLabel.CENTER);

    public CellPanel(int r, int c) {
        row = r;
        col = c;
        setLayout(new BorderLayout());

        number.setFont(new Font("Serif", Font.BOLD, 34));
        this.add(number, BorderLayout.CENTER);

        addMouseListener(mouse);
    }

    public void setDiceNumber(int value) {
        dice = value;
        number.setText(String.valueOf(dice));
    }

    public int getDiceNumber() {
        return dice;
    }

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
                } else if (!isPlayer && DiceWars.getAttackState() && DiceWars.canAttack(row, col)) {
                    DiceWars.attack(row, col);
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (DiceWars.getGameStarted()) {
                defaultBackground = getBackground();
                if (isPlayer && !DiceWars.getAttackState() && dice > 1) {
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
