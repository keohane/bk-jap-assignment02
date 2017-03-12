/**
 * File name: Client.java
 * Author: Brandon Keohane 040719123
 * Course: CST8221 - JAP, Lab Section: 302
 * Assignment: Assignment 2 p2
 * Date: December 9th, 2016
 * Professor: Svillen Ranev
 * Purpose: Runs the client application
 * Class list: Client
 */
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;

/**
 * Class sets up runs client program
 * @author Brandon Keohane
 * @version v1.0
 * @see javax.swing.JFrame
 * @since 1.8.0_73
 */
public class Client {
    public static void main(String args[]){
        EventQueue.invokeLater( () ->{
            // Instantiate new frame with title
            JFrame frame = new JFrame("Brandon's Client");
            // Set close when exit is clicked
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // Set minimum size of frame
            frame.setMinimumSize(new Dimension(600, 550));
            // Add panel to the frame (default center of border layout)
            frame.add(new ClientView());
            // Done all modifications set frame visible
            frame.setVisible(true);
        });
    }
}
