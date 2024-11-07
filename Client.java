
package phase1;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<Client> clients;
    private ArrayList<GameRoom> gameRooms;
    private String username;
    private boolean inGameRoom = false;  // Track if player is in a game room

    public Client(Socket clientSocket, ArrayList<Client> clients, ArrayList<GameRoom> gameRooms) throws IOException {
        this.clientSocket = clientSocket;
        this.clients = clients;
        this.gameRooms = gameRooms;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            out.println("Enter your username: ");
            username = in.readLine();
            out.println("Welcome, " + username + ". You're in the waiting room.");

            broadcastMessage(username + " has joined the waiting room.");
            broadcastAllPlayers();

            String message;
            while ((message = in.readLine()) != null) {
                if (message.equalsIgnoreCase("play")) {
                    handlePlayRequest();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Broadcast a message to all connected clients
    private void broadcastMessage(String message) {
        for (Client client : clients) {
            client.out.println(message);
        }
    }

    // Broadcast the list of all connected players in the waiting room
    private void broadcastAllPlayers() {
        StringBuilder connectedUsers = new StringBuilder("Connected players in the waiting room: ");
        for (Client client : clients) {
            connectedUsers.append(client.username).append(", ");
        }
        if (connectedUsers.length() > 0) {
            connectedUsers.setLength(connectedUsers.length() - 2);  // Remove the last comma
        }

        for (Client client : clients) {
            client.out.println(connectedUsers.toString());
        }
    }

    // Handle the play request and manage room capacity for the playing room
    private void handlePlayRequest() {
        if (inGameRoom) {
            out.println("You are already in the playing room!");
            return;
        }

        GameRoom availableRoom = null;

        // Find an available room that is not full
        for (GameRoom room : gameRooms) {
            if (!room.isFull()) {
                availableRoom = room;
                break;
            }
        }

        // If no room is available, create a new one
        if (availableRoom == null) {
            availableRoom = new GameRoom();
            gameRooms.add(availableRoom);
        }

        // Try to add the player to the available room
        if (availableRoom.addPlayer(this)) {
            inGameRoom = true;  // Mark the player as in a game room

            // Notify the players in the room
            for (Client player : availableRoom.getPlayers()) {
                player.out.println(username + " has joined the playing room.");
                player.out.println(availableRoom.getPlayersList());  // Show player list in the playing room
            }

            // If the room is full, start the game
            if (availableRoom.isFull()) {
                for (Client player : availableRoom.getPlayers()) {
                    player.out.println(availableRoom.getPlayersList()); 
                    //player.out.println("The game is ready to start! Click 'Start the Game'.");
                }
            } else {
                out.println("Waiting for more players in the playing room...");
                for (Client player : availableRoom.getPlayers())
                player.out.println(availableRoom.getPlayersList()); 
            }
        } else {
            // Room is full, inform the player
            out.println("The game room is full. You cannot join at this time.");
        }
    }

    public String getUsername() {
        return username;
    }
}