package com.example.martins.highcards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Martins on 04/07/2016.
 */
public class Deck {
    public ArrayList<Card> cards ;
    public Deck(int numberCards){
        this.cards = new ArrayList<Card>();
        Card card;
        int i,x=0,y;
        /*Jogos 52 Cartas*/
        if(numberCards == 52){
            for(i=1;i<numberCards+1;i++){
                if(i<14) {
                    if (i == 1)
                        x = i;
                    else
                        x += 73;
                    y=2*98;
                    card = new Card(i,"Hearts",x,y);
                    cards.add(card);


                }
                else if(i>=15 && i<28){
                    if(i==15)
                        x=i-14;
                    else
                        x+=73;
                    y=0;
                    card = new Card(i-14,"Clubs",x,y);
                    cards.add(card);
                }
                else if(i>=28 && i<41){
                    y=98;
                    if(i==28)
                        x=i-27;
                    else
                        x+= 73;
                    card = new Card(i-27,"Spades",x,y);
                    cards.add(card);
                }
                else if(i>=41 && i<54){
                    y=3*98;
                    if(i==41)
                        x=i-40;
                    else
                        x+=73;
                    card = new Card(i-40,"Diamonds",x,y);
                    cards.add(card);
                }
            }
        }
        /*Jogos 40 Cartas
        else if(numberCards == 40){
            for(i=1;i<numberCards+1;i++){
                if(i<12){
                    card = new Card(i,"Hearts");
                    cards.add(card);
                }
                else if(i>=12 && i<22){
                    card = new Card(i-10,"Clubs");
                    cards.add(card);
                }
                else if(i>=22 && i<32){
                    card = new Card(i-20,"Spaces");
                    cards.add(card);
                }
                else if(i>=32 && i<42){
                    card = new Card(i-30,"Diamonds");
                    cards.add(card);
                }
            }
        }*/


    }
    public void print(){
        int i;
        for(i=0;i<this.cards.size();i++){
            System.out.println(this.cards.get(i).toString());
        }
        System.out.println(i);
    }
    public void shuffle(){
        long seed = System.nanoTime();
        Collections.shuffle(this.cards, new Random(seed));

    }
    public void giveCards(boolean top,int numberCards,Player player){
        int i,j=0;
        Card[] cards = new Card[numberCards];
        if(top){
            for(i=0;i<numberCards;i++)
                cards[j++] = this.cards.remove(0);

        }
        else{
            for(i=0;i<numberCards;i++)
                cards[j++] = this.cards.remove(this.cards.size()-1);
        }

        player.receiveCards(cards, numberCards);

    }
    public void putInDeck(boolean top,int numberCards,Card[] cards){
        int i;
        if(top){
            for(i=0;i<numberCards;i++)
                this.cards.add(0, cards[i]);
        }
        else{
            for(i=0;i<numberCards;i++)
                this.cards.add(cards[i]);
        }
    }
    public Card removeFromDeck(boolean top,int numberCards){
        int i;
        Card card = new Card(0,null,0,0);
        if(top){
            card = this.cards.remove(0);
        }
        else{
            card =this.cards.remove(numberCards);
        }
        return card;
    }


}
