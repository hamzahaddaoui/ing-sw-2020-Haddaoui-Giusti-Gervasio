package it.polimi.ingsw.server.model;
import java.util.ArrayList;

import static it.polimi.ingsw.server.model.TurnState.WAIT;


/**
 * @author hamzahaddaoui
 *
 */
public class Match {
    private int ID;
    private int playersNum; //number of players of the match
    private int playersCurrentCount;

    private ArrayList<Player> players = new ArrayList<>(2);
    private Player currentPlayer;
    private ArrayList<GodCards> cards = new ArrayList<>(2);
    private Billboard billboard;
    private MatchState currentState;
    private Player winner;

    private boolean started;
    private boolean numReached;
    private boolean moveUpActive = true;
    private boolean finished;

    public Match(int matchID) {
        this.ID = matchID;
        billboard = new Billboard();
    }

    public boolean setPlayersNum(int playersNum){
        this.playersNum = playersNum;
        return true;
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
        if (playersCurrentCount >= playersNum)
            return;
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

    public void matchStart() {
        started = true;
        finished = false;
        currentPlayer = players.get(0);
    }

    public void nextTurn() {
        currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());
    }

    public boolean isMoveUpActive() {
        return moveUpActive;
    }

    public void setMoveUpActive(boolean moveUpActive) {
        this.moveUpActive = moveUpActive;
    }

    public void setFinished(boolean finished) { this.finished = finished; }


    public boolean isFinished() { return finished; }

    public Player getWinner() { return winner; }

    public void setWinner(Player winner) { this.winner = winner; }
}