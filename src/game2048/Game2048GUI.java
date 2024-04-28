package game2048;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

public class Game2048GUI extends JFrame {

    private static final ClassLoader classLoader = Game2048GUI.class.getClassLoader();
    private static Game2048 game;    private boolean mute = false;
    private final JLabel[][] tiles;
    private final JLabel movesLabel;
    private final JPanel cards;
    private final CardLayout cardLayout;
    private String currentPanelName = "guidePanel";

    public static void start() {
        SwingUtilities.invokeLater(() -> {
            Game2048GUI game2048GUI = new Game2048GUI();
            game2048GUI.setVisible(true);
        });
    }

    public Game2048GUI() {
        while (!startNewGame()) {

        }
        tiles = new JLabel[Board.rowNum][Board.rowCol];

        setTitle("Yuk-Sensei and Hare's 2048");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        cards = new JPanel();
        cardLayout = new CardLayout();
        cards.setLayout(cardLayout);
        add(cards, BorderLayout.CENTER);

        JPanel guidePanel = new JPanel();
        guidePanel.setLayout(new BorderLayout());
        cards.add(guidePanel, "guidePanel");

        JPanel guideLabel = getGuidePanel();
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
                tiles[i][j].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                tiles[i][j].setPreferredSize(new Dimension(100, 100));

                Font impactFont = new Font("Impact", Font.PLAIN, 39);
                tiles[i][j].setFont(impactFont);

                tiles[i][j].setForeground(Color.WHITE);
                gameBoardPanel.add(tiles[i][j]);
            }
        }

        movesLabel = new JLabel("Moves: " + game.getMoves());
        movesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        Font impactFontMoves = new Font("Impact", Font.PLAIN, 20);
        movesLabel.setFont(impactFontMoves);
        movesLabel.setForeground(Color.BLACK);
        gamePanel.add(movesLabel, BorderLayout.NORTH);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        game.operation("w");
                        break;
                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                        game.operation("s");
                        break;
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        game.operation("a");
                        break;
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        game.operation("d");
                        break;
                    case KeyEvent.VK_Z:
                        game.operation("shuffle");
                        break;
                    case KeyEvent.VK_U:
                        game.operation("undo");
                        break;
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                        break;
                    case KeyEvent.VK_M:
                        mute = !mute;
                        break;
                    case KeyEvent.VK_V:
                        SFX.cycleVolumeLevel();
                        break;
                    case KeyEvent.VK_ENTER:
                    case KeyEvent.VK_G:
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
                    SFX.playSound("8bit_chirp.wav");
                }
                updateDisplay();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        ImageIcon icon = new ImageIcon(getImageURL("hare_momotalk.png"));
        setIconImage(icon.getImage());

        setSize(400, 400);
        setLocationRelativeTo(null);
        setFocusable(true);
        requestFocusInWindow();
        updateDisplay();

        pack();
        setVisible(true);
    }

    private boolean startNewGame() {
        ImageIcon icon = new ImageIcon(getImageURL("hare_momotalk.png"));
        String title = "2048 Game Configuration";
        boolean devMode = false;
        String devModeNotification = "";
        String inputNumCol = JOptionPane.showInputDialog(null, "Enter the number of columns for the game board: (ENTER for Default)", title, JOptionPane.PLAIN_MESSAGE, icon, null, "").toString();
        if (inputNumCol.isEmpty()) {
            game = new Game2048(new Board(4, 4, 2, 1));
            return true;
        } else if (inputNumCol.split("_")[0].equals("VERITAS")) {
            devMode = true;
            devModeNotification = " (DEV MODE ACTIVATED)";
            inputNumCol = inputNumCol.split("_")[1];
        }
        String inputNumRow = JOptionPane.showInputDialog(null, "Enter the number of rows for the game board:" + devModeNotification, title, JOptionPane.PLAIN_MESSAGE, icon, null, "").toString();
        String inputInitialSpawn = JOptionPane.showInputDialog(null, "Enter the initial spawn value for the game board:", title, JOptionPane.PLAIN_MESSAGE, icon, null, "").toString();
        String inputNumSpawn = JOptionPane.showInputDialog(null, "Enter the number of spawns for each move:", title, JOptionPane.PLAIN_MESSAGE, icon, null, "").toString();

        if (!devMode && (inputNumRow == null || inputInitialSpawn == null || inputNumSpawn == null)) {
            return false;
        }

        try {
            int numCol = Integer.parseInt(inputNumCol);
            int numRow = Integer.parseInt(inputNumRow);
            int initialSpawn = Integer.parseInt(inputInitialSpawn);
            int numSpawn = Integer.parseInt(inputNumSpawn);

            if (!devMode && (numCol > 16 || numRow > 16 || numSpawn > 10)) {
                JOptionPane.showMessageDialog(null, "Please don't go too insane (Pill time lil bro)\n" +
                        "<= 16 for Rows and Columns, <= 10 for number of spawns", "Warning", JOptionPane.WARNING_MESSAGE, icon);
                return false;
            }

            game = new Game2048(new Board(numRow, numCol, initialSpawn, numSpawn));
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter valid numeric values", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }


    private static JPanel getGuidePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel textLabel = getGuideText();

        panel.add(textLabel, BorderLayout.NORTH);

        ImageIcon imageIcon = new ImageIcon(getImageURL("hare.png"));
        JLabel imageLabel = new JLabel(imageIcon);

        panel.add(imageLabel, BorderLayout.CENTER);

        return panel;
    }

    private static URL getImageURL(String fileName) {
        String image = Game2048GUI.class.getPackageName().replace('.', '/') +
                "/" + fileName;
        return classLoader.getResource(image);
    }

    private static JLabel getGuideText() {
        JLabel textLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<div style='font-size: 20px;'>Welcome to 2048!</div>" +
                "<br>" +
                "<div style='font-size: 12px;'>Move with ARROWS [&#x2190; &#x2193; &#x2191; &#x2192;] & [WASD]" +
                "<br>Shuffle with [ Z ]" +
                "<br>Undo with [ U ]" +
                "<br>Adjust volume with [ V ]" +
                "<br>Mute with [ M ]<br>Toggle between" +
                "<br>guide and gameplay with [ G ]<br>Quit with [ ESC ]" +
                "<br>Made by Yuk " +
                "<br>and my wife Hare, say hi to her &#x2193" +
                "<br>Have fun!</div></div></html>");
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return textLabel;
    }

    private void updateDisplay() {
        Tile[][] boardTiles = Board.board;

        for (int i = 0; i < Board.rowNum; i++) {
            for (int j = 0; j < Board.rowCol; j++) {
                if (boardTiles[i][j].isOccupied()) {
                    int value = boardTiles[i][j].getNumberNode().getValue();
                    tiles[i][j].setText(String.valueOf(value));

                    float hue = (float) Math.log(value) / (float) Math.log(81920);
                    Color tileColor = Color.getHSBColor(hue, 1.0f, 1.0f);
                    tiles[i][j].setForeground(Color.BLACK);
                    tiles[i][j].setBackground(tileColor);
                    int fontSize = 40 - (int) (Math.log10(value) * 6);
                    Font impactFont = new Font("Impact", Font.PLAIN, fontSize);
                    tiles[i][j].setFont(impactFont);
                } else {
                    tiles[i][j].setText("");
                    tiles[i][j].setBackground(Color.GRAY);
                }
            }
        }

        movesLabel.setText("Guide: [G] | Moves: " + game.getMoves() + " | Volume: " + (mute ? "Muted" : "||".repeat(SFX.volumeLevel + 1)));
    }

}
