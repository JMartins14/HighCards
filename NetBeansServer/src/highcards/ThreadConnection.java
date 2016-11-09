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
        String data;
            while(!this.isInterrupted()){
                data = receve_from_client();
                //read_data(data);
            }
                
    }
    public void read_data(String data){
        if(data.equals("username")){
            send_to_client("recebi o username");
        }
        else if(data.equals("castilho")){
            send_to_client("castilho burro");
        }
    }
    
    public void send_to_client(String text){    
        if(!this.isInterrupted()){
            outToClient.println(text);        
        }
    }
    
    public String receve_from_client(){
        String data =null; 
        try {
            if(inFromClient.ready()){
                 data = inFromClient.readLine();
                if(data!=null)
                    System.out.println("T["+thread_number + "] Recebeu: "+data);
            }
        } catch (IOException ex) {
            System.out.println("IO:" +ex);
        }
         return data;
    }
   
}