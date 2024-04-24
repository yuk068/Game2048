package game2048;

import java.lang.reflect.Method;
import java.util.Scanner;

public class Game2048 {

    private static Game2048 session;
    private Board board;
    private boolean displayStyleBracketOrBorder = false;
    public static final Scanner in = new Scanner(System.in);

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
        System.out.println("Welcome to 2048!\nWASD to move\nQ    to quit");
        while (!op.equals("q")) {
            if (displayStyleBracketOrBorder) {
                displayBracket();
            } else {
                displayBorder();
            }
            op = in.nextLine();
            move(op);
        }
        System.out.println("Exiting...");
    }

    public void displayBracket() {
        for (int i = 0; i < Board.DEFAULT_ROW_NUM; i++) {
            for (int j = 0; j < Board.DEFAULT_COL_NUM; j++) {
                System.out.printf("[%4s] ", (Board.getBoard()[i][j].isOccupied() ?
                        String.valueOf(Board.getBoard()[i][j].getNumberNode().getValue()) : " "));
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
                String number = Board.getBoard()[i][j].isOccupied() ?
                        String.valueOf(Board.getBoard()[i][j].getNumberNode().getValue()) : " ";
                System.out.printf("%5s |", number);
            }
            System.out.println((i != Board.DEFAULT_ROW_NUM - 1) ? "\n" : "");
        }

        for (int i = 0; i < Board.DEFAULT_COL_NUM * 7 + 1; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    public void move(String direction) {
        switch (direction) {
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
            default:
        }
    }

    private void moveOperation(String direction) {
        String movingIn = "moveMerge" + direction;
        try {
            System.out.println("Moving " + direction.toLowerCase());
            Board.setPrevBoard(Board.copyBoard(Board.getBoard()));

            Method method = Board.class.getDeclaredMethod(movingIn, boolean.class);
            method.invoke(board, true);
            method.invoke(board, false);
            method.invoke(board, true);
            board.nullifyAllRecentlyMerged();

            if (!Board.areBoardsEqual(Board.getPrevBoard(), Board.getBoard())) {
                for (int i = 0; i < Board.DEFAULT_SPAWN; i++) {
                    Board.placeRandomNumberNode();
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid direction: " + direction);
        }
    }

}
