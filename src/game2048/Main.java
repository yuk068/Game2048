package game2048;

public class Main {

    public static void main(String[] args) {
        // Default vanilla 2048 game
        // Args: numRow, numCol, numInitialNode, numSpawn
        Game2048 game = Game2048.getInstance();
        game.start();
    }

}
