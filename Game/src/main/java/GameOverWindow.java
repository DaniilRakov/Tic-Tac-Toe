import javax.swing.*;
import java.awt.*;

public class GameOverWindow extends JFrame {

    private JLabel label;

    private final int WIDTH = 300;
    private final int HEIGHT = 100;

    GameOverWindow() {
        setTitle("Игра окончена!");

        label = new JLabel("stateGameOver", SwingConstants.CENTER);
        add(label);
    }

    void setMessage(String stateGameOver) {
        label.setText(stateGameOver);
    }

    void setWindowBounds(Rectangle rect) {
        int pozX = rect.x + rect.width / 2 - WIDTH / 2;
        int pozY = rect.y + rect.height / 2 - HEIGHT / 2;

        setBounds(pozX, pozY, WIDTH, HEIGHT);
    }
}
