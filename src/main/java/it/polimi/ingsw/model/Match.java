package it.polimi.ingsw.model;

import it.polimi.ingsw.model.decorators.ApolloDecorator;

import java.util.ArrayList;

public class Match {
    private int playersNum; //number of players of the match
    private int playersCurrentCount;

    private ArrayList<Player> players = new ArrayList<>(2);
    private Player currentPlayer;
    private ArrayList<GodCards> cards = new ArrayList<>(2);
    private Billboard billboardID;

    private boolean matchStarted;

    public Match(int playersNum){
        this.playersNum = playersNum;
        billboardID = new Billboard();
    }

    public void addPlayer(Player player){
        players.add(player);
        playersCurrentCount++;
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }

    public Billboard getBillboardID() {
        return billboardID;
    }

    public int getPlayersNum() {
        return playersNum;
    }

    public int getPlayersCurrentCount() {
        return playersCurrentCount;
    }

    public boolean isMatchStarted() {
        return matchStarted;
    }

    private void matchStart(){
        matchStarted = true;
        currentPlayer = players.get(0);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void addCard(GodCards card){
        cards.add(card);
    }

    public void removeCard(GodCards card){
        if (cards.size()==0) return;
        else cards.remove(card);
    }

    public ArrayList<GodCards> getCards(){
        return cards;
    }

    public boolean isDeckFull(){
        if (cards.size() == playersNum) return true;
        else return false;
    }

    public void nextTurn(){
        if (players.indexOf(currentPlayer)== playersNum-1){
            currentPlayer = players.get(0);
        }
        else currentPlayer = players.get(players.indexOf(currentPlayer)+1);
        //
        matchStarted = true;
    }
}
