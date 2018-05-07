package app.example.martins.highcards;

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
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
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
import java.util.Collections;
import java.util.Random;


public class MauMau extends Activity {
    private ArrayList<Player> players_list = new ArrayList<>();
    private Deck deck;
    private ArrayList<Card> playedDeck;
    private Bitmap cards;
    private ArrayList<Coordinates> cardsCoords;
    private OurView v;
    private Card cardAtual;
    private Player playerAtual;
    private int contador = 1,screenWidth,screenHeight;
    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;
    private boolean playerTurn=true;
    private boolean inverteSentido = false;
    private boolean over=false;
    private boolean move = false;
    private long seed = System.nanoTime();
    private Random random = new Random(seed);

    private String jogo;
    private int posicaofinal = 1;
    private ArrayList<String> LooseStrings = new ArrayList<>();
    private ArrayList<String> winStrings = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.game_layout);
        LooseStrings.add("Perdeste. Até a minha avózinha conseguia ficar melhor classificada");
        LooseStrings.add("Não ficaste em 1º lugar, também vais culpar o jogo de fazer batota?");
        LooseStrings.add("Parece que alguém precisa de treinar mais...");
        LooseStrings.add("A sério que não nos consegues ganhar? Tens de começar a dar mais luta...");
        LooseStrings.add("Nem a uns simples bots consegues ganhar...");
        LooseStrings.add("Vais precisar de fazer muito mais que isso se nos quiseres ganhar!");
        winStrings.add("Ficaste em 1º? Surpreendeste-nos agora!");
        winStrings.add("Conseguiste-nos ganhar? Aposto que não o fazes novamente...");
        winStrings.add("Foi tudo uma questão de sorte. Leva lá a tua vitória!");
        winStrings.add("Apanhaste-nos desconcentrados. Exigimos uma desforra, será que tens coragem?");
        winStrings.add("Desta vez ganhaste. Será que foi uma questão de sorte ou talento?");
        Bundle b = getIntent().getExtras();
        String playerName = b.getString("username");
        jogo = b.getString("Jogo");
        cards = BitmapFactory.decodeResource(getResources(),R.drawable.cards);
        initDeck();
        initPlayers(playerName,4);
        playerAtual = players_list.get(0);
        v = new OurView(players_list.get(0),this,null);
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
            move=false;
            int xteste = (int)event.getX();
            int yteste = (int)event.getY();
            clickCards(0,xteste,yteste);
        }
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            move=true;
            int x = (int)event.getX();
            int y = (int)event.getY();
            clickCards(1,x,y);
        }
        if(event.getAction()== MotionEvent.ACTION_UP){
            if(move){
                int x = (int)event.getX();
                int y = (int)event.getY();
                DisplayMetrics displaymetrics = new DisplayMetrics();
                this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int screenWidth = displaymetrics.widthPixels;
                int screenHeight = displaymetrics.heightPixels;
                if(cardAtual!=null){

                    int xmin= ((screenWidth/8)+cards.getWidth()/26);
                    int xmax = ((screenWidth*7)/8)-cards.getWidth()/26;
                    int ymin = (screenHeight/4)+cards.getHeight()/8;
                    int ymax = ((screenHeight/4)*3)-(cards.getHeight()/8);
                    if((x>=xmin && x<=xmax &&(y>=ymin && y<=ymax))){
                        try {
                            confirmClicked(v);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        clickCards(2,x,y);
                    }
                }
            }
        }
        return true;
    }
    public void clickCards(int move,int xteste, int yteste){
        int i;
        Coordinates coordAtual;
        if(move==0) {
            for (i = cardsCoords.size() - 1; i >= 0; i--) {
                coordAtual = cardsCoords.get(i);
                if (xteste >= coordAtual.getXmin() && xteste <= coordAtual.getXmax() && yteste >= coordAtual.getYmin() && yteste <= coordAtual.getYmax()) {
                    cardAtual = coordAtual.getCard();
                    ((RelativeLayout) findViewById(R.id.game)).removeView(v);
                    v = new OurView(players_list.get(0), this, coordAtual.getCard());
                    ((RelativeLayout) findViewById(R.id.game)).addView(v);
                    break;
                }
            }
        }
        else if(move==2){
            ((RelativeLayout) findViewById(R.id.game)).removeView(v);
            v = new OurView(players_list.get(0), this, null);
            ((RelativeLayout) findViewById(R.id.game)).addView(v);
        }
        else{
            if(cardAtual!=null) {
                for (i = cardsCoords.size() - 1; i >= 0; i--) {
                    if (cardsCoords.get(i).getCard().getCode() == cardAtual.getCode() && cardsCoords.get(i).getCard().getNaipe().equals(cardAtual.getNaipe())) {
                        break;
                    }
                }
                if (i >= 0) {
                    ((RelativeLayout) findViewById(R.id.game)).removeView(v);
                    v = new OurView(true, xteste, yteste, players_list.get(0), this, cardsCoords.get(i).getCard());
                    ((RelativeLayout) findViewById(R.id.game)).addView(v);
                }
            }

        }
    }
    public void initDeck(){
        this.deck = new Deck(52,cards);
        this.deck.shuffle();
        this.playedDeck = new ArrayList <>();
        Card card = this.deck.removeFromDeck(true,1);
        playedDeck.add(card);
        this.deck.cards.add(card);
    }
    public void initPlayers(String playerName, int numPlayers) {
        players_list.add(new Player(playerName,false));
        players_list.add(new Player("Afonso",true));
        players_list.add(new Player("Matilde",true));
        players_list.add(new Player("Rodrigo",true));


        for (int i = 0; i < numPlayers; i++) {
            this.deck.giveCards(true, 7, this.players_list.get(i));
        }
    }
    public boolean compareCards(Card card1,Card card2){
        return card1.getNaipe().equals(card2.getNaipe()) || card1.getCode() == card2.getCode();
    }
    private void finalMessage(String message){
        if(popupWindow!=null) {
            if (popupWindow.isShowing())
                popupWindow.dismiss();
        }
        final AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
        TextView title = new TextView(this);
        title.setText("Final do Jogo");
        title.setTextSize(screenHeight/30);
        title.setTextColor(Color.rgb(153,204,255));
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        exitDialog.setCustomTitle(title);
        TextView text = new TextView(this);
        text.setText(message);
        text.setTextSize(screenHeight/50);
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        exitDialog.setView(text);
        exitDialog.setCancelable(false);
        exitDialog.setPositiveButton("Sair",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int wich){
                Intent menu = new Intent(getBaseContext(),menu_secundario.class);
                menu.putExtra("username",players_list.get(0).getNome());
                menu.putExtra("Jogo",jogo);
                startActivity(menu);
                finish();
            }
        });
        exitDialog.show();
    }

    private boolean isOver(Player player){
        if(player.getNumberCards()== 0) {
            if (player.isBot()) {
                removeFromGame(player);
                posicaofinal++;
            } else {
                if (posicaofinal == 1) {
                    int randnum = random.nextInt(winStrings.size());
                    finalMessage(winStrings.get(randnum));
                }
                else {
                    int randnum = random.nextInt(LooseStrings.size());
                    finalMessage(LooseStrings.get(randnum));
                }
                return true;
            }
        }
        if(players_list.size() == 1 && !players_list.get(0).isBot()) {
            int randnum = random.nextInt(LooseStrings.size());
            finalMessage(LooseStrings.get(randnum));
            return true;
        }
        return false;
    }
    public void drawView(){
        ((RelativeLayout)findViewById(R.id.game)).removeView(v);
        v = new OurView(players_list.get(0),this,null);
        ((RelativeLayout)findViewById(R.id.game)).addView(v);
    }
    public void pauseClicked(View view) {
        if(popupWindow!=null) {
            if (popupWindow.isShowing())
                popupWindow.dismiss();
        }
        final AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
        exitDialog.setMessage("Pretende sair?");
        exitDialog.setCancelable(true);
        exitDialog.setPositiveButton("Sim",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int wich){
                Intent menu = new Intent(getBaseContext(),menu_secundario.class);
                menu.putExtra("username",players_list.get(0).getNome());
                menu.putExtra("Jogo",jogo);
                startActivity(menu);
                finish();
            }
        });
        exitDialog.setNegativeButton("Cancelar",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int wich){
                dialog.dismiss();
            }
        });
        exitDialog.show();

    }
    public void removeFromGame(Player player){
        players_list.remove(player);
    }

    public Card playCard(Card card,Player player){
        playedDeck.add(card);
        this.deck.cards.add(card);
        player.removeCard(card);
        return card;
    }
    public Player nextPlayer(Player lastplayer) throws InterruptedException {

        if(!inverteSentido){
            for(int i =0 ;i<players_list.size();i++) {
                if(lastplayer == players_list.get(i)){
                    if (i == players_list.size() - 1)
                        return players_list.get(0);
                    else
                        return players_list.get(i + 1);
                }
            }

        }
        else{
            for(int i =0 ;i<players_list.size();i++) {
                if(lastplayer == players_list.get(i)){
                    if (i == 0)
                        return players_list.get((players_list.size()-1));
                    else
                        return players_list.get(i - 1);
                }
            }
        }

        return null;
    }
    public void checkPlayer(Player player){
        playerAtual = player;
        Handler handler = new Handler();
        if(player.isBot()){
            final Player player1 = player;
            handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
            try {
                botPlay(player1);
            } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, 2500);

        }
        else{
            playerTurn= true;
            if(!over)
                popUpTurn("É a sua vez!",true);
        }
    }
    public void naipe(String naipe,Player player) throws InterruptedException {
        Card card;
        float x=((float)cards.getWidth()/13)*10,y;
        y= jackCoords(naipe);
        card = new Card(11,naipe,x,y);
        playedDeck.add(card);
        drawView();
        checkPlayer(nextPlayer(player));
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
    public void popUpTurn(final String text,final boolean dismiss) {
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.popup_playerturn, null);
        if(popupWindow!=null) {
            if (popupWindow.isShowing())
                popupWindow.dismiss();
        }
        popupWindow = new PopupWindow(container, (int) (screenWidth*0.6), (int) (screenHeight*0.6), true);;
        v.post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (int) (screenWidth * 0.2), (int) (screenHeight * 0.2));
                TextView texto = (TextView) container.findViewById(R.id.textView5);
                Typeface currentTypeFace = texto.getTypeface();
                Typeface bold = Typeface.create(currentTypeFace, Typeface.BOLD);
                texto.setTypeface(bold);
                texto.setText(text);
                if(dismiss) {
                    container.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            popupWindow.dismiss();
                            drawView();
                            return true;
                        }
                    });
                }
            }
        });

    }
    public void confirmClicked(View view) throws InterruptedException {
        if(playerTurn) {
            if (cardAtual == null) {
                Toast.makeText(this, "Por favor escolha uma carta!", Toast.LENGTH_LONG).show();
            } else {
                if (compareCards(cardAtual, playedDeck.get(playedDeck.size() - 1))) {
                    boolean pass = true;
                    if(playedDeck.get(playedDeck.size()-1).getCode()==7 && hasSeven(players_list.get(0)) && playedDeck.size()>1) {
                        pass=false;
                        if(cardAtual.getCode()==7) {
                            pass = true;
                        }

                    }
                    if(pass){
                        playCard(cardAtual, players_list.get(0));
                        cardAtual = null;
                        playerTurn = false;
                        drawView();
                        over = isOver(players_list.get(0));
                        if(!over)
                            checkCard(playedDeck.get(playedDeck.size() - 1), players_list.get(0));
                    }
                    else{
                        Toast.makeText(this, "Não pode jogar esta carta", Toast.LENGTH_SHORT).show();
                        ((RelativeLayout) findViewById(R.id.game)).removeView(v);
                        v = new OurView(players_list.get(0), this, null);
                        ((RelativeLayout) findViewById(R.id.game)).addView(v);
                        cardAtual = null;
                    }
                } else {
                    Toast.makeText(this, "Não pode jogar esta carta", Toast.LENGTH_SHORT).show();
                    ((RelativeLayout) findViewById(R.id.game)).removeView(v);
                    v = new OurView(players_list.get(0), this, null);
                    ((RelativeLayout) findViewById(R.id.game)).addView(v);
                    cardAtual = null;
                }

            }
        }
        else{
            Toast.makeText(this,"Espere pelo seu turno",Toast.LENGTH_LONG).show();
        }
    }

    public void deckClicked(View view) {
        if(playerTurn) {
            final AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
            exitDialog.setMessage("Deseja retirar uma carta do baralho?");
            exitDialog.setCancelable(true);
            exitDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int wich) {
                    deck.giveCards(true, 1, players_list.get(0));
                    playerTurn = false;
                    drawView();
                    cardAtual = null;
                    try {
                        checkPlayer(nextPlayer(players_list.get(0)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
            exitDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int wich) {
                    dialog.dismiss();
                }
            });
            exitDialog.show();
        }
        else{
            Toast.makeText(this,"Espere pelo seu turno",Toast.LENGTH_LONG).show();
        }
    }


    private void botPlay(Player player) throws InterruptedException {
        Card lastCard = playedDeck.get(playedDeck.size()-1);
        ArrayList<Card> hand= player.getHand();
        int i;
        if(contador>1 && hasSeven(player)){
            for(i =0 ;i<hand.size();i++){
                if(hand.get(i).getCode() == 7)
                    break;
            }
            playCard(hand.get(i),player);
            checkCard(playedDeck.get(playedDeck.size()-1),player);
            isOver(player);
        }
        else{
            boolean hasCard = false;
            for(int j =0;j<hand.size();j++){
                if(compareCards(hand.get(j),lastCard)){
                    hasCard = true;
                    playCard(hand.get(j),player);
                    break;
                }
            }
            if(!hasCard){
                Card card = null;
                deck.giveCards(true,1,player);
                checkCard(card,player);
            }
            else{
                checkCard(playedDeck.get(playedDeck.size()-1),player);
                isOver(player);
            }
        }

        drawView();
    }
    private void queenPlayed(){
       if(inverteSentido){
           inverteSentido = false;
       }
        else
           inverteSentido = true;
    }
    private String freq_cards(Player player){
        ArrayList<Card>hand = player.getHand();
        ArrayList<String> naipes = new ArrayList<>();
        for(int i=0;i<player.getHand().size();i++){
            naipes.add(hand.get(i).getNaipe());
        }
        int copas = Collections.frequency(naipes,"Hearts");
        int espadas = Collections.frequency(naipes,"Spades");
        int paus = Collections.frequency(naipes,"Clubs");
        int ouros = Collections.frequency(naipes,"Diamonds");
        if(copas >= espadas && copas >= paus && copas>=ouros)
            return "Hearts";
        else if(espadas >=copas && espadas>=paus && espadas>=ouros)
            return "Spades";
        else if(paus >=espadas && paus>=copas && paus>=ouros)
            return "Clubs";
        else
            return "Diamonds";
    }
    private void jackPlayed(Player player) throws InterruptedException {
        String naipe ;
        float x = 10*(float)cards.getWidth()/13;
        float y;
        Card card;
        if(player == players_list.get(0)){
            chooseNaipe(player);
        }
        else{
            naipe = freq_cards(player);
            y= jackCoords(naipe);
            card = new Card(11,naipe,x,y);
            playedDeck.add(card);
            checkPlayer(nextPlayer(player));
        }
    }
    private float jackCoords(String naipe){
        float y ;
        if(naipe.equals("Hearts"))
            y = ((float)cards.getHeight()/4)*2;
        else if(naipe.equals("Clubs"))
            y=0;
        else if(naipe.equals("Diamonds"))
            y = ((float)cards.getHeight()/4)*3;
        else
            y = (float)cards.getHeight()/4;
        return y;
    }
    private void ninePlayed(Player player) throws InterruptedException {
        Player next = nextPlayer(player);
        deck.giveCards(true,1,next);
    }
    private void sevenPlayed(Player player1) throws InterruptedException {
        Player player = nextPlayer(player1);
        boolean hasSeven = hasSeven(player);
        if(!hasSeven) {
            deck.giveCards(true, 2 * contador, player);
            contador = 1;
        }
        else{
            contador++;
        }

    }
    private boolean hasSeven(Player player){
        boolean hasSeven = false;
        ArrayList<Card> hand = player.getHand();
        for(int i =0 ;i<hand.size();i++){
            if(hand.get(i).getCode() == 7){
                hasSeven = true;
                break;
            }
        }
        if(!hasSeven)
            return false;
        return true;
    }
    private int checkCard(Card card,Player player) throws InterruptedException {
        if(card == null)
            checkPlayer(nextPlayer(player));
        else{
            switch (card.getCode()){
                case 1:checkPlayer(nextPlayer(nextPlayer(player)));return 1;
                case 7: sevenPlayed(player);checkPlayer(nextPlayer(player));return 7;
                case 9: ninePlayed(player);checkPlayer(nextPlayer(player));return 9;
                case 11: jackPlayed(player);return 11;
                case 12: queenPlayed();checkPlayer(nextPlayer(player));return 12;
                default: checkPlayer(nextPlayer(player));return 0;
            }
        }
        return 0;
    }

    public class OurView extends View {
        private Player player;
        private Card card ;
        private Coordinates coords;
        private boolean move;
        private int xatual,yatual;

        public OurView(Player player,Context context, Card card){
            super(context);
            this.player = player;
            this.card = card;
            cardsCoords= new ArrayList<>();
            this.move = false;
        }
        public OurView(boolean move,int x,int y,Player player,Context context, Card card){
            super(context);
            this.player = player;
            this.card = card;
            cardsCoords= new ArrayList<>();
            this.move = move;
            this.xatual = x;
            this.yatual=y;
        }
        @Override
        protected void onDraw(Canvas c){
            ArrayList<Card> hand = players_list.get(0).getHand();
            int xDist,i;

            //Dese9nha jogadores e seu num cartas
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(c.getHeight()/25);
            Paint paint2 = new Paint();
            paint2.setStyle(Paint.Style.FILL);
            paint2.setTextSize(c.getHeight()/20);
            paint2.setColor(Color.RED);
            Typeface currentTypeFace =   paint.getTypeface();
            Typeface bold = Typeface.create(currentTypeFace, Typeface.BOLD);
            paint2.setTypeface(bold);
            paint.setTypeface(bold);
            String info;
            int distTable=75;
            int x=0,y=0;
            int aux;
            for(i=0;i<players_list.size();i++){
                info = players_list.get(i).getNome() +": "+ players_list.get(i).getNumberCards()+" Cartas";
                if(i==0||i==2)
                    x=c.getWidth()/2;
                else {
                    x = (c.getWidth() / 2) - ((2 - i) * (c.getWidth() / 3));
                }
                if(i==1||i==3)
                    y=c.getHeight()/2;
                else {
                    aux = (c.getHeight()/4)*(1-i);
                    y = (c.getHeight() / 2) + aux;
                }
                x-=(c.getWidth()/12);
                if(players_list.get(i).getNome().equals(playerAtual.getNome()))
                    c.drawText(info,x,y,paint2);
                else
                    c.drawText(info,x,y,paint);
            }

            //Desenha atual carta jogada
            //c.drawText("Ultima Carta Jogada",c.getWidth()-270,75,paint);
            Card atualCard = playedDeck.get(playedDeck.size()-1);
            Rect src2 = new Rect(Math.round(atualCard.getX()),Math.round(atualCard.getY()), Math.round(atualCard.getX() + (float)cards.getWidth() / 13),Math.round(atualCard.getY() + (float)cards.getHeight() / 4));
            Rect dst2 = new Rect(Math.round((float)c.getWidth()/2-(float)cards.getWidth()/26),Math.round((float)c.getHeight()/2-(float)cards.getHeight()/8) ,Math.round((float)c.getWidth()/2+(float)cards.getWidth()/26),Math.round((float)c.getHeight()/2+(float)cards.getHeight() /8));
            c.drawBitmap(cards, src2, dst2, null);


            //Desenha cartas jogador atual;

            //int aux2 = (this.player.getNumberCards()-1)*((cards.getWidth()/26));
            if(this.player.getNumberCards()>0) {
                xDist = (c.getWidth() / 4) + ((c.getWidth() / 4) / this.player.getNumberCards());
                //Sem carta selecionada;
                if (card == null) {
                    for (i = 0; i < this.player.getNumberCards(); i++) {
                        Rect src = new Rect(Math.round(hand.get(i).getX()),Math.round(hand.get(i).getY()), Math.round(hand.get(i).getX() + (float)cards.getWidth() / 13),Math.round(hand.get(i).getY() + (float)cards.getHeight() / 4));
                        Rect dst = new Rect(xDist, c.getHeight() - (((int) cards.getHeight() / 4) + (c.getHeight() / 30)), cards.getWidth() / 13 + xDist, cards.getHeight() / 4 + (c.getHeight() - (((int) cards.getHeight() / 4) + (c.getHeight() / 30))));
                        c.drawBitmap(cards, src, dst, null);
                        coords = new Coordinates(hand.get(i), xDist, cards.getWidth() / 13 + xDist, c.getHeight() - (((int) cards.getHeight() / 4) + (c.getHeight() / 30)), cards.getHeight() / 4 + (c.getHeight() - (((int) cards.getHeight() / 4) + (c.getHeight() / 30))));
                        cardsCoords.add(cardsCoords.size(), coords);
                        int aux2 = ((c.getWidth() / 2) / this.player.getNumberCards());
                        if (aux2 < cards.getWidth() / 13)
                            xDist += ((c.getWidth() / 2) / this.player.getNumberCards());//-(this.player.getNumberCards());
                        else
                            xDist += cards.getWidth() / 13;
                    }

                }
                //Com carta selecionada
                else {
                    for (i = 0; i < this.player.getNumberCards(); i++) {
                        if (hand.get(i).getNaipe().equals(card.getNaipe()) && hand.get(i).getCode() == card.getCode()) {
                            Rect src = new Rect(Math.round(hand.get(i).getX()),Math.round(hand.get(i).getY()), Math.round(hand.get(i).getX() + (float)cards.getWidth() / 13),Math.round(hand.get(i).getY() + (float)cards.getHeight() / 4));
                            Rect dst;
                            if(!move) {
                                dst = new Rect(xDist, c.getHeight() - (((int) cards.getHeight() / 4) + (c.getHeight() / 10)), cards.getWidth() / 13 + xDist, cards.getHeight() / 4 + (c.getHeight() - (((int) cards.getHeight() / 4) + (c.getHeight() / 10))));
                                coords = new Coordinates(hand.get(i), xDist, cards.getWidth() / 13 + xDist, c.getHeight() - (((int) cards.getHeight() / 4) + (c.getHeight() / 10)), cards.getHeight() / 4 + (c.getHeight() - (((int) cards.getHeight() / 4) + (c.getHeight() / 10))));
                            }
                            else{
                                dst = new Rect(xatual-(cards.getWidth()/26),yatual-(cards.getHeight()/8), xatual+(cards.getWidth() / 26),yatual+ (cards.getHeight() / 8));
                                coords = new Coordinates(hand.get(i), xatual-(cards.getWidth()/26), cards.getWidth() / 26 + xatual, yatual-(cards.getHeight()/8), yatual+(cards.getHeight() / 8 ));

                            }
                            c.drawBitmap(cards, src, dst, null);
                            //coords = new Coordinates(hand.get(i), xDist, cards.getWidth() / 13 + xDist, c.getHeight() - (((int) cards.getHeight() / 4) + (c.getHeight() / 10)), cards.getHeight() / 4 + (c.getHeight() - (((int) cards.getHeight() / 4) + (c.getHeight() / 10))));
                            cardsCoords.add(cardsCoords.size(), coords);
                            int aux2 = ((c.getWidth() / 2) / this.player.getNumberCards());
                            if (aux2 < cards.getWidth() / 13)
                                xDist += ((c.getWidth() / 2) / this.player.getNumberCards());//-(this.player.getNumberCards());
                            else
                                xDist += cards.getWidth() / 13;
                        } else {
                            Rect src = new Rect(Math.round(hand.get(i).getX()),Math.round(hand.get(i).getY()), Math.round(hand.get(i).getX() + (float)cards.getWidth() / 13),Math.round(hand.get(i).getY() + (float)cards.getHeight() / 4));
                            Rect dst = new Rect(xDist, c.getHeight() - (((int) cards.getHeight() / 4) + (c.getHeight() / 30)), cards.getWidth() / 13 + xDist, cards.getHeight() / 4 + (c.getHeight() - (((int) cards.getHeight() / 4) + (c.getHeight() / 30))));
                            c.drawBitmap(cards, src, dst, null);
                            coords = new Coordinates(hand.get(i), xDist, cards.getWidth() / 13 + xDist, c.getHeight() - (((int) cards.getHeight() / 4) + (c.getHeight() / 30)), cards.getHeight() / 4 + (c.getHeight() - (((int) cards.getHeight() / 4) + (c.getHeight() / 30))));
                            cardsCoords.add(cardsCoords.size(), coords);
                            int aux2 = ((c.getWidth() / 2) / this.player.getNumberCards());
                            if (aux2 < cards.getWidth() / 13)
                                xDist += ((c.getWidth() / 2) / this.player.getNumberCards());//-(this.player.getNumberCards());
                            else
                                xDist += cards.getWidth() / 13;
                        }

                    }
                }
            }


        }




    }





}
