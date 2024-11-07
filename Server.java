
package phase1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static ArrayList<Client> clients = new ArrayList<>();
    private static ArrayList<GameRoom> gameRooms = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5050);
        System.out.println("Server started. Waiting for clients to connect...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");

            Client clientThread = new Client(clientSocket, clients, gameRooms);
            clients.add(clientThread);
            new Thread(clientThread).start();
        }
    }
}