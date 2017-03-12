/**
 * File name: Client.java
 * Author: Brandon Keohane 040719123
 * Course: CST8221 - JAP, Lab Section: 302
 * Assignment: Assignment 2 p2
 * Date: December 9th, 2016
 * Professor: Svillen Ranev
 * Purpose: Runs the client application
 * Class list: Server
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;

/**
 * Class sets up runs server program
 * @author Brandon Keohane
 * @version v1.0
 * @see javax.swing.JFrame
 * @since 1.8.0_73
 */
public class Server extends JFrame {
    
    public static void main(String args[]){
        // Server socket to listen on
        ServerSocket sSocket = null;
        // Client socket returned by accept
        Socket socket = null;
        // Server socket runnable
        ServerSocketRunnable sSocketRunner = null;
        // Thread to run runnable
        Thread thread = null;
        // Port this server listens on
        int port = 65535;
        // Status update for displaying 
        String status;
        
        // If port was passed through command line
        if (args.length > 0){
            // Convert port and store in variable
            port = Integer.parseInt(args[0]);
            status = "Using port: " + port;
        }
        else {
            status = "Using default port: " + port;
        }
        
        // Create server socket
        try {
            // Bind the server socket to the port specified
            sSocket = new ServerSocket(port);
        }
        // If it was an illegal port
        catch(IllegalArgumentException iae){ System.out.println("Could not start server on port."); return; } 
        // If a error occured while opening the socket
        catch(IOException ioe){ System.out.println("Could not open socket."); return; }
        
        // Connection successful print status update
        System.out.println(status);
        
        // Error occured when creating socket
        if (sSocket != null){
            // Endless loop calling accept
            while (true){
                try {
                    // Wait for clients to connect
                    socket = sSocket.accept();
                    // If connection was successful
                    if (socket != null){
                        System.out.printf(
                                "Connecting to a client Socket[addr=/%s,port=%d,localport=%d]\n", 
                                socket.getInetAddress(), 
                                socket.getPort(), 
                                socket.getLocalPort()
                        );
                        // Passing new socket to instance of server runnable
                        sSocketRunner = new ServerSocketRunnable(socket);
                        // Create thread to run server runner
                        thread = new Thread(sSocketRunner);
                        // Start the thread
                        thread.start();
                    }
                }
                // Error occured while listening for clients
                catch(IOException ioe){
                    System.out.println("Error occured while listening for clients.");
                    break;
                }
            } // END WHILE
        } // IF END
    } // END MAIN
    
} // END CLASS
