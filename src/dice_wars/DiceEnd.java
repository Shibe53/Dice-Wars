package dice_wars;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Shows the victory or defeat screen (accordingly).
 */
public class DiceEnd {

    /**
     * Constructor of class DiceEnd.
     * @param win  true if the player won, false if the opponent won
     */
    public DiceEnd(boolean win) {
        if (win) {
            winScreen();
        } else {
            loseScreen();
        }
    }

    private void winScreen() {

    }

    private void loseScreen() {
        JDialog dialog = new JDialog(DiceWars.frame, true);

        dialog.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(DiceWars.frame);
        dialog.setBackground(Color.BLACK);

        JLabel label = new JLabel("YOU LOST", JLabel.CENTER);

        label.setFont(new Font("Serif", Font.BOLD, 56));
        label.setForeground(Color.RED);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.5;
        c.anchor = GridBagConstraints.PAGE_START;
        dialog.add(label, c);

        JButton play = new JButton("Play again");

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.5;
        c.gridwidth = 2;
        c.insets = new Insets(10, 100, 10, 100);
        c.anchor = GridBagConstraints.CENTER;
        dialog.add(play, c);

        JButton exit = new JButton("Exit");

        exit.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DiceWars.frame.dispose();
            }
        });

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.5;
        c.gridwidth = 2;
        c.insets = new Insets(10, 100, 10, 100);
        c.anchor = GridBagConstraints.PAGE_END;
        dialog.add(exit, c);

        dialog.setUndecorated(true);
        dialog.setVisible(true);
    }
}
