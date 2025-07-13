package main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {
    private boolean running;
    private BufferedImage image;
    private BufferedImage backgroundImage;
    private Graphics2D g;
    private MyMouseMotionListener theMouseListener;
    private int mousex;

    private Ball theBall;
    private Paddle thePaddle;
    private Map theMap;
    private Score theScore;
    private ArrayList<explosion> explosion;

    private enum GameState {
        MENU, PLAYING, EXIT
    }

    private GameState gameState = GameState.MENU;
    private Rectangle startButton = new Rectangle(200, 100, 200, 80);
    private Rectangle exitButton = new Rectangle(200, 200, 200, 80);

    public GamePanel() {
        setPreferredSize(new Dimension(BBMain.WIDTH, BBMain.HEIGHT));
        init();
        loadBackgroundImage();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/assets/game-background.jpg"));
            if (backgroundImage != null) {
                System.out.println("[DEBUG] Background image loaded successfully.");
            } else {
                System.err.println("[ERROR] Background image not found!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        mousex = 0;
        theBall = new Ball();
        thePaddle = new Paddle();
        theScore = new Score();
        theMap = new Map(6, 12);
        theMouseListener = new MyMouseMotionListener();
        explosion = new ArrayList<>();
        addMouseMotionListener(theMouseListener);

        running = true;
        image = new BufferedImage(BBMain.WIDTH, BBMain.HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    @Override
    public void run() {
        while (running) {
            switch (gameState) {
                case MENU:
                    drawMenu();
                    break;
                case PLAYING:
                    update();
                    draw();
                    break;
                case EXIT:
                    running = false;
                    break;
            }
            repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleMouseClick(int mouseX, int mouseY) {
        if (gameState == GameState.MENU) {
            if (startButton.contains(mouseX, mouseY)) {
                gameState = GameState.PLAYING;
            } else if (exitButton.contains(mouseX, mouseY)) {
                gameState = GameState.EXIT;
                running = false;
            }
        }
    }

    public void checkCollisions() {
        Rectangle ballRect = theBall.getRect();
        Rectangle paddleRect = thePaddle.getRect();

        if (ballRect.intersects(paddleRect)) {
            theBall.setDY(-theBall.getDY());
            if (theBall.getX() < mousex + thePaddle.getWidth() / 4) {
                theBall.setDX(theBall.getDX() - 0.5);
            } else {
                theBall.setDX(theBall.getDX() + 0.5);
            }
        }

        int[][] theMapArray = theMap.getMapArray();

        A: for (int row = 0; row < theMapArray.length; row++) {
            for (int col = 0; col < theMapArray[0].length; col++) {
                if (theMapArray[row][col] > 0) {
                    int brickx = col * theMap.getBrickWidth() + theMap.HOR_PAD;
                    int bricky = row * theMap.getBrickHeight() + theMap.VERT_PAD;
                    Rectangle brickRect = new Rectangle(brickx, bricky, theMap.getBrickWidth(), theMap.getBrickHeight());

                    if (ballRect.intersects(brickRect)) {
                        if (theMapArray[row][col] == 1) {
                            explosion.add(new explosion(brickx, bricky, theMap));
                        }
                        theMap.hitBrick(row, col);
                        theBall.setDY(-theBall.getDY());
                        theScore.addScore(10);
                        break A;
                    }
                }
            }
        }
    }

    public void update() {
        checkCollisions();
        theBall.update();

        for (int i = 0; i < explosion.size(); i++) {
            explosion.get(i).update();
            if (!explosion.get(i).getIsActive()) {
                explosion.remove(i);
            }
        }
    }

    public void draw() {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, BBMain.WIDTH, BBMain.HEIGHT);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, BBMain.WIDTH, BBMain.HEIGHT, null);
        }

        theBall.draw(g);
        thePaddle.draw(g);
        theMap.draw(g);
        theScore.draw(g);

        if (theMap.isThereAWin()) {
            draWin();
            running = false;
        }
        if (theBall.youLose()) {
            drawLoser();
            running = false;
        }

        for (explosion bs : explosion) {
            bs.draw(g);
        }
    }

    private void drawMenu() {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, BBMain.WIDTH, BBMain.HEIGHT);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, BBMain.WIDTH, BBMain.HEIGHT, null);
        }

        Stroke originalStroke = g.getStroke();
        g.setStroke(new BasicStroke(7));

        g.setColor(new Color(139, 0, 0));
        g.setFont(new Font("Castellar", Font.BOLD, 50));
        g.draw(startButton);
        g.drawString("Start", 213, 160);
        g.draw(exitButton);
        g.drawString("Exit", 220, 250);

        g.setStroke(originalStroke);
    }

    public void draWin() {
        g.setColor(new Color(0, 128, 0));
        g.setFont(new Font("Broadway", Font.BOLD, 50));
        g.drawString("WINNER!!", 200, 200);
    }

    public void drawLoser() {
        g.setColor(new Color(139, 0, 0));
        g.setFont(new Font("Broadway", Font.BOLD, 30));
        g.drawString("Better Luck Next Time!", 140, 260);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, BBMain.WIDTH, BBMain.HEIGHT, null);
    }

    private class MyMouseMotionListener implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {
            mousex = e.getX();
            thePaddle.mouseMoved(e.getX());
        }
    }
}
