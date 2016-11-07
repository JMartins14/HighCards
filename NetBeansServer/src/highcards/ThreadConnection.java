/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highcards;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
/**
 *
 * @author Martins
 */
public class ThreadConnection  extends Thread implements Serializable{
    private BufferedReader inFromClient;
    private PrintWriter outToClient;
    private Socket clientSocket;
    private int thread_number;

    public ThreadConnection(Player player) {
        this.clientSocket = player.getSocket();
        this.thread_number = player.getThread_number();
        try {
            this.inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //buffer com o que se recebe do cliente
            this.outToClient = new PrintWriter(clientSocket.getOutputStream(), true); //buffer com o que se envia para o cliente
        } catch (IOException ex) {
            System.out.println("IO: " +ex.getMessage());
        }
        this.start();
    }
    
    
    @Override
    public void run(){
            while(!this.isInterrupted()){
                receve_from_client();
            }
                
    }
    public void send_to_client(){
        try{
                InputStreamReader input = new InputStreamReader(System.in);
                BufferedReader reader = new BufferedReader(input);
            while(!this.isInterrupted()){
                outToClient.println(reader.readLine());
                
            }
        }catch(EOFException e){System.out.println("EOF:" + e);
        }catch(IOException e){System.out.println("IO:" + e);
        }
    }
    public void receve_from_client(){
         try {
            if(inFromClient.ready()){
                String data = inFromClient.readLine();
                if(data!=null)
                    System.out.println("T["+thread_number + "] Recebeu: "+data);
            }
        } catch (IOException ex) {
            System.out.println("IO:" +ex);
        }
    }
   
}