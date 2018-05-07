package highcards;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author João
 */
public class Game {
    private int numplayers;
    private final int numinicialplayers;
    private ArrayList<Player> players;
    /*private Deck deck;
    private ArrayList<Card> playedDeck;
    private Player playerAtual;
    private Card cardAtual;
    private int numSevens=1;*/
    private ArrayList<BufferedReader> inFromClients = new ArrayList<>();
    private ArrayList<PrintWriter> outToClients= new ArrayList<>();
    private ArrayList<Socket> clientSockets= new ArrayList<>();
    private ArrayList<Integer> thread_numbers= new ArrayList<>();
    

    public Game(ArrayList<Player> players,ArrayList<BufferedReader> inFromClients,ArrayList<PrintWriter> outToClients,ArrayList<Socket> clientSockets,ArrayList<Integer> thread_numbers){
        this.numplayers = players.size();
        this.numinicialplayers = numplayers;
        this.players = players;
        this.inFromClients = inFromClients;
        this.outToClients = outToClients;
        this.clientSockets = clientSockets;
        this.thread_numbers = thread_numbers;
    }
    public int getNumplayers() {
        return numplayers;
    }

    public void setNumplayers(int numplayers) {
        this.numplayers = numplayers;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public int getNuminicialplayers() {
        return numinicialplayers;
    }
    

    public ArrayList<BufferedReader> getInFromClients() {
        return inFromClients;
    }

    public void setInFromClients(ArrayList<BufferedReader> inFromClients) {
        this.inFromClients = inFromClients;
    }

    public ArrayList<PrintWriter> getOutToClients() {
        return outToClients;
    }

    public void setOutToClients(ArrayList<PrintWriter> outToClients) {
        this.outToClients = outToClients;
    }

    public ArrayList<Socket> getClientSockets() {
        return clientSockets;
    }

    public void setClientSockets(ArrayList<Socket> clientSockets) {
        this.clientSockets = clientSockets;
    }

    public ArrayList<Integer> getThread_numbers() {
        return thread_numbers;
    }

    public void setThread_numbers(ArrayList<Integer> thread_numbers) {
        this.thread_numbers = thread_numbers;
    }
}


