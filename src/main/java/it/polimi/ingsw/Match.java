package it.polimi.ingsw;

import java.util.ArrayList;

public class Match {
    static final int PLAYERS_NUM = 2;
    private int playersCount;
    private ArrayList<Player> players = new ArrayList<Player>(2);
    private ArrayList<GodCards> cards = new ArrayList<>(2);
    private Player currentPlayer;
    private boolean isStarted;

    private Billboard billboardID;

    public Match(){
        billboardID = new Billboard();
    }

    public void addPlayer(Player player){
        players.add(player);
        playersCount++;
        if (playersCount == PLAYERS_NUM){
            matchStart();
        }
    }


    private Player matchStart(){
        isStarted = true;
        currentPlayer = players.get(0);
        return currentPlayer;
    }

    public void addCard(GodCards card){
        if (cards.size()==PLAYERS_NUM) return;
        else cards.add(card);
    }

    public void removeCard(GodCards card){
        if (cards.size()==0) return;
        else cards.remove(card);
    }

    public boolean isDeckFull(){
        if (cards.size() == PLAYERS_NUM) return true;
        else return false;
    }

    public ArrayList<GodCards> getCards(){
        return cards;
    }

    public Player nextTurn(){
        if (players.indexOf(currentPlayer)== PLAYERS_NUM-1){
            currentPlayer = players.get(0);
        }
        else currentPlayer = players.get(players.indexOf(currentPlayer)+1);
        //
        isStarted = true;
        return currentPlayer;
    }

    public Billboard getBillboardID() {
        return billboardID;
    }

    public int getPlayersCount() {
        return playersCount;
    }
}
