package gamenumberstack;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);


        do {
            System.out.println("Enter number of stacks ( >= 3 ):");
            int numStack = Integer.parseInt(in.nextLine());
            gameplay(numStack);
            System.out.println("New game? Y/N");
        } while (in.nextLine().equalsIgnoreCase("Y"));
    }

    public static void gameplay(int numStack) {
        GameNumberStack game = null;
        try {
            game = new GameNumberStack(numStack);
            game.start();
        } catch (IllegalArgumentException e) {
            System.out.println("Number of stack must be >= 3");
        }
    }

}
