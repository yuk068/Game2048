package game2048;

import java.util.Scanner;

public class Main {

    public static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        Board board = Board.getInstance();
        System.out.println("wasd to move, q to quit");
        String op = "0";
        while (!op.equals("q")) {
            board.display();
            op = in.nextLine();
            board.move(op);
        }
    }

}
