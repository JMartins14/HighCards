package app.example.martins.highcards;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.lang.Runnable;


public class OnlineGame extends Activity {
    private PopupWindow popupWindow;
    private LayoutInflater layoutInflator;
    private ArrayList<Player> players;
    private Deck deck;
    private ArrayList<Card> playedDeck;
    private int numberPlayers;
    private String playerName;
    private Player player;
    private Card cardAtual = null;
    private OurView v;
    private Bitmap cards;
    private int screenWidth, screenHeight;
    private ArrayList<Coordinates> cardsCoords;
    private ArrayList<String> DataFromServer;
    private int numDatas;
    private boolean yourTurn;
    private SocketThread socketThread;
    private boolean finish;
    private boolean sevenPlayed;
    private LayoutInflater layoutInflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yourTurn = false;
        finish = false;
        sevenPlayed = false;
        numDatas = 0;
        cardsCoords = new ArrayList<>();
        DataFromServer = new ArrayList<>();
        socketThread = new SocketThread(this);
        Thread socketServerThread = new Thread(socketThread);
        socketServerThread.start();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.game_layout);
        Bundle b = getIntent().getExtras();
        cards = BitmapFactory.decodeResource(getResources(), R.drawable.cards);
        playerName = b.getString("username");
        player = new Player(playerName,0);
        player.setNome(playerName);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(finish) {
            if(popupWindow!=null) {
                if (popupWindow.isShowing())
                    popupWindow.dismiss();
            }
            Intent play = new Intent(this, menu_secundario.class);
            play.putExtra("username", playerName);
            startActivity(play);
            finish();
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int xteste = (int) event.getX();
            int yteste = (int) event.getY();
            clickCards(xteste, yteste, player);
        }


        return true;
    }
    public void setCardsCoords(ArrayList<Coordinates> cardsCoords) {
        this.cardsCoords = cardsCoords;
    }
    public ArrayList<Card> getPlayedDeck() {
        return playedDeck;
    }
    public int getNumberPlayers() {
        return numberPlayers;
    }
    public ArrayList<Player> getPlayers() {
        return players;
    }
    public Bitmap getCards() {
        return cards;
    }
    public void AtualizaInformacao() throws IOException {
        if(numDatas==0){
            v = new OurView(this, null, null, this);
            showPopUp("Waiting for other players!",false);
            socketThread.sendToServer(playerName);
            this.DataFromServer.remove(0);
            this.numDatas++;
        }
        else if (numDatas == 1) {
            String playerString = this.DataFromServer.remove(0);
            String[] players = playerString.split(",");
            initPlayers(players);
            this.numDatas++;
        } else if (numDatas == 2) {
            if (this.DataFromServer.size() >= 2) {
                String cardString = this.DataFromServer.remove(0);
                String playedCard = this.DataFromServer.remove(0);
                initDeck(cardString, playedCard);
                this.numDatas += 2;
                if(popupWindow!=null) {
                    if (popupWindow.isShowing())
                        popupWindow.dismiss();
                }
                drawView();
            }
        }
        else{
            if(DataFromServer.size()>0){
                String info = this.DataFromServer.remove(0);
                String[] infosplit = info.split(",");
                if(infosplit[0].equals("yourTurn")){
                    yourTurn = true;
                    receiveInfo(infosplit);
                    showPopUp("It's your turn to play!",true);
                    cardAtual= null;
                    return;
                }
                else if(info.equals("jackPlayed")){
                    chooseNaipe(player);
                }
                else if(infosplit[0].equals("deckCard")){
                    receiveCardsFromServer(infosplit);
                    drawView();
                }
                else if(info.equals("sevenPlayed")){
                    sevenPlayed = true;
                }
                else if(infosplit[0].equals("playedCard")) {
                    String[] dadoscard = infosplit[1].split(" ");
                    Card card = new Card(Integer.parseInt(dadoscard[0]), dadoscard[1]);
                    Player player = getPlayer(infosplit[2]);
                    player.setNumberCards(Integer.parseInt(infosplit[3]));
                    setPlayer(player);
                    card.setCoordenadas(this.cards);
                    playedDeck.add(card);
                    drawView();
                }
                else if(infosplit[0].equals("cardInfo")){
                    showPopUp(infosplit[1],true);
                    if(infosplit.length>=3){
                        String[] dadoscard = infosplit[2].split(" ");
                        Card card = new Card(Integer.parseInt(dadoscard[0]), dadoscard[1]);
                        card.setCoordenadas(this.cards);
                        playedDeck.add(card);
                        drawView();
                    }
                }
                else if(infosplit[0].equals("cardInfo2")){
                    showPopUp(infosplit[1],true);
                    receiveInfo(infosplit,2);
                }
                else if(infosplit[0].equals("disconnected")){
                    disconnect(infosplit);
                }
                else if(infosplit[0].equals("finish")){
                    if(infosplit.length>=3 && infosplit[1].equals("youWon")){
                        showPopUp("GANHASTE!",true);
                    }
                    else{
                        showPopUp("O JOGO ACABOU! NÂO GANHASTE!",true);
                    }
                    finish = true;
                }

            }
        }
        drawView();
    }
    private void disconnect(String[] infosplit){
        for(int i=0;i<players.size();i++){
            if(players.get(i).getNome().equals(infosplit[1])) {
                if(player.getNome().equals(players.get(i).getNome()))
                    finish = true;
                players.get(i).setNome(infosplit[2]);
            }
        }
    }
    public boolean isFinish() {
        return finish;
    }
    public void setFinish(boolean finish) {
        this.finish = finish;
    }
    private Player getPlayer(String username){
        for(int i=0;i<players.size();i++){
            if(players.get(i).getNome().equals(username))
                return players.get(i);
        }
        return null;
    }
    private void setPlayer(Player player){
        for(int i=0;i<players.size();i++){
            if(players.get(i).getNome().equals(player.getNome())) {
                players.set(i, player);
                break;
            }

        }
    }
    private void receiveInfo(String[] infosplit){
        for(int i=1;i<infosplit.length;i++){
            players.get(i-1).setNumberCards(Integer.parseInt(infosplit[i]));
        }
    }
    private void receiveInfo(String[] infosplit,int dois){
        for(int i=2;i<infosplit.length;i++){
            players.get(i-2).setNumberCards(Integer.parseInt(infosplit[i]));
        }
    }
    private void receiveCardsFromServer(String[] infosplit){
        String[] dadoscard;
        ArrayList<Card> hand = new ArrayList<>();
        for (int i=1;i<infosplit.length;i++){
            dadoscard = infosplit[i].split(" ");
            Card card = new Card(Integer.parseInt(dadoscard[0]),dadoscard[1]);
            card.setCoordenadas(this.cards);
            hand.add(card);
        }
        this.player.receiveCards(hand);
    }
    public void clickCards(int xteste, int yteste, Player playerAtual) {
        int i;
        Coordinates coordAtual;
        for (i = 0; i < cardsCoords.size(); i++) {
            coordAtual = cardsCoords.get(i);
            if (xteste >= coordAtual.getXmin() && xteste <= coordAtual.getXmax() && yteste >= coordAtual.getYmin() && yteste <= coordAtual.getYmax()) {
                cardAtual = coordAtual.getCard();
                ((RelativeLayout) findViewById(R.id.game)).removeView(v);
                v = new OurView(this, playerAtual, coordAtual.getCard(), this);
                ((RelativeLayout) findViewById(R.id.game)).addView(v);
                break;
            }
        }
    }
    public ArrayList<String> getDataFromServer() {
        return DataFromServer;
    }
    public void setDataFromServer(ArrayList<String> dataFromServer) {
        DataFromServer = dataFromServer;
    }
    public void confirmClicked(View view) {
        if(yourTurn) {
            if (cardAtual == null) {
                Toast.makeText(this, "Please choose a card!", Toast.LENGTH_LONG).show();
            } else {
                if(sevenPlayed){
                    if(cardAtual.getCode()!=7){
                        Toast.makeText(this, "Não pode jogar esta carta", Toast.LENGTH_SHORT).show();
                        cardAtual = null;
                        return;
                    }
                }
                if (compareCards(cardAtual, playedDeck.get(playedDeck.size() - 1))) {
                    playCard(cardAtual,player);
                    cardAtual = null;
                    yourTurn = false;
                    if(sevenPlayed)
                        sevenPlayed = false;
                    drawView();
                } else {
                    ((RelativeLayout) findViewById(R.id.game)).removeView(v);
                    v = new OurView(this, player, null, this);
                    ((RelativeLayout) findViewById(R.id.game)).addView(v);
                    cardAtual = null;
                }

            }
        }


    }
    public void initDeck(String cardString, String playedCard) {
        playedDeck = new ArrayList<>();
        String[] cards = cardString.split(",");
        ArrayList<Card> hand = new ArrayList<>();
        for (int i = 0; i < cards.length; i++) {
            String info[] = cards[i].split(" ");
            int width = this.cards.getWidth();
            hand.add(i, new Card(Integer.parseInt(info[0]), info[1], (Integer.parseInt(info[2]) * this.cards.getWidth() / 950), (Integer.parseInt(info[3]) * this.cards.getHeight() / 392)));
        }
        player.setHand(hand);
        player.setNumberCards(hand.size());
        String[] info2 = playedCard.split(" ");
        playedDeck.add(new Card(Integer.parseInt(info2[0]), info2[1], (Integer.parseInt(info2[2]) * this.cards.getWidth() / 950), (Integer.parseInt(info2[3]) * this.cards.getHeight() / 392)));
    }
    public void initPlayers(String[] players) {
        int i;
        this.players = new ArrayList<>();
        String[] info;
        for (i = 0; i < players.length; i++) {
            info = players[i].split(":");
            if (i == 0) {
                this.player = new Player(info[0], Integer.parseInt(info[1]));
            }
            else
                this.players.add(i-1, new Player(info[0], Integer.parseInt(info[1])));
        }

        this.numberPlayers = players.length - 1;
    }
    public boolean compareCards(Card card1, Card card2) {
        if (card1.getNaipe().equals(card2.getNaipe()) || card1.getCode() == card2.getCode())
            return true;
        else {
            Toast.makeText(this, "You can't play this card!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public void naipe(String naipe,Player player) throws InterruptedException {
        Card card;
        int x=(cards.getWidth()/13)*10,y;
        y= jackCoords(naipe);
        card = new Card(11,naipe,x,y);
        playedDeck.add(card);
        socketThread.sendToServer(String.format("11 %s",naipe));
        drawView();

    }
    private int jackCoords(String naipe){
        int y ;
        if(naipe.equals("Hearts"))
            y = (cards.getHeight()/4)*2;
        else if(naipe.equals("Clubs"))
            y=0;
        else if(naipe.equals("Diamonds"))
            y = (cards.getHeight()/4)*3;
        else
            y = (cards.getHeight()/4);
        return y;
    }
    private void chooseNaipe(final Player player){
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final ViewGroup container = (ViewGroup)layoutInflater.inflate(R.layout.choosenaipe,null);
        popupWindow = new PopupWindow(container, (int) (screenWidth*0.6), (int) (screenHeight*0.6),false);
        v.post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (int) (screenWidth*0.2), (int) (screenHeight*0.2));
                ImageButton clubs =(ImageButton) container.findViewById(R.id.clubs_symbol);
                clubs.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        try {
                            naipe("Clubs",player);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        popupWindow.dismiss();

                    }
                });
                ImageButton spades =(ImageButton) container.findViewById(R.id.spades_symbol);
                spades.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        try {
                            naipe("Spades",player);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        popupWindow.dismiss();

                    }
                });
                ImageButton diamonds =(ImageButton) container.findViewById(R.id.diamonds_symbol);
                diamonds.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        try {
                            naipe("Diamonds",player);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        popupWindow.dismiss();

                    }
                });
                ImageButton hearts =(ImageButton) container.findViewById(R.id.hearts_symbol);
                hearts.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        try {
                            naipe("Hearts",player);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        popupWindow.dismiss();

                    }
                });
            }
        });

    }
    public void showPopUp(String text,final boolean dismiss) {
        final String textaux = text;
        layoutInflator = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final ViewGroup container = (ViewGroup) layoutInflator.inflate(R.layout.popupwindow, null);
        if(popupWindow!=null) {
            if (popupWindow.isShowing())
                popupWindow.dismiss();
        }
        popupWindow = new PopupWindow(container, (int) (screenWidth * 0.6), (int) (screenHeight * 0.6), true);
        v.post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (int) (screenWidth * 0.2), (int) (screenHeight * 0.2));
                TextView next_player_string = (TextView) container.findViewById(R.id.player_name);
                next_player_string.setText(textaux);
                if(dismiss) {
                    container.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            popupWindow.dismiss();
                            drawView();
                            cardAtual = null;
                            return true;
                        }
                    });
                }
            }
        });

    }
    public Card playCard(Card card,Player player){
        playedDeck.add(card);
        player.removeCard(card);
        socketThread.sendToServer("playedCard,"+card.toString());
        return card;
    }
    public void drawView() {
        ((RelativeLayout) findViewById(R.id.game)).removeView(v);
        v = new OurView(this, player, cardAtual, this);
        ((RelativeLayout) findViewById(R.id.game)).addView(v);
    }
    public void deckClicked(View view) {
        if(yourTurn) {
            if(sevenPlayed){
                Toast.makeText(this, "És obrigado a assistir com um 7", Toast.LENGTH_SHORT).show();
                cardAtual = null;
                return;
            }
            final AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
            exitDialog.setMessage("Are you sure you want to take a card from deck?");
            exitDialog.setCancelable(true);
            exitDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int wich) {
                    socketThread.sendToServer("requestCard");

                    yourTurn = false;
                    cardAtual = null;

                }
            });
            exitDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int wich) {
                    dialog.dismiss();
                }
            });
            exitDialog.show();
        }
    }
    public void goToMenu(){
        Intent play = new Intent(this, menu_secundario.class);
        play.putExtra("username", playerName);
        startActivity(play);
        finish();
    }
    public void pauseClicked(View view) {
        final AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
        exitDialog.setMessage("Are you sure you want to exit?");
        exitDialog.setCancelable(true);
        exitDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int wich) {
                if(popupWindow!=null) {
                    if (popupWindow.isShowing())
                        popupWindow.dismiss();
                }
                goToMenu();

            }
        });
        exitDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int wich) {
                dialog.dismiss();
            }
        });
        exitDialog.show();

    }
}
class SocketThread extends Thread{
    private Socket socket;
    public static final int server_port = 6000;
    public static final String hostname = "194.210.173.153";//65;
    private BufferedReader inFromServer;
    private PrintWriter outToServer;
    private OnlineGame game;
    public SocketThread(OnlineGame game){
        this.game = game;
    }
    @Override
    public void run() {
        try {
            InetAddress serverAddr =InetAddress.getByName(hostname);
            this.socket = new Socket(serverAddr, server_port);
            this.inFromServer = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.outToServer = new PrintWriter(this.socket.getOutputStream(),true);
        } catch(SocketException e){
            e.printStackTrace();
            game.setFinish(true);
        } catch (IOException e) {
            e.printStackTrace();
            game.setFinish(true);
        }

        ArrayList<String> fromServer;
        while(true) {
            try {
                String msgReader = inFromServer.readLine();
                if (msgReader != null) {
                    if (Thread.interrupted()) {
                        game.setFinish(true);
                        return;
                    }
                    fromServer = game.getDataFromServer();

                    fromServer.add(msgReader);
                    game.setDataFromServer(fromServer);

                    game.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                game.AtualizaInformacao();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else{
                    game.setFinish(true);
                    socket.close();
                    break;
                }
            }catch(IOException e){
                e.printStackTrace();
                game.setFinish(true);
            }
        }

    }
    public void sendToServer(String info){
        final String aux = info;
        new Thread(){
            @Override
            public void run() {
                outToServer.println(aux);

            }
        }.start();
    }
}
class OurView extends View {
    private Player player;
    private Card card ;
    private Coordinates coords;
    private OnlineGame game;
    public OurView(Context context, Player player, Card card, OnlineGame game){
        super(context);
        this.player = player;
        this.card = card;
        this.game = game;
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDraw(Canvas c){
        ArrayList<Card> hand = player.getHand();
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
        for(i=0;i<game.getNumberPlayers();i++){
            if(this.player.getNome().equals( game.getPlayers().get(i).getNome()))
                c.drawText("♠",50,distTable-1,paint);
            if(player.getNome().equals(game.getPlayers().get(i).getNome())){
                info = player.getNome() +": "+ player.getNumberCards()+" Cards";
            }
            else
                info = game.getPlayers().get(i).getNome() +": "+ game.getPlayers().get(i).getNumberCards()+" Cards";
            c.drawText(info,75,distTable,paint);
            distTable+=30;

        }

        //Desenha atual carta jogada
        c.drawText("Last Played Card",c.getWidth()-270,75,paint);
        if(game.getPlayedDeck()!=null) {
            Card atualCard = game.getPlayedDeck().get(game.getPlayedDeck().size() - 1);
            Rect src2 = new Rect(Math.round(atualCard.getX()),Math.round(atualCard.getY()), Math.round(atualCard.getX() + (float)(game.getCards().getWidth() / 13)),Math.round(atualCard.getY() + (float)(game.getCards().getHeight() / 4)));
            Rect dst2 = new Rect(c.getWidth() - 220, 100, c.getWidth() - 220 + game.getCards().getWidth() / 13, 100 + game.getCards().getHeight() / 4);
            c.drawBitmap(game.getCards(), src2, dst2, null);
        }


        //Desenha cartas jogador atual;
        xDist = (c.getWidth()/2)-((((this.player.getNumberCards()-1)*((game.getCards().getWidth()/13)-30))+(game.getCards().getWidth()/13))/2);
        //Sem carta selecionada;
        ArrayList<Coordinates> cardscoords = new ArrayList<>();
        if(player.getNumberCards()==hand.size()) {
            if (card == null) {
                for (i = 0; i < this.player.getNumberCards(); i++) {
                    Rect src = new Rect(Math.round(hand.get(i).getX()),Math.round(hand.get(i).getY()), Math.round(hand.get(i).getX() + (float)(game.getCards().getWidth() / 13)),Math.round(hand.get(i).getY() + (float)(game.getCards().getHeight() / 4)));
                    Rect dst = new Rect(xDist, c.getHeight() - (((int) game.getCards().getHeight() / 4) + 20), game.getCards().getWidth() / 13 + xDist, game.getCards().getHeight() / 4 + (c.getHeight() - (((int) game.getCards().getHeight() / 4) + 20)));
                    c.drawBitmap(game.getCards(), src, dst, null);
                    coords = new Coordinates(hand.get(i), xDist, game.getCards().getWidth() / 13 + xDist, c.getHeight() - (((int) game.getCards().getHeight() / 4) + 20), game.getCards().getHeight() / 4 + (c.getHeight() - (((int) game.getCards().getHeight() / 4) + 20)));
                    if (cardscoords.size() > i) {
                        if (cardscoords.get(i).getCard().getCode() == hand.get(i).getCode() && cardscoords.get(i).getCard().getNaipe().equals(hand.get(i).getNaipe()))
                            cardscoords.set(i, coords);
                        else
                            cardscoords.add(coords);
                    } else
                        cardscoords.add(coords);
                    xDist += c.getWidth() / 13 - (5 * this.player.getNumberCards());
                /*Rect src = new Rect(hand.get(i).getX(), hand.get(i).getY(), hand.get(i).getX() + game.getCards().getWidth() / 13, hand.get(i).getY() + game.getCards().getHeight() / 4);
                Rect dst = new Rect(xDist, c.getHeight() - (((int) game.getCards().getHeight() / 4)+20), game.getCards().getWidth() / 13 + xDist, game.getCards().getHeight() / 4 + (c.getHeight() - (((int) game.getCards().getHeight() / 4)+20)));
                c.drawBitmap(game.getCards(), src, dst, null);
                coords = new Coordinates(hand.get(i),xDist,game.getCards().getWidth() / 13 + xDist,c.getHeight() - (((int) game.getCards().getHeight() / 4)+20),game.getCards().getHeight() / 4 + (c.getHeight() - (((int) game.getCards().getHeight() / 4)+20)));
                cardscoords.add(cardscoords.size(),coords);
                xDist += c.getWidth()/13 - (5*this.player.getNumberCards());*/
                }
            }
            //Com carta selecionada
            else {
                for (i = 0; i < this.player.getNumberCards(); i++) {
                    if (hand.get(i).getNaipe().equals(card.getNaipe()) && hand.get(i).getCode() == card.getCode()) {
                        Rect src = new Rect(Math.round(hand.get(i).getX()),Math.round(hand.get(i).getY()), Math.round(hand.get(i).getX() + (float)(game.getCards().getWidth() / 13)),Math.round(hand.get(i).getY() + (float)(game.getCards().getHeight() / 4)));
                        Rect dst = new Rect(xDist, c.getHeight() - (((int) game.getCards().getHeight() / 4) + 70), game.getCards().getWidth() / 13 + xDist, game.getCards().getHeight() / 4 + (c.getHeight() - (((int) game.getCards().getHeight() / 4) + 70)));
                        c.drawBitmap(game.getCards(), src, dst, null);
                        coords = new Coordinates(hand.get(i), xDist, game.getCards().getWidth() / 13 + xDist, c.getHeight() - (((int) game.getCards().getHeight() / 4) + 70), game.getCards().getHeight() / 4 + (c.getHeight() - (((int) game.getCards().getHeight() / 4) + 70)));
                        if (cardscoords.size() > i) {
                            if (cardscoords.get(i).getCard().getCode() == hand.get(i).getCode() && cardscoords.get(i).getCard().getNaipe().equals(hand.get(i).getNaipe()))
                                cardscoords.set(i, coords);
                            else
                                cardscoords.add(coords);
                        } else
                            cardscoords.add(coords);
                        xDist += c.getWidth() / 13 - (5 * this.player.getNumberCards());
                    } else {
                        Rect src = new Rect(Math.round(hand.get(i).getX()),Math.round(hand.get(i).getY()), Math.round(hand.get(i).getX() + (float)(game.getCards().getWidth() / 13)),Math.round(hand.get(i).getY() + (float)(game.getCards().getHeight() / 4)));
                        Rect dst = new Rect(xDist, c.getHeight() - (((int) game.getCards().getHeight() / 4) + 20), game.getCards().getWidth() / 13 + xDist, game.getCards().getHeight() / 4 + (c.getHeight() - (((int) game.getCards().getHeight() / 4) + 20)));
                        c.drawBitmap(game.getCards(), src, dst, null);
                        coords = new Coordinates(hand.get(i), xDist, game.getCards().getWidth() / 13 + xDist, c.getHeight() - (((int) game.getCards().getHeight() / 4) + 20), game.getCards().getHeight() / 4 + (c.getHeight() - (((int) game.getCards().getHeight() / 4) + 20)));
                        if (cardscoords.size() > i) {
                            if (cardscoords.get(i).getCard().getCode() == hand.get(i).getCode() && cardscoords.get(i).getCard().getNaipe().equals(hand.get(i).getNaipe()))
                                cardscoords.set(i, coords);
                            else
                                cardscoords.add(coords);
                        } else
                            cardscoords.add(coords);
                        xDist += c.getWidth() / 13 - (5 * this.player.getNumberCards());
                    }

                }
            }
        }
        game.setCardsCoords(cardscoords);


    }
}