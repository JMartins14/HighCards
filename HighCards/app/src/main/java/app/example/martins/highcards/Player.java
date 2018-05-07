package app.example.martins.highcards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Martins on 04/07/2016.
 */
public class Player {
    private String nome;
    private ArrayList<Card> hand = new ArrayList<>();
    private int numberCards;
    private boolean bot;
    private boolean estado = true;
    private ArrayList<Card> deckPile =new ArrayList<>();

    public Player(String nome,boolean bot){
        this.nome = nome;
        this.bot = bot;
    }
    public Player(String nome,int numberCards){
        this.nome = nome;
        this.numberCards = numberCards;
        this.bot=false;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
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

    public ArrayList getHand() {
        return hand;
    }

    public void setHand(ArrayList hand) {
        this.hand = hand;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void receiveCards(ArrayList<Card> hand){
        int j;
        for(j=0;j<hand.size();j++){
            this.hand.add(hand.get(j));
        }
        Collections.sort(this.hand, new Comparator<Card>() {
            @Override
            public int compare(Card card1, Card card2) {
                if(card1.getCode() > card2.getCode())
                    return 1;
                else if(card1.getCode()<card2.getCode())
                    return -1;
                else
                    return 0;
            }
        });
        this.numberCards = this.getHand().size();
    }
    public void printHand(){
        int i;
        for(i=0;i<this.hand.size();i++){
            System.out.println((i+1)+") "+this.hand.get(i).toString());
        }
    }

    public void removeCard(Card card){
        int i;
        for(i=0;i<this.hand.size();i++){
            if(this.hand.get(i).getNaipe().equals(card.getNaipe()) && this.hand.get(i).getCode() == card.getCode()){
                this.hand.remove(card);
                break;
            }
        }
        this.numberCards = this.hand.size();
    }

    public ArrayList<Card> getDeckPile() {
        return deckPile;
    }

    public void setDeckPile(ArrayList<Card> deckPile) {
        this.deckPile = deckPile;
    }
}
