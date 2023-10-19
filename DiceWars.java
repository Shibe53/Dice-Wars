import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.Math;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class DiceWars {

    static JFrame frame = new JFrame("Dice Wars");
    static ImageIcon icon = new ImageIcon("icon.png");
    DicePanel panel = new DicePanel();
    static JButton end = new JButton("Play");
    static JButton reroll = new JButton("Reroll map");
    static JLabel playerScore = new JLabel("0", JLabel.CENTER);
    static JLabel enemyScore = new JLabel("0", JLabel.CENTER);

    static Random randomizer = new Random();
    static boolean attackingState = false;
    static boolean gameStarted = false;
    static int attackingRow = 0;
    static int attackingCol = 0;
    static CellPanel[][] cells;

    public DiceWars() {

        SwingUtilities.invokeLater(() -> {
            panel.setBorder(new MatteBorder(6, 6, 6, 6, Color.BLACK));

            playerScore.setFont(new Font("Roboto", Font.BOLD, 64));
            playerScore.setBorder(new EmptyBorder(7, 7, 7, 7));
            playerScore.setForeground(new Color(100, 50, 168));
            playerScore.setText(String.valueOf(DicePanel.getPlayerTerritories()));

            enemyScore.setFont(new Font("Roboto", Font.BOLD, 64));
            enemyScore.setBorder(new EmptyBorder(7, 7, 7, 7));
            enemyScore.setForeground(new Color(61, 138, 30));
            enemyScore.setText(String.valueOf(DicePanel.getEnemyTerritories()));

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
                    } else {
                        gameStarted = true;
                        end.setText("END TURN");
                        frame.remove(reroll);
                        frame.pack();
                        frame.setLocationRelativeTo(null);
                        if (randomizer.nextInt(2) == 0) {
                            aiTurn();
                        }
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

            frame.setIconImage(icon.getImage());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(panel);
            frame.add(reroll, BorderLayout.NORTH);
            frame.add(end, BorderLayout.SOUTH);
            frame.add(playerScore, BorderLayout.WEST);
            frame.add(enemyScore, BorderLayout.EAST);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);

            cells = panel.getCellMatrix();
        });
    }

    public static boolean getGameStarted() {
        return gameStarted;
    }

    public static void setAttackState(boolean state, int row, int col) {
        attackingState = state;
        attackingCol = col;
        attackingRow = row;
    }

    public static boolean getAttackState() {
        return attackingState;
    }

    public static boolean canAttack(int enemyRow, int enemyCol) {
        if (Math.abs(attackingCol - enemyCol) <= 1 && Math.abs(attackingRow - enemyRow) <= 1) {
            return true;
        }
        return false;
    }

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
            win();
        }
    }

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
            if (cells[attackedRow][attackedCol].getDiceNumber() != 1) {
                cells[attackedRow][attackedCol].setDiceNumber(enemyDice - 1);
            }
            cells[attackedRow][attackedCol].setIsPlayer(false);
            DicePanel.setPlayerTerritories(DicePanel.getPlayerTerritories() - 1);
            playerScore.setText(String.valueOf(DicePanel.getPlayerTerritories()));
            enemyScore.setText(String.valueOf(DicePanel.getEnemyTerritories()));
        }
        cells[attackingRow][attackingCol].setDiceNumber(1);
        //cells[attackingRow][attackingCol].number.setForeground(Color.WHITE);
        attackingState = false;
        frame.pack();
        frame.setLocationRelativeTo(null);
        if (DicePanel.getPlayerTerritories() == 0) {
            lose();
        }
    }

    public void endTurn() {
        int newDice = DicePanel.getPlayerTerritories();
        boolean full = false;
        end.setEnabled(false);

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

        aiTurn();
    }

    public void endTurnAI() {
        int newDice = DicePanel.getEnemyTerritories();
        boolean full = false;
        end.setEnabled(true);

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

    public void aiTurn() {

        boolean attackableNeighbour = true;

        while (attackableNeighbour) {
            attackableNeighbour = false;
            for (int i = 0; i < DicePanel.ROWS; i++) {
                for (int j = 0; j < DicePanel.COLUMNS; j++) {
                    if (!cells[i][j].getIsPlayer()) {
                        int min = 9;
                        int minI = 0;
                        int minJ = 0;

                        for (int neighbourI = Math.max(0, i - 1); 
                            neighbourI <= Math.min(i + 1, cells.length - 1); neighbourI++) {
                            for (int neighbourJ = Math.max(0, j - 1); 
                                neighbourJ <= Math.min(j + 1, cells[0].length - 1); neighbourJ++) {
                                if (neighbourI != i && neighbourJ != j
                                    && cells[neighbourI][neighbourJ].getIsPlayer() 
                                    && cells[neighbourI][neighbourJ].getDiceNumber() < min) {
                                    min = cells[neighbourI][neighbourJ].getDiceNumber();
                                    minI = neighbourI;
                                    minJ = neighbourJ;
                                }
                            }
                        }

                        if (cells[i][j].getDiceNumber() >= min) {
                            attackableNeighbour = true;
                            cells[minI][minJ].setBackground(new Color(119, 82, 168));
                            cells[i][j].setBackground(new Color(121, 204, 88));

                            // TODO: swing timer thingy ~1000ms

                            attackAI(i, j, minI, minJ);
                            cells[i][j].setBackground(new Color(61, 153, 49));
                            if (cells[minI][minJ].getIsPlayer()) {
                                cells[minI][minJ].setBackground(new Color(100, 50, 168));
                            }

                            // TODO: swing timer thingy ~1000ms
                        }
                    }
                }
            }
        }

        endTurnAI();
    }

    static void win() {
        System.out.println("You won :)");
    }

    static void lose() {
        System.out.println("You lost :(");
    }

    public static void main(String[] args) {
        new DiceWars();
    }
}