package game2048;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Scanner;

public class Game2048 {

    private static Game2048 session;
    private Board board;
    public static final Scanner in = new Scanner(System.in);

    private Game2048(Board board) {
        this.board = board;
    }

    public static Game2048 getInstance() {
        if (session == null) {
            session = new Game2048(Board.getInstance());
        }
        return session;
    }

    public void start() {
        String op = "0";
        System.out.println("Welcome to 2048!\nWASD to move\nQ    to quit");
        while (!op.equals("q")) {
            display();
            op = in.nextLine();
            move(op);
        }
        System.out.println("Exiting...");
    }

    public void display() {
        for (int i = 0; i < Board.DEFAULT_ROW_NUM; i++) {
            for (int j = 0; j < Board.DEFAULT_COL_NUM; j++) {
                System.out.printf("[%4s] ", (Board.getBoard()[i][j].isOccupied() ?
                        String.valueOf(Board.getBoard()[i][j].getNumberNode().getValue()) : " "));
            }
            System.out.println("\n");
        }
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
                Board.placeRandomNumberNode();
            }
        } catch (Exception e) {
            System.out.println("Invalid direction: " + direction);
        }
    }

}
