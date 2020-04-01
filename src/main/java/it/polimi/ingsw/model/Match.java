package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Match {
    private int ID;
    private int playersNum; //number of players of the match
    private int playersCurrentCount;

    private ArrayList<Player> players = new ArrayList<>(2);
    private Player currentPlayer;
    private ArrayList<GodCards> cards = new ArrayList<>(2);
    private Billboard billboard;
    private MatchState currentState;

    private boolean started;
    private boolean numReached;

    public Match(int matchID, int playersNum) {
        this.playersNum = playersNum;
        this.ID = matchID;
        billboard = new Billboard();
    }

    public int getID() {
        return ID;
    }

    public int getPlayersNum() {
        return playersNum;
    }

    public int getPlayersCurrentCount() {
        return playersCurrentCount;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public ArrayList<GodCards> getCards() {
        return cards;
    }

    public Billboard getBillboard() {
        return billboard;
    }

    public MatchState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(MatchState currentState) {
        this.currentState = currentState;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isNumReached() {
        return numReached;
    }

    public boolean isDeckFull() {
        if (cards.size() == playersNum) return true;
        else return false;
    }

    //to do - write javadoc

    public void addPlayer(Player player) {
        players.add(player);
        currentPlayer = player;
        playersCurrentCount++;
        if(playersNum == playersCurrentCount) numReached = true;
    }

    public void addCard(GodCards card) {
        cards.add(card);
    }

    public void removeCard(GodCards card) {
        cards.remove(card);
    }

    private void matchStart() {
        started = true;
        currentPlayer = players.get(0);
    }

    public void nextTurn() {
        currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());
        /*if (players.indexOf(currentPlayer) == playersNum - 1) {
            currentPlayer = players.get(0);
        } else currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
        */
    }












}