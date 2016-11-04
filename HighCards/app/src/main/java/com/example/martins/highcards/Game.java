package com.example.martins.highcards;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;


public class Game extends Activity  {
    private PopupWindow popupWindow;
    private LayoutInflater layoutInflator;
    private Player[] players = new Player[10];
    private Deck deck;
    private ArrayList<Card> playedDeck;
    private int numberPlayers;
    private String playerName;
    private Player playerAtual;
    private Card cardAtual = null;
    OurView v;
    Bitmap cards;
    int screenWidth,screenHeight;
    private ArrayList<Coordinates> cardsCoords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.game_layout);

        Bundle b = getIntent().getExtras();
        playerName = b.getString("username");
        initDeck();
        initPlayers(4);
        playerAtual = players[0];
        cards = BitmapFactory.decodeResource(getResources(),R.drawable.cards);
        v = new OurView(this, playerAtual,null);
        ((RelativeLayout)findViewById(R.id.game)).addView(v);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth= size.x;
        screenHeight = size.y;


    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            int xteste = (int)event.getX();
            int yteste = (int)event.getY();
            clickCards(xteste,yteste,playerAtual);
        }

        return true;
    }

    public void clickCards(int xteste, int yteste,Player playerAtual){
        int i;
        Coordinates coordAtual;
        for(i=0;i<cardsCoords.size();i++){
            coordAtual = cardsCoords.get(i);
            if(xteste>=coordAtual.getXmin() && xteste<=coordAtual.getXmax() && yteste>=coordAtual.getYmin() && yteste<=coordAtual.getYmax()){
               cardAtual = coordAtual.getCard();
                ((RelativeLayout)findViewById(R.id.game)).removeView(v);
                v = new OurView(this, playerAtual,coordAtual.getCard());
                ((RelativeLayout)findViewById(R.id.game)).addView(v);
                break;
            }
        }
    }
    public void confirmClicked(View view) {

        if(cardAtual == null){
            Toast.makeText(this,"Please choose a card!",Toast.LENGTH_LONG).show();
        }
        else{
            if(compareCards(cardAtual,playedDeck.get(playedDeck.size()-1))){
                playCard(cardAtual);
            }
            else{
                ((RelativeLayout)findViewById(R.id.game)).removeView(v);
                v = new OurView(this, playerAtual,null);
                ((RelativeLayout)findViewById(R.id.game)).addView(v);
                cardAtual = null;
            }

        }


    }

    public void initDeck(){
        this.deck = new Deck(52);
        this.deck.shuffle();
        this.playedDeck = new ArrayList <>();
        Card card = this.deck.removeFromDeck(true,1);
        playedDeck.add(card);
        this.deck.cards.add(card);
    }


    public void initPlayers(int numBots){
        int i;
        for(i=0;i<1+numBots;i++){
            if(i==0)
                this.players[i] = new Player(playerName,false);
            else
                this.players[i] = new Player("Player"+(i+1),true);

            this.deck.giveCards(true,7,this.players[i]);
        }

        this.numberPlayers = 1+numBots;
    }


    public Player isOver(){
        int i;
        for(i=0;i<numberPlayers;i++){
            if(this.players[i].getNumberCards() == 0)
                return this.players[i];
        }
        return null;
    }
    public boolean compareCards(Card card1,Card card2){
        if(card1.getNaipe().equals(card2.getNaipe()) || card1.getCode() == card2.getCode())
            return true;
        else{
            Toast.makeText(this,"You can't play this card!",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public void removeFromGame(Player player){
        int i,j;
        for(i=0;i<numberPlayers;i++){
            if(players[i].getNome().equals(player.getNome())){
                if(i == numberPlayers-1)
                    players[i] = null;
                else{
                    for(j=i;j<numberPlayers-1;j++) {
                        players[j] = players[j + 1];
                    }
                    players[numberPlayers-1] = null;
                }
                numberPlayers--;

            }
        }
    }
    public void queenPlayed(){
        Player[] aux = new Player[numberPlayers];
        int i,j=0 ;
        for(i=0;i<numberPlayers-2;i++){
            nextPlayer();
        }
        for(i=numberPlayers-1;i>=0;i--){
            aux[j++] = players[i];
        }
        players = aux;
    }

    public void chooseNaipe(){
         layoutInflator = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup container = (ViewGroup)layoutInflator.inflate(R.layout.choosenaipe,null);
         popupWindow = new PopupWindow(container, (int) (screenWidth*0.6), (int) (screenHeight*0.6),true);
        popupWindow.showAtLocation(this.v,Gravity.NO_GRAVITY, (int) (screenWidth*0.2), (int) (screenHeight*0.2));
        ImageButton image =(ImageButton) findViewById(R.id.clubs_symbol);
        image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Card card = new Card(11,"Clubs");
                playedDeck.add(card);
                popupWindow.dismiss();
                showPopUp();
            }
        });
    }

    public int checkCard(){
        if(cardAtual == null)
            return 0;
        switch (cardAtual.getCode()){
            case 1: nextPlayer();return 1;
            case 9:deck.giveCards(true,1,playerAtual);return 9;
            case 7: deck.giveCards(true,2,playerAtual);return 7;
            case 11: ;return 11;
            case 12: queenPlayed(); return 12;
            default: return 0;
        }
    }
    public void showPopUp(){
        layoutInflator = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup container = (ViewGroup)layoutInflator.inflate(R.layout.popupwindow,null);
        popupWindow = new PopupWindow(container, (int) (screenWidth*0.6), (int) (screenHeight*0.6),true);
        popupWindow.showAtLocation(this.v,Gravity.NO_GRAVITY, (int) (screenWidth*0.2), (int) (screenHeight*0.2));
        String text = new String();
        TextView next_player_string =(TextView) container.findViewById(R.id.player_name);
        switch (checkCard()){
            case 1: text = "Ace played: next player doesn't play";break;
            case 7:text = "7 played: next player receives 2 cards\n or plays another 7";break;
            case 9:text = "9 played: next player receives 2 cards";break;
            case 12:text = "Queen played: the game order was inverted";break;
        }
        String text2 ="\n\nNext Player: "+playerAtual.getNome();
        String total = text + text2;
        next_player_string.setText(total);
        container.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view,MotionEvent motionEvent){
                popupWindow.dismiss();
                drawView();
                cardAtual = null;
                return true;
            }
        });
    }
    public void playCard(Card card){
        playedDeck.add(card);
        this.deck.cards.add(card);
        playerAtual.removeCard(cardAtual);
        nextPlayer();
        if(card.getCode() == 11) {
            chooseNaipe();
        }
        else{
            showPopUp();
        }

    }
    public void nextPlayer() {
        int i;
        for(i=0;i<numberPlayers;i++){
            if(playerAtual.getNome().equals(players[i].getNome())){
                if(i == numberPlayers-1) playerAtual = players[0];
                else playerAtual = players[i+1];
                break;
            }
        }
        if(isOver()!=null){
            Toast.makeText(this,isOver().getNome()+" just win!",Toast.LENGTH_LONG).show();
            removeFromGame(isOver());
            if(numberPlayers == 1)
                finish();
        }



    }
    public void drawView(){
        ((RelativeLayout)findViewById(R.id.game)).removeView(v);
        v = new OurView(this, playerAtual,cardAtual);
        ((RelativeLayout)findViewById(R.id.game)).addView(v);
    }
    public void deckClicked(View view) {
        final AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
        exitDialog.setMessage("Are you sure you want to take a card from deck?");
        exitDialog.setCancelable(true);
        exitDialog.setPositiveButton("Yes",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int wich){
                deck.giveCards(true,1,playerAtual);
                nextPlayer();
                cardAtual = null;
                showPopUp();

            }
        });
        exitDialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int wich){
                dialog.dismiss();
            }
        });
        exitDialog.show();
    }
    public void pauseClicked(View view) {

        final AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
        exitDialog.setMessage("Are you sure you want to exit?");
        exitDialog.setCancelable(true);
        exitDialog.setPositiveButton("Yes",new DialogInterface.OnClickListener(){
           @Override
            public void onClick(DialogInterface dialog, int wich){
               finish();
           }
        });
        exitDialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int wich){
                dialog.dismiss();
            }
        });
        exitDialog.show();

    }



    public class OurView extends View {
        private Player player;
        private Card card ;
        private Coordinates coords;

        public OurView(Context context,Player player,Card card){
            super(context);
            this.player = player;
            this.card = card;
            cardsCoords= new ArrayList<>();


        }
        @Override
        protected void onDraw(Canvas c){
            Card[] hand = player.getHand();
           int xDist,i;


            //Desenha jogadores e seu num cartas
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(25);
            Typeface currentTypeFace =   paint.getTypeface();
            Typeface bold = Typeface.create(currentTypeFace, Typeface.BOLD);
            paint.setTypeface(bold);
            String info;
            int distTable=75;
            for(i=0;i<numberPlayers;i++){
                if(this.player.getNome().equals( players[i].getNome()))
                    c.drawText("♠",50,distTable-1,paint);
                info = players[i].getNome() +": "+ players[i].getNumberCards()+" Cards";
                c.drawText(info,75,distTable,paint);
                distTable+=30;
            }

            //Desenha atual carta jogada
            c.drawText("Last Played Card",c.getWidth()-270,75,paint);
            Card atualCard = playedDeck.get(playedDeck.size()-1);
            Rect src2 = new Rect(atualCard.getX(), atualCard.getY(), atualCard.getX() + cards.getWidth() / 13, atualCard.getY() + cards.getHeight() / 4);
            Rect dst2 = new Rect(c.getWidth()-220,100 , c.getWidth()-220+cards.getWidth()/13, 100+cards.getHeight() / 4);
            c.drawBitmap(cards, src2, dst2, null);


            //Desenha cartas jogador atual;
            xDist = (c.getWidth()/2)-((((this.player.getNumberCards()-1)*((cards.getWidth()/13)-30))+(cards.getWidth()/13))/2);
            //Sem carta selecionada;
            if(card == null){
                for(i=0;i<this.player.getNumberCards();i++) {
                    Rect src = new Rect(hand[i].getX(), hand[i].getY(), hand[i].getX() + cards.getWidth() / 13, hand[i].getY() + cards.getHeight() / 4);
                    Rect dst = new Rect(xDist, c.getHeight() - (((int) cards.getHeight() / 4)+20), cards.getWidth() / 13 + xDist, cards.getHeight() / 4 + (c.getHeight() - (((int) cards.getHeight() / 4)+20)));
                    c.drawBitmap(cards, src, dst, null);
                    coords = new Coordinates(hand[i],xDist,cards.getWidth() / 13 + xDist,c.getHeight() - (((int) cards.getHeight() / 4)+20),cards.getHeight() / 4 + (c.getHeight() - (((int) cards.getHeight() / 4)+20)));
                    cardsCoords.add(cardsCoords.size(),coords);
                    xDist += c.getWidth()/13-(this.player.getNumberCards());
                }

            }
            //Com carta selecionada
            else{
                for(i=0;i<this.player.getNumberCards();i++) {
                    if(hand[i].getNaipe().equals(card.getNaipe())&& hand[i].getCode() == card.getCode()){
                        Rect src = new Rect(hand[i].getX(), hand[i].getY(), hand[i].getX() + cards.getWidth() / 13, hand[i].getY() + cards.getHeight() / 4);
                        Rect dst = new Rect(xDist, c.getHeight() - (((int) cards.getHeight() / 4)+70), cards.getWidth() / 13 + xDist, cards.getHeight() / 4 + (c.getHeight() - (((int) cards.getHeight() / 4)+70)));
                        c.drawBitmap(cards, src, dst, null);
                        coords = new Coordinates(hand[i],xDist,cards.getWidth() / 13 + xDist,c.getHeight() - (((int) cards.getHeight() / 4)+20),cards.getHeight() / 4 + (c.getHeight() - (((int) cards.getHeight() / 4)+20)));
                        cardsCoords.add(cardsCoords.size(),coords);
                        xDist += c.getWidth()/13-(this.player.getNumberCards());
                    }
                    else{
                        Rect src = new Rect(hand[i].getX(), hand[i].getY(), hand[i].getX() + cards.getWidth() / 13, hand[i].getY() + cards.getHeight() / 4);
                        Rect dst = new Rect(xDist, c.getHeight() - (((int) cards.getHeight() / 4)+20), cards.getWidth() / 13 + xDist, cards.getHeight() / 4 + (c.getHeight() - (((int) cards.getHeight() / 4)+20)));
                        c.drawBitmap(cards, src, dst, null);
                        coords = new Coordinates(hand[i],xDist,cards.getWidth() / 13 + xDist,c.getHeight() - (((int) cards.getHeight() / 4)+20),cards.getHeight() / 4 + (c.getHeight() - (((int) cards.getHeight() / 4)+20)));
                        cardsCoords.add(cardsCoords.size(),coords);
                        xDist += c.getWidth()/13-(this.player.getNumberCards());
                    }

                }
            }


        }




    }


}
