package dice_wars;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

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
        JDialog dialog = new JDialog(DiceWars.frame, true);

        dialog.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(DiceWars.frame);
        dialog.getContentPane().setBackground(new Color(230, 230, 230));

        JLabel label = new JLabel("YOU WON! :D", JLabel.CENTER);

        label.setFont(new Font("Serif", Font.BOLD, 60));
        label.setForeground(new Color(19, 145, 52));
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.5;
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(10, 10, 30, 10);
        dialog.add(label, c);

        JButton play = new JButton("Play again");

        buttonDesign(play, true);
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DiceWars.frame.dispose();
                new DiceWars();
            }
        });

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.5;
        c.gridwidth = 2;
        c.insets = new Insets(10, 100, 15, 100);
        c.anchor = GridBagConstraints.CENTER;
        dialog.add(play, c);

        JButton exit = new JButton("Exit");

        buttonDesign(exit, true);
        exit.addActionListener(new ActionListener() {
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
        c.insets = new Insets(15, 100, 10, 100);
        c.anchor = GridBagConstraints.PAGE_END;
        dialog.add(exit, c);

        dialog.setUndecorated(true);
        dialog.setVisible(true);
    }

    private void loseScreen() {
        JDialog dialog = new JDialog(DiceWars.frame, true);

        dialog.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(DiceWars.frame);
        dialog.getContentPane().setBackground(new Color(230, 230, 230));

        JLabel label = new JLabel("YOU LOST :(", JLabel.CENTER);

        label.setFont(new Font("Serif", Font.BOLD, 60));
        label.setForeground(Color.RED);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.5;
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(10, 10, 30, 10);
        dialog.add(label, c);

        JButton play = new JButton("Play again");

        buttonDesign(play, false);
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DiceWars.frame.dispose();
                new DiceWars();
            }
        });

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.5;
        c.gridwidth = 2;
        c.insets = new Insets(10, 100, 15, 100);
        c.anchor = GridBagConstraints.CENTER;
        dialog.add(play, c);

        JButton exit = new JButton("Exit");

        buttonDesign(exit, false);
        exit.addActionListener(new ActionListener() {
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
        c.insets = new Insets(15, 100, 10, 100);
        c.anchor = GridBagConstraints.PAGE_END;
        dialog.add(exit, c);

        dialog.setUndecorated(true);
        dialog.setVisible(true);
    }

    /**
     * Designs the button given as parameter according to the game's outcome.
     * 
     * @param button  the button to change the design of
     * @param win     true if the player won the game, false otherwise
     */
    protected void buttonDesign(JButton button, boolean win) {
        Color mainColor;
        if (win) {
            mainColor = new Color(24, 184, 66);
        } else {
            mainColor = Color.RED;
        }

        button.setFont(new Font("Roboto", Font.BOLD, 30));
        button.setFocusPainted(false);
        button.setBackground(Color.BLUE);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(3, 3, 3, 3, mainColor),
            new EmptyBorder(3, 3, 3, 3)));
        button.setMargin(new Insets(3, 3, 3, 3));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(mainColor);
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createCompoundBorder(
                    new MatteBorder(3, 3, 3, 3, Color.BLUE),
                    new EmptyBorder(3, 3, 3, 3)));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.BLUE);
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createCompoundBorder(
                    new MatteBorder(3, 3, 3, 3, mainColor),
                    new EmptyBorder(3, 3, 3, 3)));
            }
        });
    }
}
