/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highcards;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;


/**
 *
 * @author Martins
 */
public class TcpServer {
    private static ArrayList<Player> online_users;
    private static ArrayList<Player> queue1,queue2,queue3;
    private static int numplayers1=3,numplayers2,numplayers3;
    private static int server_port;
    private static ServerSocket listenSocket;
    private static Socket clientSocket;
   
   public static void connection(int server_port){
       online_users = new ArrayList<>();
       queue1 = new ArrayList<>();
       int i =0;
        int counter=0;
        boolean allplayersready;
        Player aux;

       System.out.println("A Escuta no Porto " + server_port);
        try {
            listenSocket = new ServerSocket(server_port);
        } catch (IOException e) {
            System.out.println("IO: "+e.getMessage());
        }
            System.out.println("LISTEN SOCKET="+listenSocket);
            
            try {
                while(true){
                    clientSocket = listenSocket.accept();
                    clientSocket.getInputStream();// procura que algum cliente se conecte no porto.
                    System.out.println("CLIENT_SOCKET (created at accept())="+clientSocket);
                    String username = String.format("user%d",counter++);
                    Player player = new Player(username,clientSocket,i++);
                    online_users.add(online_users.size(), player);
                    queue1.add(queue1.size(), player);
                    if(queue1.size()==numplayers1){
                        allplayersready = true;
                        for(int c=0;c<queue1.size();c++){
                            aux = queue1.get(c);
                            if(aux.getSocket().getInetAddress()==null){
                                System.out.println("O utilizador " + aux.getUsername()+" foi disconectado!");
                                allplayersready = false;
                                online_users.remove(aux);
                                queue1.remove(aux);
                            }
                        }
                        if(allplayersready){
                            if(checkPlayers()){
                                ArrayList<Player> game = letsdoagame(queue1);
                                System.out.println("Estão " + numplayers1 + " jogadores na fila de espera. Vai-se dar início ao jogo!");
                                new ThreadConnection(game); 
                                
                            }
                        }
                }
                }
           } catch (IOException e) {
                System.out.println("IO: "+e.getMessage());              
           }
           
       
   }
    private static boolean checkPlayers() throws IOException{
        boolean total = true;
        boolean disconnected;
        ArrayList<String> ips = new ArrayList<>();
        for(int i=0;i<queue1.size();i++){
            try{
                disconnected =false;
                queue1.get(i).getSocket().setSoTimeout(500);
                if(queue1.get(i).getSocket().getInputStream().read()!=-1){                
                    for(int j=0;j<queue1.size();j++){
                        ips.add(queue1.get(j).getSocket().getRemoteSocketAddress().toString().split(":")[0]);
                        if(ips.get(j).equals(ips.get(i))&&j!=i){
                            queue1.remove(i);
                            System.out.println("Player " + (i+1) + ": DISCONNECTED");
                            total = false;
                            disconnected = true;
                            break;
                        }
                    }
                    if(!disconnected){
                        System.out.println("Player " + (i+1) + ": OK");
                        PrintWriter fout = new PrintWriter(queue1.get(i).getSocket().getOutputStream(), true); 
                        fout.println("Connected");
                    }
                }
                else{
                    queue1.remove(i);
                    System.out.println("Player " + (i+1) + ": DISCONNECTED");
                    total = false;  
                }
            }
            catch(SocketTimeoutException es){
                disconnected = false;
                for(int j=0;j<queue1.size();j++){
                    ips.add(queue1.get(j).getSocket().getRemoteSocketAddress().toString().split(":")[0]);
                    if(ips.get(j).equals(ips.get(i))&&j!=i){
                        queue1.remove(i);
                        System.out.println("Player " + (i+1) + ": DISCONNECTED");
                        total = false;
                        disconnected = true;
                        break;
                    }
                }
                if(!disconnected){
                    System.out.println("Player " + (i+1) + ": OK");
                    PrintWriter fout = new PrintWriter(queue1.get(i).getSocket().getOutputStream(), true); 
                    fout.println("Connected");
                }

            }
            catch(IOException es){
                es.printStackTrace();
                queue1.remove(i);
                System.out.println("Player " + (i+1) + ": DISCONNECTED");
                total = false;
            }
        }
        return total;
    }
    private static ArrayList<Player> letsdoagame(ArrayList<Player> queue){
        ArrayList<Player> game1 = new ArrayList<>();
        for(int i=0;i<numplayers1;i++){
            game1.add(queue.remove(0));
        }
        return game1;
    }
    public static void main(String[] args) {
        server_port = Integer.parseInt(args[0]);
        connection(server_port);
    }
}
