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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Martins
 */
public class ThreadConnection  extends Thread implements Serializable{
    private ArrayList<BufferedReader> inFromClients = new ArrayList<>();
    private ArrayList<PrintWriter> outToClients= new ArrayList<>();
    private ArrayList<Socket> clientSockets= new ArrayList<>();
    private ArrayList<Integer> thread_numbers= new ArrayList<>();
    private ArrayList<Player> players= new ArrayList<>();
    public ThreadConnection(ArrayList<Player> players) {
        for(int i=0;i<players.size();i++){
            clientSockets.add(players.get(i).getSocket());
            thread_numbers.add(players.get(i).getThread_number());
            this.players = players;
            try {
                this.inFromClients.add(new BufferedReader(new InputStreamReader(clientSockets.get(i).getInputStream()))); //buffer com o que se recebe do cliente
                this.outToClients.add(new PrintWriter(clientSockets.get(i).getOutputStream(), true)); //buffer com o que se envia para o cliente
            } catch (IOException ex) {
            System.out.println("IO: " +ex.getMessage());
            }
        }
        this.start();
    }
    
    
    @Override
    public void run(){
        //Avisa os utilizadores que o jogo vai ser começado
        MauMau jogo = new MauMau(players,inFromClients,outToClients,clientSockets,thread_numbers);
        try {
            jogo.initGame();
        } catch (IOException ex) {
            Logger.getLogger(ThreadConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
    