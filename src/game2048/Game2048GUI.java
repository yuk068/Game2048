package game2048;

import numbergame.NumberGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game2048GUI extends JFrame implements NumberGame {

    private static NumberGame session;
    private static Game2048 game;
    private boolean mute = false;
    private final JLabel[][] tiles;
    private final JLabel movesLabel;
    private final JPanel cards;
    private final CardLayout cardLayout;
    private String currentPanelName = "guidePanel";

    public static NumberGame getInstance() {
        if (session == null) {
            session = new Game2048GUI();
        }
        return session;
    }

    @Override
    public void start() {
        SwingUtilities.invokeLater(() -> {
            Game2048GUI game2048GUI = (Game2048GUI) getInstance();
            game2048GUI.setVisible(true); // Assuming you want to make the GUI visible when starting the game
        });
    }

    public Game2048GUI() {
        while (!startNewGame()) {

        }
        tiles = new JLabel[Board.rowNum][Board.rowCol];

        setTitle("2048");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        cards = new JPanel();
        cardLayout = new CardLayout();
        cards.setLayout(cardLayout);
        add(cards, BorderLayout.CENTER);

        JPanel guidePanel = new JPanel();
        guidePanel.setLayout(new BorderLayout());
        cards.add(guidePanel, "guidePanel");

        JLabel guideLabel = getjLabel();
        guidePanel.add(guideLabel, BorderLayout.CENTER);

        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout());
        cards.add(gamePanel, "gamePanel");

        JPanel gameBoardPanel = new JPanel();
        gameBoardPanel.setLayout(new GridLayout(Board.rowNum, Board.rowCol));
        gamePanel.add(gameBoardPanel, BorderLayout.CENTER);

        for (int i = 0; i < Board.rowNum; i++) {
            for (int j = 0; j < Board.rowCol; j++) {
                tiles[i][j] = new JLabel();
                tiles[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                tiles[i][j].setOpaque(true);
                tiles[i][j].setBackground(Color.BLACK);
                tiles[i][j].setBorder(BorderFactory.createLineBorder(Color.WHITE));

                tiles[i][j].setPreferredSize(new Dimension(100, 100));

                Font font = tiles[i][j].getFont();
                tiles[i][j].setFont(font.deriveFont(Font.BOLD, 32));
                tiles[i][j].setForeground(Color.WHITE);
                gameBoardPanel.add(tiles[i][j]);
            }
        }

        movesLabel = new JLabel("Moves: " + game.getMoves());
        movesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        Font movesFont = movesLabel.getFont();
        movesLabel.setFont(movesFont.deriveFont(Font.BOLD, 30));
        movesLabel.setForeground(Color.BLACK);
        gamePanel.add(movesLabel, BorderLayout.NORTH);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        game.operation("w");
                        break;
                    case KeyEvent.VK_DOWN:
                        game.operation("s");
                        break;
                    case KeyEvent.VK_LEFT:
                        game.operation("a");
                        break;
                    case KeyEvent.VK_RIGHT:
                        game.operation("d");
                        break;
                    case KeyEvent.VK_1:
                        game.operation("shuffle");
                        break;
                    case KeyEvent.VK_2:
                        game.operation("undo");
                        break;
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                        break;
                    case KeyEvent.VK_BACK_SPACE:
                        mute = !mute;
                        break;
                    case KeyEvent.VK_ENTER:
                        if (currentPanelName.equals("gamePanel")) {
                            cardLayout.show(cards, "guidePanel");
                            currentPanelName = "guidePanel";
                        } else {
                            cardLayout.show(cards, "gamePanel");
                            currentPanelName = "gamePanel";
                        }
                        break;
                    default:
                        break;
                }
                if (!mute) {
                    SFX.playSound("C:\\Users\\Phong Vu\\IdeaProjects\\Game2048\\src\\game2048\\xphit.wav");
                }
                updateDisplay();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        setFocusable(true);
        requestFocusInWindow();
        updateDisplay();

        pack();
        setVisible(true);
    }

    private boolean startNewGame() {
        ImageIcon icon = new ImageIcon("");
        String title = "2048 Game Configuration";

        String inputNumCol = JOptionPane.showInputDialog(null, "Enter the number of columns for the game board:", title, JOptionPane.PLAIN_MESSAGE, icon, null, "").toString();
        String inputNumRow = JOptionPane.showInputDialog(null, "Enter the number of rows for the game board:", title, JOptionPane.PLAIN_MESSAGE, icon, null, "").toString();
        String inputInitialSpawn = JOptionPane.showInputDialog(null, "Enter the initial spawn value for the game board:", title, JOptionPane.PLAIN_MESSAGE, icon, null, "").toString();
        String inputNumSpawn = JOptionPane.showInputDialog(null, "Enter the number of spawns for each move:", title, JOptionPane.PLAIN_MESSAGE, icon, null, "").toString();

        // Check if any input is null (indicating cancel or close button)
        if (inputNumCol == null || inputNumRow == null || inputInitialSpawn == null || inputNumSpawn == null) {
            return false; // User canceled or closed the dialog
        }

        try {
            int numCol = Integer.parseInt(inputNumCol);
            int numRow = Integer.parseInt(inputNumRow);
            int initialSpawn = Integer.parseInt(inputInitialSpawn);
            int numSpawn = Integer.parseInt(inputNumSpawn);

            if (numCol > 16 || numRow > 16 || numSpawn > 10) {
                JOptionPane.showMessageDialog(null, "Please don't go too insane\n" +
                        "<= 16 for Rows and Columns, <= 10 for number of spawns", "Warning", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            game = new Game2048(new Board(numRow, numCol, initialSpawn, numSpawn));
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter valid numeric values", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }


    private static JLabel getjLabel() {
        JLabel guideLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<div style='font-size: 20px;'>Welcome to 2048!</div>" +
                "<br>" +
                "<div style='font-size: 16px;'>Move with ARROWS<br>Shuffle with 1<br>Undo with 2" +
                "<br>Mute with BACKSPACE<br>Toggle between" +
                "<br>guide and gameplay with ENTER<br>Quit with ESC" +
                "<br>Made by Yuk, Have fun!</div></div></html>");
        guideLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return guideLabel;
    }

    private void updateDisplay() {
        Tile[][] boardTiles = Board.board;

        for (int i = 0; i < Board.rowNum; i++) {
            for (int j = 0; j < Board.rowCol; j++) {
                if (boardTiles[i][j].isOccupied()) {
                    int value = boardTiles[i][j].getNumberNode().getValue();
                    tiles[i][j].setText(String.valueOf(value));

                    float hue = (float) Math.log(value) / (float) Math.log(2048);
                    Color tileColor = Color.getHSBColor(hue, 1.0f, 1.0f);
                    tiles[i][j].setForeground(Color.BLACK);
                    tiles[i][j].setBackground(tileColor);
                } else {
                    tiles[i][j].setText("");
                    tiles[i][j].setBackground(Color.LIGHT_GRAY);
                }
            }
        }

        movesLabel.setText("Moves: " + game.getMoves());
    }

}
