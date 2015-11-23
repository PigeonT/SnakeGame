/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greedysnake;

import static greedysnake.Directions.D;
import static greedysnake.Directions.L;
import static greedysnake.Directions.R;
import static greedysnake.Directions.U;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.List;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author pigeon
 */
final class Snake {

    private final List<Node> body = new ArrayList<>();
    private Node head;
    private Node tail;
    private int size;
    private Directions dir;
    private int score = 0;

    Snake() {
        super();

        this.size = 2;
        dir = L;
        head = new Node(Yard.COLUMN_NUMBER / 2, Yard.ROW_NUMBER / 2);
        head.setPrev(null);

        tail = new Node(Yard.COLUMN_NUMBER / 2 + Yard.BLOCK_SIZE, Yard.ROW_NUMBER / 2);
        head.setNext(tail);
        tail.setNext(null);
        tail.setPrev(head);

        body.add(head);
        body.add(tail);
    }

    // add body length of this snake, this added node will be in the tail and become the new tail
    void addBodyLength() {
        int newAddedNodeX = 10, newAddedNodeY = 10;

        Node newNode = new Node(newAddedNodeX, newAddedNodeY);

        body.add(newNode);
        tail = newNode;
        size++;
    }

    // draw whole snake body
    void draw(Graphics g) {
        for (int i = 0; i < this.body.size(); i++) {
            this.body.get(i).paint(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    int getSize() {
        return this.size;
    }

    Node getHead() {
        return head;
    }

    void addToHead() {
        Node newNode = null;
        switch (dir) {
            case U:
                newNode = new Node(head.x, head.y - Yard.BLOCK_SIZE);
                break;
            case D:
                newNode = new Node(head.x, head.y + Yard.BLOCK_SIZE);

                break;
            case L:
                newNode = new Node(head.x - Yard.BLOCK_SIZE, 0);

                break;
            case R:
                newNode = new Node(head.x + Yard.BLOCK_SIZE, 0);

                break;
        }
        head = newNode;
        head.prev = null;
        head.next = body.get(0);
        body.get(1).prev = head;
        body.add(0, head);
        //body.add(0, newNode);
        //tail = body.get(body.size() - 1);
        size++;
    }

    void addToTail() {
        Node newNode = new Node(tail.x, tail.y);

        size++;
    }

    void deleteTail() {
        body.remove(body.size() - 1);
    }

    void moveUp() {
        if (dir == D) {
            return;
        }
        dir = U;

        Node newNode = new Node(head.x, head.y - Yard.BLOCK_SIZE);
        body.add(0, newNode);
        head = body.get(0);
        body.remove(body.size() - 1);

        //body.get(3).prev = head;
    }

    void moveDown() {

        if (dir == U) {
            return;
        }
        dir = D;
        Node newHead = new Node(head.x, head.y + Yard.BLOCK_SIZE);
        body.add(0, newHead);
        head = body.get(0);
        body.remove(body.size() - 1);
    }

    void moveLeft() {
        if (dir == R) {
            return;
        }
        dir = L;
        Node newHead = new Node(head.x - Yard.BLOCK_SIZE, head.y);
        body.add(0, newHead);
        head = body.get(0);
        body.remove(body.size() - 1);
    }

    void moveRight() {
        if (dir == L) {
            return;
        }
        dir = R;
        Node newHead = new Node(head.x + Yard.BLOCK_SIZE, head.y);
        body.add(0, newHead);
        head = body.get(0);
        body.remove(body.size() - 1);

    }

    Node getTail() {
        return tail;
    }

    Rectangle getHeadBounds() {
        return new Rectangle(head.x, head.y, Yard.BLOCK_SIZE, Yard.BLOCK_SIZE);
    }

    List<Rectangle> getBodyBounds() {

        List<Rectangle> bodyBounds = new ArrayList<>();
        for (Node n : body) {
            bodyBounds.add(n.getNodeBound());
        }
        return bodyBounds;
    }

    boolean checkCollision(Egg egg) {
        return getHeadBounds().intersects(egg.getBound());
    }

    boolean checkBodyCollision() {
        Node newNode = null;
//        if (body.size() >= 4) {
//            switch (dir) {
//                case U:
//                    newNode = new Node(head.x, head.x - Yard.BLOCK_SIZE);
//                    break;
//                case D:
//                    newNode = new Node(head.x, head.x + Yard.BLOCK_SIZE);
//                    break;
//                case L:
//                    newNode = new Node(head.x - Yard.BLOCK_SIZE, head.y);
//                    break;
//                case R:
//                    newNode = new Node(head.x + Yard.BLOCK_SIZE, head.y);
//                    break;
//            }
        for (Node n : body) {
            if (n == head) {
                continue;
            }
            if (n.x == head.x && n.y == head.y) {
                return true;
            }
        }

        return false;
    }

    void addToBody(Egg egg) {
        Node newNode = null;
        switch (dir) {
            case U:
                newNode = new Node(egg.getX(), egg.getY() - Yard.BLOCK_SIZE);
                break;
            case D:
                newNode = new Node(egg.getX(), egg.getY() + Yard.BLOCK_SIZE);
                break;
            case L:
                newNode = new Node(egg.getX() - Yard.BLOCK_SIZE, egg.getY());
                break;
            case R:
                newNode = new Node(egg.getX() + Yard.BLOCK_SIZE, egg.getY());
                break;
        }
        //newNode = new Node(egg.getX(), egg.getY());
        head = newNode;
        body.add(0, head);
//        head.prev = null;
//        head.next = body.get(1);
//        body.get(1).prev = head;
//        body.get(1).next = body.get(2);
//        for (Node n : body) {
//            if (n == head || n == body.get(1)) {
//                continue;
//            }
//            if (n == tail) {
//                n.prev = body.get(body.size() - 2);
//                n.next = null;
//                break;
//            }
//            n.prev = body.get(body.indexOf(n) - 1);
//            n.next = body.get(body.indexOf(n) + 1);
//        }
        size++;
    }

    Directions getDir() {
        return dir;
    }

    int getScore() {
        return this.score;
    }

    void setScore(int score) {
        this.score = score;
    }

    // fill every node of this snake
    final class Node {

        private final int WIDTH = Yard.BLOCK_SIZE;
        private final int HEIGHT = Yard.BLOCK_SIZE;
        private final Color fillColor = Color.cyan;
        private Node prev;
        private Node next;
        private Directions dir;

        //up left point of each grid
        int x, y;

        private Node(int x, int y) {
            super();
            this.x = x;
            this.y = y;
        }

        private void paint(Graphics g) {
            Color c = g.getColor();
            if (this == Snake.this.head) {
                Graphics2D g2d = (Graphics2D) g;
                ImageIcon headIcon = new ImageIcon("resources/head.jpg");
                g2d.drawImage(headIcon.getImage(), x, y, Yard.BLOCK_SIZE, Yard.BLOCK_SIZE, null);
                //setPreferredSize(new Dimension(headIcon.getIconWidth(), headIcon.getIconHeight()));        
            } else {
                Graphics2D g2d = (Graphics2D) g;
                ImageIcon headIcon = new ImageIcon("resources/snakeTail.gif");
                g2d.drawImage(headIcon.getImage(), x, y, WIDTH, HEIGHT, null);
            }
        }

        private Rectangle getNodeBound() {
            return new Rectangle(x, y);
        }

        private void setPrev(Node node) {
            this.prev = node;
        }

        private void setNext(Node node) {
            this.next = node;
        }
    }

}
