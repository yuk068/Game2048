package game2048;

import java.util.*;

public class Board {

    private static final int DEFAULT_ROW_NUM = 4;
    private static final int DEFAULT_COL_NUM = 4;
    private static final int DEFAULT_INITIAL_SPAWN = 2;
    private static final int DEFAULT_NUM_SPAWN = 1;

    protected static int rowNum;
    protected static int rowCol;
    protected static int initialSpawn;
    protected static int numSpawn;
    protected static Tile[][] board;
    protected static Tile[][] prevBoard;
    protected static Stack<Tile[][]> gameState = new Stack<>();
    protected static Stack<Integer> action = new Stack<>(); // 0 is moving, 1 is shuffling
    protected static Board session;

    private Board() {
        setRowNum(DEFAULT_ROW_NUM);
        setColNum(DEFAULT_COL_NUM);
        setInitialSpawn(DEFAULT_INITIAL_SPAWN);
        setNumSpawn(DEFAULT_NUM_SPAWN);
        board = new Tile[rowNum][rowCol];
        init();
    }

    private Board(int numRow, int numCol) {
        setRowNum(numRow);
        setColNum(numCol);
        setInitialSpawn(2);
        setNumSpawn(1);
        board = new Tile[rowNum][rowCol];
        init();
    }

    private Board(int numRow, int numCol, int numInitNode, int numSpawn) {
        setRowNum(numRow);
        setColNum(numCol);
        setInitialSpawn(numInitNode);
        setNumSpawn(numSpawn);
        board = new Tile[rowNum][rowCol];
        init();
    }

    protected static Board getInstance() {
        if (session == null) {
            session = new Board();
        }
        return session;
    }

    protected static Board getInstance(int numRow, int numCol) {
        if (session == null) {
            session = new Board(numRow, numCol);
        }
        return session;
    }

    protected static Board getInstance(int numRow, int numCol, int numInitNode, int numSpawn) {
        if (session == null) {
            session = new Board(numRow, numCol, numInitNode, numSpawn);
        }
        return session;
    }

    private void setRowNum(int numRow) {
        rowNum = numRow == 0 ? 1 : Math.abs(numRow);
    }

    private void setColNum(int numCol) {
        rowCol = numCol == 0 ? 1 : Math.abs(numCol);
    }

    private void setInitialSpawn(int numInitNode) {
        initialSpawn = numInitNode == 0 ? 1 : Math.abs(numInitNode);
    }

    private void setNumSpawn(int numSpawn) {
        Board.numSpawn = numSpawn == 0 ? 1 : Math.abs(numSpawn);
    }

    private static void init() {
        initializeBlankBoard();
        for (int i = 0; i < initialSpawn; i++) {
            placeRandomNumberNode();
        }
        gameState.push(board);
    }

    private static void initializeBlankBoard() {
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < rowCol; j++) {
                board[i][j] = new Tile();
            }
        }
    }

    protected static void placeRandomNumberNode() {
        Random random = new Random();
        NumberNode node = new NumberNode();
        node.setRandomInitialValue();

        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < rowCol; j++) {
                if (!board[i][j].isOccupied()) {
                    int row;
                    int col;
                    do {
                        row = random.nextInt(rowNum);
                        col = random.nextInt(rowCol);
                    } while (board[row][col].isOccupied());

                    board[row][col].setNumberNode(node);
                    return;
                }
            }
        }
    }

    private void moveMergeHelper(boolean moveOrMerge, int startRow, int startCol, int deltaRow, int deltaCol) {
        for (int row = startRow; (startRow == 0 ? row < rowNum : row >= 0); row += (startRow == 0 ? 1 : -1)) {
            for (int col = startCol; (startCol == 0 ? col < rowCol : col >= 0); col += (startCol == 0 ? 1 : -1)) {
                Tile tile = board[row][col];
                if (tile.isOccupied()) {
                    NumberNode currentNode = tile.getNumberNode();
                    if (moveOrMerge) moveNode(row, col, currentNode, deltaRow, deltaCol);
                    else if (!tile.getNumberNode().isRecentlyMerged())
                        mergeNode(row, col, currentNode, deltaRow, deltaCol);
                }
            }
        }
    }

    protected void moveMergeUp(boolean moveOrMerge) {
        moveMergeHelper(moveOrMerge, 0, 0, -1, 0);
    }

    protected void moveMergeDown(boolean moveOrMerge) {
        moveMergeHelper(moveOrMerge, rowNum - 1, 0, 1, 0);
    }

    protected void moveMergeRight(boolean moveOrMerge) {
        moveMergeHelper(moveOrMerge, 0, rowCol - 1, 0, 1);
    }

    protected void moveMergeLeft(boolean moveOrMerge) {
        moveMergeHelper(moveOrMerge, 0, 0, 0, -1);
    }

    protected void shuffleBoard() {
        prevBoard = copyOfBoard(board);
        gameState.push(prevBoard);
        action.push(1);
        List<NumberNode> numberNodes = new ArrayList<>();

        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < rowCol; j++) {
                if (board[i][j].isOccupied()) {
                    numberNodes.add(board[i][j].getNumberNode());
                }
            }
        }
        Collections.shuffle(numberNodes);

        int index = 0;
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < rowCol; j++) {
                if (board[i][j].isOccupied()) {
                    board[i][j].setNumberNode(numberNodes.get(index++));
                }
            }
        }
        prevBoard = null;
    }

    protected boolean undo() {
        if (gameState.size() <= 1) {
            System.out.println("Can't undo any further");
            return false;
        }
        System.out.println("Undoing last action...");
        board = gameState.pop();
        return action.pop() == 0;
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
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < rowCol; j++) {
                board[i][j].nullifyRecentlyMerged();
            }
        }
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < rowNum && col >= 0 && col < rowCol;
    }

    protected static Tile[][] copyOfBoard(Tile[][] originalMatrix) {
        int numRows = originalMatrix.length;
        int numCols = originalMatrix[0].length;
        Tile[][] copiedMatrix = new Tile[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                copiedMatrix[i][j] = new Tile(originalMatrix[i][j].getNumberNode());
            }
        }
        return copiedMatrix;
    }

    protected static boolean areBoardsEqual(Tile[][] board1, Tile[][] board2) {
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