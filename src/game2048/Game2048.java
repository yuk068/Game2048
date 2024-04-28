package game2048;

import java.lang.reflect.Method;
import java.util.Scanner;

public class Game2048 {

    private static Game2048 session;
    protected final Board board;
    private boolean displayStyleBracketOrBorder = false;
    private static final Scanner in = new Scanner(System.in);
    private int moves = 0;

    public Game2048(Board board) {
        this.board = board;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public void setDisplayStyleBracketOrBorder(boolean displayStyleBracketOrBorder) {
        this.displayStyleBracketOrBorder = displayStyleBracketOrBorder;
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
        for (int i = 0; i < Board.rowNum; i++) {
            for (int j = 0; j < Board.rowCol; j++) {
                System.out.printf("[%4s] ", (Board.board[i][j].isOccupied() ?
                        String.valueOf(Board.board[i][j].getNumberNode().getValue()) : " "));
            }
            System.out.println("\n");
        }
    }

    public void displayBorder() {
        for (int i = 0; i < Board.rowCol * 7 + 1; i++) {
            System.out.print("-");
        }
        System.out.println();

        for (int i = 0; i < Board.rowNum; i++) {
            System.out.print("|");
            for (int j = 0; j < Board.rowCol; j++) {
                String number = Board.board[i][j].isOccupied() ?
                        String.valueOf(Board.board[i][j].getNumberNode().getValue()) : " ";
                System.out.printf("%5s |", number);
            }
            System.out.println((i != Board.rowNum - 1) ? "\n" : "");
        }

        for (int i = 0; i < Board.rowCol * 7 + 1; i++) {
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
//                System.out.println("Shuffling...");
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
                for (int i = 0; i < Board.numSpawn; i++) {
                    Board.placeRandomNumberNode();
                }
            }
//            System.out.printf("Moving %-10s Moves:  %d%n", direction.toLowerCase(), moves);
        } catch (Exception e) {
            System.out.println("Invalid direction: " + direction);
        }
    }

}
