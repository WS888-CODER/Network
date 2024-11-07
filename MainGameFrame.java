
package phase1;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainGameFrame extends javax.swing.JFrame {



    // Network components
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;

    // UI components
    private JTextField usernameField;
    private JButton connectButton;
    private JTextArea connectedClientsArea;
    private JButton playButton;
    private JButton startGameButton;  // New button to start the game
    private JPanel connectionPanel;
    private JPanel waitingRoomPanel;
    private JPanel playingRoomPanel;
    private JLabel usernameLabel;

 public MainGameFrame() {
        initComponents();
    }

  private void initComponents() {
        setTitle("Number Spy Game");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Connection Panel
        connectionPanel = new JPanel(new BorderLayout());

        // Add the username label
        usernameLabel = new JLabel("Enter your username:");
        usernameLabel.setHorizontalAlignment(JLabel.CENTER);

        // Username input field
        usernameField = new JTextField();

        // Connect button
        connectButton = new JButton("Connect");

        // Use a panel with a BoxLayout for the label and text field
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);

        connectionPanel.add(inputPanel, BorderLayout.CENTER);
        connectionPanel.add(connectButton, BorderLayout.SOUTH);

        // Waiting Room Panel
        waitingRoomPanel = new JPanel(new BorderLayout());
        connectedClientsArea = new JTextArea();
        connectedClientsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(connectedClientsArea);
        playButton = new JButton("Play");

        waitingRoomPanel.add(scrollPane, BorderLayout.CENTER);
        waitingRoomPanel.add(playButton, BorderLayout.SOUTH);
        waitingRoomPanel.setVisible(false);  // Initially hidden until connected

        // Playing Room Panel
        playingRoomPanel = new JPanel(new BorderLayout());
        startGameButton = new JButton("Start the Game");
        startGameButton.setVisible(true);  // Hidden until 4 players join the room

        playingRoomPanel.add(startGameButton, BorderLayout.SOUTH);
        playingRoomPanel.setVisible(false);  // Initially hidden until they join the playing room

        // Add panels to the frame
        add(connectionPanel, BorderLayout.NORTH);
        add(waitingRoomPanel, BorderLayout.CENTER);
        add(playingRoomPanel, BorderLayout.SOUTH);

        // Action Listeners
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                out.println("play");// Send "play" request to server to join playing room
                playButton.setVisible(false); 
            }
        });

        // Action for "Start the Game" button
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainGameFrame.this, "The game is now starting!");
            }
        });
    }

    // Connect to the server
    private void connectToServer() {
        username = usernameField.getText();
        connectButton.setEnabled(false);

        try {
            // Setup socket and IO streams
            socket = new Socket("localhost", 5050);  // Make sure server is running on port 5050
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println(username);  // Send username to server

            // Start a new thread to listen for server messages
            new Thread(new ServerListener(in)).start();

            JOptionPane.showMessageDialog(this, "Connected as " + username);

            // Show the waiting room and hide connection panel
            connectionPanel.setVisible(false);
            waitingRoomPanel.setVisible(true);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to connect to the server.");
            connectButton.setEnabled(true);
        }
    }

    // Update the waiting room with the list of connected clients and manage transitions
    private void updateConnectedClients(String message) {
        connectedClientsArea.setText(message);

        // If the game is ready, show the "Start the Game" button and move to playing room
        if (message.contains("The game is ready to start!")) {
            waitingRoomPanel.setVisible(false);  // Hide waiting room
            playingRoomPanel.setVisible(true);  // Show playing room
            startGameButton.setVisible(true);  // Show the start game button
        }
    }

    // Inner class to handle incoming server messages
    class ServerListener implements Runnable {
        private BufferedReader in;

        public ServerListener(BufferedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            try {
                String messageFromServer;
                while ((messageFromServer = in.readLine()) != null) {
                    updateConnectedClients(messageFromServer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")/*
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

/*
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainGameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainGameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainGameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainGameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainGameFrame().setVisible(true);
            }
        });*/
        public static void main(String args[]) {
         //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainGameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainGameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainGameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainGameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
   SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainGameFrame gameFrame = new MainGameFrame();
                gameFrame.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
