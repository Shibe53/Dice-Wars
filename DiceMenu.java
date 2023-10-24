import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Creates the main menu of the game.
 */
public class DiceMenu extends JPanel {

    /**
     * Constructor of the DiceMenu class.
     */
    public DiceMenu() {

        this.setLayout(new GridBagLayout());

        JLabel textLabel = new JLabel("Dice Wars");
        textLabel.setFont(new Font("Stencil", Font.BOLD, 34)); 
        GridBagConstraints textConstraints = new GridBagConstraints();
        textConstraints.gridx = 0;
        textConstraints.gridy = 0;
        this.add(textLabel, textConstraints);

        try {
            BufferedImage image = ImageIO.read(new File("Assets/dice_icon.png")); 
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            GridBagConstraints imageConstraints = new GridBagConstraints();
            imageConstraints.gridx = 0;
            imageConstraints.gridy = 1;
            this.add(imageLabel, imageConstraints);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JButton vsComputer = new JButton("Vs Computer");
        vsComputer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DiceWars.gameScreen();
            }
        });

        JButton tutorial = new JButton("How to play");
        tutorial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(vsComputer, gbc);
        gbc.gridy = 3;
        this.add(tutorial, gbc);

        textLabel.setHorizontalAlignment(JLabel.CENTER);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }
}