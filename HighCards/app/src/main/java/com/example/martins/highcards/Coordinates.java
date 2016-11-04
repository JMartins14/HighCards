package com.example.martins.highcards;

/**
 * Created by Martins on 10/07/2016.
 */
public class Coordinates{
    private Card card;
    private int xmin,xmax,ymin,ymax;

    public Coordinates(Card card,int xmin, int xmax, int ymin, int ymax) {
        this.card = card;
        this.ymin = ymin;
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymax = ymax;
    }
    public Card getCard(){
        return card;
    }
    public int getXmin() {
        return xmin;
    }

    public int getXmax() {
        return xmax;
    }

    public int getYmin() {
        return ymin;
    }

    public int getYmax() {
        return ymax;
    }
}
