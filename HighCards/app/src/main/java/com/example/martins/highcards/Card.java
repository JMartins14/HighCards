package com.example.martins.highcards;

import android.widget.ImageView;


/**
 * Created by Martins on 04/07/2016.
 */
public class Card {

    private int code;
    private String naipe;
    private int x,y;

    public Card(int code,String naipe){
        this.code = code;
        this.naipe = naipe;
    }
    public Card(int code, String naipe,int x,int y){
        this.code = code;
        this.naipe = naipe;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getNaipe() {
        return naipe;
    }

    public void setNaipe(String naipe) {
        this.naipe = naipe;
    }

    @Override
    public String toString(){
            return this.code + " "+this.naipe;
    }


}
