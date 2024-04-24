package game2048;

import java.util.Random;

public class Board {

    public static int DEFAULT_ROW_NUM;
    public static int DEFAULT_COL_NUM;
    public static int DEFAULT_INITIAL_NODE;
    public static int DEFAULT_SPAWN;
    private static Square[][] board;
    private static Square[][] prevBoard;
    private static Board session;

    private Board() {
        setDefaultRowNum(4);
        setDefaultColNum(4);
        setDefaultInitialNode(2);
        setDefaultSpawn(1);
        board = new Square[DEFAULT_ROW_NUM][DEFAULT_COL_NUM];
        init();
    }

    private Board(int numRow, int numCol) {
        setDefaultRowNum(numRow);
        setDefaultColNum(numCol);
        setDefaultInitialNode(2);
        setDefaultSpawn(1);
        board = new Square[DEFAULT_ROW_NUM][DEFAULT_COL_NUM];
        init();
    }

    private Board(int numRow, int numCol, int numInitNode, int numSpawn) {
        setDefaultRowNum(numRow);
        setDefaultColNum(numCol);
        setDefaultInitialNode(numInitNode);
        setDefaultSpawn(numSpawn);
        board = new Square[DEFAULT_ROW_NUM][DEFAULT_COL_NUM];
        init();
    }

    public static Board getInstance() {
        if (session == null) {
            session = new Board();
        }
        return session;
    }

    public static Board getInstance(int numRow, int numCol) {
        if (session == null) {
            session = new Board(numRow, numCol);
        }
        return session;
    }

    public static Board getInstance(int numRow, int numCol, int numInitNode, int numSpawn) {
        if (session == null) {
            session = new Board(numRow, numCol, numInitNode, numSpawn);
        }
        return session;
    }

    private void setDefaultRowNum(int numRow) {
        DEFAULT_ROW_NUM = numRow;
    }

    private void setDefaultColNum(int numCol) {
        DEFAULT_COL_NUM = numCol;
    }

    public void setDefaultInitialNode(int numInitNode) {
        DEFAULT_INITIAL_NODE = numInitNode;
    }

    public void setDefaultSpawn(int numSpawn) {
        DEFAULT_SPAWN = numSpawn;
    }

    public static Square[][] getPrevBoard() {
        return prevBoard;
    }

    public static void setPrevBoard(Square[][] prevBoard) {
        Board.prevBoard = prevBoard;
    }

    public static Square[][] getBoard() {
        return board;
    }

    private static void init() {
        initializeBoard();
        for (int i = 0; i < DEFAULT_INITIAL_NODE; i++) {
            placeRandomNumberNode();
        }
    }

    private static void initializeBoard() {
        for (int i = 0; i < DEFAULT_ROW_NUM; i++) {
            for (int j = 0; j < DEFAULT_COL_NUM; j++) {
                board[i][j] = new Square(i, j);
            }
        }
    }

    protected static void placeRandomNumberNode() {
        Random random = new Random();
        NumberNode node = new NumberNode();
        node.setRandomInitialValue();

        for (int i = 0; i < DEFAULT_ROW_NUM; i++) {
            for (int j = 0; j < DEFAULT_COL_NUM; j++) {
                if (!board[i][j].isOccupied()) {
                    int row;
                    int col;
                    do {
                        row = random.nextInt(DEFAULT_ROW_NUM);
                        col = random.nextInt(DEFAULT_COL_NUM);
                    } while (board[row][col].isOccupied());

                    board[row][col].setNumberNode(node);
                    return;
                }
            }
        }
    }

    private void moveMergeHelper(boolean moveOrMerge, int startRow, int startCol, int deltaRow, int deltaCol) {
        for (int row = startRow; (startRow == 0 ? row < DEFAULT_ROW_NUM : row >= 0); row += (startRow == 0 ? 1 : -1)) {
            for (int col = startCol; (startCol == 0 ? col < DEFAULT_COL_NUM : col >= 0); col += (startCol == 0 ? 1 : -1)) {
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

    protected void moveMergeUp(boolean moveOrMerge) {
        moveMergeHelper(moveOrMerge, 0, 0, -1, 0);
    }

    protected void moveMergeDown(boolean moveOrMerge) {
        moveMergeHelper(moveOrMerge, DEFAULT_ROW_NUM - 1, 0, 1, 0);
    }

    protected void moveMergeRight(boolean moveOrMerge) {
        moveMergeHelper(moveOrMerge, 0, DEFAULT_COL_NUM - 1, 0, 1);
    }

    protected void moveMergeLeft(boolean moveOrMerge) {
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

    protected void nullifyAllRecentlyMerged() {
        for (int i = 0; i < DEFAULT_ROW_NUM; i++) {
            for (int j = 0; j < DEFAULT_COL_NUM; j++) {
                board[i][j].nullifyRecentlyMerged();
            }
        }
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < DEFAULT_ROW_NUM && col >= 0 && col < DEFAULT_COL_NUM;
    }

    protected static Square[][] copyBoard(Square[][] originalMatrix) {
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

    protected static boolean areBoardsEqual(Square[][] board1, Square[][] board2) {
        if (board1.length != board2.length || board1[0].length != board2[0].length) {
            return false;
        }
        for (int i = 0; i < board1.length; i++) {
            for (int j = 0; j < board1[0].length; j++) {
                boolean isOccupied1 = board1[i][j].isOccupied();
                boolean isOccupied2 = board2[i][j].isOccupied();

                if (isOccupied1 && isOccupied2) {
                    NumberNode numberNode1 = board1[i][j].getNumberNode();
                    NumberNode numberNode2 = board2[i][j].getNumberNode();

                    if (numberNode1 != null && numberNode2 != null && numberNode1.getValue() != numberNode2.getValue()) {
                        return false;
                    }
                } else if (isOccupied1 != isOccupied2) {
                    return false;
                }
            }
        }
        return true;
    }

}