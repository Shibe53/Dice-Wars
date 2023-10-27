package dice_wars;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;

/**
 * Creates the main menu of the game.
 */
public class DiceMenu extends JPanel {

    /**
     * Constructor of the DiceMenu class.
     */
    public DiceMenu() {
        this.setLayout(new GridBagLayout());

        try {
            BufferedImage image = ImageIO.read(new File("Assets/dice_logo.png"));
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            GridBagConstraints imageConstraints = new GridBagConstraints();
            imageConstraints.gridx = 0;
            imageConstraints.gridy = 1;
            this.add(imageLabel, imageConstraints);
        } catch (IOException e) {
            throw new UnsupportedOperationException("Can't show image ;-;");
        }

        JButton vsComputerButton = createButton("Vs. Computer", "Assets/dice_vscomputer.jpeg");
        JButton howToButton = createButton("How to play", "Assets/dice_howto.jpeg");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(vsComputerButton, gbc);
        gbc.gridy = 3;
        this.add(howToButton, gbc);

        howToButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(DiceWars.frame, howtoplay, 
                    "How To Play Dice Wars", JOptionPane.PLAIN_MESSAGE);
            }
        });

        vsComputerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DiceWars.gameScreen();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        GradientPaint gradient = new GradientPaint(0, 0, new Color(120, 0, 120), 
            400, 500, new Color(0, 120, 0));
        ((Graphics2D) g).setPaint(gradient);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Create the menu buttons.
     * 
     * @param text         text on button
     * @param imageName    icon to show on button
     * @return             resulting JButton
     */
    private JButton createButton(String text, String imageName) {
        JButton button = new JButton(text);

        button.setPreferredSize(new Dimension(200, 60));
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setFocusPainted(false);
        button.setBackground(DicePanel.PLAYER_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorder(new MatteBorder(3, 3, 3, 3, DicePanel.LIGHT_ENEMY_COLOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.WHITE);
                button.setForeground(Color.BLACK);
                button.setPreferredSize(new Dimension(230, 60));
                try {
                    BufferedImage image = ImageIO.read(new File(imageName));
                    button.setIcon(new ImageIcon(image));
                } catch (IOException ex) {
                    throw new UnsupportedOperationException("Can't show image ;-;");
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(DicePanel.PLAYER_COLOR);
                button.setBorder(new MatteBorder(3, 3, 3, 3, DicePanel.LIGHT_ENEMY_COLOR));
                button.setForeground(Color.WHITE);
                button.setPreferredSize(new Dimension(200, 60));
                button.setIcon(null);
            }
        });

        return button;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }

    String howtoplay = "Press the \"Vs. Computer\" button to play the game against an AI. \n"
            + "A map will be generated randomly, shown in a grid-like pattern.\n"
            + "The scores on the left and right represent the number of territories of each side.\n"
            + "The player-controlled territories are shown in purple, \n"
            + "while the computer-controlled ones are green. Before the game starts, \n"
            + "the player can reroll the map by clicking on the button at the top of the screen.\n"
            + "\n"
            + "Each cell of the grid has a number assigned to it. These numbers symbolize dice.\n"
            + "When a territory (A) attacks an opposing territory (B),\n"
            + "a number of dice equal to the number on each of those territories get rolled.\n"
            + "The sum of the rolled dice get compared against each other.\n"
            + "If the value of territory A is higher than that of territory B, then the latter is\n"
            + "captured by A and every die except one moves over to the newly captured territory.\n"
            + "If the value is equal or lower, then territory A loses all of its dice except one.\n"
            + "A territory can only attack if it has more than one die.\n"
            + "\n"
            + "The game consists of turns that alternate between the player and the computer.\n"
            + "The first turn is chosen randomly. On their turn, the\n"
            + "player can chose any territory and attack a neighbouring enemy territory.\n"
            + "If the same cell is clicked again, the attack state can be cancelled.\n"
            + "At any point, the player can end their turn. \n"
            + "Then the computer will take its turn and so on until one side wins by controlling\n"
            + "every single territory. At the end of every turn, a number of dice equal to the\n"
            + "number of territories controlled by that whoever ended their turn\n"
            + "are randomly assigned to their controlled territories.";
}