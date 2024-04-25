package game2048;

import java.lang.reflect.Method;
import java.util.Scanner;

public class Game2048 {

    private static Game2048 session;
    private final Board board;
    private boolean displayStyleBracketOrBorder = false;
    private static final Scanner in = new Scanner(System.in);
    private int moves = 0;

    private Game2048(Board board) {
        this.board = board;
    }

    public void setDisplayStyleBracketOrBorder(boolean displayStyleBracketOrBorder) {
        this.displayStyleBracketOrBorder = displayStyleBracketOrBorder;
    }

    public static Game2048 getInstance() {
        if (session == null) {
            session = new Game2048(Board.getInstance());
        }
        return session;
    }

    public static Game2048 getInstance(int numRow, int numCol) {
        if (session == null) {
            session = new Game2048(Board.getInstance(numRow, numCol));
        }
        return session;
    }

    public static Game2048 getInstance(int numRow, int numCol, int numInitNode, int numSpawn) {
        if (session == null) {
            session = new Game2048(Board.getInstance(numRow, numCol, numInitNode, numSpawn));
        }
        return session;
    }

    public void start() {
        String op = "0";
        System.out.println("Welcome to 2048!");
        System.out.println("wasd    to move");
        System.out.println("q       to quit");
        System.out.println("shuffle to shuffle the board");
        System.out.println("undo    to undo last action");
        while (!op.equals("q")) {
            if (displayStyleBracketOrBorder) {
                displayBracket();
            } else {
                displayBorder();
            }
            op = in.nextLine();
            operation(op);
        }
        System.out.println("Exiting...");
    }

    public void displayBracket() {
        for (int i = 0; i < Board.DEFAULT_ROW_NUM; i++) {
            for (int j = 0; j < Board.DEFAULT_COL_NUM; j++) {
                System.out.printf("[%4s] ", (Board.board[i][j].isOccupied() ?
                        String.valueOf(Board.board[i][j].getNumberNode().getValue()) : " "));
            }
            System.out.println("\n");
        }
    }

    public void displayBorder() {
        for (int i = 0; i < Board.DEFAULT_COL_NUM * 7 + 1; i++) {
            System.out.print("-");
        }
        System.out.println();

        for (int i = 0; i < Board.DEFAULT_ROW_NUM; i++) {
            System.out.print("|");
            for (int j = 0; j < Board.DEFAULT_COL_NUM; j++) {
                String number = Board.board[i][j].isOccupied() ?
                        String.valueOf(Board.board[i][j].getNumberNode().getValue()) : " ";
                System.out.printf("%5s |", number);
            }
            System.out.println((i != Board.DEFAULT_ROW_NUM - 1) ? "\n" : "");
        }

        for (int i = 0; i < Board.DEFAULT_COL_NUM * 7 + 1; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    public void operation(String operation) {
        switch (operation.toLowerCase()) {
            case "w":
                moveOperation("Up");
                break;
            case "s":
                moveOperation("Down");
                break;
            case "d":
                moveOperation("Right");
                break;
            case "a":
                moveOperation("Left");
                break;
            case "undo":
                if (board.undo()) moves--;
                break;
            case "shuffle":
                System.out.println("Shuffling...");
                board.shuffleBoard();
                break;
            default:
        }
    }

    private void moveOperation(String direction) {
        String moveMergeIn = "moveMerge" + direction;
        try {
            Board.prevBoard = Board.copyOfBoard(Board.board);

            Method method = Board.class.getDeclaredMethod(moveMergeIn, boolean.class);
            method.invoke(board, true);
            method.invoke(board, false);
            method.invoke(board, true);
            board.nullifyAllRecentlyMerged();

            if (!Board.areBoardsEqual(Board.prevBoard, Board.board)) {
                Board.gameState.push(Board.prevBoard);
                Board.action.push(0);
                moves++;
                for (int i = 0; i < Board.DEFAULT_SPAWN; i++) {
                    Board.placeRandomNumberNode();
                }
            }
            System.out.printf("Moving %-10s Moves:  %d%n", direction.toLowerCase(), moves);
        } catch (Exception e) {
            System.out.println("Invalid direction: " + direction);
        }
    }

}
