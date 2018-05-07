package app.example.martins.highcards;


import android.graphics.Bitmap;

/**
 * Created by Martins on 04/07/2016.
 */
public class Card {

    private int code;
    private String naipe;
    private float x,y;
    private boolean escolhida;

    public Card(int code,String naipe){
        this.code = code;
        this.naipe = naipe;
    }
    public Card(int code, String naipe,float x,float y){
        this.code = code;
        this.naipe = naipe;
        this.x = x;
        this.y = y;
    }
    public Card(int code){
        this.code = code;
    }
    public void setCoordenadas(Bitmap cards){
        this.x = (cards.getWidth()/13)*(code-1);
        switch (naipe){
            case "Clubs":this.y =0;return;
            case "Spades":this.y = cards.getHeight()/4;return;
            case "Hearts":this.y = (cards.getHeight()/4)*2;return;
            case "Diamonds":this.y = (cards.getHeight()/4)*3;return;
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getCode() {
        return code;
    }

    public String getNaipe() {
        return naipe;
    }

    public boolean isEscolhida() {
        return escolhida;
    }

    public void setEscolhida(boolean escolhida) {
        this.escolhida = escolhida;
    }

    @Override
    public String toString(){
            return this.code + " "+this.naipe;
    }


}
