/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greedysnake;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author pigeon
 */
public final class GreedySnake {
    private Yard yard;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GreedySnake game = new GreedySnake();
            game.init();
            game.run();
            System.out.println();
        });
    }

    private void init() {
        
        yard = new Yard();
        //run();

    }

    //TODO game logic
    private void run() {
        new Thread(() -> {
            for(; ; ) {
                if(!yard.lose) {

                }else {
                   System.exit(0); 
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GreedySnake.class.getName()).log(Level.SEVERE, null, ex);
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
        });//;
    }

}
