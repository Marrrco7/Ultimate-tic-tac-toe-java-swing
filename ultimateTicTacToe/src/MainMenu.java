package ultimateTicTacToe;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JPanel {
    public MainMenu(GamePanel gamePanel) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JButton pvpButton = new JButton("Player vs Player");
        JButton pvcomButton = new JButton("Player vs COM");
        JButton restartButton = new JButton("Restart Game");


        Font buttonFont = new Font("Arial", Font.PLAIN, 20);
        pvpButton.setFont(buttonFont);
        pvcomButton.setFont(buttonFont);
        restartButton.setFont(buttonFont);


        Dimension buttonSize = new Dimension(200, 80);
        pvpButton.setPreferredSize(buttonSize);
        pvcomButton.setPreferredSize(buttonSize);
        restartButton.setPreferredSize(buttonSize);


        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(pvpButton, gbc);

        gbc.gridy = 1;
        add(pvcomButton, gbc);

        gbc.gridy = 2;
        add(restartButton, gbc);

        pvpButton.addActionListener(e -> gamePanel.startNewGame(false));
        pvcomButton.addActionListener(e -> gamePanel.startNewGame(true));
        restartButton.addActionListener(e -> gamePanel.restartGame());
    }
}
