package app.example.martins.highcards;

/**
 * Created by Martins on 10/07/2016.
 */
public class Coordinates{
    private Card card;
    private float xmin,xmax,ymin,ymax;

    public Coordinates(Card card,float xmin, float xmax, float ymin, float ymax) {
        this.card = card;
        this.ymin = ymin;
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymax = ymax;
    }
    public Card getCard(){
        return card;
    }
    public float getXmin() {
        return xmin;
    }

    public float getXmax() {
        return xmax;
    }

    public float getYmin() {
        return ymin;
    }

    public float getYmax() {
        return ymax;
    }
}
