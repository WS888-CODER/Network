
package newserver;

import java.io.*; 
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {
   private static ArrayList<ServerThread> clients=new ArrayList<>();
    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(5050);

        while (true){
         System.out.println("Waiting for client connection");
         Socket client=serverSocket.accept();
         System.out.println("Connected to client");
         ServerThread clientThread=new ServerThread(client,clients); // new thread 
         clients.add(clientThread);
         new Thread (clientThread).start();
         
        }

         
        }
    }

 

