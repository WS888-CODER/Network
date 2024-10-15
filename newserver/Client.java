
package newserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JFrame;

public class Client  { 
    private static final String Server_IP="192.168.100.127";
    private static final int Server_port=5050;
    public String Name ;
    
   
    
    public Client(String name) throws IOException{
          Name = name;
          try(
                  Socket socket = new Socket (Server_IP,Server_port)) {
              ClientThread servcon=new ClientThread(socket);
              BufferedReader keyboard=new BufferedReader (new InputStreamReader(System.in));
              PrintWriter out=new PrintWriter(socket.getOutputStream(),true);
              new Thread (servcon).start();
              try{
                   
                
                  while(true){
                      System.out.println("> ");
                     
                       
                      String command=keyboard.readLine();                    
                      if(command.equals("stop"))
                        break;
                      out.println(command);
                  } // end of while loop
              } catch (Exception e){
                  e.printStackTrace();
              }
              finally {
                System.exit(0);
              }
          }
        }
    
     public String getName(){
        return Name;}
}
