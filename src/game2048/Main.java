package game2048;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Game2048 game = Game2048.getInstance(6, 6, 4, 2);
        game.setDisplayStyleBracketOrBorder(false);
        game.start();
    }

}
