import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameWindow extends JFrame {

    private Settings settings;
    private GameMap gameMap;

    private JButton startButton;
    private JButton quitButton;
    private JPanel buttonPanel;

    private static final int WIDTH = 500;
    private static final int HEIGHT = 300;

    GameWindow() {
        createButtons();
        setWindowSettings();

        settings = new Settings(this);
        gameMap = new GameMap(this);

        final GameWindow gameWindow = this;
        settings.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                gameWindow.setEnabled(true);
            }
        });

        add(gameMap);
        setVisible(true);
    }

    void startGame(int mapSizeY, int mapSizeX, int gameMode, int winLineLen) {
        gameMap.createMap(mapSizeY, mapSizeX, gameMode, winLineLen);
    }

    private void setWindowSettings() {
        setTitle("Крестики-нолики");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int pozX = dimension.width / 2 - WIDTH / 2;
        int pozY = dimension.height / 2 - HEIGHT / 2;
        setBounds(pozX, pozY, WIDTH, HEIGHT);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void createButtons() {
        startButton = new JButton("Начать новую игру!");
        final GameWindow gameWindow = this;
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setWindowBounds(gameWindow.getBounds());
                settings.setVisible(true);
                setEnabled(false);
            }
        });

        quitButton = new JButton("Выйти");
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout());
        buttonPanel.add(startButton);
        buttonPanel.add(quitButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
