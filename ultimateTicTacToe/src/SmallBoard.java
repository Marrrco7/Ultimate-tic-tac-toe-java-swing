package ultimateTicTacToe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SmallBoard extends JPanel {
    private JButton[][] buttons = new JButton[3][3];
    private char[][] board = new char[3][3];
    private char winner = ' ';
    private Board parentBoard;
    private int smallBoardRow, smallBoardCol;

    private final ImageIcon xIcon = new ImageIcon("x.png");
    private final ImageIcon oIcon = new ImageIcon("circle.png");
    private final ImageIcon bigXIcon = new ImageIcon("BiggerX.png");
    private final ImageIcon bigOIcon = new ImageIcon("biggerCircle.png");

    public SmallBoard(Board parentBoard, int smallBoardRow, int smallBoardCol) {
        this.parentBoard = parentBoard;
        this.smallBoardRow = smallBoardRow;
        this.smallBoardCol = smallBoardCol;
        setLayout(new GridLayout(3, 3));
        setBackground(Color.DARK_GRAY); 
        initializeButtons();
    }

    private void initializeButtons() {
        Color buttonColor = Color.LIGHT_GRAY;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col] = new JButton();
                buttons[row][col].setBackground(buttonColor);
                buttons[row][col].setPreferredSize(new Dimension(50, 50));
                buttons[row][col].setFont(new Font("Arial", Font.PLAIN, 20));
                buttons[row][col].addActionListener(new ButtonClickListener(row, col));


                int top = row == 0 ? 3 : 1;
                int left = col == 0 ? 3 : 1;
                int bottom = row == 2 ? 3 : 1;
                int right = col == 2 ? 3 : 1;
                buttons[row][col].setBorder(new CustomBorder(top, left, bottom, right));

                add(buttons[row][col]);
                board[row][col] = ' ';
            }
        }
    }

    private boolean checkWin(char player) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true;
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true;
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) return true;
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) return true;
        return false;
    }

    public boolean hasWinner() {
        return winner != ' ';
    }

    public boolean isFull() {
        return isBoardFull();
    }

    private boolean isBoardFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == ' ') return false;
            }
        }
        return true;
    }

    public JButton getButton(int row, int col) {
        return buttons[row][col];
    }

    public char[][] getBoardState() {
        return board;
    }

    private void markBoardAsWon(char player) {
        removeAll();
        setLayout(new BorderLayout());
        JLabel winnerLabel = new JLabel();
        winnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        winnerLabel.setVerticalAlignment(SwingConstants.CENTER);
        winnerLabel.setIcon(player == 'X' ? bigXIcon : bigOIcon);
        add(winnerLabel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private class ButtonClickListener implements ActionListener {
        private int row, col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton buttonClicked = (JButton) e.getSource();
            if (buttonClicked.getIcon() == null && winner == ' ' && !isFull()) {
                if (!parentBoard.canPlayInAnyBoard() && !parentBoard.canPlayInBoard(smallBoardRow, smallBoardCol)) {
                    System.out.println("Invalid move: not allowed in this board");
                    return;
                }

                char currentPlayer = parentBoard.getCurrentPlayer();
                buttonClicked.setIcon(currentPlayer == 'X' ? xIcon : oIcon);
                board[row][col] = currentPlayer;

                if (checkWin(currentPlayer)) {
                    winner = currentPlayer;
                    markBoardAsWon(currentPlayer);
                    parentBoard.updateMainBoard(smallBoardRow * 3 + row, smallBoardCol * 3 + col, currentPlayer);
                } else if (isBoardFull()) {
                    parentBoard.updateMainBoard(smallBoardRow * 3 + row, smallBoardCol * 3 + col, 'D');
                }

                parentBoard.setNextSmallBoard(row, col);
                if (parentBoard.SmallBoardWon(row, col) || parentBoard.SmallBoardFull(row, col)) {
                    parentBoard.setNextSmallBoard(-1, -1);
                }
                parentBoard.switchPlayer();
            }
        }
    }
}
