package game2048;

import java.util.Random;

public class Board {

    private static final int DEFAULT_WIDTH = 4;
    private static final int DEFAULT_LENGTH = 4;
    private static final int DEFAULT_INITIAL_NODE = 2;
    private static Square[][] board = new Square[DEFAULT_LENGTH][DEFAULT_WIDTH];
    //    private static Square[][] prevBoard;
    private static Board session;

    private Board() {
        init();
    }

    public static Board getInstance() {
        if (session == null) {
            session = new Board();
        }
        return session;
    }

    private static void init() {
        initializeBoard();
        for (int i = 0; i < DEFAULT_INITIAL_NODE; i++) {
            placeRandomNumberNode();
        }
    }

    private static void initializeBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = new Square(i, j);
            }
        }
    }

    private static boolean placeRandomNumberNode() {
        Random random = new Random();
        NumberNode node = new NumberNode();
        node.setRandomInitialValue();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (!board[i][j].isOccupied()) {
                    int row;
                    int col;
                    do {
                        row = random.nextInt(4);
                        col = random.nextInt(4);
                    } while (board[row][col].isOccupied());

                    board[row][col].setNumberNode(node);
                    return true;
                }
            }
        }
        return false;
    }

    public void display() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.printf("[%4s] ", (board[i][j].isOccupied() ?
                        String.valueOf(board[i][j].getNumberNode().getValue()) : " "));
            }
            System.out.println("\n");
        }
    }

    public void move(String direction) {
        switch (direction) {
            case "w":
                System.out.println("Moving up");
                moveMergeUp(true);
                moveMergeUp(false);
                moveMergeUp(true);
                break;
            case "s":
                System.out.println("Moving down");
                moveMergeDown(true);
                moveMergeDown(false);
                moveMergeDown(true);
                break;
            case "d":
                System.out.println("Moving right");
                moveMergeRight(true);
                moveMergeRight(false);
                moveMergeRight(true);
                break;
            case "a":
                System.out.println("Moving left");
                moveMergeLeft(true);
                moveMergeLeft(false);
                moveMergeLeft(true);
        }
        nullifyAllRecentlyMerged();
        placeRandomNumberNode();
    }

    private void moveMergeHelper(boolean moveOrMerge, int startRow, int startCol, int deltaRow, int deltaCol) {
        for (int row = startRow; (startRow == 0 ? row < 4 : row >= 0); row += (startRow == 0 ? 1 : -1)) {
            for (int col = startCol; (startCol == 0 ? col < 4 : col >= 0); col += (startCol == 0 ? 1 : -1)) {
                Square square = board[row][col];
                if (square.isOccupied()) {
                    NumberNode currentNode = square.getNumberNode();
                    if (moveOrMerge) moveNode(row, col, currentNode, deltaRow, deltaCol);
                    else if (!square.getNumberNode().isRecentlyMerged())
                        mergeNode(row, col, currentNode, deltaRow, deltaCol);
                }
            }
        }
    }

    private void moveMergeUp(boolean moveOrMerge) {
        moveMergeHelper(moveOrMerge, 0, 0, -1, 0);
    }

    private void moveMergeDown(boolean moveOrMerge) {
        moveMergeHelper(moveOrMerge, 3, 0, 1, 0);
    }

    private void moveMergeRight(boolean moveOrMerge) {
        moveMergeHelper(moveOrMerge, 0, 3, 0, 1);
    }

    private void moveMergeLeft(boolean moveOrMerge) {
        moveMergeHelper(moveOrMerge, 0, 0, 0, -1);
    }

    private void moveNode(int row, int col, NumberNode currentNode, int deltaRow, int deltaCol) {
        int newRow = row + deltaRow;
        int newCol = col + deltaCol;
        while (isValidPosition(newRow, newCol) && !board[newRow][newCol].isOccupied()) {
            board[newRow - deltaRow][newCol - deltaCol].setNumberNode(null);
            board[newRow][newCol].setNumberNode(currentNode);
            newRow += deltaRow;
            newCol += deltaCol;
        }
    }

    private void mergeNode(int row, int col, NumberNode currentNode, int deltaRow, int deltaCol) {
        int nextRow = row + deltaRow;
        int nextCol = col + deltaCol;
        if (isValidPosition(nextRow, nextCol) && board[nextRow][nextCol].isOccupied()) {
            NumberNode nextNode = board[nextRow][nextCol].getNumberNode();
            if (currentNode.compareTo(nextNode) == 0) {
                board[nextRow][nextCol].setNumberNode(nextNode.mergeWith(currentNode));
                board[nextRow][nextCol].getNumberNode().setRecentlyMerged(true);
                board[row][col].setNumberNode(null);
            }
        }
    }

    private static void nullifyAllRecentlyMerged() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j].nullifyRecentlyMerged();
            }
        }
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 4 && col >= 0 && col < 4;
    }

    private static Square[][] copyBoard(Square[][] originalMatrix) {
        int numRows = originalMatrix.length;
        int numCols = originalMatrix[0].length;
        Square[][] copiedMatrix = new Square[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                copiedMatrix[i][j] = new Square(originalMatrix[i][j].getNumberNode(), i, j);
            }
        }
        return copiedMatrix;
    }

    private boolean areBoardsEqual(Square[][] board1, Square[][] board2) {
        if (board1.length != board2.length || board1[0].length != board2[0].length) {
            return false;
        }
        for (int i = 0; i < board1.length; i++) {
            for (int j = 0; j < board1[0].length; j++) {
                if ((board1[i][j].isOccupied() && board2[i][j].isOccupied()) &&
                        board1[i][j].getNumberNode().getValue() !=
                                board2[i][j].getNumberNode().getValue()) {
                    return false;
                }
            }
        }
        return true;
    }

}