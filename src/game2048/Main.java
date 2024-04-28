package game2048;

import numbergame.NumberGame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        NumberGame game = Game2048GUI.getInstance();
        game.start();
    }

}
