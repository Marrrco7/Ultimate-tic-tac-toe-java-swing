package ultimateTicTacToe;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class GamePanel extends JPanel {
    private Board board;
    private MainMenu mainMenu;

    public GamePanel() {
        setLayout(new BorderLayout());

        mainMenu = new MainMenu(this);
        add(mainMenu, BorderLayout.WEST);

        board = new Board();
        add(board, BorderLayout.CENTER);
    }

    public void startNewGame(boolean isPlayerVsCom) {
        remove(board);
        board = new Board(isPlayerVsCom);
        add(board, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void saveGame() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("gameState.ser"))) {
            out.writeObject(board);
            JOptionPane.showMessageDialog(this, "Game saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving game.");
        }
    }

    public void loadGame() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("gameState.ser"))) {
            remove(board);
            board = (Board) in.readObject();
            add(board, BorderLayout.CENTER);
            revalidate();
            repaint();
            JOptionPane.showMessageDialog(this, "Game loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading game.");
        }
    }

    public void restartGame() {
        startNewGame(false);
    }

}
