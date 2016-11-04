package com.example.martins.highcards;

import java.util.ArrayList;

/**
 * Created by Martins on 04/07/2016.
 */
public class Player {
    private String nome;
    private Card[] hand = new Card[51];
    private int numberCards;
    private boolean bot;

    public Player(String nome,boolean bot){
        this.nome = nome;
        this.bot = bot;
    }

    public boolean isBot() {
        return bot;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }

    public int getNumberCards() {
        return numberCards;
    }

    public void setNumberCards(int numberCards) {
        this.numberCards = numberCards;
    }

    public Card[] getHand() {
        return hand;
    }

    public void setHand(Card[] hand) {
        this.hand = hand;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void receiveCards(Card[] hand, int numberCards){
        int j;
        for(j=0;j<numberCards;j++){
            this.hand[this.numberCards++] = hand[j];
        }
    }
    public void printHand(){
        int i;
        for(i=0;i<this.numberCards;i++){
            System.out.println((i+1)+") "+this.hand[i].toString());
        }
    }
   /* public Card playCard(){
        Card card = new Card(0,null);
        int i,index;
        Scanner sc = new Scanner(System.in);
        System.out.println("\nPlayer:"+" " +this.nome + "\n\n"+"Your Cards:");
        this.printHand();
        System.out.println("\n\nWrite the position of the card in your hand (*Note: Introduce 0 to take a card from deck):");
        index = sc.nextInt();
        if(index == 0){
            card = null;
        }
        else{
            card =  this.hand[index-1];

        }
        return card;
    }*/
    public void removeCard(Card card){
        int i,j;
        for(i=0;i<this.numberCards;i++){
            if(this.hand[i].getNaipe().equals(card.getNaipe()) && this.hand[i].getCode() == card.getCode()){
                for(j=i;j<this.numberCards;j++){
                    this.hand[j] = this.hand[j+1];
                }
                this.numberCards--;
                break;
            }
        }
    }
    public Card botPlay(ArrayList playedDeck, Deck deck){
        int i;
        Card card =(Card) playedDeck.get(playedDeck.size()-1);
        for(i=0;i<this.numberCards;i++){
            if(this.hand[i].getNaipe().equals(card.getNaipe()) || this.hand[i].getCode() == card.getCode()){
                return this.hand[i];

            }
        }
        /*Se nao tem vai ao baralho*/
        deck.giveCards(true, 1, this);
        return null;
    }
}
