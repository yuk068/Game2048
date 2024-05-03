package gamenumberstack;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameNumberStack {

    protected int moves; // Number of moves
    protected final int numStack; // Number of stacks
    protected final int maxSize; // Maximum size for stacks
    private static GameNumberStack session; // Singleton
    protected final List<NumberNodeStack> stacks; // List of stacks
    private static final Scanner in = new Scanner(System.in);
    private static final int DEFAULT_MIN_NUM_STACK = 3;
    private static final int DEFAULT_NUM_STACK = 4;

    public GameNumberStack(int numStack) {
        if (numStack < DEFAULT_MIN_NUM_STACK) throw new IllegalArgumentException("Must be >= 3");
        maxSize = numStack + 1;
        this.numStack = numStack;
        stacks = new ArrayList<>();
        initializeGame(numStack);
    }

    public static GameNumberStack getInstance(int numStack) {
        if (numStack < DEFAULT_MIN_NUM_STACK)
            throw new IllegalArgumentException("Must be >= 3");
        if (session == null) {
            session = new GameNumberStack(numStack);
        }
        return session;
    }

    public static GameNumberStack getInstance() {
        session = getInstance(DEFAULT_NUM_STACK);
        return session;
    }

    public void start() {
        String op;
        while (true) {
            display();
            System.out.println("Select column");
            op = in.nextLine();
            if (op.equals("q")) {
                System.out.println("Exiting...");
                break;
            }
            try {
                int fromColumn = Integer.parseInt(op) - 1;
                if (fromColumn < 0 || fromColumn >= numStack) continue;
                System.out.println("Move to");
                op = in.nextLine();
                if (op.equals("q")) {
                    System.out.println("Exiting...");
                    break;
                }
                try {
                    int toColumn = Integer.parseInt(op) - 1;
                    if (toColumn < 0 || toColumn >= numStack) continue;
                    if (stacks.get(fromColumn).moveTo(stacks.get(toColumn))) {
                        moves++;
                    }
                    if (checkWin()) {
                        display();
                        System.out.println("You won!");
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid column number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid column number.");
            }
        }
    }


    private void initializeGame(int numStack) {
        for (int i = 0; i < numStack; i++) {
            stacks.add(new NumberNodeStack(maxSize, i + 1));
        }
    }

    protected boolean checkWin() {
        for (NumberNodeStack stack : stacks) {
            if (!stack.isEmpty()) {
                int firstValue = stack.numberNodeStack.get(0).getValue();
                for (NumberNode value : stack.numberNodeStack) {
                    if (value.getValue() != (firstValue)) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        return true;
    }


    public void display() {
        System.out.println("Moves: " + moves);
        System.out.println("-".repeat(numStack * 7 + 1));
        for (int i = 0; i < maxSize; i++) {
            System.out.print("|  ");
            for (int j = 0; j < numStack; j++) {
                try {
                    int value = stacks.get(j).numberNodeStack.get(maxSize - i - 1).getValue();
                    String formattedValue = String.format("%2d", value);
                    System.out.print(formattedValue + "  |  ");
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.print("    |  ");
                }
            }
            System.out.println();
        }
        System.out.println("-".repeat(numStack * 7 + 1));
    }


}
