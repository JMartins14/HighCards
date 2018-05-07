/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highcards;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author João
 */
public class MauMau extends Game{
    private Deck deck;
    private ArrayList<Card> playedDeck;
    private Card cardAtual;
    private int numSevens=1;
    private int playerAtualpos;
    private boolean inverte;
    private int numBots;
    private int waitTime;

    public MauMau(ArrayList<Player> players, ArrayList<BufferedReader> inFromClients, ArrayList<PrintWriter> outToClients, ArrayList<Socket> clientSockets, ArrayList<Integer> thread_numbers) {
        super(players, inFromClients, outToClients, clientSockets, thread_numbers);
        cardAtual = null;
        playerAtualpos=0;
        this.playedDeck = new ArrayList<>();
        inverte = false;
        numBots = 0;
        waitTime = 30000;
    }
    public void initGame() throws IOException{
        initDeck();
        ArrayList<String> sendcards = initPlayers();
        sendPlayersInfo();
        sendCardsInfo(sendcards);
        while(true){
            try {
                playPlayerAtual(playerAtualpos);
                if (isOver() != null) {
                    if(getPlayers().size()==getNuminicialplayers()){
                        if(!isOver().isBot())
                            sendDatatoClient(String.format("finish,youWon,%s",isOver().getUsername()),isOver());
                        else{
                            System.out.println("Send: finish,youWon to " + isOver().getUsername());
                        }
                        isOver().getSocket().close();

                    }
                    else if(!isOver().isBot()){
                        sendDatatoClient(String.format("finish,%s",isOver().getUsername()),isOver());
                        isOver().getSocket().close();
                    }
                    else
                        System.out.println("Send: finish to " + isOver().getUsername());
                    removeFromGame(isOver());
                    if (getNumplayers() == 1){
                        if(!getPlayers().get(0).isBot())
                            sendDatatoClient("finish",getPlayers().get(0));   
                        else
                            System.out.println("Send: finish to " + getPlayers().get(0).getUsername());
                        System.out.println("GAME FINISHED");
                        return;
                    }
                }
                if(allBots()){
                    System.out.println("All players are bots! GAME FINISHED");
                    return;
                }
            } catch (IOException ex) {
                Logger.getLogger(ThreadConnection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    private boolean allBots(){
        ArrayList<Player> players = getPlayers();
        for(int i=0;i<players.size();i++){
            if(!players.get(i).isBot())
                return false;
        }
        return true;
    }
    private Player isOver() {
        int i;
        for (i = 0; i < getNumplayers(); i++) {
            if (getPlayers().get(i).getNumcards() == 0)
                return this.getPlayers().get(i);
        }
        return null;
    }
    private void initDeck(){
        this.deck = new Deck(52);
        this.deck.shuffle();
        this.playedDeck = new ArrayList <>();
        Card card = this.deck.removeFromDeck(true,1);
        playedDeck.add(card);
        this.cardAtual = card;
        deck.cards.add(card);
    }
    private ArrayList<String> initPlayers(){
        int i;
        ArrayList<Card> cartas = new ArrayList<>();
        ArrayList<String> strings = new ArrayList<>();
        String cards = new String();
        for(i=0;i<getNumplayers();i++){
            cartas = deck.giveCards(true,7,getPlayers().get(i));
            cards = String.format("%d %s %d %d",cartas.get(0).getCode(),cartas.get(0).getNaipe(),cartas.get(0).getX(),cartas.get(0).getY());
            for(int j=1;j<7;j++){
                cards = cards.concat("," + cartas.get(j).getCode() + " " + cartas.get(j).getNaipe() + " " + cartas.get(j).getX() + " " + cartas.get(j).getY());
            }
            strings.add(i,cards);
        }
        return strings;
        
    }
    private void playCard(Card card) throws InterruptedException, IOException{
        for(int i=0;i<getPlayers().get(playerAtualpos).getHand().size();i++){
            if(getPlayers().get(playerAtualpos).getHand().get(i).getCode()==card.getCode() && getPlayers().get(playerAtualpos).getHand().get(i).getNaipe().equals(card.getNaipe())){
                card = getPlayers().get(playerAtualpos).getHand().remove(i);
                getPlayers().get(playerAtualpos).setNumcards(getPlayers().get(playerAtualpos).getHand().size());
                break;
            }
        }
        this.playedDeck.add(card);
        this.deck.cards.add(card);
        Player player = getPlayers().get(playerAtualpos);
        String dataToClients = checkCard(card);
        String sendcard = String.format("playedCard,%d %s,%s,%d",card.getCode(),card.getNaipe(),lastPlayer().getUsername(),lastPlayer().getNumcards());
        sendDatatoClients(sendcard,dataToClients,player);
    }
    private void sendDatatoClient(String data,Player player){
        getOutToClients().get(getPlayers().indexOf(player)).println(data);
        System.out.println("Send: " + data + " to " + player.getUsername());
        /*for(int i=0;i<getPlayers().size();i++){
            if(getPlayers().get(i).getUsername().equals(player.getUsername())){
                getOutToClients().get(i).println(data);
                return;
            }
        }*/
    }
    private void sendDatatoClients(String card, String data,Player player){
        for(int i=0;i<getPlayers().size();i++){
            if(!getPlayers().get(i).isBot()){
                if(!getPlayers().get(i).getUsername().equals(player.getUsername())){
                    sendDatatoClient(card,getPlayers().get(i));
                    /*getOutToClients().get(i).println(card);
                    System.out.println("Send: " + card + " to " + getPlayers().get(i).getUsername());*/
                }     
                sendDatatoClient(data,getPlayers().get(i));
                //getOutToClients().get(i).println(data);
                //System.out.println("Send: " + data + " to " + getPlayers().get(i).getUsername());
            }
            else{
                System.out.println("Send: " + data + " to " + getPlayers().get(i).getUsername());
            }

        }
    }
    private void sendDatatoClients(String data){
        for(int i=0;i<getPlayers().size();i++){
            if(!getPlayers().get(i).isBot())
                sendDatatoClient(data,getPlayers().get(i));
            else
                System.out.println("Send: " + data + " to " + getPlayers().get(i).getUsername());
            /*getOutToClients().get(i).println(data);
            System.out.println("Send: " + data + " to " + getPlayers().get(i).getUsername());*/

        }
    }
    private void removeFromGame(Player player){
        ArrayList<Player> players = getPlayers();
        int pos = players.indexOf(player);
        players.remove(player);
        ArrayList<PrintWriter> toplayer = getOutToClients();
        toplayer.remove(pos);
        setOutToClients(toplayer);
        ArrayList<BufferedReader> inplayer = getInFromClients();
        inplayer.remove(pos);
        setInFromClients(inplayer);
        setPlayers(players);
        setNumplayers(players.size());
    }
    private String checkCard(Card card) throws InterruptedException, IOException{
        String resposta;
        resposta = String.format("cardInfo,O jogador %s já jogou! O próximo jogador a jogar será %s",getPlayers().get(playerAtualpos).getUsername(),getPlayers().get(nextPlayer(playerAtualpos)).getUsername());
        if(card==null){
            playerAtualpos = nextPlayer(playerAtualpos);
            return resposta;
        }
        switch(card.getCode()){
            case 1: resposta = String.format("cardInfo,O jogador %s já jogou e proibiu o jogador seguinte! O próximo jogador a jogar será %s",getPlayers().get(playerAtualpos).getUsername(),getPlayers().get(nextPlayer(nextPlayer(playerAtualpos))).getUsername());
            playerAtualpos = nextPlayer(nextPlayer(playerAtualpos)); return resposta;
            case 7: sevenPlayed(getPlayers().get(playerAtualpos));playerAtualpos = nextPlayer(playerAtualpos);return resposta;
            case 9: ninePlayed(getPlayers().get(playerAtualpos));playerAtualpos = nextPlayer(playerAtualpos);return resposta;
            case 11: resposta = resposta.concat(jackPlayed(playerAtualpos));playerAtualpos = nextPlayer(playerAtualpos);return resposta;
            case 12: queenPlayed();playerAtualpos = nextPlayer(playerAtualpos);return resposta;
            default: playerAtualpos = nextPlayer(playerAtualpos);return resposta;             
        }
    }
    private Player nextPlayer(Player lastplayer) throws InterruptedException {
        for(int i =0 ;i<getPlayers().size();i++) {
            if(lastplayer == getPlayers().get(i)){
                if (i == getPlayers().size() - 1)
                    return getPlayers().get(0);
                else
                    return getPlayers().get(i + 1);
            }
        }
        return null;
    }
    private Player lastPlayer(){
        if(playerAtualpos == 0){
            return getPlayers().get(getPlayers().size()-1);
        } 
        else{
            return getPlayers().get(playerAtualpos-1);
        }
    }
    private void sevenPlayed(Player player1) throws InterruptedException {
        Player player = nextPlayer(player1);
        boolean hasSeven = hasSeven(player);
        if(!hasSeven) {
            giveCards(deck,true, 2 * numSevens, player,false);
            numSevens = 1;
        }
        else{
            numSevens++;
            if(!player.isBot())
                sendDatatoClient("sevenPlayed",player);
            else
                System.out.println("Send: sevenPlayed to " + player.getUsername());
        }

    }
    private ArrayList<Card> giveCards(Deck deck,boolean top,int numberCards,Player player,boolean fromDeck){
        int i,j=0;
        ArrayList<Card> cards = new ArrayList<>();
        if(top){
            for(i=0;i<numberCards;i++)
                cards.add(deck.cards.remove(0));

        }
        else{
            for(i=0;i<numberCards;i++)
                cards.add(deck.cards.remove(deck.cards.size()-1));
        }
        for(i=0;i<getPlayers().size();i++){
            if(getPlayers().get(i)==player)
                break;
        }
        String data;
        data = String.format("deckCard");
        for(j=0;j<cards.size();j++){
            data = data.concat("," + cards.get(j).getCode() + " " + cards.get(j).getNaipe());
        }
        if(!player.isBot()){
            sendDatatoClient(data,getPlayers().get(i));
            /*getOutToClients().get(i).println(data);
            System.out.println("Send: " + data + " to " + getPlayers().get(i).getUsername());*/
        }
        else
            System.out.println("Send: " + data + " to " + getPlayers().get(i).getUsername());
        player.receiveCards(cards, numberCards);       
        return cards;
    }
    private void ninePlayed(Player player){
        for(int i = 0;i<getPlayers().size();i++){
            if(getPlayers().get(i).getUsername().equals(player.getUsername()))
                if(i == getPlayers().size()-1){
                    giveCards(deck,true,1,getPlayers().get(0),false);
                }
                else{
                    giveCards(deck,true,1,getPlayers().get(i+1),false);
                }
        }
    }
    private void queenPlayed(){
        if(!inverte)
            inverte = true;
        else
            inverte = false;
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
    private String jackPlayed(int playerAtualpos) throws IOException{
        String naipe;
        if(!getPlayers().get(playerAtualpos).isBot())
            sendDatatoClient("jackPlayed",getPlayers().get(playerAtualpos));
        //getOutToClients().get(playerAtualpos).println("jackPlayed");
        else
            System.out.println("Send: jackPlayed to " + getPlayers().get(playerAtualpos).getUsername());
        
        String card;
        if(!getPlayers().get(playerAtualpos).isBot()){
            getPlayers().get(playerAtualpos).getSocket().setSoTimeout(waitTime);
            try{
                card = getInFromClients().get(playerAtualpos).readLine();
            }
            catch(SocketTimeoutException es){
                String disc = String.format("disconnected,%s,Bot%d",getPlayers().get(playerAtualpos).getUsername(),numBots+1);
                sendDatatoClients(disc);
                getPlayers().get(playerAtualpos).getSocket().close();
                System.out.println("Connection with " + getPlayers().get(playerAtualpos).getUsername() + " was lost! Bot will be integrated in the game");
                getPlayers().get(playerAtualpos).setBot(true);
                getPlayers().get(playerAtualpos).setUsername("Bot" + ++numBots);
                card = String.format("11 %s",freq_cards(getPlayers().get(playerAtualpos)));
            }
        }
        else{
            card = String.format("11 %s",freq_cards(getPlayers().get(playerAtualpos)));
        }
        System.out.println("Received: " + card + " from " + getPlayers().get(playerAtualpos).getUsername());
        return String.format(",%s",card);
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
    private void playPlayerAtual(int playerAtualpos) throws IOException, InterruptedException{
        if(!getPlayers().get(playerAtualpos).isBot()){
            String data = String.format("yourTurn");
            for(int i=0;i<getNumplayers();i++){
                data = data.concat("," + getPlayers().get(i).getNumcards());
            }
            sendDatatoClient(data,getPlayers().get(playerAtualpos));
            //getOutToClients().get(playerAtualpos).println(data);
            //System.out.println("Send: " + data + " to " + getPlayers().get(playerAtualpos).getUsername());
            getPlayers().get(playerAtualpos).getSocket().setSoTimeout(waitTime);
            String info = null;
            try{
                info = getInFromClients().get(playerAtualpos).readLine();
                System.out.println("Received: " + info + " from " + getPlayers().get(playerAtualpos).getUsername());
            }
            catch(SocketTimeoutException es){
                System.out.println("Connection with " + getPlayers().get(playerAtualpos).getUsername() + " was lost! Bot will be integrated in the game");
                String disc = String.format("disconnected,%s,Bot%d",getPlayers().get(playerAtualpos).getUsername(),numBots+1);
                sendDatatoClients(disc);
                getPlayers().get(playerAtualpos).getSocket().close();
                getPlayers().get(playerAtualpos).setBot(true);
                getPlayers().get(playerAtualpos).setUsername("Bot" + ++numBots);
                botPlay();
                return;
            }
            if(info==null){
                System.out.println("Connection with " + getPlayers().get(playerAtualpos).getUsername() + " was lost! Bot will be integrated in the game");
                String disc = String.format("disconnected,%s,Bot%d",getPlayers().get(playerAtualpos).getUsername(),numBots+1);
                sendDatatoClients(disc);
                getPlayers().get(playerAtualpos).getSocket().close();
                getPlayers().get(playerAtualpos).setBot(true);
                getPlayers().get(playerAtualpos).setUsername("Bot" + ++numBots);
                botPlay();
                //Fechar a conexão e substituir por bot
            }
            else{
                String[] info2 = info.split(",");
                if(info2[0].equals("playedCard")){
                    Card card = createaCard(info2[1]);
                    playCard(card);
                }
                else if(info2[0].equals("requestCard")){
                    giveCards(deck,true,1,getPlayers().get(playerAtualpos),true);
                    this.playerAtualpos = nextPlayer(playerAtualpos);
                    String resposta = String.format("cardInfo2,O jogador %s já jogou! O próximo jogador a jogar será %s",getPlayers().get(playerAtualpos).getUsername(),getPlayers().get(nextPlayer(playerAtualpos)).getUsername());
                    for(int i=0;i<getNumplayers();i++){
                        resposta = resposta.concat("," + getPlayers().get(i).getNumcards());
                    }
                    sendDatatoClients(resposta);
                }

            }
        }
        else{
            Thread.sleep(2000);
            botPlay();
        }
        
        return;
    }
    private int nextPlayer(int lastplayer) throws InterruptedException {
        if(!inverte){
            if (lastplayer == getPlayers().size() - 1)
                return 0;
            else
                return lastplayer+1;
        }
        else{
            if (lastplayer == 0)
                return getPlayers().size()-1;
            else
                return lastplayer-1;
        }
    }
    private Card createaCard(String info){
        String[] infoCard = info.split(" ");
        return new Card(Integer.parseInt(infoCard[0]),infoCard[1]);
    }
    private void sendPlayersInfo() throws IOException{
        String reader;
        int counter=1;
        for(int i=0;i<getPlayers().size();i++){
            getPlayers().get(i).getSocket().setSoTimeout(waitTime);
            try{
                reader = getInFromClients().get(i).readLine();
                if(reader==null){
                    System.out.println("Connection with " + getPlayers().get(i).getUsername() + " was lost! Bot will be integrated in the game");
                    String disc = String.format("disconnected,%s,Bot%d",getPlayers().get(playerAtualpos).getUsername(),numBots+1);
                    sendDatatoClients(disc);
                    getPlayers().get(i).getSocket().close();
                    getPlayers().get(i).setBot(true);
                    getPlayers().get(i).setUsername("Bot" + ++numBots);
                }
                else{
                    for(int j = 0;j<i;j++){
                        if(getPlayers().get(j).getUsername().equalsIgnoreCase(reader)){
                            reader = reader.concat(String.valueOf(counter++));
                            break;
                        }

                    }
                    getPlayers().get(i).setUsername(reader);
                    System.out.println("Received: " + reader + " from " + getPlayers().get(i).getUsername());
                }
            }
            catch(SocketTimeoutException es){
                String disc = String.format("disconnected,%s,Bot%d",getPlayers().get(playerAtualpos).getUsername(),numBots+1);
                sendDatatoClients(disc);
                getPlayers().get(i).getSocket().close();
                getPlayers().get(i).setBot(true);
                System.out.println("Connection with " + getPlayers().get(i).getUsername() + " was lost! Bot will be integrated in the game");
                getPlayers().get(i).setUsername("Bot" + ++numBots);


            }
        }
        String playersString;
        for(int i=0;i<getPlayers().size();i++){
            playersString = new String();
            for(int j=0;j<getPlayers().size();j++){
                if(j==0){
                    playersString = String.format("%s:%d,%s:%d",getPlayers().get(i).getUsername(),getPlayers().get(i).getNumcards(),getPlayers().get(j).getUsername(),getPlayers().get(j).getNumcards());
                }
                else{
                    playersString = playersString.concat("," + getPlayers().get(j).getUsername()+":"+getPlayers().get(j).getNumcards());
                }
            }
            if(!getPlayers().get(i).isBot()){
                sendDatatoClient(playersString,getPlayers().get(i));
            }
            //getOutToClients().get(i).println(playersString);
            if(getPlayers().get(i).isBot())
                System.out.println("Send: " + playersString + " to " + getPlayers().get(i).getUsername());
        }
    }
    private void sendCardsInfo(ArrayList<String> cartas){
        //ArrayList<String> cartas = initGame();
        for(int i=0;i<cartas.size();i++){
            System.out.println("----PLAYER: " + getPlayers().get(i).getUsername());
            if(!getPlayers().get(i).isBot()){
                sendDatatoClient(cartas.get(i),getPlayers().get(i));
                sendDatatoClient(cardAtual.toString(),getPlayers().get(i));
            }
            if(getPlayers().get(i).isBot()){
                System.out.println("Send: " + cartas.get(i) + " to " + getPlayers().get(i).getUsername());
                System.out.println("Send: " + cardAtual.toString() + " to " + getPlayers().get(i).getUsername());
            }
        }
    }   
    private boolean compareCards(Card card1,Card card2){
        return card1.getNaipe().equals(card2.getNaipe()) || card1.getCode() == card2.getCode();
    }
    private void botPlay() throws InterruptedException, IOException{
        Card lastcard = playedDeck.get(playedDeck.size()-1);
        ArrayList<Card> hand= getPlayers().get(playerAtualpos).getHand();
        if(numSevens>1 && hasSeven(getPlayers().get(playerAtualpos))){
            int i=0;
            for(i =0 ;i<getPlayers().get(playerAtualpos).getHand().size();i++){
                if(getPlayers().get(playerAtualpos).getHand().get(i).getCode() == 7)
                    break;
            }
            playCard(hand.get(i));
        }
        else{
            boolean hasCard = false;
            for(int j =0;j<getPlayers().get(playerAtualpos).getHand().size();j++){
                if(compareCards(hand.get(j),lastcard)){
                    hasCard = true;
                    playCard(hand.get(j));
                    break;
                }
            }
            if(!hasCard){
                Card card = null;
                deck.giveCards(true,1,getPlayers().get(playerAtualpos));
                checkCard(card);
            }
        }
    }
    
}
