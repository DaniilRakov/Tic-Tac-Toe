import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import javax.swing.*;

public class GameMap extends JPanel {

    private final GameWindow gameWindow;
    private GameOverWindow gameOverWindow;

    public static final int GAME_MODE_PVE = 0;
    public static final int GAME_MODE_PVP = 1;

    private int gameMode;
    private char[][] map;
    private int mapSizeX;
    private int mapSizeY;
    private int winLineLen;

    private int cellWidth;
    private int cellHeight;

    private final char HUMAN = 'X';
    private final char AI = 'O';
    private final char EMPTY = '_';

    private int stateGameOver;
    private final int STATE_GO_DRAW = 0;
    private final int STATE_GO_HUMAN_WIN = 1;
    private final int STATE_GO_AI_WIN = 2;

    private Random random = new Random();

    private boolean mapInitialized;
    private boolean isGameOver;
    private boolean isMessageShowed = false;
    private boolean firstPlayerTurn = true;

    GameMap(final GameWindow gameWindow) {
        this.gameWindow = gameWindow;

        setBackground(Color.BLACK);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                updateMap(e);
            }
        });

        mapInitialized = false;

        gameOverWindow = new GameOverWindow();
        gameOverWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                gameWindow.setEnabled(true);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    private void render(Graphics g) {
        if (!mapInitialized)
            return;

        int width = getWidth();
        cellWidth = width / mapSizeX;
        int height = getHeight();
        cellHeight = height / mapSizeY;

        printMapBorders(g, width, height);

        printPlayersSymbols(g);

        if (isGameOver && !isMessageShowed) {
            showGameOverMessage();
            paintWinLine(g);
            isMessageShowed = true;
        }
    }

    private void showGameOverMessage() {
        String message;
        String MSG_WIN_HUMAN = "Победил человек!";
        String MSG_WIN_PLAYER_1 = "Победил первый игрок!";
        String MSG_WIN_PLAYER_2 = "Победил второй игрок!";
        String MSG_WIN_AI = "Победил искусственный интеллект!";
        String MSG_DRAW = "Ничья";
        String MSG_WRONG = "Что-то пошло не так";
        switch (stateGameOver) {
            case STATE_GO_DRAW:
                message = MSG_DRAW;
                break;
            case STATE_GO_HUMAN_WIN:
                message = gameMode == GAME_MODE_PVE ? MSG_WIN_HUMAN : MSG_WIN_PLAYER_1;
                break;
            case STATE_GO_AI_WIN:
                message = gameMode == GAME_MODE_PVE ? MSG_WIN_AI : MSG_WIN_PLAYER_2;
                break;
            default:
                message = MSG_WRONG;
        }

        gameOverWindow.setMessage(message);
        gameOverWindow.setWindowBounds(gameWindow.getBounds());
        gameOverWindow.setVisible(true);
        gameWindow.setEnabled(false);
    }

    // метод будет рисовать черту на выирышной линии на поле
    private void paintWinLine(Graphics g) {
//        g.setColor(Color.WHITE);
//        int x = getX() + getWidth() / 4;
//        int y = getY() + getHeight() / 4;
//        g.fillRoundRect(x, y,
//                getWidth() / 2, getHeight() / 2, 30, 30);
//        g.setColor(Color.BLACK);
//        g.setFont(new Font("Verdana", Font.BOLD, 30));
//
//        switch (stateGameOver) {
//            case STATE_GO_DRAW:
//                g.drawString("MSG_DRAW", x, y + getHeight() / 4);
//                break;
//            case STATE_GO_HUMAN_WIN:
//                g.drawString("MSG_WIN_HUMAN", x, y + getHeight() / 4);
//                break;
//            case STATE_GO_AI_WIN:
//                g.drawString("MSG_WIN_AI", x, y + getHeight() / 4);
//                break;
//        }
//        repaint();
    }

    private void printMapBorders(Graphics g, int width, int height) {
        g.setColor(Color.WHITE);

        for (int i = 1; i < mapSizeY; i++) {
            int y = i * cellHeight;
            g.drawLine(0, y, width, y);
        }

        for (int i = 1; i < mapSizeX; i++) {
            int x = i * cellWidth;
            g.drawLine(x, 0, x, height);
        }
    }

    private void printPlayersSymbols(Graphics g) {
        for (int x = 0; x < mapSizeX; x++) {
            for (int y = 0; y < mapSizeY; y++) {
                if (map[x][y] == EMPTY)
                    continue;

                int widthOffset = cellWidth / 7,
                        heightOffset = cellHeight / 7;

                if (map[x][y] == AI) {
                    g.setColor(Color.BLUE);
                    g.fillOval(x * cellWidth + widthOffset, y * cellHeight + heightOffset,
                            cellWidth - widthOffset * 2, cellHeight - heightOffset * 2);
                    g.setColor(Color.BLACK);
                    g.fillOval(x * cellWidth + widthOffset + widthOffset / 2,
                            y * cellHeight + heightOffset + heightOffset / 2,
                            cellWidth - widthOffset * 3, cellHeight - heightOffset * 3);

                } else {
                    g.setColor(Color.GREEN);
                    g.fillRect(x * cellWidth + widthOffset, y * cellHeight + heightOffset,
                            cellWidth - widthOffset * 2, cellHeight - heightOffset * 2);

                    g.setColor(Color.BLACK);
                    // верхний треугольник
                    int[] xPoints = new int[]{x * cellWidth + widthOffset + widthOffset / 2,
                            (x + 1) * cellWidth - widthOffset - widthOffset / 2,
                            x * cellWidth + cellWidth / 2};
                    int[] yPoints = new int[]{y * cellHeight + heightOffset,
                            y * cellHeight + heightOffset,
                            y * cellHeight + heightOffset * 3};
                    g.fillPolygon(xPoints, yPoints, 3);
                    // нижний треугольник
                    xPoints = new int[]{x * cellWidth + widthOffset + widthOffset / 2,
                            (x + 1) * cellWidth - widthOffset - widthOffset / 2,
                            x * cellWidth + cellWidth / 2};
                    yPoints = new int[]{(y + 1) * cellHeight - heightOffset,
                            (y + 1) * cellHeight - heightOffset,
                            (y + 1) * cellHeight - heightOffset * 3};
                    g.fillPolygon(xPoints, yPoints, 3);
                    // левый треугольник
                    xPoints = new int[]{x * cellWidth + widthOffset,
                            x * cellWidth + widthOffset,
                            x * cellWidth + widthOffset * 3};
                    yPoints = new int[]{y * cellHeight + heightOffset + heightOffset / 2,
                            (y + 1) * cellHeight - heightOffset - heightOffset / 2,
                            y * cellHeight + cellHeight / 2};
                    g.fillPolygon(xPoints, yPoints, 3);
                    // правый треугольник
                    xPoints = new int[]{(x + 1) * cellWidth - widthOffset,
                            (x + 1) * cellWidth - widthOffset,
                            (x + 1) * cellWidth - widthOffset * 3};
                    yPoints = new int[]{y * cellHeight + heightOffset + heightOffset / 2,
                            (y + 1) * cellHeight - heightOffset - heightOffset / 2,
                            y * cellHeight + cellHeight / 2};
                    g.fillPolygon(xPoints, yPoints, 3);
                }
            }
        }
    }

    private void updateMap(MouseEvent e) {
        if (isGameOver)
            return;

        if (!mapInitialized)
            return;

        int cellX = e.getX() / cellWidth;
        int cellY = e.getY() / cellHeight;

        if (map[cellX][cellY] != EMPTY)
            return;
        char player = firstPlayerTurn ? HUMAN : AI;
        map[cellX][cellY] = player;

        if (checkWinLine(HUMAN)) {
            setGameOver(STATE_GO_HUMAN_WIN);
            return;
        }

        if (isFullMap()) {
            setGameOver(STATE_GO_DRAW);
            return;
        }

        if (gameMode == GAME_MODE_PVE)
            turnPC();
        else {
            firstPlayerTurn = !firstPlayerTurn;
        }

        if (checkWinLine(AI)) {
            setGameOver(STATE_GO_AI_WIN);
            return;
        }

        if (isFullMap()) {
            setGameOver(STATE_GO_DRAW);
        }
        repaint();
    }

    private void setGameOver(int state) {
        isGameOver = true;
        stateGameOver = state;
        repaint();
    }

    void createMap(int mapSizeY, int mapSizeX, int gameMode, int winLineLen) {
        this.mapSizeX = mapSizeX;
        this.mapSizeY = mapSizeY;
        this.gameMode = gameMode;
        this.winLineLen = winLineLen;

        map = new char[mapSizeY][mapSizeX];
        for (int y = 0; y < mapSizeY; y++) {
            for (int x = 0; x < mapSizeX; x++) {
                map[y][x] = EMPTY;
            }
        }

        isGameOver = false;
        mapInitialized = true;
        isMessageShowed = false;
        repaint();
    }

    private void turnPC() {
        int x, y;

        // сначала проверять, будет ли быстрая победа у ИИ
        if (checkPreWinState(AI))
            return;

        // затем только всё остальное
        if (checkPreWinState(HUMAN))
            return;

        do {
            x = random.nextInt(mapSizeX);
            y = random.nextInt(mapSizeY);
        } while (map[y][x] != EMPTY);
        map[y][x] = AI;
    }

    private boolean isFullMap() {
        for (int y = 0; y < mapSizeY; y++) {
            for (int x = 0; x < mapSizeX; x++) {
                if (map[y][x] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    /*
     * Проверка сделана блоками: горизонтальные линии, вертикальные линии,
     * главные диагонали (слева сверху вправо вниз) и второстепенные диагонали (слева снизу вправо вверх).
     * Можно вынести блоки горизонтальных и вертикальных проверок в один метод, диагоналей - в другой:
     * это позволит сократить код в два раза
     * */
    private boolean checkWinLine(char player) {
        byte lineLen;
        // проверка горизонтальных линий
        for (int i = 0; i < mapSizeY; i++) {
            lineLen = 0;
            for (int j = 0; j < mapSizeX; j++) {
                if (map[i][j] == player) {
                    lineLen++;
                    if (lineLen == winLineLen)
                        return true;
                } else {
                    lineLen = 0;
                    if (j > mapSizeX - winLineLen - 1) break;
                }
            }
        }

        // проверка вертикальных линий
        for (int i = 0; i < mapSizeY; i++) {
            lineLen = 0;
            for (int j = 0; j < mapSizeX; j++) {
                if (map[j][i] == player) {
                    lineLen++;
                    if (lineLen == winLineLen)
                        return true;
                } else {
                    lineLen = 0;
                    if (j > mapSizeX - winLineLen - 1) break;
                }
            }
        }

        // проверка главных диагоналей
        for (int k = 0; k < mapSizeY - winLineLen + 1; k++) {
            for (int j = 0; j < mapSizeX - winLineLen + 1; j++) {
                lineLen = 0;
                for (int i = 0; i < mapSizeX; i++) {

                    // чтобы проверка не вылетала за пределы поля
                    if (i + j >= mapSizeX || i + k >= mapSizeY) break;

                    if (map[i + k][i + j] == player) {
                        lineLen++;
                        if (lineLen == winLineLen)
                            return true;
                    } else {
                        lineLen = 0;
                        if (i > mapSizeX - winLineLen - 1) break;
                    }
                }
            }
        }

        // проверка обратных диагоналей
        for (int k = 0; k < mapSizeX - winLineLen + 1; k++) {
            for (int j = 0; j < mapSizeY - winLineLen + 1; j++) {
                lineLen = 0;
                for (int i = 0; i < mapSizeX; i++) {

                    // чтобы проверка не вылетала за пределы поля
                    if (i + j >= mapSizeY || i + 1 + k > mapSizeX) break;

                    if (map[i + j][mapSizeX - i - 1 - k] == player) {
                        lineLen++;
                        if (lineLen == winLineLen)
                            return true;
                    } else {
                        lineLen = 0;
                        if (i > mapSizeX - winLineLen - 1) break;
                    }
                }
            }
        }

        return false;
    }

    private boolean checkPreWinState(char player) {
        for (int i = 0; i < mapSizeY; i++) {
            for (int j = 0; j < mapSizeX; j++) {
                if (map[i][j] == EMPTY) {
                    map[i][j] = player;
                    if (checkWinLine(player)) {
                        map[i][j] = AI;
                        return true;
                    }
                    map[i][j] = EMPTY;
                }
            }
        }
        return false;
    }
}
