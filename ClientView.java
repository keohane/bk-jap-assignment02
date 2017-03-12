/**
 * File name: ClientView.java
 * Author: Brandon Keohane 040719123
 * Course: CST8221 - JAP, Lab Section: 302
 * Assignment: Assignment 2 p2
 * Date: December 9th, 2016
 * Professor: Svillen Ranev
 * Purpose: Paints the client view to the panel
 * Class list: ClientView, ClientSocketRunnable
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

/**
 * Class puts components in panel
 * @author Brandon Keohane
 * @version v1.0
 * @see javax.swing.JPanel
 * @since 1.8.0_73
 */
public class ClientView extends JPanel implements ActionListener {
    
    /** Action command for the connect button: {@value}*/
    private static final String CONNECT_AC = "CONNECT_AC";
    /** Action command for the send button: {@value}*/
    private static final String SEND_AC = "SEND_AC";
    
    /* Port combo box to display default port numbers */
    private JComboBox<String> portCombo;
    /* Terminal area where server messages appear */
    private JTextArea terminalArea;
    /* Holds host name */
    private JTextField hostTextField;
    /* Holds client requests to send to the server */
    private JTextField serverRL;
    /* Client socket runnable to manage the client connection */
    private ClientSocketRunnable csRunner;
    /* Used to connect to the server */
    private JButton connectButton;
    /* Used to send the server request to the server */
    private JButton sendButton;
    
    /**
     * Instantiates this client view panel
     */
    public ClientView(){
        // Replace flow with border
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(4, 2, 4, 2));
        
        // Ports to choose from
        String ports[] = new String[] { "", "8088", "65000", "65535" };
        // Grid bag properties
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Instantiates container structure
        JPanel topLevelNorthPanel = new JPanel(new BorderLayout());
        JPanel topLevelCenterPanel = new JPanel(new BorderLayout());
        JPanel setConnectionPanel = new JPanel(new BorderLayout());
        JPanel clientRequestPanel = new JPanel(new BorderLayout());
        JPanel setConnectionGridBag = new JPanel(new GridBagLayout());
        JPanel clientRequestGridBag = new JPanel(new GridBagLayout());
        
        // Host label
        JLabel hostLabel = new JLabel("Host:");
        hostLabel.setDisplayedMnemonic('H');
        hostLabel.setPreferredSize(new Dimension(40, 40));
        // Port label
        JLabel portLabel = new JLabel("Port:");
        portLabel.setDisplayedMnemonic('P');
        portLabel.setPreferredSize(new Dimension(40, 40));
        // Host name text field
        hostTextField = new JTextField("localhost");
        hostLabel.setLabelFor(hostTextField);
        // Server requesr line text field
        serverRL = new JTextField("Type a server request line");
        serverRL.setPreferredSize(new Dimension(468, 20));
        // Connect button
        connectButton = new JButton("Connect");
        connectButton.setActionCommand(CONNECT_AC);
        connectButton.addActionListener(this);
        connectButton.setMnemonic('C');
        connectButton.setBackground(Color.red);
        connectButton.setPreferredSize(new Dimension(90, 20));
        // Send button
        sendButton = new JButton("Send");
        sendButton.setActionCommand(SEND_AC);
        sendButton.addActionListener(this);
        sendButton.setMnemonic('S');
        sendButton.setEnabled(false);
        sendButton.setPreferredSize(new Dimension(70, 20));
        // Port combo box 
        portCombo = new JComboBox<>(ports);
        portLabel.setLabelFor(portCombo);
        portCombo.setEditable(true);
        portCombo.setBackground(Color.WHITE);
        portCombo.setPreferredSize(new Dimension(90, 20));
        // Terminal text area with scroll bar
        terminalArea = new JTextArea();
        terminalArea.setEditable(false);
        terminalArea.setRows(50);
        JScrollPane scrollPane = new JScrollPane(terminalArea);
        
        // Set border to each panel
        setConnectionPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.RED, 10), 
                        "SET CONNECTION"
                )
        );
        clientRequestPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.BLACK, 10), 
                        "CLIENT REQUEST"
                )
        );
        topLevelCenterPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.BLUE, 10), 
                        "TERMINAL", 
                        TitledBorder.CENTER, 
                        TitledBorder.CENTER
                )
        );
        
        // Adding components to grid bag
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 4, 0, 0);
        setConnectionGridBag.add(hostLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        setConnectionGridBag.add(hostTextField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 4, 0, 0);
        setConnectionGridBag.add(portLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        setConnectionGridBag.add(portCombo, gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 4, 0, 0);
        setConnectionGridBag.add(connectButton, gbc);
        setConnectionGridBag.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        // Adding components to client request grid bag
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 0, 4, 4);
        clientRequestGridBag.add(serverRL, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.insets = new Insets(4, 0, 4, 0);
        clientRequestGridBag.add(sendButton, gbc);
        clientRequestGridBag.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
        
        // Add components to there proper place
        setConnectionPanel.add(setConnectionGridBag, BorderLayout.NORTH);
        topLevelNorthPanel.add(setConnectionPanel, BorderLayout.NORTH);
        topLevelNorthPanel.add(clientRequestPanel, BorderLayout.SOUTH);
        topLevelCenterPanel.add(scrollPane, BorderLayout.CENTER);
        clientRequestPanel.add(clientRequestGridBag, BorderLayout.WEST);
        
        add(topLevelNorthPanel, BorderLayout.NORTH);
        add(topLevelCenterPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            // Connect button
            case CONNECT_AC:
                // Port to create socket on
                int port;
                // Host name to connect to
                String hostname = hostTextField.getText().trim();
                // Socket to pass to runnable
                Socket socket;
                
                // Check if host name is not empty
                if (hostname.isEmpty()) {
                    return;
                }
                // Convert port string to int
                try {
                    port = Integer.parseUnsignedInt((String)portCombo.getSelectedItem());
                } catch (NumberFormatException nfe){
                    return;
                }

                try {
                    // Holds address of server to connect to
                    InetAddress address = InetAddress.getByName(hostname);
                    // Instantiates new socket
                    socket = new Socket();
                    // Attempts connection to server with a timeout of 10 seconds
                    socket.connect(new InetSocketAddress(address, port), 10000);
                } catch (UnknownHostException uhe){
                    terminalArea.append("CLIENT>ERROR: Unknown Host.\n");
                    return;
                } catch (IllegalArgumentException | IOException io){
                    terminalArea.append("CLIENT>ERROR: Connection refused: "
                            + "server is not avaliable. Check port or restart server.\n");
                    return;
                }
                
                terminalArea.append(String.format(
                                "Connecting to Socket[addr=/%s,port=%d,localport=%d]\n", 
                                socket.getInetAddress(), 
                                socket.getPort(), 
                                socket.getLocalPort())
                );
                
                csRunner = new ClientSocketRunnable(socket);
                Thread thread = new Thread(csRunner);
                thread.start();
                break;
            // Send button 
            case SEND_AC:
                if (!serverRL.getText().isEmpty())
                    csRunner.send(serverRL.getText());
                break;
            default:
                
                break;
        }
    }
    
    /**
     * Updates UI to either connected state or not connected state
     * @param connected true if connected, false if not.
     */
    public void setConnected(boolean connected){
        SwingUtilities.invokeLater(()->{
            if (connected){
                connectButton.setBackground(Color.BLUE);
                connectButton.setEnabled(false);
                sendButton.setEnabled(true);
            }
            else {
                connectButton.setBackground(Color.RED);
                connectButton.setEnabled(true);
                sendButton.setEnabled(false);
            }
        });
    }
    
    /**
     * sends the text to the terminal area and appends it 
     * @param s string to append to terminal area
     */
    public synchronized void sendTextToTerminalArea(String s){
        SwingUtilities.invokeLater(()->{
            terminalArea.append(s + "\n");
        });
    }
    
    /**
     * Clears the terminal area
     */
    public void clearTerminalArea(){
        SwingUtilities.invokeLater(()->{
            terminalArea.setText("");
        });
    }
    
    /**
    * Class handles all client requests
    * @author Brandon Keohane
    * @version v1.0
    * @see java.lang.Runnable
    * @since 1.8.0_73
    */
    private class ClientSocketRunnable implements Runnable {

        /* Socket to communicate to server on */
        private Socket socket;
        /* Allows you to recieve objects from client */
        private ObjectInputStream in;
        /* Allows you to send objects to server */
        private ObjectOutputStream out;
        /* Holds the response from the server */
        private String response;
        
        /**
         * Creates a server socket runnable that a thread runs 
         * @param socket socket of server
         */
        public ClientSocketRunnable(Socket socket){
            this.socket = socket;
        }
        
        @Override
        public void run() {
            // If connection was not successful
            if (socket == null) 
                return;
            
            // Gather input stream
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ioe){
                sendTextToTerminalArea("Error: could not connect to the server.");
                return;
            }
            
            // Update UI to show that client is connected to server
            setConnected(true);
            
            // Run the application until user cancels the application or server shuts down
            while (true){
                try {
                    // Get object from stream
                    response = (String) in.readObject();
                    // Response is not null 
                    if (response != null) {
                        if (response.equals("SERVER>Connection closed.")) {
                            sendTextToTerminalArea("CLIENT>Connection closed.");
                            break;
                        } else if (response.equals("SERVER>CLS:")) {
                            // Clear terminal
                            clearTerminalArea();
                        } else {
                            // Print response to terminal
                            sendTextToTerminalArea(response);
                        }
                    }
                    // Reset response variable
                    response = null;
                } catch (IOException ex) {
                    sendTextToTerminalArea("Error: connection with server was lost");
                    break;
                } catch (ClassNotFoundException ex) {
                    sendTextToTerminalArea("Error: connection with server was lost");
                    break;
                }
            }
            // Update UI telling user connection has closed
            sendTextToTerminalArea("SERVER>Connection closed.");
            // Clean up the thread and streams
            clean();
        }
        
        /**
         * Sends a request message to the connected server
         * @param command command to send to the server
         */
        public void send(String command){
            if (command != null && !command.isEmpty()){
                try {
                    out.writeObject(command);
                    out.flush();
                } catch (IOException ex) {
                    sendTextToTerminalArea("Error: request could not be sent");
                }
            }
        }
        
        /**
         * Cleans up server sockets and streams
         */
        public void clean(){
            try {
                if (socket != null)
                    socket.close();
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (IOException ioe){
                sendTextToTerminalArea("Error: Could not clean up sockets and streams.");
            } finally {
                setConnected(false);
            }
        }
        
    }
}
