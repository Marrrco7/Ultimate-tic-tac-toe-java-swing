package ultimateTicTacToe;

import javax.swing.*;

public class UltimateTicTacToe {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Ultimate Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,800);
        frame.setContentPane(new GamePanel());
        frame.setVisible(true);
    }
}
