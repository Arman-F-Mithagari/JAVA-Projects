import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    static final int DELAY = 100;

    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int speed = 150; // Initial speed (ms delay between movements)
    final int SPEED_MIN = 50; // Fastest
    final int SPEED_MAX = 300; // Slowest
    int bodyParts = 6;
    int foodx, foodY;
    int score = 0;
    char direction = 'R';// U,D,L,R
    boolean running = false;
    boolean gameStarted = false;
    boolean paused = false;
    Timer timer;
    Random random;

    GamePanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
        random = new Random();
        startGame();
    }

    public void startGame() {
        newFood();
        direction = 'R';
        bodyParts = 6;
        score = 0;
        running = true;
        timer = new Timer(speed, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (!gameStarted) {
            // Start screen
            g.setColor(Color.green);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Press ENTER to Start", WIDTH / 2 - 200, HEIGHT / 2);
            return;
        }

        if (paused) {
            g.setColor(Color.yellow);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Paused", WIDTH / 2 - 80, HEIGHT / 2);
            return;
        }
        if (running) {
            // To Draw food
            g.setColor(Color.red);
            g.fillOval(foodx, foodY, UNIT_SIZE, UNIT_SIZE);

            // To Show score
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString("Score: " + score, 10, 20);
            g.drawString("Speed: " + (300 - speed), 10, 40); // Higher = faster
        } else {
            gameOver(g);
        }

        // To Draw snake
        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                g.setColor(Color.green); // head of snake
            } else {
                g.setColor(Color.white); // body of snake
            }
            g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
        }
    }

    public void newFood() {
        foodx = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        foodY = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] -= UNIT_SIZE;
                break;
            case 'D':
                y[0] += UNIT_SIZE;
                break;
            case 'L':
                x[0] -= UNIT_SIZE;
                break;
            case 'R':
                x[0] += UNIT_SIZE;
                break;
        }
    }

    public void checkFood() {
        if (x[0] == foodx && y[0] == foodY) {
            bodyParts++;
            score++;
            newFood();
        }
    }

    public void checkCollision() {
        // Collide with body
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
                break;
            }
        }

        // collide with walls
        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Game Over", WIDTH / 2 - 150, HEIGHT / 2);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.drawString("Score: " + score, WIDTH / 2 - 50, HEIGHT / 2 + 40);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running && !paused && gameStarted) {
            move();
            checkFood();
            checkCollision();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT && direction != 'R') {
            direction = 'L';
        } else if (key == KeyEvent.VK_RIGHT && direction != 'L') {
            direction = 'R';
        } else if (key == KeyEvent.VK_UP && direction != 'D') {
            direction = 'U';
        } else if (key == KeyEvent.VK_DOWN && direction != 'U') {
            direction = 'D';
        } else if (key == KeyEvent.VK_P) {
            if (running) {
                timer.stop();
                running = false;
            } else {
                timer.start();
                running = true;
            }
        } else if (key == KeyEvent.VK_ENTER && !running) {
            speed = 150;
            startGame();
            repaint();
        }

        // ✅ Speed Control
        else if (key == KeyEvent.VK_Z) { // Z = slow down
            if (speed < SPEED_MAX) {
                speed += 10;
                timer.setDelay(speed);
            }
        } else if (key == KeyEvent.VK_X) { // X = speed up
            if (speed > SPEED_MIN) {
                speed -= 10;
                timer.setDelay(speed);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
