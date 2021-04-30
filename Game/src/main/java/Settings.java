import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Settings extends JFrame {

    private GameWindow gameWindow;

    private final int WIDTH = 300;
    private final int HEIGHT = 200;

    private final int MIN_MAP_SIZE = 3;
    private final int MAX_MAP_SIZE = 10;
    private final int MIN_WIN_LEN = 3;

    private int mapSize = 3;
    private String mapSizeLabelText = "Размер игрового поля - " + mapSize + " на " + mapSize;
    private int winLen = 3;
    private String winLenLabelText = "Длина выигрышной линии - " + winLen;

    private JSlider mapSizeSlider;
    private JSlider winLenSlider;
    private JRadioButton pVe;
    private JRadioButton pVp;

    Settings(GameWindow gameWindow) {
        this.gameWindow = gameWindow;

        setTitle("Настройки игры");

        addItems();
    }

    private void addItems() {
        setLayout(new GridLayout(8, 1));

        addRadioButtons();

        addSliders();

        JButton startBtn = new JButton("Начать игру!");
        add(startBtn);
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start();
            }
        });
    }

    private void start() {
        int mapSize = mapSizeSlider.getValue();
        int winLineLen = winLenSlider.getValue();
        int gameMode = pVe.isSelected() ? GameMap.GAME_MODE_PVE : GameMap.GAME_MODE_PVP;

        gameWindow.startGame(mapSize, mapSize, gameMode, winLineLen);

        setVisible(false);
        gameWindow.setEnabled(true);
        gameWindow.setVisible(true);
    }

    private void addSliders() {
        final JLabel labelSize = new JLabel(mapSizeLabelText, SwingConstants.CENTER);
        add(labelSize);
        mapSizeSlider = new JSlider(MIN_MAP_SIZE, MAX_MAP_SIZE, MIN_MAP_SIZE);
        mapSizeSlider.setSize(WIDTH / 10 * 9, HEIGHT / 10 * 9);
        add(mapSizeSlider);

        final JLabel labelWin = new JLabel(winLenLabelText, SwingConstants.CENTER);
        add(labelWin);
        winLenSlider = new JSlider(MIN_WIN_LEN, MIN_MAP_SIZE, MIN_WIN_LEN);
        add(winLenSlider);

        mapSizeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                mapSize = mapSizeSlider.getValue();
                mapSizeLabelText = "Размер игрового поля - " + mapSize + " на " + mapSize;
                labelSize.setText(mapSizeLabelText);
                winLenSlider.setMaximum(mapSize);
            }
        });

        winLenSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                winLenLabelText = "Длина выигрышной линии - " + winLenSlider.getValue();
                labelWin.setText(winLenLabelText);
            }
        });
    }

    private void addRadioButtons() {
        JLabel labelMode = new JLabel("Выберите режим игры", SwingConstants.CENTER);
        add(labelMode);
        pVe = new JRadioButton("Против компьютера", true);
        pVp = new JRadioButton("Против другого ирока");

        ButtonGroup group = new ButtonGroup();
        group.add(pVe);
        group.add(pVp);

        add(pVe);
        add(pVp);
    }

    void setWindowBounds(Rectangle rect) {
        int pozX = rect.x + rect.width / 2 - WIDTH / 2;
        int pozY = rect.y + rect.height / 2 - HEIGHT / 2;

        setBounds(pozX, pozY, WIDTH, HEIGHT);
    }
}
