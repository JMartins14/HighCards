package app.example.martins.highcards;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
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
import android.os.Handler;
import android.app.AlertDialog;
import android.content.DialogInterface;

import app.example.martins.highcards.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Random;

public class Desconfia extends Activity {
    private ArrayList<Player> players_list = new ArrayList<>();
    private Deck deck;
    private ArrayList<Card> playedDeck;
    private Bitmap cards;
    private Bitmap backgroundcard;
    private ArrayList<Coordinates> cardsCoords;
    private OurView v;
    private boolean pressed = false;
    private ArrayList<Card> cardsEscolhidos = new ArrayList<>();
    private ArrayList<Card> ditasCartas = new ArrayList<>();
    private ArrayList<Card> cartasCampo = new ArrayList<>();
    private boolean playerTurn = true;
    private int screenWidth, screenHeight;
    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;
    private Player playerAtual;
    private int posremovedPlayer = 0;
    private ArrayList<Card> userPlayed = new ArrayList<>();
    private boolean tempoDesconfiar;
    private boolean terminou = false;
    private int codeAtual;
    private int contaPassas = 0;
    private boolean first = true;
    private String jogo;
    private int posicaofinal=1;
    private ArrayList<String> LooseStrings = new ArrayList<>();
    private ArrayList<String> winStrings = new ArrayList<>();
    private long seed = System.nanoTime();
    private Random random = new Random(seed);

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.desconfia);
        cards = BitmapFactory.decodeResource(getResources(), R.drawable.cards);
        backgroundcard = BitmapFactory.decodeResource(getResources(), R.drawable.card_background);
        LooseStrings.add("Perdeste. Até a minha avozinha conseguia ficar melhor classificada");
        LooseStrings.add("Não ficaste em 1º lugar, também vais culpar o jogo de fazer batota?");
        LooseStrings.add("Parece que alguém precisa de treinar mais...");
        LooseStrings.add("A sério que não nos consegues ganhar? Tens de começar a dar mais luta...");
        LooseStrings.add("Nem a uns simples bots consegues ganhar...");
        LooseStrings.add("Vais precisar de fazer muito mais que isso se nos quiseres ganhar!");
        winStrings.add("Ficaste em 1º? Surpreendeste-nos agora!");
        winStrings.add("Conseguiste-nos ganhar? Aposto que não o fazes novamente...");
        winStrings.add("Foi tudo uma questão de sorte. Leva lá a tua vitória!");
        winStrings.add("Apanhaste-nos desconcentrados. Exigimos uma desforra, será que tens coragem?");
        winStrings.add("Desta vez ganhaste. Será que foi uma questão de sorte ou de talento?");
        Bundle b = getIntent().getExtras();
        String playerName = b.getString("username");
        jogo = b.getString("Jogo");
        Bitmap cardmap = null;
        initDeck();
        initPlayers(playerName, 4);
        v = new OurView(players_list.get(0), this, null);
        ((RelativeLayout) findViewById(R.id.game)).addView(v);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        playerAtual = players_list.get(0);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void clickCards(int xteste, int yteste) {
        int i;
        Coordinates coordAtual;
        for (i = cardsCoords.size() - 1; i >= 0; i--) {
            coordAtual = cardsCoords.get(i);
            if (xteste >= coordAtual.getXmin() && xteste <= coordAtual.getXmax() && yteste >= coordAtual.getYmin() && yteste <= coordAtual.getYmax()) {
                if (coordAtual.getCard().isEscolhida()) {
                    coordAtual.getCard().setEscolhida(false);
                    if (cardsEscolhidos.indexOf(coordAtual.getCard()) >= 0)
                        cardsEscolhidos.remove(cardsEscolhidos.indexOf(coordAtual.getCard()));
                    ((RelativeLayout) findViewById(R.id.game)).removeView(v);
                    v = new OurView(players_list.get(0), this, cardsEscolhidos);
                    ((RelativeLayout) findViewById(R.id.game)).addView(v);
                } else {
                    if (cardsEscolhidos.size() <= 3) {
                        cardsEscolhidos.add(coordAtual.getCard());
                        cardsEscolhidos.get(cardsEscolhidos.size() - 1).setEscolhida(true);
                        ((RelativeLayout) findViewById(R.id.game)).removeView(v);
                        v = new OurView(players_list.get(0), this, cardsEscolhidos);
                        ((RelativeLayout) findViewById(R.id.game)).addView(v);
                    }
                }
                break;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int xteste = (int) event.getX();
            int yteste = (int) event.getY();
            if (playerTurn)
                clickCards(xteste, yteste);
        }

        return true;
    }


    public void desconfiarClicked(View view) {
        int i;
        if (!playerTurn) {
            pressed = true;
            if (tempoDesconfiar) {
                if (verificaVerdade(cardsEscolhidos, ditasCartas)) {
                    players_list.get(0).receiveCards(playedDeck);
                    showPopUp(players_list.get(0).getNome() + " apanhou todas as cartas jogadas", true);
                    playedDeck = new ArrayList<>();
                    try {
                        playerAtual = lastPlayer(playerAtual);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    playerAtual.receiveCards(playedDeck);
                    showPopUp(playerAtual.getNome() + " apanhou todas as cartas jogadas", true);
                    playedDeck = new ArrayList<>();
                    try {
                        playerAtual = lastPlayer(players_list.get(0));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                cartasCampo.clear();
                for (i = 0; i < players_list.size(); i++) {
                    players_list.get(i).setEstado(true);
                }
                contaPassas = players_list.size() - 1;
            } else {
                Toast.makeText(this, "O tempo para desconfiar esgotou", Toast.LENGTH_SHORT).show();
            }
            pressed = false;
        } else {
            Toast.makeText(this, "Não pode desconfiar de si mesmo", Toast.LENGTH_SHORT).show();

        }
    }

    public boolean verificaVerdade(ArrayList<Card> cardsEscolhidos, ArrayList<Card> ditasCartas) {
        int aux = 0;
        for (int i = 0; i < cardsEscolhidos.size(); i++) {
            if (cardsEscolhidos.get(i).getCode() == ditasCartas.get(0).getCode()) {
                aux++;
            }
        }
        if (aux == cardsEscolhidos.size()) {
            return true;
        } else {
            return false;
        }
    }


    private boolean isOver() {
        if (playerAtual.getNumberCards() == 0) {
            if (playerAtual.isBot()) {
                removeFromGame();
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
        if (players_list.size() == 1 && !players_list.get(0).isBot()) {
            int randnum = random.nextInt(LooseStrings.size());
            finalMessage(LooseStrings.get(randnum));
            return true;
        }
        return false;
    }

    public void drawView() {
        ((RelativeLayout) findViewById(R.id.game)).removeView(v);
        v = new OurView(players_list.get(0), this, null);
        ((RelativeLayout) findViewById(R.id.game)).addView(v);
    }

    public void pauseClicked(View view) {
        if (popupWindow != null) {
            if (popupWindow.isShowing())
                popupWindow.dismiss();
        }
        final AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
        exitDialog.setMessage("Pretende sair?");
        exitDialog.setCancelable(true);
        exitDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int wich) {
                terminou = true;
                finish();
                Intent menu = new Intent(getBaseContext(), menu_secundario.class);
                menu.putExtra("username", players_list.get(0).getNome());
                menu.putExtra("Jogo", jogo);
                startActivity(menu);
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

    public Player isPlayer(String username) {
        for (int i = 0; i < players_list.size(); i++) {
            if (players_list.get(i).getNome().equals(username))
                return players_list.get(i);
        }
        return null;
    }

    public void removeFromGame() {
        try {
            posremovedPlayer = players_list.indexOf(lastPlayer(isPlayer(playerAtual.getNome())));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        players_list.remove(isPlayer(playerAtual.getNome()));
    }

    public ArrayList<Card> playCards(ArrayList<Card> cardsEscolhidos) throws InterruptedException {
        for (int i = 0; i < cardsEscolhidos.size(); i++) {
            playedDeck.add(cardsEscolhidos.get(i));
            playerAtual.removeCard(cardsEscolhidos.get(i));
        }
        drawView();
        checkDesconfia();
        return cardsEscolhidos;
    }

    public boolean allplayerspassed() {
        for (int i = 0; i < players_list.size(); i++) {
            if (players_list.get(i).isEstado())
                return false;
        }
        for (int i = 0; i < players_list.size(); i++) {
            players_list.get(i).setEstado(true);
        }
        contaPassas = players_list.size() - 1;
        return true;
    }

    public void nextPlayer() throws InterruptedException {
        int i = 0;
        int contador = 0;
        while (true) {
            contador++;
            if (i == players_list.size())
                i = 0;
            if (playerAtual.getNome().equals(players_list.get(i).getNome())) {
                contador = 0;
                if (i == players_list.size() - 1) {
                    playerAtual = players_list.get(0);
                } else
                    playerAtual = players_list.get(i + 1);
                if (playerAtual.isEstado() || allplayerspassed())
                    break;
            }
            if (contador > players_list.size() - 1) {
                playerAtual = players_list.get(posremovedPlayer);
            }
            i++;
        }
    }

    public Player lastPlayer(Player playerAtual) throws InterruptedException {
        int i = players_list.size() - 1;
        while (true) {
            if (playerAtual == players_list.get(i)) {
                if (i == 0) {
                    return players_list.get(players_list.size() - 1);
                } else
                    return players_list.get(i - 1);
            }
            i--;
        }
    }

    public void checkPlayer() {
        Handler handler = new Handler();
        if (playerAtual.isBot()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!terminou)
                            botPlay();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, 2000);

        } else {
            playerTurn = true;
            String msg;
            if (playerAtual.isEstado()) {
                if (contaPassas < players_list.size() - 1)
                    msg = String.format("É o seu turno. A jogada está puxada a %s", cardName(new Card(codeAtual)));
                else {
                    msg = String.format("É o seu turno. Você pode começar uma rodada nova", codeAtual);
                }
            } else {
                msg = String.format("É o seu turno. Você é obrigado a passar.");
            }
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void botdesconfia() {
        int rand;
        rand = random.nextInt(4) + 1;
        boolean desconfia = false;
        if (!pressed) {
            int contador;
            ArrayList<Card> hand;

            if (players_list.size() > 1) {
                for (int i = 1; i < players_list.size(); i++) {
                    hand = players_list.get(i).getHand();
                    contador = 0;
                    for (int j = 0; j < players_list.get(i).getHand().size(); j++) {
                        if (hand.get(j).getCode() == codeAtual) {
                            contador++;
                        }
                    }
                    if (contador > 4 - userPlayed.size()) {
                        desconfia = true;
                        break;
                    }
                }
            }
            if (rand == 1 || desconfia) {
                tempoDesconfiar = false;
                if (players_list.size() > 1)
                    rand = random.nextInt(players_list.size() - 1) + 1;
                else
                    rand = 1;
                while (playerAtual.getNome().equals(players_list.get(rand).getNome())) {
                    if (players_list.size() > 1)
                        rand = random.nextInt(players_list.size() - 1) + 1;
                    else {
                        rand = 1;
                        break;
                    }
                }
                if (verificaVerdade(cardsEscolhidos, ditasCartas)) {
                    players_list.get(rand).receiveCards(playedDeck);
                    showPopUp(players_list.get(rand).getNome() + " apanhou todas as cartas jogadas", true);
                    playedDeck = new ArrayList<>();
                    try {
                        playerAtual = lastPlayer(playerAtual);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    playerAtual.receiveCards(playedDeck);
                    showPopUp(playerAtual.getNome() + " apanhou todas as cartas jogadas", true);
                    playedDeck = new ArrayList<>();
                    try {
                        playerAtual = lastPlayer(players_list.get(rand));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                cartasCampo.clear();
                userPlayed.clear();
                for (int i = 0; i < players_list.size(); i++) {
                    players_list.get(i).setEstado(true);
                }
                contaPassas = players_list.size() - 1;
            }
        }
    }

    public void checkDesconfia() throws InterruptedException {
        Handler handler = new Handler();
        tempoDesconfiar = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!terminou) {
                    botdesconfia();
                    cardsEscolhidos = new ArrayList<>();
                    ditasCartas = new ArrayList<>();
                    tempoDesconfiar = false;
                    drawView();
                    try {

                        if (!isOver()) {
                            if (playerTurn)
                                playerTurn = false;
                            nextPlayer();
                            checkPlayer();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 4000);

    }

    public void popUpTurn(final String text) {
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.popup_playerturn, null);
        if (popupWindow != null) {
            if (popupWindow.isShowing())
                popupWindow.dismiss();
        }
        popupWindow = new PopupWindow(container, (int) (screenWidth * 0.4), (int) (screenHeight * 0.4), true);
        v.post(new Runnable() {
            @Override
            public void run() {
                TextView texto = (TextView) container.findViewById(R.id.textView5);
                texto.setText(text);
                popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (int) (screenWidth * 0.4), (int) (screenHeight * 0.4));
                container.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });

    }

    public String cardName(Card card) {
        if (card.getCode() == 1) {
            return "Ás";
        }
        if (card.getCode() == 2) {
            return "2";
        } else if (card.getCode() == 3) {
            return "3";
        } else if (card.getCode() == 4) {
            return "4";
        } else if (card.getCode() == 5) {
            return "5";
        } else if (card.getCode() == 6) {
            return "6";
        } else if (card.getCode() == 7) {
            return "7";
        } else if (card.getCode() == 8) {
            return "8";
        } else if (card.getCode() == 9) {
            return "9";
        } else if (card.getCode() == 10) {
            return "10";
        } else if (card.getCode() == 11) {
            return "Valete";
        } else if (card.getCode() == 12) {
            return "Dama";
        } else if (card.getCode() == 13) {
            return "Rei";
        }
        return null;
    }

    public void showPopUp(final String text, final boolean dismiss) {
        if (popupWindow != null) {
            if (popupWindow.isShowing())
                popupWindow.dismiss();
        }
        if (!terminou) {
            layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            final ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.popup_playerturn, null);
            popupWindow = new PopupWindow(container, (int) (screenWidth * 0.6), (int) (screenHeight * 0.6), true);
            v.post(new Runnable() {
                @Override
                public void run() {
                    popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (int) (screenWidth * 0.2), (int) (screenHeight * 0.2));
                    TextView texto = (TextView) container.findViewById(R.id.textView5);
                    texto.setText(text);
                    if (dismiss) {
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

    }

    private void botPlay() throws InterruptedException {
        boolean hasCard = false;
        ArrayList<Card> hand = playerAtual.getHand();
        String name = "";
        int rand, randCard;
        if (playerAtual.isEstado()) {
            System.out.println("isEstado: OK");
            if (contaPassas == players_list.size() - 1) {
                System.out.println("PASSAGENS: 3");
                int randBluff;
                randBluff = random.nextInt(4) + 1;
                Card card = null;
                if (randBluff == 1) {
                    System.out.println("BLUFF: TRUE");
                    randCard = random.nextInt(13) + 1;
                    if (hand.size() >= 4) {
                        rand = random.nextInt(4) + 1;
                    } else {
                        if (hand.size() > 1)
                            rand = random.nextInt(hand.size() - 1) + 1;
                        else rand = hand.size();
                    }
                    for (int k = 0; k < rand; k++) {
                        cardsEscolhidos.add(hand.get(k));
                        card = new Card(randCard);
                        name = cardName(card);
                        ditasCartas.add(card);
                    }
                } else {
                    System.out.println("BLUFF: FALSE");
                    rand = 0;
                    if (hand.size() > 0) {
                        randCard = random.nextInt(hand.size());
                        randCard = hand.get(randCard).getCode();
                    } else
                        randCard = 0;
                    for (int i = 0; i < hand.size(); i++) {
                        if (hand.get(i).getCode() == randCard) {
                            rand++;
                            cardsEscolhidos.add(hand.get(i));
                            card = new Card(randCard);
                            name = cardName(card);
                            ditasCartas.add(card);
                        }
                    }
                }
                for (int i = 0; i < players_list.size(); i++) {
                    players_list.get(i).setEstado(true);
                }
                System.out.println("ALL PLAYERS UP");
                contaPassas = 0;
                if (rand > 0) {
                    if (rand == 1)
                        showPopUp(playerAtual.getNome() + " jogou 1 carta: " + name, true);
                    else
                        showPopUp(playerAtual.getNome() + " jogou " + rand + " cartas: " + name, true);
                }
                if (ditasCartas.size() > 0)
                    codeAtual = ditasCartas.get(0).getCode();
                playCards(cardsEscolhidos);
            } else {
                System.out.println("PASSAGENS: " + contaPassas);
                rand = 0;
                Card card = null;
                if (codeAtual <= 0) {
                    codeAtual = hand.get(random.nextInt(hand.size())).getCode();
                }
                for (int i = 0; i < hand.size(); i++) {
                    if (hand.get(i).getCode() == codeAtual) {
                        cardsEscolhidos.add(hand.get(i));
                        card = new Card(codeAtual);
                        name = cardName(card);
                        ditasCartas.add(card);
                        rand++;
                        hasCard = true;
                    }
                }
                if (rand > 0) {
                    if (rand == 1)
                        showPopUp(playerAtual.getNome() + " jogou 1 carta: " + name, true);
                    else
                        showPopUp(playerAtual.getNome() + " jogou " + rand + " cartas: " + name, true);
                }
                if (!hasCard) {
                    rand = random.nextInt(5) + 1;
                    if (rand == 1 && userPlayed.size() < 4) {
                        System.out.println("BLUFF: TRUE");
                        do {
                            if (hand.size() >= 4) {
                                rand = random.nextInt(4) + 1;
                            } else {
                                if (hand.size() > 1)
                                    rand = random.nextInt(hand.size() - 1) + 1;
                                else {
                                    rand = hand.size();
                                    break;
                                }
                            }
                        } while (rand > 4 - userPlayed.size());
                        for (int k = 0; k < rand; k++) {
                            cardsEscolhidos.add(hand.get(k));
                            card = new Card(codeAtual);
                            name = cardName(card);
                            ditasCartas.add(card);
                        }
                        if (rand > 0) {
                            if (rand == 1)
                                showPopUp(playerAtual.getNome() + " jogou 1 carta: " + name, true);
                            else
                                showPopUp(playerAtual.getNome() + " jogou " + rand + " cartas: " + name, true);
                        }
                        if (ditasCartas.size() > 0)
                            codeAtual = ditasCartas.get(0).getCode();
                        playCards(cardsEscolhidos);
                    } else {
                        showPopUp(playerAtual.getNome() + " passou ", true);
                        playerAtual.setEstado(false);
                        if (contaPassas < players_list.size())
                            contaPassas++;
                        if (!isOver()) {
                            nextPlayer();
                            checkPlayer();
                        }
                    }
                } else {
                    if (ditasCartas.size() > 0)
                        codeAtual = ditasCartas.get(0).getCode();
                    playCards(cardsEscolhidos);
                }
                drawView();

            }
        } else {
            showPopUp(playerAtual.getNome() + " passou ", true);
            nextPlayer();
            checkPlayer();
        }


    }

    public void cards(int cardnumb) throws InterruptedException {
        Card card = new Card(cardnumb);
        for (int i = 0; i < cardsEscolhidos.size(); i++) {
            ditasCartas.add(card);
            cartasCampo.add(card);
            userPlayed.add(card);
        }
        codeAtual = ditasCartas.get(0).getCode();
        playCards(cardsEscolhidos);
        //cardsEscolhidos = new ArrayList<>();
        if (first)
            first = false;
        drawView();

    }

    private void chooseCard() {
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.choosefigure, null);
        if (popupWindow != null) {
            if (popupWindow.isShowing())
                popupWindow.dismiss();
        }
        popupWindow = new PopupWindow(container, (int) (screenWidth * 0.6), (int) (screenHeight * 0.6), false);
        v.post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (int) (screenWidth * 0.2), (int) (screenHeight * 0.2));

                ImageButton as = (ImageButton) container.findViewById(R.id.as);
                as.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            cards(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        popupWindow.dismiss();

                    }
                });

                ImageButton carta2 = (ImageButton) container.findViewById(R.id.carta2);
                carta2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            cards(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        popupWindow.dismiss();

                    }
                });

                ImageButton carta3 = (ImageButton) container.findViewById(R.id.carta3);
                carta3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            cards(3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        popupWindow.dismiss();

                    }
                });

                ImageButton carta4 = (ImageButton) container.findViewById(R.id.carta4);
                carta4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            cards(4);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        popupWindow.dismiss();

                    }
                });

                ImageButton carta5 = (ImageButton) container.findViewById(R.id.carta5);
                carta5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            cards(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        popupWindow.dismiss();

                    }
                });

                ImageButton carta6 = (ImageButton) container.findViewById(R.id.carta6);
                carta6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            cards(6);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        popupWindow.dismiss();

                    }
                });


                ImageButton carta7 = (ImageButton) container.findViewById(R.id.carta7);
                carta7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            cards(7);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        popupWindow.dismiss();

                    }
                });

                ImageButton carta8 = (ImageButton) container.findViewById(R.id.carta8);
                carta8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            cards(8);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        popupWindow.dismiss();

                    }
                });

                ImageButton carta9 = (ImageButton) container.findViewById(R.id.carta9);
                carta9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            cards(9);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        popupWindow.dismiss();

                    }
                });

                ImageButton carta10 = (ImageButton) container.findViewById(R.id.carta10);
                carta10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            cards(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        popupWindow.dismiss();

                    }
                });

                ImageButton valete = (ImageButton) container.findViewById(R.id.valete);
                valete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            cards(11);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        popupWindow.dismiss();

                    }
                });

                ImageButton dama = (ImageButton) container.findViewById(R.id.dama);
                dama.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            cards(12);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        popupWindow.dismiss();

                    }
                });

                ImageButton rei = (ImageButton) container.findViewById(R.id.rei);
                rei.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            cards(13);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        popupWindow.dismiss();

                    }
                });

            }
        });
    }

    public void confirmClicked(View view) throws InterruptedException {
        if (playerTurn) {
            userPlayed.clear();
            if (cardsEscolhidos.size() == 0) {
                Toast.makeText(this, "Por favor escolha pelo menos uma carta!", Toast.LENGTH_LONG).show();
            } else {
                if (players_list.get(0).isEstado()) {
                    if (first) {
                        chooseCard();
                    } else if (contaPassas == players_list.size() - 1) {
                        chooseCard();
                        for (int i = 0; i < players_list.size(); i++) {
                            players_list.get(i).setEstado(true);
                        }
                        contaPassas = 0;
                    } else {
                        Card card = new Card(codeAtual);
                        for (int i = 0; i < cardsEscolhidos.size(); i++) {
                            ditasCartas.add(card);
                            cartasCampo.add(card);
                            userPlayed.add(card);
                        }
                        playCards(cardsEscolhidos);
                    }
                } else {
                    Toast.makeText(this, "Você passou nesta ronda, por favor espere que a ronda termine", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(this, "Espere pelo seu turno", Toast.LENGTH_LONG).show();
        }
    }

    private void finalMessage(String message) {
        terminou = true;
        Handler handler = new Handler();

        if (popupWindow != null) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        }
        final AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
        TextView title = new TextView(this);
        title.setText("Final do Jogo");
        title.setTextSize(screenHeight/30);
        title.setTextColor(Color.rgb(0,220,220));
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        exitDialog.setCustomTitle(title);
        TextView text = new TextView(this);
        text.setText(message);
        text.setTextSize(screenHeight/40);
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        exitDialog.setView(text);
        exitDialog.setCancelable(false);
        exitDialog.setPositiveButton("Sair", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int wich) {
                Intent menu = new Intent(getBaseContext(), menu_secundario.class);
                menu.putExtra("username", players_list.get(0).getNome());
                menu.putExtra("Jogo", jogo);
                startActivity(menu);
                finish();
            }
        });
        exitDialog.show();
    }

    public void initDeck() {
        this.deck = new Deck(52, cards);
        this.deck.shuffle();
        this.playedDeck = new ArrayList<>();
    }


    public void initPlayers(String playerName, int numPlayers) {
        players_list.add(new Player(playerName, false));
        players_list.add(new Player("Afonso", true));
        players_list.add(new Player("Matilde", true));
        players_list.add(new Player("Rodrigo", true));


        for (int i = 0; i < numPlayers; i++) {
            this.deck.giveCards(true, 13, this.players_list.get(i));
        }
    }

    public void passarClicked(View view) throws InterruptedException {
        if (playerTurn) {
            if (popupWindow != null) {
                if (popupWindow.isShowing())
                    popupWindow.dismiss();
            }
            if (codeAtual > 0) {
                players_list.get(0).setEstado(false);
                if (contaPassas < players_list.size())
                    contaPassas++;
            }
            playerTurn = false;
            drawView();
            nextPlayer();
            checkPlayer();
        } else {
            Toast.makeText(this, "Não pode passar pois não é o seu turno.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Desconfia Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public class OurView extends View {
        private Player player;
        private Coordinates coords;

        public OurView(Player player, Context context, ArrayList<Card> cardsEscolhidos) {
            super(context);
            this.player = player;
            cardsCoords = new ArrayList<>();


        }

        @Override
        protected void onDraw(Canvas c) {
            ArrayList<Card> hand = players_list.get(0).getHand();
            int xDist, i, xDistPlayed;

            //Desenha jogadores e seu num cartas
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(c.getHeight() / 25);
            Typeface currentTypeFace = paint.getTypeface();
            Typeface bold = Typeface.create(currentTypeFace, Typeface.BOLD);
            Paint paint2 = new Paint();
            paint2.setStyle(Paint.Style.FILL);
            paint2.setTextSize(c.getHeight() / 20);
            paint2.setColor(Color.RED);
            paint2.setTypeface(bold);
            Paint paint3 = new Paint();
            paint3.setStyle(Paint.Style.FILL);
            paint3.setTextSize(c.getHeight() / 25);
            paint3.setColor(Color.GRAY);
            paint3.setTypeface(bold);
            paint.setTypeface(bold);
            String info;
            int x = 0, y = 0;
            int aux;
            int distTable = 75;
            for (i = 0; i < players_list.size(); i++) {
                info = players_list.get(i).getNome() + ": " + players_list.get(i).getNumberCards() + " Cartas";
                if (i == 0 || i == 2)
                    x = c.getWidth() / 2;
                else {
                    x = (c.getWidth() / 2) - ((2 - i) * (c.getWidth() / 3));
                }
                if (i == 1 || i == 3)
                    y = c.getHeight() / 2;
                else {
                    aux = (c.getHeight() / 4) * (1 - i);
                    y = (c.getHeight() / 2) + aux;
                }
                x -= (c.getWidth() / 12);
                if (players_list.get(i).getNome().equals(playerAtual.getNome()))
                    c.drawText(info, x, y, paint2);
                else if (players_list.get(i).isEstado())
                    c.drawText(info, x, y, paint);
                else
                    c.drawText(info, x, y, paint3);
            }


            xDistPlayed = (c.getWidth() / 2) - backgroundcard.getWidth() / 2;

            //Desenhar cartas jogadas viradas para baixo
            String numCards = "Cartas no monte: " + playedDeck.size();
            paint.setTextAlign(Paint.Align.CENTER);
            c.drawText(numCards, c.getWidth() / 2 - numCards.length() / 2, c.getHeight() / 3, paint);
            if (playedDeck.size() > 0) {
                Rect src3 = new Rect(0, 0, backgroundcard.getWidth(), backgroundcard.getHeight());
                Rect dst3 = new Rect(xDistPlayed, c.getHeight() / 2 - backgroundcard.getHeight() / 2, backgroundcard.getWidth() + xDistPlayed, c.getHeight() / 2 - backgroundcard.getHeight() / 2 + backgroundcard.getHeight());
                c.drawBitmap(backgroundcard, src3, dst3, null);
            }

            //Desenha cartas jogador atual;
            if (this.player.getNumberCards() > 0) {
                xDist = (c.getWidth() / 4) + ((c.getWidth() / 4) / this.player.getNumberCards());
                //Sem carta selecionada;
                if (cardsEscolhidos.size() == 0) {
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
                    boolean encontrou = false;
                    for (i = 0; i < this.player.getNumberCards(); i++) {
                        encontrou = false;
                        for (int j = 0; j < cardsEscolhidos.size(); j++) {
                            if (hand.get(i).getCode() == cardsEscolhidos.get(j).getCode() && hand.get(i).getNaipe().equals(cardsEscolhidos.get(j).getNaipe())) {
                                encontrou = true;
                                Rect src = new Rect(Math.round(hand.get(i).getX()),Math.round(hand.get(i).getY()), Math.round(hand.get(i).getX() + (float)cards.getWidth() / 13),Math.round(hand.get(i).getY() + (float)cards.getHeight() / 4));
                                Rect dst = new Rect(xDist, c.getHeight() - (((int) cards.getHeight() / 4) + (c.getHeight() / 10)), cards.getWidth() / 13 + xDist, cards.getHeight() / 4 + (c.getHeight() - (((int) cards.getHeight() / 4) + (c.getHeight() / 10))));
                                c.drawBitmap(cards, src, dst, null);
                                coords = new Coordinates(hand.get(i), xDist, cards.getWidth() / 13 + xDist, c.getHeight() - (((int) cards.getHeight() / 4) + (c.getHeight() / 10)), cards.getHeight() / 4 + (c.getHeight() - (((int) cards.getHeight() / 4) + (c.getHeight() / 10))));
                                cardsCoords.add(cardsCoords.size(), coords);
                                int aux2 = ((c.getWidth() / 2) / this.player.getNumberCards());
                                if (aux2 < cards.getWidth() / 13)
                                    xDist += ((c.getWidth() / 2) / this.player.getNumberCards());//-(this.player.getNumberCards());
                                else
                                    xDist += cards.getWidth() / 13;
                                break;
                            }

                        }
                        if (!encontrou) {
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