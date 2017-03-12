
/**
 * File name: ServerSocketRunnable.java
 * Author: Brandon Keohane 040719123
 * Course: CST8221 - JAP, Lab Section: 302
 * Assignment: Assignment 2 p2
 * Date: December 9th, 2016
 * Professor: Svillen Ranev
 * Purpose: Interacts with client and handles requests
 * Class list: ServerSocketRunnable
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;

/**
 * Class sets up runs server program
 *
 * @author Brandon Keohane
 * @version v1.0
 * @see java.lang.Runnable
 * @since 1.8.0_73
 */
public class ServerSocketRunnable implements Runnable {

    /** Used to check if command sent by client is a date command: value({@value})*/
    private static final String C_DATE = "-date";
    /** Used to check if command sent by client is a quit command: value({@value})*/
    private static final String C_QUIT = "-quit";
    /** Used to check if command sent by client is a echo command: value({@value})*/
    private static final String C_ECHO = "-echo";
    /** Used to check if command sent by client is a time command: value({@value})*/
    private static final String C_TIME = "-time";
    /** Used to check if command sent by client is a help command: value({@value})*/
    private static final String C_HELP = "-help";
    /** Used to check if command sent by client is a clear command: value({@value})*/
    private static final String C_CLS = "-cls";

    /**
     * Months of the year
     */
    private final String months[] = {
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    };

    // Socket of client
    private Socket socket;

    /**
     * Constructs a server socket runnable class
     * @param socket socket used to communicate with client
     */
    ServerSocketRunnable(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // Input stream used to listen for commands
        ObjectInputStream in = null;
        // Output stream used to send clients information
        ObjectOutputStream out = null;
        // String to hold command
        String command, optional, reply = null;
        // Calendar used for time and date
        LocalDateTime datetime = LocalDateTime.now();

        // If socket passed was null
        if (socket == null) {
            System.out.println("Connection with client failed.");
            return;
        }

        // Obtain input and output stream from client socket
        try {
            // Get output stream from client socket
            out = new ObjectOutputStream(socket.getOutputStream());
            // Get input stream from client socket
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ioe) {
            System.out.println("Error retrieving sockets from client.");
            return;
        }

        // Listen for commands to process from client
        while (true) {
            try {
                // Read object from user
                command = (String) in.readObject();
                // If proper command
                if (command != null && command.startsWith("-")) {
                    /* QUIT COMMAND */
                    if (command.startsWith(C_QUIT)) 
                    {
                        // optional command to hold tbe optional string
                        optional = command.replace(C_QUIT, "").trim();
                        // If it has a optional string 
                        if (optional.startsWith("-") || optional.isEmpty()) {
                            // Quit the server program
                            break;
                        }
                    } 
                    /* ECHO COMMAND */ 
                    else if (command.startsWith(C_ECHO)) 
                    {
                        // optional command to hold tbe optional string
                        optional = command.replace(C_ECHO, "");
                        if (optional.startsWith("-") || optional.isEmpty()) {
                            optional = optional.replaceFirst(C_ECHO, "");
                            // If still starts with a '-' remove it and send the echo
                            if (optional.startsWith("-")) optional = optional.replaceFirst("-", "");
                            // Build reply to send to client
                            reply = "SERVER>ECHO:" + optional;
                        }
                    } 
                    /* TIME COMMAND */ 
                    else if (command.startsWith(C_TIME)) 
                    {
                        // optional command to hold tbe optional string
                        optional = command.replace(C_TIME, "");
                        // optional command parameter with correct syntax
                        if (optional.startsWith("-") || optional.isEmpty()) {
                            datetime = LocalDateTime.now();
                            reply = String.format("SERVER>TIME: %02d:%02d:%02d %s", 
                                    datetime.getHour() % 12 == 0 ? 12 : datetime.getHour() % 12,
                                    datetime.getMinute(), datetime.getSecond(),
                                    datetime.getHour() > 11 ? "PM" : "AM");
                        }

                    } 
                    /* DATE COMMAND */ 
                    else if (command.startsWith(C_DATE)) 
                    {
                        // optional command to hold tbe optional string
                        optional = command.replace(C_DATE, "");
                        // optional command parameter with correct syntax
                        if (optional.startsWith("-") || optional.isEmpty()) {
                            datetime = LocalDateTime.now();
                            reply = "SERVER>DATE: " + datetime.getDayOfMonth() + " "
                                    + months[datetime.getMonthValue()-1] + " " + datetime.getYear();
                        }
                    } 
                    /* HELP COMMAND */ 
                    else if (command.startsWith(C_HELP)) 
                    {
                        // optional command to hold tbe optional string
                        optional = command.replace(C_HELP, "").trim();
                        // optional command parameter with correct syntax
                        if (optional.startsWith("-") || optional.isEmpty()) {
                            reply = "SERVER>Avaliable Services:\n"
                                    + "quit\n" + "echo\n" + "time\n"
                                    + "date\n" + "help\n" + "cls\n";
                        }
                    } 
                    /* CLEAR COMMAND */ 
                    else if (command.startsWith(C_CLS)) 
                    {
                        // optional command to hold tbe optional string
                        optional = command.replace(C_CLS, "");
                        // optional command parameter with correct syntax
                        if (optional.startsWith("-") || optional.isEmpty()) {
                            reply = "SERVER>CLS:";
                        }
                    } /* INCORRECT COMMAND */ 
                    
                } 
                

                // Send reply to client
                if (reply != null) 
                {
                    out.writeObject(reply);
                }
                // Wrong command was sent and reply never got set
                else 
                {
                    out.writeObject("SERVER>ERROR: Unrecognized command.");
                }

                // Reset variables
                reply = null;
                optional = null;
                command = null;

                // Sleep for 100 milliseconds before accepting another 100 miliseconds
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }

            } catch (IOException ioe) {
                // Break loop and notify client connection was interrupted
                break;
            } catch (ClassNotFoundException cnfe) {
                // Break loop and notify client connection was interrupted
                break;
            }
        } // END WHILE

        // Notify client of connection ending if connection wasnt closed 
        try {
            // If output stream is not closed
            if (out != null) {
                // Notify client of the server shutting down
                out.writeObject("SERVER>Connection closed.");
                // Flush string down the output stream
                out.flush();
                // Close stream and socket
                out.close();
            }
            // Close input stream
            if (in != null) {
                in.close();
            }
            // Close socket
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            System.out.println("Server Socket: Closing client connection...");
        }
    } // END RUN METHOD
}
