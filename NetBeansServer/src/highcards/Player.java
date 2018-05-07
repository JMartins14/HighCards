/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highcards;

import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Martins
 */
public class Player {
    private String username;
    private Socket socket;
    private ArrayList<Card> hand;
    private boolean bot;
    private int thread_number;
    private int numcards;

    public Player(String username,Socket socket, int thread_number) {
        this.username = username;
        this.socket = socket;
        this.thread_number = thread_number;
        this.hand = new ArrayList<>();
        bot = false;
    }

    public boolean isBot() {
        return bot;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public int getNumcards() {
        return numcards;
    }

    public void setNumcards(int numcards) {
        this.numcards = numcards;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public int getThread_number() {
        return thread_number;
    }

    public void setThread_number(int thread_number) {
        this.thread_number = thread_number;
    }
    public void receiveCards(ArrayList<Card> hand){
        this.hand = hand;
    }
    public void printHand(){
        int i;
        for(i=0;i<hand.size();i++){
            System.out.println((i+1)+") "+this.hand.get(i).toString());
        }
    }
    public void removeCard(Card card){
        int i,j;
        for(i=0;i<hand.size();i++){
            if(this.hand.get(i).getNaipe().equals(card.getNaipe()) && this.hand.get(i).getCode() == card.getCode()){
                this.hand.remove(card);
                break;
            }
        }
        this.numcards = this.hand.size();

    }
    public void receiveCards(ArrayList<Card> hand, int numberCards){
        int j;
        for(j=0;j<numberCards;j++){
            this.hand.add(hand.get(j));
        }
                this.numcards = this.getHand().size();

    }
    
}
