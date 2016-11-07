/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highcards;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


/**
 *
 * @author Martins
 */
public class TcpServer {
    private static ArrayList<Player> online_users;
    private static int server_port;
    private static ServerSocket listenSocket;
    private static Socket clientSocket;

   
   public static void connection(int server_port){
       online_users = new ArrayList<>();
       System.out.println("A Escuta no Porto " + server_port);
        try {
            listenSocket = new ServerSocket(server_port);
        } catch (IOException e) {
            System.out.println("IO: "+e.getMessage());
        }
            System.out.println("LISTEN SOCKET="+listenSocket);
            
       while(true){
           try {
               clientSocket = listenSocket.accept(); // procura que algum cliente se conecte no porto.
           } catch (IOException e) {
                System.out.println("IO: "+e.getMessage());              
           }
           int i=0;
           System.out.println("CLIENT_SOCKET (created at accept())="+clientSocket);
           Player player = new Player(clientSocket,i++);
           online_users.add(online_users.size(), player);
           ThreadConnection conection= new ThreadConnection(player);         
       }
   }
    public static void main(String[] args) {
        server_port = Integer.parseInt(args[0]);
        connection(server_port);
    }
}
