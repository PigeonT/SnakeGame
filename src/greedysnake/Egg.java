/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greedysnake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author pigeon
 */
final class Egg {

    private int x, y;

    Egg() {
        initializeEgg();
    }

    private void initializeEgg() {
        this.x = Yard.BLOCK_SIZE * 10;
        this.y = Yard.BLOCK_SIZE * 10;
    }

    Rectangle getBound() {
        return new Rectangle(x, y, Yard.BLOCK_SIZE, Yard.BLOCK_SIZE);
    }

    void drawEgg(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        ImageIcon image = new ImageIcon("resources/egg.jpg");
        g2d.drawImage(image.getImage(), x, y, Yard.BLOCK_SIZE, Yard.BLOCK_SIZE, null);
        Toolkit.getDefaultToolkit().sync();

    }

    void generateNewEgg() {
        Random randomNr = new Random();
        this.x = Yard.BLOCK_SIZE * randomNr.nextInt(39);
        this.y = Yard.BLOCK_SIZE * randomNr.nextInt(29);
    }

    int getX() {
        return this.x;
    }

    int getY() {
        return this.y;
    }

}
