package ultimateTicTacToe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends JPanel {
    private SmallBoard[][] smallBoards = new SmallBoard[3][3];
    private char[][] mainBoard = new char[3][3];
    private char currentPlayer = 'X';
    private boolean canPlayAnywhere = false;
    private int nextSmallBoardRow = -1;
    private int nextSmallBoardCol = -1;

    private Player currentPlayerType;
    private Player xPlayerType;
    private Player oPlayerType;

    public Board() {
        this(false);
    }

    public Board(boolean isPlayerVsCom) {
        setLayout(new GridLayout(3, 3));
        initializeSmallBoards();

        if (isPlayerVsCom) {
            xPlayerType = Player.HUMAN;
            oPlayerType = Player.COMPUTER;
        } else {
            xPlayerType = Player.HUMAN;
            oPlayerType = Player.HUMAN;
        }

        currentPlayerType = xPlayerType;
    }

    private void initializeSmallBoards() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                smallBoards[row][col] = new SmallBoard(this, row, col);
                add(smallBoards[row][col]);
                mainBoard[row][col] = ' ';
            }
        }
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        currentPlayerType = (currentPlayer == 'X') ? xPlayerType : oPlayerType;
        handleMove();
    }

    public boolean canPlayInAnyBoard() {
        return canPlayAnywhere;
    }

    public boolean canPlayInBoard(int row, int col) {
        if (canPlayAnywhere) return true;
        if (nextSmallBoardRow == -1 && nextSmallBoardCol == -1) return true;
        if (nextSmallBoardRow == row && nextSmallBoardCol == col) {
            SmallBoard nextBoard = smallBoards[row][col];
            if (nextBoard.hasWinner() || nextBoard.isFull()) {
                canPlayAnywhere = true;
                return false;
            }
            return true;
        }
        return false;
    }

    public void setNextSmallBoard(int row, int col) {
        nextSmallBoardRow = row;
        nextSmallBoardCol = col;
        canPlayAnywhere = row == -1 && col == -1;
    }

    public void updateMainBoard(int row, int col, char player) {
        int mainRow = row / 3;
        int mainCol = col / 3;
        mainBoard[mainRow][mainCol] = player;
        if (checkWin(mainBoard, player)) {
            JOptionPane.showMessageDialog(this, "Player " + player + " wins the game!!!");
        } else if (isBoardFull(mainBoard)) {
            JOptionPane.showMessageDialog(this, "The game is a draw!");
        }
    }

    private boolean checkWin(char[][] board, char player) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true;
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true;
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) return true;
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) return true;
        return false;
    }

    private boolean isBoardFull(char[][] board) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == ' ') return false;
            }
        }
        return true;
    }

    private void makeComputerMove() {
        if (canPlayAnywhere) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (!smallBoards[i][j].hasWinner() && !smallBoards[i][j].isFull()) {
                        int[] bestMove = minimax(smallBoards[i][j].getBoardState(), 0, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
                        smallBoards[i][j].getButton(bestMove[1], bestMove[2]).doClick();
                        return;
                    }
                }
            }
        } else {
            SmallBoard chosenBoard = smallBoards[nextSmallBoardRow][nextSmallBoardCol];
            int[] bestMove = minimax(chosenBoard.getBoardState(), 0, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
            chosenBoard.getButton(bestMove[1], bestMove[2]).doClick();
        }
    }

    private int[] minimax(char[][] board, int depth, boolean isMaximizing, int alpha, int beta) {
        if (checkWin(board, 'X')) return new int[]{-10 + depth, -1, -1};
        if (checkWin(board, 'O')) return new int[]{10 - depth, -1, -1};
        if (isBoardFull(board)) return new int[]{0, -1, -1};

        int bestValue;
        int[] bestMove = new int[]{-1, -1, -1};

        if (isMaximizing) {
            bestValue = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = 'O';
                        int[] currentMove = minimax(board, depth + 1, false, alpha, beta);
                        board[i][j] = ' ';
                        currentMove[1] = i;
                        currentMove[2] = j;
                        if (currentMove[0] > bestValue) {
                            bestValue = currentMove[0];
                            bestMove = currentMove;
                        }
                        alpha = Math.max(alpha, bestValue);
                        if (beta <= alpha) break;
                    }
                }
            }
        } else {
            bestValue = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = 'X';
                        int[] currentMove = minimax(board, depth + 1, true, alpha, beta);
                        board[i][j] = ' ';
                        currentMove[1] = i;
                        currentMove[2] = j;
                        if (currentMove[0] < bestValue) {
                            bestValue = currentMove[0];
                            bestMove = currentMove;
                        }
                        beta = Math.min(beta, bestValue);
                        if (beta <= alpha) break;
                    }
                }
            }
        }
        return bestMove;
    }

    public void handleMove() {
        if (currentPlayerType == Player.COMPUTER) {
            Timer timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    makeComputerMove();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    public boolean SmallBoardWon(int row, int col) {
        return smallBoards[row][col].hasWinner();
    }

    public boolean SmallBoardFull(int row, int col) {
        return smallBoards[row][col].isFull();
    }
}
