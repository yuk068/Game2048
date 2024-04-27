package gamenumberstack;

import numbergame.NumberGame;

public class Main {

    public static void main(String[] args) {
        NumberGame game = GameNumberStack.getInstance();
        game.start();
    }

}
