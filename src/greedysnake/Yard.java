/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greedysnake;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * head
 *
 * @author pigeon
 */
final class Yard {

    //discribe grids
    static final int COLUMN_NUMBER = 800;
    static final int ROW_NUMBER = 600;
    static final int BLOCK_SIZE = 20;

    //background canvas
    private JPanel backgroundCanvas;
    //game objects
    private Snake snake;
    private Egg egg;
    private boolean checkCollision = false;
    boolean lose = false;

    Yard() {
        super();
        init();
        playBackgroundMusic();
    }

    void init() {

        //initialize game objects 
        JFrame backgroundYard = new JFrame();
        snake = new Snake();
        egg = new Egg();
        //new Thread(new PaintThread()).start();
        backgroundYard.pack();

        backgroundYard.setTitle("Greedy Snake");
        backgroundYard.setSize(COLUMN_NUMBER, ROW_NUMBER);
        backgroundYard.setLocationRelativeTo(null);
        backgroundYard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //backgroundYard.setResizable(false);
        //backgroundYard.pack();
        // paint background lines

        this.backgroundCanvas = backgroundPaint();

        backgroundYard.getContentPane().setLayout(new BorderLayout());
        backgroundYard.getContentPane().add(backgroundCanvas, BorderLayout.CENTER);

        backgroundYard.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                switch (key) {
                    case KeyEvent.VK_UP:
                        snake.moveUp();
                        break;
                    case KeyEvent.VK_DOWN:
                        snake.moveDown();
                        break;
                    case KeyEvent.VK_LEFT:
                        snake.moveLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        snake.moveRight();
                        break;
                }
            }
        });

        //backgroundYard.pack();
        backgroundYard.setVisible(true);
    }

    private JPanel backgroundPaint() {
        final class BackgroundCanvas extends JPanel implements ActionListener, Runnable {

            final Timer timer = new Timer(50, this);

            BackgroundCanvas() {
                init();
            }

            private void init() {
                timer.start();
                //new Thread(this).start();
                setDoubleBuffered(true);
            }

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                drawSprites(g);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("actionPerfomed");
                lose = checkLose();
                Directions dir = snake.getDir();
                switch (dir) {
                    case U:
                        snake.moveUp();
                        //snake.moveUp();
                        break;
                    case D:
                        snake.moveDown();
                        //snake.moveDown();
                        break;
                    case L:
                        //snake.moveLeft();
                        snake.moveLeft();
                        break;
                    case R:
                        snake.moveRight();
                        //snake.moveRight();
                        break;
                }
                repaint();

            }

            private void drawSprites(Graphics g) {

                Graphics2D g2d = (Graphics2D) g;

                Color c;
                c = g2d.getColor();
                g2d.setColor(Color.gray);
                g2d.fillRect(0, 0, COLUMN_NUMBER, ROW_NUMBER);
                g2d.setColor(c);

                //paint row lines
                for (int i = 0; i < COLUMN_NUMBER * BLOCK_SIZE; i++) {
                    g2d.drawLine(0, BLOCK_SIZE * i, COLUMN_NUMBER, BLOCK_SIZE * i);
                }
                // paint column lines
                for (int i = 0; i < COLUMN_NUMBER * BLOCK_SIZE; i++) {
                    g2d.drawLine(BLOCK_SIZE * i, 0, BLOCK_SIZE * i, COLUMN_NUMBER);
                }
                //repaint();
                //System.out.println("paintMethod");

                if (!lose) {
                    drawGameObjects(g2d);
                } else {
                    drawGameOver(g2d);
                }
                Toolkit.getDefaultToolkit().sync();

            }

            private boolean checkLose() {

                return beyondBound() || bodyCollision();
            }

            private boolean beyondBound() {
                //if beyond bounds
                if (snake.getHead().x < 0 || snake.getHead().x > COLUMN_NUMBER - 2 * BLOCK_SIZE
                        || snake.getHead().y < 0 || snake.getHead().y > ROW_NUMBER - 2 * BLOCK_SIZE) {
                    return true;
                }

                return false;
            }

            private boolean bodyCollision() {
                //if collision body part
                return snake.checkBodyCollision();
            }

            void drawGameOver(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.drawString("Game Over", 500, 50);
                ImageIcon gameOver = new ImageIcon("resources/gameOver.jpg");
                //g2d.drawImage(gameOver.getImage(), 0, 0, 800, 600, null);
                g.drawString(String.valueOf(snake.getScore()), 50, 50);
                timer.stop();

            }

            @Override
            public void run() {
                while (true) {
                    lose = checkLose();
                    Directions dir = snake.getDir();
                    switch (dir) {
                        case U:
                            snake.moveUp();
                            //snake.moveUp();
                            break;
                        case D:
                            snake.moveDown();
                            //snake.moveDown();
                            break;
                        case L:
                            //snake.moveLeft();
                            snake.moveLeft();
                            break;
                        case R:
                            snake.moveRight();
                            //snake.moveRight();
                            break;
                    }
                    this.repaint();

                    //this.repaint();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Yard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }

        this.backgroundCanvas = new BackgroundCanvas();
        //set LayoutManager
        backgroundCanvas.setLayout(new FlowLayout(FlowLayout.CENTER));

        return backgroundCanvas;
    }

    void drawGameObjects(Graphics g) {

        checkCollision = snake.checkCollision(egg);
        if (checkCollision == true) {

            //snake.addToBody(egg);
            snake.addToBody(egg);
            //snake.addToTail();
            //snake.addBodyLength();
            egg.generateNewEgg();
            checkCollision = false;
            int score = snake.getScore();
            score += 5;
            snake.setScore(score);
        }

        snake.draw(g);
        egg.drawEgg(g);
        this.drawString(g);
        this.backgroundCanvas.repaint();

    }

    private void playBackgroundMusic() {
        //PlayBackgroundMusic backGroundMusic = new PlayBackgroundMusic("./resources/1.wav"); 
        //new Thread(backGroundMusic).start();
    }

    private void drawString(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawString(String.valueOf(snake.getScore()), 50, 50);
    }

    private final class PlayBackgroundMusic implements Runnable {

        private final File file;
        private Clip clip;

        private PlayBackgroundMusic(String filePath) {
            file = new File(filePath);
        }

        @SuppressWarnings("SleepWhileInLoop")
        @Override
        public void run() {

            try {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
                do {
                    Thread.sleep(500);
                } while (clip.isActive());

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException ex) {
                Logger.getLogger(Yard.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                clip.stop();
            }

        }
    }

    private final class PaintThread implements Runnable {

        @Override
        public void run() {
            Yard.this.backgroundCanvas.repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Yard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
