package game2048;

import numbergame.NumberGame;

public class Main {

    public static void main(String[] args) {
        // Default vanilla 2048 game
        // Args: numRow, numCol, numInitialNode, numSpawn
        NumberGame game = Game2048.getInstance(4);
        game.start();
    }

}
