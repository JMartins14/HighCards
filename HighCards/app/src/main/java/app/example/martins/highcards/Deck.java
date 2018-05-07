package app.example.martins.highcards;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Martins on 04/07/2016.
 */
public class Deck {
    public ArrayList<Card> cards;

    public Deck(int numberCards, Bitmap cardmap) {
        this.cards = new ArrayList<Card>();
        Card card;
        int i;
        float x = 0, y;
        /*Jogos 52 Cartas*/
        if (numberCards == 52) {
            for (i = 1; i < numberCards + 1; i++) {
                if (i < 14) {
                    if (i == 1)
                        x = i;
                    else
                        x += (float)cardmap.getWidth() / 13;
                    y = 2 *(float)cardmap.getHeight() / 4;
                    card = new Card(i, "Hearts", x, y);
                    cards.add(card);


                } else if (i >= 14 && i < 27) {
                    if (i == 14)
                        x = i - 13;
                    else
                        x += (float)cardmap.getWidth() / 13;
                    ;
                    y = 0;
                    card = new Card(i - 13, "Clubs", x, y);
                    cards.add(card);
                } else if (i >= 27 && i < 40) {
                    y = (float)cardmap.getHeight() / 4;
                    if (i == 27)
                        x = i - 26;
                    else
                        x += (float) cardmap.getWidth() / 13;
                    card = new Card(i - 26, "Spades", x, y);
                    cards.add(card);
                } else if (i >= 40 && i < 53) {
                    y = 3 *(float)cardmap.getHeight() / 4;
                    if (i == 40)
                        x = i - 39;
                    else
                        x += (float)cardmap.getWidth() / 13;
                    card = new Card(i - 39, "Diamonds", x, y);
                    cards.add(card);
                }
            }
        }

        /*Jogos 40 Cartas*/
        else if(numberCards == 40){
            for (i = 1; i < 52 + 1; i++) {
                if (i < 14) {
                    if (i == 1)
                        x = i;
                    else if(i==8){
                        x+=3*(float)cardmap.getWidth()/13;
                        i+=3;
                    }
                    else
                        x += (float)cardmap.getWidth() / 13;
                    y = 2*(float)cardmap.getHeight() / 4;
                    card = new Card(i, "Hearts", x, y);
                    cards.add(card);


                } else if (i >= 14 && i < 27) {
                    if (i == 14)
                        x = i - 13;
                    else if(i==21){
                        x+=3*(float)cardmap.getWidth()/13;
                        i+=3;
                    }
                    else
                        x += (float)cardmap.getWidth() / 13;
                    ;
                    y = 0;
                    card = new Card(i - 13, "Clubs", x, y);
                    cards.add(card);
                } else if (i >= 27 && i < 40) {
                    y = (float)cardmap.getHeight() / 4;
                    if (i == 27)
                        x = i - 26;
                    else if(i==34){
                        x+=3*(float)cardmap.getWidth()/13;
                        i+=3;
                    }
                    else
                        x += (float)cardmap.getWidth() / 13;
                    card = new Card(i - 26, "Spades", x, y);
                    cards.add(card);
                } else if (i >= 40 && i < 53) {
                    y = 3 * (float)cardmap.getHeight() / 4;
                    if (i == 40)
                        x = i - 39;
                    else if(i==47){
                        x+=3*(float)cardmap.getWidth()/13;
                        i+=3;
                    }
                    else
                        x += (float)cardmap.getWidth() / 13;
                    card = new Card(i - 39, "Diamonds", x, y);
                    cards.add(card);
                }
            }
        }


    }

    public void print() {
        int i;
        for (i = 0; i < this.cards.size(); i++) {
            System.out.println(this.cards.get(i).toString());
        }
        System.out.println(i);
    }

    public void shuffle() {
        long seed = System.nanoTime();
        Collections.shuffle(this.cards, new Random(seed));

    }

    public void giveCards(boolean top, int numberCards, Player player) {
        int i, j = 0;
        ArrayList<Card> cards = new ArrayList<>();
        if (top) {
            for (i = 0; i < numberCards; i++)
                cards.add(this.cards.remove(0));
        } else {
            for (i = 0; i < numberCards; i++)
                cards.add(this.cards.remove(this.cards.size() - 1));
        }

        player.receiveCards(cards);
    }

    public void putInDeck(boolean top, int numberCards, Card[] cards) {
        int i;
        if (top) {
            for (i = 0; i < numberCards; i++)
                this.cards.add(0, cards[i]);
        } else {
            for (i = 0; i < numberCards; i++)
                this.cards.add(cards[i]);
        }
    }

    public Card removeFromDeck(boolean top, int numberCards) {
        int i;
        Card card = new Card(0, null, 0, 0);
        if (top) {
            card = this.cards.remove(0);
        } else {
            card = this.cards.remove(numberCards);
        }
        return card;
    }


}
